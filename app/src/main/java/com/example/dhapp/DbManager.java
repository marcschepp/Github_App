package com.example.dhapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbManager extends SQLiteOpenHelper {

    static SQLiteDatabase db;


    public DbManager(Context context) {
        super(context,
                "stockDB.db",
                null,
                1);
        Log.d("hallo1234", this.toString());
        db=getWritableDatabase();
        //db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            db.execSQL("CREATE TABLE depot (" +
                    "depotID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "symbol TEXT NOT NULL," +
                    "open TEXT NOT NULL," +
                    "change TEXT NOT NULL)"
            );

            db.execSQL("CREATE INDEX depot_index ON depot(symbol)");

            db.execSQL("CREATE TABLE history (" +
                    "historyID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL)"
            );

            db.execSQL("CREATE INDEX history_index ON history(name)");


        } catch (SQLException e) {
        }
    }

    public void addDepotElement(String elementName, String elementSymbol, String elementOpen, String elementChange){
        db.execSQL("INSERT INTO depot (name, symbol, open, change) VALUES ('" + elementName + "', '" + elementSymbol + "', '" + elementOpen + "', '" + elementChange + "')");

    }

    public void addHistoryElement(String historyname) {
        db.execSQL("INSERT INTO history (name) VALUES ('" + historyname + "')");
    }

    public String[] ausgabeAktie() throws SQLException {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT valueID, symb, value " +
                        " FROM value " +
                        " ORDER BY valueID ASC",
                null);

// Ergebnis der Query auswerten
        int anzahlErgebnisZeilen = cursor.getCount();
        if (anzahlErgebnisZeilen == 0) {
            return new String[]{};
        }

        String[] resultStrings = new String[anzahlErgebnisZeilen];
        int counter = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            resultStrings[counter] = cursor.getString(0);
            counter++;
        }

        cursor.close();

        return resultStrings;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //leer
    }

    public String[] getElements(String ColumnName)  {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT "+ ColumnName + " FROM depot", null);
        int amountResultRows = cursor.getCount();
        if (amountResultRows == 0) {
            return new String[]{};
        }

        String[] resultValues = new String[amountResultRows];
        int counter = 0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            resultValues[counter] = cursor.getString(0);
            counter++;
        }

        cursor.close();
        return resultValues;
    }

    public void deleteHistoryElement(int id) {
        db.execSQL("DELETE FROM history WHERE historyID="+id);
    }
}
