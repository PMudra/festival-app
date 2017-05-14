package com.example.drachim.festivalapp.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.drachim.festivalapp.data.Festival;

import java.util.ArrayList;
import java.util.List;

public class FestivalPlannerDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FestivalPlannerContract.FestivalEntry.TABLE_NAME + " (" +
                    FestivalPlannerContract.FestivalEntry._ID + " INTEGER PRIMARY KEY," +
                    FestivalPlannerContract.FestivalEntry.COLUMN_NAME_TITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FestivalPlannerContract.FestivalEntry.TABLE_NAME;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FestivalPlanner.db";

    public FestivalPlannerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        CreateFestival(new Festival("Tante Mia Tanzt", null, null, null), db);
        CreateFestival(new Festival("Oldenbora", null, null, null), db);
        CreateFestival(new Festival("Rock am Ring", null, null, null), db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void CreateFestival(final Festival festival, final SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(FestivalPlannerContract.FestivalEntry.COLUMN_NAME_TITLE, festival.getName());

        db.insert(FestivalPlannerContract.FestivalEntry.TABLE_NAME, null, values);
    }

    public List<Festival> ReadFestivals() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                FestivalPlannerContract.FestivalEntry._ID,
                FestivalPlannerContract.FestivalEntry.COLUMN_NAME_TITLE,
        };

        Cursor cursor = db.query(
                FestivalPlannerContract.FestivalEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Festival> festivals = new ArrayList<>();
        while (cursor.moveToNext()) {
            festivals.add(new Festival(cursor.getString(cursor.getColumnIndexOrThrow(FestivalPlannerContract.FestivalEntry.COLUMN_NAME_TITLE)), null, null, null));
        }
        cursor.close();
        return festivals;
    }
}
