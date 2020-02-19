package com.example.bookfirm.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookfirm.Adaptor;
import com.example.bookfirm.R;
import com.example.bookfirm.db.BookDatabaseHandler;
import com.example.bookfirm.db.UserDatabaseHandler;
import com.example.bookfirm.models.Book;
import com.example.bookfirm.models.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment implements Adaptor.OnBookClickListener {

    Context context = getContext();
    OnBookDetailListener onBookDetailListener;

    private BookDatabaseHandler dbBook;
    private UserDatabaseHandler dbUser;

    private RecyclerView rvBooks;
    private Adaptor adapterBooks;
    private ArrayList<Book> booksList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.nav_home));

        rvBooks = view.findViewById(R.id.recyclerView);
        rvBooks.setLayoutManager(new LinearLayoutManager(context));

        adapterBooks = new Adaptor(context, new ArrayList<Book>(), MainFragment.this);
        rvBooks.setAdapter(adapterBooks);

        dbBook = new BookDatabaseHandler(getContext());
        dbUser = new UserDatabaseHandler(getContext());

        if (Objects.requireNonNull(getContext()).getSharedPreferences("app", MODE_PRIVATE).getBoolean("isFirstRun", true)) {
            ArrayList<Book> books = getMyList();
            dbBook.addBooks(books); // if it the first run, add simple list of default books to db.
            dbUser.addUser(getMyUser()); // if it the first run, add simple user to db.
        }

        // Get the books list from database.
        booksList = dbBook.allBooks();

        if (booksList != null) {
            adapterBooks = new Adaptor(context, booksList, MainFragment.this);
            rvBooks.setAdapter(adapterBooks);
        }

        ArrayList<User> users;
        users = dbUser.allUsers();
        for (User u : users) {
            Log.e("Username : ", u.getEmail());
            Log.e("Password : ", u.getPassword());
        }
/*
        Log.e("Login Data ::::: ", "");

        User user = dbUser.login("dhondesooraj@gmail.com","123456");
        Log.e("Username : ", user.getEmail());
        Log.e("Password : ", user.getPassword());
        Log.e("Address : ", user.getAddress());*/

        return view;
    }

    private User getMyUser() {
        return new User("admin", "admin@gmail.com", "Mumbai", "1234567890", "123456");
    }

    private ArrayList<Book> getMyList() {

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bi1);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        ArrayList<Book> books = new ArrayList<>();

        Book m = new Book();
        m.setBookName("computer network notes");
        m.setBookDesc("this is an sem 3 notes of B.Sc. IT");
        m.setImage(bitMapData);
        m.setPrice(100);
        m.setQuantity(4);
        m.setSellType("SELL");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Business Intelligence notes");
        m.setBookDesc("this is an sem 6 notes of B.Sc. IT");
        m.setImage(bitMapData);
        m.setPrice(100);
        m.setQuantity(4);
        m.setSellType("RENT");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Core Java notes");
        m.setBookDesc("this is an sem 4 notes of B.Sc. IT");
        m.setImage(bitMapData);
        m.setPrice(120);
        m.setQuantity(4);
        m.setSellType("SELL");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Advance java notes");
        m.setBookDesc("this is an sem 5 notes of B.Sc. IT");
        m.setImage(bitMapData);
        m.setPrice(139);
        m.setQuantity(4);
        m.setSellType("RENT");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Data Structure notes");
        m.setBookDesc("this is an sem 3 notes of B.Sc. IT");
        m.setImage(bitMapData);
        m.setPrice(200);
        m.setQuantity(4);
        m.setSellType("SELL");
        m.setUsername("pawan");
        books.add(m);

        return books;
    }

    @Override
    public void onBookClick(int position) {
        onBookDetailListener.onBookSent(booksList.get(position));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnBookDetailListener) {
            onBookDetailListener = (OnBookDetailListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnBookDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onBookDetailListener = null;
    }

    public interface OnBookDetailListener {
        void onBookSent(Book book);
    }
}


