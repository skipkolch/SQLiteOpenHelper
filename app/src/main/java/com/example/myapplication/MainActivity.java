package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etEmail;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        Button btnRead = findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);

        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View v) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.btnAdd:
                addDataBase(etName, etEmail, database);
                break;
            case R.id.btnRead:
                readDataBase(database);
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                Toast.makeText(this, "Table clear!", Toast.LENGTH_LONG).show();
                break;
        }
        dbHelper.close();
    }

    private void addDataBase(EditText etname, EditText etemail, SQLiteDatabase database) {
        final String name = etname.getText().toString();
        final String email = etemail.getText().toString();
        etName.setText("");
        etEmail.setText("");

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_MAIL, email);

        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
    }

    private void readDataBase(SQLiteDatabase database) {
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) { // перемещает курсор на первую строку
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);

            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", NAME = " + cursor.getString(nameIndex) +
                        ", EMAIL = " + cursor.getString(emailIndex));
            } while (cursor.moveToNext()); // перемещает курсор на след. строку
        } else Log.d("mLog", "0 rows");
        cursor.close();
    }
}
