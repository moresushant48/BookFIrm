package com.example.bookfirm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookfirm.db.BookDatabaseHandler;
import com.example.bookfirm.models.Book;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MyProductExtendedView extends AppCompatActivity implements View.OnClickListener {

    private final static String INC = "INCREMENT";
    private final static String DEC = "DECREMENT";

    private Book book;
    private ImageView bookImage;
    private FloatingActionButton btnQtyIncrease, btnQtyDecrease;
    private ExtendedFloatingActionButton btnChangePrice;
    private TextView bookName, bookDesc, bookAvailQuantity, bookPrice;
    private ImageButton btnBack;

    private RelativeLayout thisLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_extended_view);

        Objects.requireNonNull(getSupportActionBar()).hide();

        thisLayout = findViewById(R.id.bookExtendedRelLayout);
        btnQtyIncrease = findViewById(R.id.btnQtyIncrease);
        btnQtyDecrease = findViewById(R.id.btnQtyDecrease);
        btnChangePrice = findViewById(R.id.btnChangePrice);
        bookImage = findViewById(R.id.imgDetailProductImage);
        bookName = findViewById(R.id.txtDetailBookName);
        bookDesc = findViewById(R.id.txtDetailBookDesc);
        bookAvailQuantity = findViewById(R.id.txtMaxBookQuantity);
        bookPrice = findViewById(R.id.txtDetailBookPrice);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        book = (Book) getIntent().getSerializableExtra("book");

        btnQtyIncrease.setOnClickListener(this);
        btnQtyDecrease.setOnClickListener(this);
        btnChangePrice.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAndSetDataIntoViews();
    }

    private void fetchAndSetDataIntoViews() {
        if (book != null) {
            BookDatabaseHandler dbBook = new BookDatabaseHandler(this);
            Book oneBookDetails = dbBook.getBook(book.getId());
            Glide.with(this).load(oneBookDetails.getImage()).into(bookImage);
            bookName.setText(oneBookDetails.getBookName());
            bookDesc.setText(oneBookDetails.getBookDesc());
            bookPrice.setText(String.valueOf(oneBookDetails.getPrice()));
            bookAvailQuantity.setText(String.valueOf(oneBookDetails.getQuantity()));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnQtyIncrease:
                askForQuantity(INC);
                break;
            case R.id.btnQtyDecrease:
                askForQuantity(DEC);
                break;
            case R.id.btnChangePrice:
                askForPrice();
                break;
        }
    }

    private void askForPrice() {

        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText(String.valueOf(book.getPrice()));

        final BookDatabaseHandler dbBook = new BookDatabaseHandler(this);

        new AlertDialog.Builder(this)
                .setTitle("Update Price")
                .setView(editText)
                .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!editText.getText().toString().trim().isEmpty())
                            dbBook.updatePrice(book.getId(), Integer.parseInt(editText.getText().toString().trim()));

                        fetchAndSetDataIntoViews();
                    }
                })
                .setNegativeButton("cancel", null)
                .create().show();
    }

    private void askForQuantity(final String option) {

        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        final BookDatabaseHandler dbBook = new BookDatabaseHandler(this);

        new AlertDialog.Builder(this)
                .setTitle(option)
                .setView(editText)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!editText.getText().toString().trim().isEmpty()) {

                            int availQty = Integer.parseInt(bookAvailQuantity.getText().toString().trim());
                            int enteredQty = Integer.parseInt(editText.getText().toString().trim());

                            if (option.equals(INC)) {
                                dbBook.updateQty(book.getId(), availQty + enteredQty);
                            } else if (option.equals(DEC)) {
                                dbBook.updateQty(book.getId(), availQty - enteredQty);
                            }

                            fetchAndSetDataIntoViews();
                        }
                    }
                }).setNegativeButton("Cancel", null)
                .create().show();
    }
}
