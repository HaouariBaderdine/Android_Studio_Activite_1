package com.e.activite_1.database;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ClientTable {

    // Database table
    public static final String TABLE_CLIENT = "Client";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SEXE = "sexe";
    public static final String COLUMN_NOM = "nom";
    public static final String COLUMN_ADRESSE= "adresse";
    public static final String COLUMN_EMAIL= "email";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_CLIENT
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_SEXE + " text not null, "
            + COLUMN_NOM  + " text not null,"
            + COLUMN_ADRESSE + " text not null,"
            + COLUMN_EMAIL + " text not null"
            + ");"
            ;

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ClientTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        onCreate(database);
    }
}
