package com.example.bookfirm.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class MainFragment extends Fragment implements Adaptor.OnBookClickListener, SwipeRefreshLayout.OnRefreshListener {

    Context context = getContext();
    OnBookDetailListener onBookDetailListener;

    private BookDatabaseHandler dbBook;
    private UserDatabaseHandler dbUser;

    private RecyclerView rvBooks;
    private Adaptor adapterBooks;
    private ArrayList<Book> booksList;
    private SwipeRefreshLayout refreshBooks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.nav_home));

        refreshBooks = view.findViewById(R.id.refreshBooks);

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

        refreshBooks.setOnRefreshListener(this);
        onRefresh();
        /*ArrayList<User> users;
        users = dbUser.allUsers();
        for (User u : users) {
            Log.e("Username : ", u.getEmail());
            Log.e("Password : ", u.getPassword());
        }*/
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

    private byte[] getBytesFromDrawable(int id) {
        Drawable drawable = getResources().getDrawable(id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();
        return bitMapData;
    }

    private ArrayList<Book> getMyList()
    {
        ArrayList<Book> books = new ArrayList<>();

        Book m = new Book();
        m.setBookName("computer network notes");
        m.setBookDesc("this is an sem 3 notes of B.Sc. IT");
        m.setImage(getBytesFromDrawable(R.drawable.computernetworks));
        m.setPrice(100);
        m.setQuantity(4);
        m.setSellType("SELL");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Business Intelligence notes");
        m.setBookDesc("this is an sem 6 notes of B.Sc. IT");
        m.setImage(getBytesFromDrawable(R.drawable.businessintelligence));
        m.setPrice(100);
        m.setQuantity(4);
        m.setSellType("RENT");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Core Java notes");
        m.setBookDesc("this is an sem 4 notes of B.Sc. IT");
        m.setImage(getBytesFromDrawable(R.drawable.corejava));
        m.setPrice(120);
        m.setQuantity(4);
        m.setSellType("SELL");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Advance java notes");
        m.setBookDesc("this is an sem 5 notes of B.Sc. IT");
        m.setImage(getBytesFromDrawable(R.drawable.advancedjava));
        m.setPrice(139);
        m.setQuantity(4);
        m.setSellType("RENT");
        m.setUsername("pawan");
        books.add(m);

        m = new Book();
        m.setBookName("Data Structure notes");
        m.setBookDesc("this is an sem 3 notes of B.Sc. IT");
        m.setImage(getBytesFromDrawable(R.drawable.datastructure));
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

    @Override
    public void onRefresh() {
        // Get the books list from database.
        booksList = dbBook.allBooks();

        if (booksList != null) {
            adapterBooks = new Adaptor(context, booksList, MainFragment.this);
            rvBooks.setAdapter(adapterBooks);
        }

        refreshBooks.setRefreshing(false);
    }

    public interface OnBookDetailListener {
        void onBookSent(Book book);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint("Type here to Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterBooks.getFilter().filter(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}


