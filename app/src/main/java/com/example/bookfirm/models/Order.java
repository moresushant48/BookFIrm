package com.example.bookfirm.models;

public class Order {

    private int id;

    private int user_id;

    private int book_id;

    private String status;

    public Order() {
    }

    public Order(int user_id, int book_id, String status) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
