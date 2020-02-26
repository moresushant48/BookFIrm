package com.example.bookfirm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bookfirm.R;
import com.example.bookfirm.models.Book;

import java.util.ArrayList;

public class BookDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookfirm";
    private static final String TABLE_NAME = "books";

    private static final String COL_ID = "id";
    private static final String COL_BOOKNAME = "bookname";
    private static final String COL_BOOKDESC = "bookdesc";
    private static final String COL_IMAGE = "image";
    private static final String COL_PRICE = "price";
    private static final String COL_SELLTYPE = "selltype";
    private static final String COL_USERNAME = "username";
    private static final String COL_QUANTITY = "quantity";
    private static final String[] COLUMNS = { COL_ID, COL_BOOKNAME, COL_BOOKDESC, COL_IMAGE, COL_PRICE, COL_SELLTYPE, COL_USERNAME, COL_QUANTITY };

    public BookDatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, R.string.DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "bookname TEXT, "
                + "bookdesc TEXT,"
                + "image BLOB,"
                + "price INTEGER,"
                + "selltype TEXT,"
                + "username TEXT,"
                + "quantity INTEGER )";

        sqLiteDatabase.execSQL(CREATION_TABLE);

        String TABLE_USERS = "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT, "
                + "email TEXT, "
                + "address TEXT,"
                + "mobileno TEXT,"
                + "password TEXT)";

        sqLiteDatabase.execSQL(TABLE_USERS);

        String ORDERS_TABLE = "CREATE TABLE orders(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER, "
                + "book_id INTEGER, "
                + "status TEXT )";

        sqLiteDatabase.execSQL(ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(sqLiteDatabase);

    }

    public void deleteOne(Book book) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ? ", new String[] { String.valueOf(book.getId()) });
        db.close();

    }

    public Book getBook(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " id = ?", new String[] {String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Book book = new Book();
        book.setId(cursor.getInt(0));
        book.setBookName(cursor.getString(1) );
        book.setBookDesc(cursor.getString(2));
        book.setImage(cursor.getBlob(3));
        book.setPrice(cursor.getInt(4));
        book.setSellType(cursor.getString(5));
        book.setUsername(cursor.getString(6));
        book.setQuantity(cursor.getInt(7));

        return book;
    }

    public ArrayList<Book> allBooks() {

        ArrayList<Book> books = new ArrayList<Book>();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Book book = null;

        if(cursor.moveToFirst()) {

            do {

                book = new Book();
                book.setId(cursor.getInt(0));
                book.setBookName(cursor.getString(1) );
                book.setBookDesc(cursor.getString(2));
                book.setImage(cursor.getBlob(3));
                book.setPrice(cursor.getInt(4));
                book.setSellType(cursor.getString(5));
                book.setUsername(cursor.getString(6));
                book.setQuantity(cursor.getInt(7));
                books.add(book);

            } while(cursor.moveToNext());

        }

        return books;
    }

    public ArrayList<Book> allBooksByUser(String username) {

        ArrayList<Book> books = new ArrayList<Book>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMNS, " username = ?", new String[] {username},
                null, null, null, null);
        Book book = null;

        if(cursor.moveToFirst()) {

            do {

                book = new Book();
                book.setId(cursor.getInt(0));
                book.setBookName(cursor.getString(1) );
                book.setBookDesc(cursor.getString(2));
                book.setImage(cursor.getBlob(3));
                book.setPrice(cursor.getInt(4));
                book.setSellType(cursor.getString(5));
                book.setUsername(cursor.getString(6));
                book.setQuantity(cursor.getInt(7));
                books.add(book);

            } while(cursor.moveToNext());

        }
        cursor.close();
        return books;
    }

    public void addBooks(ArrayList<Book> books) {

        SQLiteDatabase db = this.getWritableDatabase();

        for(Book book : books) {
            ContentValues values = new ContentValues();
            values.put(COL_BOOKNAME, book.getBookName());
            values.put(COL_BOOKDESC, book.getBookDesc());
            values.put(COL_IMAGE, book.getImage());
            values.put(COL_PRICE, book.getPrice());
            values.put(COL_SELLTYPE, book.getSellType());
            values.put(COL_USERNAME, book.getUsername());
            values.put(COL_QUANTITY, book.getQuantity());
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }

    /*
    *   Decrease the quantity for that item.
    * */
    public void decreaseBookCount(int id, int newQuantity){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_QUANTITY, newQuantity);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
    }

}
