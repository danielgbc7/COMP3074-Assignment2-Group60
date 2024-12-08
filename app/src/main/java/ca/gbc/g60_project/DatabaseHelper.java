// COMP3074 - Assignment 2 - Group 60
package ca.gbc.g60_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TipCalculator.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_HISTORY = "history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIP_PERCENTAGE = "tip_percentage";
    private static final String COLUMN_BILL_AMOUNT = "bill_amount";
    private static final String COLUMN_TIP_AMOUNT = "tip_amount";
    private static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String COLUMN_SHARE_PER_PERSON = "share_per_person";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TIP_PERCENTAGE + " REAL,"
                + COLUMN_BILL_AMOUNT + " REAL,"
                + COLUMN_TIP_AMOUNT + " REAL,"
                + COLUMN_TOTAL_AMOUNT + " REAL,"
                + COLUMN_SHARE_PER_PERSON + " REAL"
                + ")";
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public void addHistoryEntry(String[] entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIP_PERCENTAGE, Double.parseDouble(entry[0]));
        values.put(COLUMN_BILL_AMOUNT, Double.parseDouble(entry[1]));
        values.put(COLUMN_TIP_AMOUNT, Double.parseDouble(entry[2]));
        values.put(COLUMN_TOTAL_AMOUNT, Double.parseDouble(entry[3]));
        values.put(COLUMN_SHARE_PER_PERSON, Double.parseDouble(entry[4]));
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public List<String[]> getAllHistoryEntries() {
        List<String[]> historyEntries = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String[] entry = new String[6]; // Changed from 5 to 6
                entry[0] = String.valueOf(cursor.getInt(0)); // ID
                entry[1] = String.valueOf(cursor.getDouble(1));
                entry[2] = String.valueOf(cursor.getDouble(2));
                entry[3] = String.valueOf(cursor.getDouble(3));
                entry[4] = String.valueOf(cursor.getDouble(4));
                entry[5] = String.valueOf(cursor.getDouble(5));
                historyEntries.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyEntries;
    }

    public void deleteHistoryEntry(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, COLUMN_ID + " = ?", new String[]{id});
        db.close();
    }
}

