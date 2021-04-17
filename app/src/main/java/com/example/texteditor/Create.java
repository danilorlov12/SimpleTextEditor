package com.example.texteditor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Create extends AppCompatActivity {
    EditText headBox, textBox;
    Database sql;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        headBox = (EditText) findViewById(R.id.head);
        textBox = (EditText) findViewById(R.id.text);

        sql = new Database(this);
        db = sql.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) userId = extras.getLong("id");
        if (userId > 0) {
            userCursor = db.rawQuery("select * from " + Database.TABLE + " where " +
                    Database.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            headBox.setText(userCursor.getString(1));
            textBox.setText(userCursor.getString(2));
            userCursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        MenuItem item_up = menu.findItem(R.id.menu_delete);
        if (userId <= 0) item_up.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_save:
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String timeText = timeFormat.format(currentDate);
                dateText = dateText + " " + timeText;
                ContentValues cv = new ContentValues();
                cv.put(Database.COLUMN_HEAD, headBox.getText().toString());
                cv.put(Database.COLUMN_TEXT, textBox.getText().toString());
                cv.put(Database.COLUMN_TIME, dateText.toString());

                if (userId > 0) db.update(Database.TABLE, cv,
                        Database.COLUMN_ID + "=" + String.valueOf(userId), null);
                else db.insert(Database.TABLE, null, cv);
                goHome();
                break;

            case R.id.menu_delete:
                db.delete(Database.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
                goHome();
                break;
        }
        return true;
    }

    private void goHome(){
        db.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}