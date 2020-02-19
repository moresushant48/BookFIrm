package com.example.bookfirm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookfirm.models.Book;

import java.util.ArrayList;

public class Adaptor extends RecyclerView.Adapter<Adaptor.MyHolder> {

    Context c;
    ArrayList<Book> books;
    OnBookClickListener onBookClickListener;

    public Adaptor(Context c, ArrayList<Book> books, OnBookClickListener onBookClickListener) {
        this.c = c;
        this.books = books;
        this.onBookClickListener = onBookClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);

        return new MyHolder(view, onBookClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        myHolder.mTitle.setText(books.get(i).getBookName());
        myHolder.mDesc.setText(books.get(i).getBookDesc());
        myHolder.mSellType.setText(books.get(i).getSellType());

        Bitmap bmp = BitmapFactory.decodeByteArray(books.get(i).getImage(), 0, books.get(i).getImage().length);
        myHolder.mImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 150,
                150, false));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface OnBookClickListener {
        void onBookClick(int position);
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        TextView mTitle, mDesc, mSellType;
        OnBookClickListener onBookClickListener;

        MyHolder(@NonNull View itemView, OnBookClickListener onBookClickListener) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.iv_image);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDesc = itemView.findViewById(R.id.tv_description);
            mSellType = itemView.findViewById(R.id.tv_selltype);

            this.onBookClickListener = onBookClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onBookClickListener.onBookClick(getAdapterPosition());
        }
    }
}
