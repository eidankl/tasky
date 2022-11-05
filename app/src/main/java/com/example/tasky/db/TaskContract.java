package com.example.tasky.db;

/**
 * Created by eidan on 05/11/2022.
 * TaskTable that will hold our tasks.
 * Before doing this, we create a Contract class called TaskContract
 * and place it in the db package
 * TaskContract class defines the final variables (i.e. constants) which we will use to access the data in the database.
 * After this, we create a helper class called TaskDBHelper to open the database for us.
 * Create this class in the db package with the following source code:
 */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.TodoList.db.tasks";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
    }
}