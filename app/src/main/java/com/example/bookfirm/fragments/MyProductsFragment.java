package com.example.bookfirm.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookfirm.Adaptor;
import com.example.bookfirm.R;
import com.example.bookfirm.db.BookDatabaseHandler;
import com.example.bookfirm.models.Book;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class MyProductsFragment extends Fragment implements Adaptor.OnBookClickListener {

    private LinearLayout thisLayout;
    private RecyclerView rvMyProducts;

    private Adaptor adaptorMyProducts;
    private String loggedUsername;
    private ArrayList<Book> booksList;

    private BookDatabaseHandler dbBook;

    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable deleteDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;
    private MainFragment.OnBookDetailListener onBookDetailListener;
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Delete list item and Delete file from the server, using Delete Service.
            deleteItemFromListAndDatabase(viewHolder.getAdapterPosition());
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;
            int itemHeight = itemView.getHeight();

            boolean isCancelled = dX == 0 && !isCurrentlyActive;

            if (isCancelled) {
                clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                return;
            }

            mBackground.setColor(backgroundColor);
            mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            mBackground.draw(c);

            int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            int deleteIconBottom = deleteIconTop + intrinsicHeight;

            deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            deleteDrawable.draw(c);

        }

        private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
            c.drawRect(left, top, right, bottom, mClearPaint);

        }
    };

    // Constructor
    public MyProductsFragment() {
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#b80f0a");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loggedUsername = Objects.requireNonNull(getContext()).getSharedPreferences("user", Context.MODE_PRIVATE).getString("email", "undefined");
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(getString(R.string.nav_my_books));

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_products_fragment, container, false);

        // Initialize some variables to use in Deletion Animation.
        deleteDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_delete);
        intrinsicWidth = Objects.requireNonNull(deleteDrawable).getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();

        dbBook = new BookDatabaseHandler(getContext());

        thisLayout = view.findViewById(R.id.myProductsLinearLayout);
        rvMyProducts = view.findViewById(R.id.rvMyProducts);

        rvMyProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptorMyProducts = new Adaptor(getContext(), new ArrayList<Book>(), MyProductsFragment.this);
        rvMyProducts.setAdapter(adaptorMyProducts);

        booksList = dbBook.allBooksByUser(loggedUsername);

        if (booksList != null) {
            adaptorMyProducts = new Adaptor(getContext(), booksList, MyProductsFragment.this);
            rvMyProducts.setAdapter(adaptorMyProducts);
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvMyProducts);
        }

        return view;
    }

    private void deleteItemFromListAndDatabase(int adapterPosition) {

        Book book = booksList.get(adapterPosition);
        //Delete from the recycler list.
        booksList.remove(adapterPosition);
        adaptorMyProducts.notifyItemRemoved(adapterPosition);

        // Delete from the Database.
        dbBook.deleteOne(book);

        Snackbar.make(thisLayout, "Deleted " + book.getBookName(), Snackbar.LENGTH_SHORT).show();
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
                adaptorMyProducts.getFilter().filter(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onBookClick(int position) {
        onBookDetailListener.onBookSent(booksList.get(position));
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainFragment.OnBookDetailListener) {
            onBookDetailListener = (MainFragment.OnBookDetailListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnBookDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onBookDetailListener = null;
    }
}
