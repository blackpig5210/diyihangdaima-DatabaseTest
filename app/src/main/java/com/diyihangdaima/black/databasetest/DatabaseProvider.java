package com.diyihangdaima.black.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;


public class DatabaseProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;
    private static UriMatcher uriMatcher;
    private static final String Authority = "com.diyihangdaima.black.databasetest.provider";
    private MyDatabaseHelper dbHelper;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //常量UriMatcher.NO_MATCH表示不匹配任何路径的返回码
        uriMatcher.addURI(Authority, "book", BOOK_DIR);//传入权限、路径、自定义代码
        uriMatcher.addURI(Authority, "book/#", BOOK_ITEM);
        uriMatcher.addURI(Authority, "category", CATEGORY_DIR);
        uriMatcher.addURI(Authority, "category/#", CATEGORY_ITEM);
    }
    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        //查询数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor = db.query("Book", strings, s, strings1, null, null, s1);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book", strings, "id = ?", new String[]{bookId}, null, null, s1);
                break;
            case CATEGORY_DIR:
                cursor = db.query("Category", strings, s, strings1, null, null, s1);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category", strings,
                        "id = ?", new String[]{ categoryId }, null, null, s1);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.diyihangdaima.black.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.diyihangdaima.black.databasetest.provider.book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd.com.diyihangdaima.black.databasetest.provider.category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item.vnd.com.diyihangdaima.black.databasetest.provider.category";
        }
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        //添加数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book", null, contentValues);
                uriReturn = Uri.parse("content://" + Authority + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategory = db.insert("Category", null, contentValues);
                uriReturn = Uri.parse("content://" + Authority + "/category/" + newCategory);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        //删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows = db.delete("Book", s, strings);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Book", "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete("Category", s, strings);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Category", "id = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        //更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows = db.update("Book", contentValues, s, strings);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update("Book", contentValues, "id = ?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update("Category",contentValues, s, strings);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update("Category", contentValues, "id = ?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
