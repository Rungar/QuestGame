package com.game.quest.questgame;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener{

    public static final int START_ID = -4;
    int id;
    String text;
    Button btnStart;
    LinearLayout linearLayout;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);


        linearLayout = new LinearLayout(this);
        btnStart = new Button(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams linLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(linearLayout,linLayoutParams);



        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.gravity = Gravity.CENTER;

        //btnStart.setId();
        btnStart.setOnClickListener(this);
        btnStart.setText("Начать игру");
        linearLayout.addView(btnStart,btnParams);

    }

    public void startGame() {

        int columnID;

        DataBaseHelper newDBH = new DataBaseHelper(this);
        try {
            newDBH.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            newDBH.openDataBase();
        }catch(SQLException sqle){
            throw sqle;
        }

        SQLiteDatabase gameDB = newDBH.getReadableDatabase();

        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        while (id < 400) {
            c = gameDB.query("locations", null, "id = ?", new String[]{Integer.toString(id)},null,null,null,null );
            columnID = c.getColumnIndex("text");
            text = c.getString(columnID);
            c.close();
            TextView tvLocationText = new TextView(this);
            tvLocationText.setText(text);
            linearLayout.addView(tvLocationText, tvParams);
            c = gameDB.query("transitions",null,"idFrom = ?", new String[]{Integer.toString(id)},null,null,null,null );
            if (c != null) {
                while (c.moveToNext()) {
                    Button btnMove = new Button(this);
                    columnID = c.getColumnIndex("text");
                    btnMove.setText(c.getString(columnID));
                    linearLayout.addView(btnMove);
                    btnMove.setOnClickListener(this);
                }
            }
            c.close();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view == btnStart) {
            linearLayout.removeAllViews();
            id = START_ID;
            startGame();
        } else {
            id = c.getInt(c.getColumnIndex("idTo"));
        }
    }
}
