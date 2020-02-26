package com.example.bookfirm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bookfirm.R;
import com.example.bookfirm.models.Book;
import com.example.bookfirm.models.Order;

import java.util.ArrayList;

public class OrdersDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookfirm";

    private static final String COL_STATUS = "status";
    private static final String COL_BOOK_ID = "book_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_ID = "id";
    private static final String[] COLUMNS = {COL_ID, COL_USER_ID, COL_BOOK_ID, COL_STATUS};
    private static final String TABLE_NAME = "orders";

    public OrdersDatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, R.string.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String ORDERS_TABLE = "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
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

    public void addOrders(ArrayList<Order> orders) {

        SQLiteDatabase db = this.getWritableDatabase();

        for(Order order : orders) {
            ContentValues values = new ContentValues();
            values.put(COL_USER_ID, order.getUser_id());
            values.put(COL_BOOK_ID, order.getBook_id());
            values.put(COL_STATUS, order.getStatus());
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }

    public ArrayList<Order> allOrders() {

        ArrayList<Order> orders = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Order order;

        if (cursor.moveToFirst()) {
            do {

                order = new Order();
                order.setId(cursor.getInt(0));
                order.setUser_id(cursor.getInt(1));
                order.setBook_id(cursor.getInt(2));
                order.setStatus(cursor.getString(3));
                orders.add(order);

            } while (cursor.moveToNext());

        }

        cursor.close();
        return orders;
    }

    public void getOrdersOfUser(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  = db.rawQuery("SELECT o.id, o.user_id, o.book_id, o.status FROM orders o INNER JOIN books b ON(o.book_id = b.id) WHERE o.user_id = ?", new String[]{String.valueOf(userId)});



        cursor.close();
    }
}
