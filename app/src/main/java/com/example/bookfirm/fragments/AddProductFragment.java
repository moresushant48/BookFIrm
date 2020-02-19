package com.example.bookfirm.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookfirm.R;
import com.example.bookfirm.db.BookDatabaseHandler;
import com.example.bookfirm.models.Book;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class AddProductFragment extends Fragment {

    private static final int CODE_PICK_IMAGE = 1000;
    private ScrollView thisLayout;
    private TextInputEditText etBookName, etBookDesc, etPrice, etQuantity;
    private ImageView imgProduct;
    private Button btnAddImage, btnAddProduct;
    private RadioGroup rdSellType;
    private RadioButton rdBtn;

    private BookDatabaseHandler dbBook;

    private String loggedUsername;
    private byte[] byteProductImage;

    public static byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try {
                byteBuffer.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
        return bytesResult;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedUsername = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE).getString("email", "undefined");
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.nav_add_book));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_product, container, false);

        dbBook = new BookDatabaseHandler(getContext());

        thisLayout = view.findViewById(R.id.addProductScrollLayout);

        etBookName = view.findViewById(R.id.book_name);
        etBookDesc = view.findViewById(R.id.book_desc);
        etPrice = view.findViewById(R.id.price);
        etQuantity = view.findViewById(R.id.quantity);

        rdSellType = view.findViewById(R.id.sell_type);

        btnAddImage = view.findViewById(R.id.btnAddPhoto);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        imgProduct = view.findViewById(R.id.imgProduct);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = rdSellType.getCheckedRadioButtonId();
                rdBtn = getView().findViewById(selectedId);
                addProductToDB();
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Intent chooser = Intent.createChooser(intent, "Choose an application");
                startActivityForResult(chooser, CODE_PICK_IMAGE);
            }
        });

        return view;
    }

    private void addProductToDB() {

        if (etBookName.getText().toString().isEmpty() || etQuantity.getText().toString().isEmpty()
                || etPrice.getText().toString().isEmpty() || etBookDesc.getText().toString().isEmpty()
                || byteProductImage == null) {

            Snackbar.make(thisLayout, "Please fill everything properly.", Snackbar.LENGTH_SHORT).show();

        } else {

            Book book = new Book(etBookName.getText().toString().trim(),
                    etBookDesc.getText().toString().trim(),
                    byteProductImage,
                    Integer.parseInt(etPrice.getText().toString().trim()),
                    rdBtn.getText().toString().trim(),
                    loggedUsername,
                    Integer.parseInt(etQuantity.getText().toString().trim())
            );

            ArrayList<Book> books = new ArrayList<>();
            books.add(book);
            dbBook.addBooks(books);

            Snackbar.make(thisLayout, "Added new Book Product.", Snackbar.LENGTH_SHORT).show();

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new AddProductFragment()).commit();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_PICK_IMAGE) {
            Glide.with(this).load(data.getData()).into(imgProduct);
            imgProduct.setVisibility(View.VISIBLE);
            try {
                InputStream iStream = getContext().getContentResolver().openInputStream(data.getData());
                byteProductImage = getBytes(iStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}