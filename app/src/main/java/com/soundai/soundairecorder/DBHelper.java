package com.soundai.soundairecorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by fez on 2017/2/24.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABESE_NAME = "recording.db";
    private static final int DATABASE_VERSION = 1;

    private OnDatabaseChangedListener onDatabaseChangedListener;

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "recording";
        public static final String COLUMN_NAME_RECORDING_NAME = "name";
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "path";
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        public static final String COLUMN_NAME_TIME_ADDED = "time";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DBHelperItem.TABLE_NAME + " (" +
            DBHelperItem._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + DBHelperItem.COLUMN_NAME_RECORDING_NAME + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH + TEXT_TYPE + COMMA_SEP +
            DBHelperItem.COLUMN_NAME_RECORDING_LENGTH + " INTEGER " + COMMA_SEP +
            DBHelperItem.COLUMN_NAME_TIME_ADDED + " INTEGER " + ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST " + DBHelperItem.TABLE_NAME;

    private Context context;

    public DBHelper (Context context) {
        super(context, DATABESE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void setOnDatabaseChangedListener(OnDatabaseChangedListener onDatabaseChangedListener) {
        this.onDatabaseChangedListener = onDatabaseChangedListener;
    }

    public RecordItem getItem(int position) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DBHelperItem._ID,
                DBHelperItem.COLUMN_NAME_RECORDING_NAME,
                DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH,
                DBHelperItem.COLUMN_NAME_RECORDING_LENGTH,
                DBHelperItem.COLUMN_NAME_TIME_ADDED
        };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        if (c.moveToPosition(position)) {
            RecordItem item = new RecordItem();
            item.setId(c.getInt(c.getColumnIndex(DBHelperItem._ID)));
            item.setName(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_NAME)));
            item.setFilePath(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH)));
            item.setLength(c.getInt(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH)));
            item.setTime(c.getLong(c.getColumnIndex(DBHelperItem.COLUMN_NAME_TIME_ADDED)));
            c.close();
            return item;
        }
        return null;
    }

    public void removeItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = { String.valueOf(id)};
        db.delete(DBHelperItem.TABLE_NAME, "_ID=?", whereArgs);
    }

    public long addItem(String name, String filePath, long length) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, name);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_TIME_ADDED, System.currentTimeMillis());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);
        if (onDatabaseChangedListener != null) {
            onDatabaseChangedListener.onDataAdded();
        }
        return rowId;
    }

    public void renameItem(RecordItem item, String name, String filePath) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, name);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        db.update(DBHelperItem.TABLE_NAME,cv,DBHelperItem._ID + "=" + item.getId(), null);
        if (onDatabaseChangedListener != null) {
            onDatabaseChangedListener.onDataRenamed();
        }
    }

    public int getCount() {
        SQLiteDatabase db  = getReadableDatabase();
        String[] projection = { DBHelperItem._ID};
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }
}
