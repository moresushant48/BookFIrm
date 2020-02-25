package com.example.bookfirm;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookfirm.db.BookDatabaseHandler;
import com.example.bookfirm.models.Book;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class BookExtendedView extends AppCompatActivity implements TextWatcher {

    private TextView bookName, bookDesc, bookAvailQuantity, bookPrice, bookSellerEmail;
    private EditText enteredQuantity;
    private Book book;
    private String newPrice;
    private ImageView bookImage;
    private ImageButton btnBack;

    private ExtendedFloatingActionButton btnBuyBook;

    private RelativeLayout thisLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_extended_view);

        Objects.requireNonNull(getSupportActionBar()).hide();

        thisLayout = findViewById(R.id.bookExtendedRelLayout);

        bookImage = findViewById(R.id.imgDetailProductImage);
        bookName = findViewById(R.id.txtDetailBookName);
        bookDesc = findViewById(R.id.txtDetailBookDesc);
        bookAvailQuantity = findViewById(R.id.txtMaxBookQuantity);
        bookPrice = findViewById(R.id.txtDetailBookPrice);
        bookSellerEmail = findViewById(R.id.txtDetailBookSellerEmail);
        enteredQuantity = findViewById(R.id.etDetailBookQuantity);
        btnBuyBook = findViewById(R.id.btnBuyBook);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        book = (Book) getIntent().getSerializableExtra("book");


        btnBuyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (enteredQuantity.getText().toString().isEmpty()) {
                    enteredQuantity.setText(String.valueOf(1));
                }

                Intent intent = new Intent(BookExtendedView.this, PurchaseBookView.class);
                intent.putExtra("book", book);
                intent.putExtra("finalPrice", bookPrice.getText().toString().trim());
                intent.putExtra("selectedQuantity", enteredQuantity.getText().toString().trim());
                startActivity(intent);

            }
        });


        enteredQuantity.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        try {
            int enteredValue = Integer.parseInt(charSequence.toString().trim());

            if (enteredValue > book.getQuantity()) {
                Snackbar.make(thisLayout, "Only " + book.getQuantity() + " units are available.", Snackbar.LENGTH_SHORT).show();
                btnBuyBook.setEnabled(false);
            } else {
                btnBuyBook.setEnabled(true);
            }

            newPrice = String.valueOf(book.getPrice() * enteredValue);
            bookPrice.setText(newPrice);

            if (charSequence.toString().trim().isEmpty())
                bookPrice.setText(book.getPrice());
        } catch (NumberFormatException e) {
            e.getMessage();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (book != null) {
            BookDatabaseHandler dbBook = new BookDatabaseHandler(this);
            Book oneBookDetails = dbBook.getBook(book.getId());
            Glide.with(this).load(oneBookDetails.getImage()).into(bookImage);
            bookName.setText(oneBookDetails.getBookName());
            bookDesc.setText(oneBookDetails.getBookDesc());
            bookSellerEmail.setText(oneBookDetails.getUsername());
            bookPrice.setText(String.valueOf(oneBookDetails.getPrice()));
            bookAvailQuantity.setText("(Max " + oneBookDetails.getQuantity() + ")");

            if (book.getSellType().equals("SELL"))
                btnBuyBook.setText("Buy Now");
            else
                btnBuyBook.setText("Rent Now");

        }
    }
}
