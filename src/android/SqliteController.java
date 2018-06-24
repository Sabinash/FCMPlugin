package com.gae.scaffolder.plugin;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Abinash.Sahoo on 4/5/2017.
 */

public class SqliteController extends SQLiteOpenHelper {


  // All Static variables
  // Database Version
  private static final int DATABASE_VERSION = 1;

  // Database Name
  private static final String DATABASE_NAME = "AldarMessageCenter";

  // Contacts table name
  private static final String TABLE_NOTIFICATIONS = "Notifications";

  // Contacts Table Columns names
  private static final String KEY_ID = "id";
  private static final String KEY_MESSAGE = "message";
  private static final String KEY_TITLE = "title";
  private static final String KEY_FLAG = "flag";
  private static final String KEY_MESSAGEDATE = "messagedate";
  private static final String KEY_USERID = "messageid";


  public SqliteController(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  // Creating Tables
  @Override
  public void onCreate(SQLiteDatabase db) {
    String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
      + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MESSAGE + " TEXT,"+ KEY_TITLE + " TEXT,"
      + KEY_FLAG + " TEXT,"
      + KEY_MESSAGEDATE + " TEXT," + KEY_USERID + " TEXT" + ")";
    db.execSQL(CREATE_CONTACTS_TABLE);
  }

  // Upgrading database
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Drop older table if existed
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

    // Create tables again
    onCreate(db);
  }

  /**
   * All CRUD(Create, Read, Update, Delete) Operations
   */

  // Adding new Notification
  public void insertNotification(Notifications mNotification) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_MESSAGE, mNotification.getMessage());
    values.put(KEY_TITLE,mNotification.getTitle());
    values.put(KEY_FLAG, "false");
    values.put(KEY_MESSAGEDATE,mNotification.getMessageDate());
    values.put(KEY_USERID,mNotification.getMessageId());


    // Inserting Row
    db.insert(TABLE_NOTIFICATIONS, null, values);
    db.close(); // Closing database connection
  }



  // Getting All Notifications
  public List<Notifications> getAllNotifications() {
    List<Notifications> notificationList = new ArrayList<Notifications>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        Notifications mNotification = new Notifications();
        mNotification.setId(Integer.parseInt(cursor.getString(0)));
        mNotification.setMessage(cursor.getString(1));
        mNotification.setTitle(cursor.getString(2));
        mNotification.setFlag(cursor.getString(3));
        mNotification.setMessageDate(cursor.getString(4));
        mNotification.setMessageId(cursor.getString(5));


        // Adding  to list
        notificationList.add(mNotification);
      } while (cursor.moveToNext());
    }

    // return contact list
    return notificationList;
  }

  // Updating single Notification
  public int updateNotification(String id, String flag) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_FLAG, flag);
    // updating row
    return db.update(TABLE_NOTIFICATIONS, values, KEY_USERID + " = ?",
      new String[] { String.valueOf(id) });
  }

  // Deleting single Notification
  public void deleteNotification(Notifications mNotification) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NOTIFICATIONS, KEY_ID + " = ?",
      new String[] { String.valueOf(mNotification.getId()) });
    db.close();
  }


  // Getting contacts Notification
  public int getNotificationsCount() {
    String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    // return count
    return cursor.getCount();
  }

}


