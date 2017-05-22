package com.example.drachim.festivalapp.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.example.drachim.festivalapp.data.Festival;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FestivalPlannerDbHelper extends SQLiteOpenHelper {

    private static class FestivalEntry implements BaseColumns {
        static final String TABLE_NAME = "Festival";
        static final String COLUMN_NAME = "Name";
        static final String COLUMN_DESCRIPTION = "Description";
        static final String COLUMN_COUNTRY = "Country";
        static final String COLUMN_PLACE = "Place";
        static final String COLUMN_POSTALCODE = "PostalCode";
        static final String COLUMN_STREET = "Street";
        static final String COLUMN_STARTDATE = "StartDate";
        static final String COLUMN_ENDDATE = "EndDate";
        static final String COLUMN_LINEUP = "Lineup";
        static final String COLUMN_PROFILEIMAGE = "ProfileImage";
        static final String COLUMN_TITLEIMAGE = "TitleImage";
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FestivalEntry.TABLE_NAME + " (" +
            FestivalEntry._ID + " INTEGER PRIMARY KEY," +
            FestivalEntry.COLUMN_NAME + " TEXT," +
            FestivalEntry.COLUMN_DESCRIPTION + " TEXT," +
            FestivalEntry.COLUMN_COUNTRY + " TEXT," +
            FestivalEntry.COLUMN_PLACE + " TEXT," +
            FestivalEntry.COLUMN_POSTALCODE + " TEXT," +
            FestivalEntry.COLUMN_STREET + " TEXT," +
            FestivalEntry.COLUMN_STARTDATE + " INTEGER," +
            FestivalEntry.COLUMN_ENDDATE + " INTEGER," +
            FestivalEntry.COLUMN_LINEUP + " TEXT," +
            FestivalEntry.COLUMN_PROFILEIMAGE + " INTEGER," +
            FestivalEntry.COLUMN_TITLEIMAGE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FestivalEntry.TABLE_NAME;

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "FestivalPlanner.db";

    public FestivalPlannerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        for (Festival festival : new FestivalExampleData().getFestivals()) {
            CreateFestival(festival, db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    private void CreateFestival(final Festival festival, final SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(FestivalEntry.COLUMN_NAME, festival.getName());
        values.put(FestivalEntry.COLUMN_DESCRIPTION, festival.getDescription());
        values.put(FestivalEntry.COLUMN_COUNTRY, festival.getCountry());
        values.put(FestivalEntry.COLUMN_PLACE, festival.getPlace());
        values.put(FestivalEntry.COLUMN_POSTALCODE, festival.getPostalCode());
        values.put(FestivalEntry.COLUMN_STREET, festival.getStreet());
        values.put(FestivalEntry.COLUMN_STARTDATE, festival.getStartDate().getTime());
        values.put(FestivalEntry.COLUMN_ENDDATE, festival.getEndDate().getTime());
        values.put(FestivalEntry.COLUMN_LINEUP, TextUtils.join(";", festival.getLineup()));
        values.put(FestivalEntry.COLUMN_PROFILEIMAGE, festival.getProfileImage());
        values.put(FestivalEntry.COLUMN_TITLEIMAGE, festival.getTitleImage());

        db.insert(FestivalEntry.TABLE_NAME, null, values);
    }

    public List<Festival> ReadFestivals() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FestivalEntry.TABLE_NAME, null, null, null, null, null, null);

        List<Festival> festivals = new ArrayList<>();
        while (cursor.moveToNext()) {
            Festival festival = getFestival(cursor);

            festivals.add(festival);
        }
        cursor.close();
        return festivals;
    }

    private Festival getFestival(Cursor cursor) {
        Festival festival = new Festival();
        festival.setId(cursor.getInt(cursor.getColumnIndex(FestivalEntry._ID)));
        festival.setName(cursor.getString(cursor.getColumnIndex(FestivalEntry.COLUMN_NAME)));
        festival.setDescription(cursor.getString(cursor.getColumnIndex(FestivalEntry.COLUMN_DESCRIPTION)));
        festival.setCountry(cursor.getString(cursor.getColumnIndex(FestivalEntry.COLUMN_COUNTRY)));
        festival.setPlace(cursor.getString(cursor.getColumnIndex(FestivalEntry.COLUMN_PLACE)));
        festival.setPostalCode(cursor.getString(cursor.getColumnIndex(FestivalEntry.COLUMN_POSTALCODE)));
        festival.setStreet(cursor.getString(cursor.getColumnIndex(FestivalEntry.COLUMN_STREET)));
        festival.setStartDate(new Date(cursor.getLong(cursor.getColumnIndex(FestivalEntry.COLUMN_STARTDATE))));
        festival.setEndDate(new Date(cursor.getLong(cursor.getColumnIndex(FestivalEntry.COLUMN_ENDDATE))));
        festival.setLineup(Arrays.asList(TextUtils.split(cursor.getString(cursor.getColumnIndex(FestivalEntry.COLUMN_LINEUP)), ";")));
        festival.setProfileImage(cursor.getInt(cursor.getColumnIndex(FestivalEntry.COLUMN_PROFILEIMAGE)));
        festival.setTitleImage(cursor.getInt(cursor.getColumnIndex(FestivalEntry.COLUMN_TITLEIMAGE)));
        return festival;
    }

    public Festival ReadFestival(int festivalId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FestivalEntry.TABLE_NAME, null, FestivalEntry._ID + " = ?", new String[]{String.valueOf(festivalId)}, null, null, null);
        cursor.moveToFirst();
        Festival festival = getFestival(cursor);
        cursor.close();
        return festival;
    }
}
