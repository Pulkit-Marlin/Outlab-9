package com.example.outlab_9;

import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.LinearLayout;

import com.example.outlab_9.db.TaskContract;
import com.example.outlab_9.db.TaskDbHelper;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo);

        updateUI();

//        SQLiteDatabase db = mHelper.getWritableDatabase();
//        db.execSQL("DROP TABLE "+"tasks");
//        mTaskListView.setAdapter(mAdapter); // set it as the adapter of the ListView instance

//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
//                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
//                null, null, null, null, null);
//        while(cursor.moveToNext()) {
//            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
//            Log.d(TAG, "Task: " + cursor.getString(idx));
//        }
//        cursor.close();
//        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:

//                LayoutInflater inflater = getLayoutInflater();
//                final EditText taskEditText1 = new EditText(this);
//                final EditText taskEditText2 = new EditText(this);

//                final EditText taskEditText1 = (EditText)

//                Context context = mapView.getContext();
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Add a TextView here for the "Title" label, as noted in the comments
                final EditText titleBox = new EditText(this);
                titleBox.setHint("Title");
                layout.addView(titleBox); // Notice this is an add method

                // Add another TextView here for the "Description" label
                final EditText descriptionBox = new EditText(this);
                descriptionBox.setHint("Description");
                layout.addView(descriptionBox); // Another add method

//                dialog.setView(layout); // Again this is a set method, not add

//                LayoutInflater factory = LayoutInflater.from(this);
//
//                //text_entry is an Layout XML file containing two text field to display in alert dialog
//                final View textEntryView = factory.inflate(R.layout.dialogue1, null);
//
//                final EditText input1 = (EditText) textEntryView.findViewById(R.id.EditText1);
//                final EditText input2 = (EditText) textEntryView.findViewById(R.id.EditText2);

                AlertDialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("Add a new task")
//                        .setMessage("What do you want to do next?")
//                        .setView(taskEditText1, taskEditText1)
//                        .setView(taskEditText2)
//
//                        .setView(getLayoutInflater().inflate(R.layout.dialogue1, null))
                        .setView(layout)

                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                String task = String.valueOf(taskEditText.getText());
                                String task1 = String.valueOf(titleBox.getText());
                                String task2 = String.valueOf(descriptionBox.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task1);
                                values.put(TaskContract.TaskEntry.COL_TASK_DESCRIPTION, task2);
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
//                updateUI();
                dialog.show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_DESCRIPTION, TaskContract.TaskEntry.COL_TASK_DATE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
}
