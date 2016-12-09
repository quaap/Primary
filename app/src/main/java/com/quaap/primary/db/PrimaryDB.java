package com.quaap.primary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tom on 11/30/16.
 */

public class PrimaryDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static String DBNAME = "primarydb";

    //Note: the "passphrase" isn't intended to be cryptographically secure: it's just a mechanism to
    // prevent someone from using the wrong username
    private static final String USER_TABLE = "users";
    private static final String[] USER_COLS =     { "id", "name", "passphrase", "date" };
    private static final String[] USER_COLTYPES = {"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT", "TEXT", "DATETIME"};
    private static final String[] USER_INDEXES =  {"name"};

    private static final String CLASS_TABLE = "classes";
    private static final String[] CLASS_COLS =     {"id", "subject", "level"};
    private static final String[] CLASS_COLTYPES = {"INTEGER PRIMARY KEY AUTOINCREMENT", "TEXT", "TEXT"};
    private static final String[] CLASS_INDEXES =  {"subject, level"};

    private static final String CLASS_RECORD_TABLE = "classrecords";
    private static final String[] CLASS_RECORD_COLS =     {"classid", "userid", "date", "answered", "score"};
    private static final String[] CLASS_RECORD_COLTYPES = {"int references classes(id)", "int references users(id)", "date", "int", "float"};
    private static final String[] CLASS_RECORD_INDEXES =  {"classid, userid, date"};


    private static final String DETAIL_RECORD_TABLE = "detailrecords";
    private static final String[] DETAIL_RECORD_COLS =     {"classid", "userid", "date", "problem", "answer", "uanswer", "correct"};
    private static final String[] DETAIL_RECORD_COLTYPES = {"int references classes(id)", "int references users(id)", "date", "text", "text", "text", "boolean"};
    private static final String[] DETAIL_RECORD_INDEXES =  { "classid, userid, date"};

    public PrimaryDB(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(buildCreateTableStmt(USER_TABLE, USER_COLS, USER_COLTYPES));
        for(String stmt: buildIndexStmts(USER_TABLE, USER_INDEXES)) {
            db.execSQL(stmt);
        }

        db.execSQL(buildCreateTableStmt(CLASS_TABLE, CLASS_COLS, CLASS_COLTYPES));
        for(String stmt: buildIndexStmts(CLASS_TABLE, CLASS_INDEXES)) {
            db.execSQL(stmt);
        }

        db.execSQL(buildCreateTableStmt(CLASS_RECORD_TABLE, CLASS_RECORD_COLS, CLASS_RECORD_COLTYPES));
        for(String stmt: buildIndexStmts(CLASS_RECORD_TABLE, CLASS_RECORD_INDEXES)) {
            db.execSQL(stmt);
        }

        db.execSQL(buildCreateTableStmt(DETAIL_RECORD_TABLE, DETAIL_RECORD_COLS, DETAIL_RECORD_COLTYPES));
        for(String stmt: buildIndexStmts(DETAIL_RECORD_TABLE, DETAIL_RECORD_INDEXES)) {
            db.execSQL(stmt);
        }

        // //{"classid", "userid", "date", "problem", "answer", "uanswer", "correct"};
        db.execSQL("create view detailjoined as " +
                "select classid, userid, d.date, problem, answer, uanswer, correct, subject, level " +
                " from detailrecords d " +
                "   join users u on u.id=d.userid " +
                "   join classes c on c.id=d.classid "
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public User addUser(String name, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("passphrase", pass);
        values.put("date", new Date().getTime());
        int id = (int)db.insert(USER_TABLE, null, values);

        return getUser(id);
    }

    public User getUser(int id) {
        return getUserBy("id", id+"");
    }

    public User getUser(String name) {
        return getUserBy("name", name);
    }


    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, "id=?", new String[]{id+""});
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(USER_TABLE, USER_COLS, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    users.add(getUserFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return users;
    }


    private User getUserBy(String colname, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(USER_TABLE, USER_COLS, colname+"=?", new String[]{value}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                User user = getUserFromCursor(cursor);
                return user;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private User getUserFromCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setName(cursor.getString(1));
        user.setPass(cursor.getString(2));
        user.setDate(new Date(cursor.getLong(3)));
        return user;
    }


    private ClassSubject getClassSubject(String subject, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CLASS_TABLE, CLASS_COLS, "subject=? and level=?", new String[]{subject, level}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                ClassSubject csub = new ClassSubject();
                csub.setId(cursor.getInt(0));
                csub.setSubject(cursor.getString(1));
                csub.setLevel(cursor.getString(2));
                return csub;
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private ClassSubject addClassSubject(String subject, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject", subject);
        values.put("level", level);
        int id = (int)db.insert(CLASS_TABLE, null, values);
        return new ClassSubject(id, subject, level);
    }

    public void log(User user, String subject, String level, String problem, String answer, String uanswer, boolean correct) {
        ClassSubject csub = getClassSubject(subject, level);
        if (csub==null) {
            csub = addClassSubject(subject, level);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("classid", csub.getId());
        values.put("userid", user.getId());
        values.put("date", System.currentTimeMillis());
        values.put("problem", problem);
        values.put("answer", answer);
        values.put("uanswer", uanswer);
        values.put("correct", correct);

        db.insert(DETAIL_RECORD_TABLE, null, values);

        //{"classid", "userid", "date", "problem", "answer", "uanswer", "correct"};

    }

    public List<DetailRecord> getLogs(User user, String subject, String level, Date start, Date end) {
        List<DetailRecord> records = new ArrayList<>();
        ClassSubject csub = getClassSubject(subject, level);
        if (csub==null) return records;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DETAIL_RECORD_TABLE, DETAIL_RECORD_COLS,
                                    "classid=? and userid=? and date>? and date<?",
                      new String[]{csub.getId()+"", user.getId()+"", start.getTime()+"", end.getTime()+""}, null, null, "date");
        try {
            if (cursor.moveToFirst()) {
                do {
                    DetailRecord record = new DetailRecord();
                    record.setClassSubject(csub);
                    record.setUser(user);
                    record.setDate(new Date(cursor.getLong(2)));
                    record.setProblem(cursor.getString(3));
                    record.setAnswer(cursor.getString(4));
                    record.setUserAnswer(cursor.getString(5));
                    record.setCorrect(cursor.getInt(6)==1);
                    records.add(record);

                } while(cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }
        return records;
    }

    public List<DetailRecord> getLogs(User user, Date start, Date end) {
        List<DetailRecord> records = new ArrayList<>();


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query("detailjoined", null,
                "userid=? and date>? and date<?",
                new String[]{user.getId()+"", start.getTime()+"", end.getTime()+""}, null, null, "date");
        try {
            if (cursor.moveToFirst()) {
                do {

                    DetailRecord record = new DetailRecord();
                    ClassSubject csub = new ClassSubject();
                    csub.setId(cursor.getInt(0));
                    csub.setSubject(cursor.getString(7));
                    csub.setLevel(cursor.getString(8));
                    record.setClassSubject(csub);
                    record.setUser(user);
                    record.setDate(new Date(cursor.getLong(2)));
                    record.setProblem(cursor.getString(3));
                    record.setAnswer(cursor.getString(4));
                    record.setUserAnswer(cursor.getString(5));
                    record.setCorrect(cursor.getInt(6)!=0);
                    records.add(record);

                } while(cursor.moveToNext());

            }
        } finally {
            cursor.close();
        }
        return records;
    }


    public void logClass(User user, String subject, String level, int answered, float score) {
        ClassSubject csub = getClassSubject(subject, level);
        if (csub==null) {
            csub = addClassSubject(subject, level);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("classid", csub.getId());
        values.put("userid", user.getId());
        values.put("date", System.currentTimeMillis());
        values.put("answered", answered);
        values.put("score", score);

        db.insert(DETAIL_RECORD_TABLE, null, values);

        //{"classid", "userid", "date", "answered", "score"};
        //{"int references classes(id)", "int references users(id)", "date", "int", "float"};


    }

    private static String buildCreateTableStmt(String tablename, String[] cols, String[] coltypes) {

        String create =  "CREATE TABLE " + tablename + " (";
        for (int i=0; i<cols.length; i++) {
            create += cols[i] + " " + coltypes[i];
            if (i<cols.length-1) create += ", ";
        }
        create += ")";

        Log.d("Primary", create);

        return create;
    }

    private static String[] buildIndexStmts(String tablename, String [] indexes) {
        String [] creates = new String[indexes.length];


        for (int i=0; i<indexes.length; i++) {
            String indname = tablename + i;

            creates[i] = "create index " + indname + " on " + tablename + " (" + indexes[i] + ")";

            Log.d("Primary", creates[i]);
        }

        return creates;
    }

}



/*
create table users (
  id       int primary key,
  name     text,
  pass     text
);

create table classes (
  id       int primary key,
  subject  text,
  level    text
);

create table classrecords (
  userid   int references users(id),
  classid  int references classes(id),
  date     datetime,
  answered int,
  score    float
);


create table detailrecords (
  userid   references users(id),
  classid  references classes(id),
  date     datetime,
  problem  text,
  answer   text,
  uanswer  text,
  correct  bool
);


 */
