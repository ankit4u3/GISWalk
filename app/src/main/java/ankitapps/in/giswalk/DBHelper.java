package ankitapps.in.giswalk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Daniel on 12/29/2014.
 */
public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;

    private static final String LOG_TAG = "DBHelper";


    public static final String DATABASE_NAME = "saved_recordings.db";
    private static final int DATABASE_VERSION = 1;

    public static abstract class DBHelperItem implements BaseColumns {
        public static final String TABLE_NAME = "saved_recordings";

        public static final String COLUMN_NAME_RECORDING_NAME = "recording_name";
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        public static final String COLUMN_NAME_TIME_ADDED = "time_added";
    }

    public static abstract class DBHelperItemLog implements BaseColumns {
        public static final String TABLE_NAME = "saved_recordingslogs";

        public static final String COLUMN_NAME_RECORDING_NAME = "recording_name";
        public static final String COLUMN_NAME_RECORDING_FILE_PATH = "file_path";
        public static final String COLUMN_NAME_RECORDING_LENGTH = "length";
        public static final String COLUMN_NAME_TIME_ADDED = "time_added";
    }
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";
    public static final String FOURTH_COLUMN="Fourth";






    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBHelperItem.TABLE_NAME + " (" +
                    DBHelperItem._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_NAME + TEXT_TYPE + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH + TEXT_TYPE + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_LENGTH + " INTEGER " + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_TIME_ADDED + " INTEGER " + ")";
    private static final String SQL_CREATE_ENTRIESLOG =
            "CREATE TABLE " + DBHelperItemLog.TABLE_NAME + " (" +
                    DBHelperItem._ID + " INTEGER  " + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_NAME + TEXT_TYPE + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH + TEXT_TYPE + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_RECORDING_LENGTH + " INTEGER " + COMMA_SEP +
                    DBHelperItem.COLUMN_NAME_TIME_ADDED + " INTEGER " + ")";

    @SuppressWarnings("unused")
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBHelperItem.TABLE_NAME;
    private static final String SQL_DELETE_ENTRIESLOG = "DROP TABLE IF EXISTS " + DBHelperItemLog.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIESLOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }



    public RecordingItem getItemAt(int position) {
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
            RecordingItem item = new RecordingItem();
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

    public ArrayList<HashMap<String, String>>  getAll() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String, String>> list  =new ArrayList<HashMap<String,String>>();
        ArrayList<String> array_list = new ArrayList<String>();
        String[] projection = {
                DBHelperItem._ID,
                DBHelperItem.COLUMN_NAME_RECORDING_NAME,
                DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH,
                DBHelperItem.COLUMN_NAME_RECORDING_LENGTH,
                DBHelperItem.COLUMN_NAME_TIME_ADDED
        };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);


            c.moveToFirst();

            while(c.isAfterLast() == false){
                RecordingItem item = new RecordingItem();
                item.setId(c.getInt(c.getColumnIndex(DBHelperItem._ID)));
                item.setName(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_NAME)));
                item.setFilePath(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH)));
                item.setLength(c.getInt(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH)));
                item.setTime(c.getLong(c.getColumnIndex(DBHelperItem.COLUMN_NAME_TIME_ADDED)));
                HashMap<String,String> temp=new HashMap<String, String>();
                temp.put(FIRST_COLUMN, item.getName());
                temp.put(SECOND_COLUMN, item.getFilePath());
                temp.put(THIRD_COLUMN, String.valueOf(item.getId()));
                temp.put(FOURTH_COLUMN, " * ");
                list.add(temp);

                c.moveToNext();
            }

        return list;
    }

    public ArrayList<HashMap<String, String>>  getAllImageLog() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String, String>> list  =new ArrayList<HashMap<String,String>>();
        ArrayList<String> array_list = new ArrayList<String>();
        String[] projection = {
                DBHelperItem._ID,
                DBHelperItem.COLUMN_NAME_RECORDING_NAME,
                DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH,
                DBHelperItem.COLUMN_NAME_RECORDING_LENGTH,
                DBHelperItem.COLUMN_NAME_TIME_ADDED
        };
        Cursor c = db.query(DBHelperItemLog.TABLE_NAME, projection, null, null, null, null, null);


        c.moveToFirst();

        while(c.isAfterLast() == false){
            RecordingItem item = new RecordingItem();
            item.setId(c.getInt(c.getColumnIndex(DBHelperItem._ID)));
            item.setName(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_NAME)));
            item.setFilePath(c.getString(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH)));
            item.setLength(c.getInt(c.getColumnIndex(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH)));
            item.setTime(c.getLong(c.getColumnIndex(DBHelperItem.COLUMN_NAME_TIME_ADDED)));
            HashMap<String,String> temp=new HashMap<String, String>();
            temp.put(FIRST_COLUMN, item.getName());
            temp.put(SECOND_COLUMN, item.getFilePath());
            temp.put(THIRD_COLUMN, String.valueOf(item.getId()));
            temp.put(FOURTH_COLUMN, " * ");
            list.add(temp);

            c.moveToNext();
        }

        return list;
    }

    public void removeItemWithId(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = { String.valueOf(id) };
        db.delete(DBHelperItem.TABLE_NAME, "_ID=?", whereArgs);
    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { DBHelperItem._ID };
        Cursor c = db.query(DBHelperItem.TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public Context getContext() {
        return mContext;
    }

    public class RecordingComparator implements Comparator<RecordingItem> {
        public int compare(RecordingItem item1, RecordingItem item2) {
            Long o1 = item1.getTime();
            Long o2 = item2.getTime();
            return o2.compareTo(o1);
        }
    }

    public long addRecording(String recordingName, String filePath, long length) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, length);
        cv.put(DBHelperItem.COLUMN_NAME_TIME_ADDED, System.currentTimeMillis());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);



        return rowId;
    }

    public long addRecordinglog(int refid,String recordingName, String filePath, long length) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItemLog._ID, refid);
        cv.put(DBHelperItemLog.COLUMN_NAME_RECORDING_NAME, recordingName);
        cv.put(DBHelperItemLog.COLUMN_NAME_RECORDING_FILE_PATH, filePath);
        cv.put(DBHelperItemLog.COLUMN_NAME_RECORDING_LENGTH, length);
        cv.put(DBHelperItemLog.COLUMN_NAME_TIME_ADDED, System.currentTimeMillis());
        long rowId = db.insert(DBHelperItemLog.TABLE_NAME, null, cv);



        return rowId;
    }

    public void renameItem(RecordingItem item, String recordingName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, recordingName);
        db.update(DBHelperItem.TABLE_NAME, cv,
                DBHelperItem._ID + "=" + item.getId(), null);


    }

    public long restoreRecording(RecordingItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_NAME, item.getName());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_FILE_PATH, item.getFilePath());
        cv.put(DBHelperItem.COLUMN_NAME_RECORDING_LENGTH, item.getLength());
        cv.put(DBHelperItem.COLUMN_NAME_TIME_ADDED, item.getTime());
        cv.put(DBHelperItem._ID, item.getId());
        long rowId = db.insert(DBHelperItem.TABLE_NAME, null, cv);

        return rowId;
    }
}
