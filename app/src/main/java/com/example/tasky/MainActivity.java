package com.example.tasky;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.tasky.db.TaskContract;
import com.example.tasky.db.TaskDBHelper;

//http://www.sitepoint.com/starting-android-development-creating-todo-app/

public class MainActivity extends  AppCompatActivity{
    // global variables
    private ListAdapter listAdapter;
    private TaskDBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            //initial code for layout
            setContentView(R.layout.activity_main);
             //we need to get all the data from the database and show it in the main view.
            updateUI();

        }


    //onCreateOptionsMenu() to add the menu to the main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //onOptionsItemSelected() to get the selected item from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //AlertDialog to get the task from the user when the add item button is clicked
        if (id == R.id.action_add_task) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add a task");
            builder.setMessage("What do you want to do?");
            final EditText inputField = new EditText(this);
            builder.setView(inputField);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String task = inputField.getText().toString();
                    Log.d("MainActivity",task);

                    TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.clear();
                    values.put(TaskContract.Columns.TASK, task);

                    db.insertWithOnConflict(TaskContract.TABLE, null, values,
                            SQLiteDatabase.CONFLICT_IGNORE);
                    updateUI();

                }
            });

            builder.setNegativeButton("Cancel",null);

            builder.create().show();
            return true;
        }
        else{
            return false;
        }

    }

    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0
        );

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);

    }

    //clicking the Done button will delete the task from the main view
    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);


        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        //call updateUI() when the add menu item is clicked.
        updateUI();
    }

}
