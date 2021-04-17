package com.example.texteditor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView userList;
    Database database;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = (ListView) findViewById(R.id.list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Create.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        database = new Database(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        db = database.getReadableDatabase();
        userCursor =  db.rawQuery("select * from "+ Database.TABLE, null);
        String[] headers = new String[] {Database.COLUMN_HEAD, Database.COLUMN_TIME};

        userAdapter = new SimpleCursorAdapter(this, R.layout.listitem,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

        userList.setAdapter(userAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuAdd:
                Intent intent = new Intent(this, Create.class);
                startActivity(intent);
                break;

            case R.id.menuAbout:
                Toast toast = Toast.makeText(this, "Simple \nText Editor", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if(v != null) v.setGravity(Gravity.CENTER);
                toast.show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }
}