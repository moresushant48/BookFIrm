package com.example.bookfirm.models;

import java.io.Serializable;

public class Book implements Serializable {

    private Integer id;

    private String bookName;

    private String bookDesc;

    private byte[] image;

    private int price;

    private String sellType;

    private String username;

    private int quantity;

    public Book() {
    }

    public Book(String bookName, String bookDesc, byte[] image, int price, String sellType, String username, int quantity) {
        this.bookName = bookName;
        this.bookDesc = bookDesc;
        this.image = image;
        this.price = price;
        this.sellType = sellType;
        this.username = username;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", bookDesc='" + bookDesc + '\'' +
                ", price=" + price +
                ", sellType='" + sellType + '\'' +
                ", username='" + username + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
