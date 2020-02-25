package com.example.bookfirm;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookfirm.db.BookDatabaseHandler;
import com.example.bookfirm.models.Book;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class PurchaseBookView extends AppCompatActivity {

    private Book book;
    private String txtFinalPrice, txtSelectedQuantity;

    private TextView bookName, bookFinalPrice, bookSelectedQuantity;
    private ImageView bookImage;
    private RadioGroup rdGroupPaymentSelection;
    private RadioButton rdBtnSelectedPaymentOption;
    private ExtendedFloatingActionButton btnProceedPayment;
    private ImageButton btnBack;

    private RelativeLayout thisLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_book_view);

        Objects.requireNonNull(getSupportActionBar()).hide();

        /*
         * Initiating everything up.
         * */
        thisLayout = findViewById(R.id.purBookRelLayout);
        bookImage = findViewById(R.id.purBookImage);
        bookName = findViewById(R.id.purBookName);
        bookSelectedQuantity = findViewById(R.id.purBookSelectedQuantity);
        bookFinalPrice = findViewById(R.id.purBookFinalPrice);
        rdGroupPaymentSelection = findViewById(R.id.rdGroupPaymentSelection);
        btnProceedPayment = findViewById(R.id.btnProceedPayment);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
         * Getting all the data.
         * */
        book = (Book) getIntent().getSerializableExtra("book");
        txtSelectedQuantity = getIntent().getStringExtra("selectedQuantity");
        txtFinalPrice = getIntent().getStringExtra("finalPrice");

        /*
         * Setting up all the data.
         * */
        Glide.with(this).load(book.getImage()).into(bookImage);
        bookName.setText(book.getBookName());
        bookSelectedQuantity.setText(txtSelectedQuantity + " Qty.");
        bookFinalPrice.setText(txtFinalPrice);

        btnProceedPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rdGroupPaymentSelection.getCheckedRadioButtonId() == -1) {
                    Snackbar.make(thisLayout, "Please choose the payment option.", Snackbar.LENGTH_SHORT).show();
                } else {

                    proceedPaymentFromSelection();
                }
            }
        });
    }

    private void proceedPaymentFromSelection() {

        /*switch (selectedPaymentOption) {
            case R.id.rdBtnPaytm: {
                Snackbar.make(thisLayout, "Thank you.", Snackbar.LENGTH_SHORT).show();
            }
            case R.id.rdBtnBhimUpi: {
                Snackbar.make(thisLayout, "Thank you.", Snackbar.LENGTH_SHORT).show();
            }
            case R.id.rdBtnDebit: {
                Snackbar.make(thisLayout, "Thank you.", Snackbar.LENGTH_SHORT).show();
            }
            case R.id.rdBtnOnMeet: {
                Snackbar.make(thisLayout, "Thank you.", Snackbar.LENGTH_SHORT).show();
            }
        }*/

        Snackbar.make(thisLayout, "Waiting for the Payment.", Snackbar.LENGTH_INDEFINITE).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(thisLayout, "Payment Received.", Snackbar.LENGTH_INDEFINITE).show();
                decreaseQuantity(book.getId(), txtSelectedQuantity);

            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(thisLayout, "Seller will contact you in a while. Thank you.", Snackbar.LENGTH_LONG).show();
            }
        }, 4000);

        btnProceedPayment.setEnabled(false);
    }

    private void decreaseQuantity(Integer id, String txtSelectedQuantity) {
        BookDatabaseHandler db = new BookDatabaseHandler(this);
        db.decreaseBookCount(id, book.getQuantity() - Integer.parseInt(txtSelectedQuantity));
    }
}
