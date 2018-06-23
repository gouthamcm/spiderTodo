package com.example.rec.spider3;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.Menu;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    ListView lv;
    Button button;
    DBAdapter myDb ;
    EditText etTasks;
    Calendar today=new Calendar() {
        @Override
        protected void computeTime() {

        }

        @Override
        protected void computeFields() {

        }

        @Override
        public void add(int field, int amount) {

        }

        @Override
        public void roll(int field, boolean up) {

        }

        @Override
        public int getMinimum(int field) {
            return 0;
        }

        @Override
        public int getMaximum(int field) {
            return 0;
        }

        @Override
        public int getGreatestMinimum(int field) {
            return 0;
        }

        @Override
        public int getLeastMaximum(int field) {
            return 0;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        etTasks = (EditText) findViewById(R.id.editTextTask);
        openDB();
        populateListView();
        listViewItemClick();
        listViewItemLongClick();
    }
    private void openDB() { myDb = new DBAdapter(this);
    myDb.open();
    }

    public void onClick_AddTask(View v) {

        today=Calendar.getInstance();
       SimpleDateFormat Timestamp= new SimpleDateFormat("dd MMM  hh:mm:ss");

        if(!TextUtils.isEmpty(etTasks.getText())) {
            myDb.insertRow(etTasks.getText().toString(),Timestamp.format(today.getTime()));}
            etTasks.setText(null);

        populateListView();
        listViewItemClick();
    }


    private void populateListView() { Cursor cursor = myDb.getAllRows();
           String[] fromFieldNames = new String[] {DBAdapter.KEY_DATE,DBAdapter.KEY_TASK};



        int[] toViewIDs = new int[] { R.id.Edit, R.id.textViewItemTask};
        SimpleCursorAdapter myCursorAdapter; myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.listViewTasks);
        myList.setAdapter(myCursorAdapter);
    }


    private void updateTask(long id) { Cursor cursor = myDb.getRow(id);
    if (cursor.moveToFirst()) {
        String task = etTasks.getText().toString();

        SimpleDateFormat Timestamp= new SimpleDateFormat("dd MMM  hh:mm:ss");
        String date = Timestamp.format(today.getTime().toString());
    myDb.updateRow(id, task, date); } cursor.close();
    }

    private void listViewItemClick() {
        ListView myList = (ListView) findViewById(R.id.listViewTasks);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                updateTask(id); populateListView(); } });
    }

    public void onClick_DeleteTasks(View v) {
        myDb.deleteAll(); populateListView();
    }


    private void listViewItemLongClick() {
        ListView myList = (ListView) findViewById(R.id.listViewTasks);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View viewClicked, int position, long id) {
        myDb.deleteRow(id); populateListView(); return false; } });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        openActivity();
        return super.onOptionsItemSelected(item);
    }
    public void openActivity(){
        Intent i=new Intent(this,Main2Activity.class);
        startActivity(i);
    }
}
