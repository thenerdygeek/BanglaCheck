package com.soumik.subhankar.banglacheck;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import com.soumik.subhankar.banglacheck.Contract.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CheckerActivity extends AppCompatActivity{
    private final String LOG = CheckerActivity.class.getSimpleName();
    String text;
    private TextView tv;
    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker);

        initialize();


        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("EditText");
        Charset charset = Charset.forName("UTF-8");
//        String text1, text2 = Normalizer.normalize(new String(bytes, charset), Normalizer.Form.NFD);

        try {
            text = new String(bytes, "UTF-8");
            startChecking();
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
    }

    private void initialize()
    {
        tv = findViewById(R.id.text_view);
        uri = getIntent().getData();
    }


    public void startChecking() throws UnsupportedEncodingException {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                DatabaseEntry.ID,
                DatabaseEntry.WRONG,
                DatabaseEntry.RIGHT};

        String[] words = text.split(" ");
        for(String word:words)
        {
            Log.println(Log.ASSERT, LOG, word);
        }
        HashMap<byte[],byte[]> wordMap = checkForWord(text.split(" "));
        Iterator it = wordMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            appendColouredText(new String(pair.getKey().toString().getBytes(), "UTF-8"), Color.RED);
            tv.append("  -->  ");
            appendColouredText(new String(pair.getValue().toString().getBytes(), "UTF-8"), Color.GREEN);
            tv.append("\n");
            it.remove(); // avoids a ConcurrentModificationException
        }


    }



    private HashMap<byte[],byte[]> checkForWord(String[] words) throws UnsupportedEncodingException {

//        SQLiteDatabase db = dbHelper.getReadableDatabase();
        DbHelper myDbHelper = new DbHelper(this);
        SQLiteDatabase db;
        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            db = myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                DatabaseEntry.ID,
                DatabaseEntry.WRONG,
                DatabaseEntry.RIGHT};

// Filter results WHERE "title" = 'My Title'
        String selection = DatabaseEntry.WRONG + " = ?";

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                DatabaseEntry.WRONG + " DESC";

        HashMap<byte[], byte[]> wordMap = new HashMap<>();
        for (String word : words) {
            String[] value = {word};
            Cursor cursor = db.query(
                    DatabaseEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    value,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            while (cursor.moveToNext()) {
                byte[] wrongWord = cursor.getBlob(1);
                Log.println(Log.ASSERT,LOG,"wrong Word: " + new String(wrongWord, "utf-8"));
                byte[] rightWord = cursor.getBlob(2);
                wordMap.put(wrongWord, rightWord);
            }

            cursor.close();
        }
        return wordMap;
    }
    private void appendColouredText(String message, int color)
    {
        Spannable word = new SpannableString(message);
        word.setSpan(new ForegroundColorSpan(color), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(word);
    }

    private void accessDatabase()
    {
        DbHelper myDbHelper = new DbHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

    }

}
