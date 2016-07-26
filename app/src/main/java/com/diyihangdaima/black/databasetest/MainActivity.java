package com.diyihangdaima.black.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button createDatabase;
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);
        Button replaceData = (Button) findViewById(R.id.replace_data);
        replaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();//开启事务
                try{
                    db.delete("Book", null, null);
                    if (true){
                        //在这里手动抛一个异常，让事务失败
                        throw new NullPointerException();
                    }
                    ContentValues values = new ContentValues();
                    values.put("name", "Game of Thrones");
                    values.put("author", "George Martin");
                    values.put("pages", 720);
                    values.put("price", 20.85);
                    db.insert("Book", null, values);
                    db.setTransactionSuccessful();//事物已经执行成功
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    db.endTransaction();//结束事务
                }
            }
        });
        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //查询Book表中所有的数据
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()){
                    do{
                        //遍历Cursor对象，取出数据并打印
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getInt(cursor.getColumnIndex("price"));
                        Log.d("MainActivity", "book name is" + name);
                        Log.d("MainActivity", "book author is" + author);
                        Log.d("MainActivity", "book pages is" + pages);
                        Log.d("MainActivity", "book price is" + price);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Book", "pages > ?", new String[] {"500"});
            }
        });
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                db.update("Book", values, "name = ?", new String[] {"The Da Vinci Code"});
                //第三个参数对应的是SQL语句的where部分，表示去更新所有name等于？的行，而？是一个占位符，
                //可以通过第四个参数提供一个字符串数组为第三个参数中的每个占位符指定相应的内容
            }
        });
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //开始组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", "454");
                values.put("price", "16.96");
                db.insert("Book", null, values);//插入第一条数据

                //开始组装第二条数据
                values.put("name", "The Lost Symbel");
                values.put("author", "Dan Brown");
                values.put("pages", "510");
                values.put("price", "19.95");
                db.insert("Book", null, values);//插入第二条数据
            }
        });
        createDatabase = (Button) findViewById(R.id.creata_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.getWritableDatabase();
            }
        });

    }
}
