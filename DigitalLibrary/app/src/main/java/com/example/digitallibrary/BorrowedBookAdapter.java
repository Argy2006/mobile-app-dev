package com.example.digitallibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Adapter for displaying user's borrowed books in BorrowedBooksActivity
public class BorrowedBookAdapter extends RecyclerView.Adapter<BorrowedBookAdapter.BorrowedViewHolder> {

    // Interface callback for return button clicks on individual items
    public interface OnReturnClickListener {
        void onReturnClick(Book book);
    }

    private Context context;
    private ArrayList<Book> borrowedList;
    private OnReturnClickListener listener;

    // ViewHolder class holding references to borrowed book item views
    public static class BorrowedViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textAuthor;
        Button buttonReturn;

        // Constructor initializing item view references
        public BorrowedViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_borrowed_title);
            textAuthor = itemView.findViewById(R.id.text_borrowed_author);
            buttonReturn = itemView.findViewById(R.id.button_item_return);
        }
    }

    // Constructor for BorrowedBookAdapter
    public BorrowedBookAdapter(Context context, ArrayList<Book> borrowedList, OnReturnClickListener listener) {
        this.context = context;
        this.borrowedList = (borrowedList != null) ? borrowedList : new ArrayList<Book>();
        this.listener = listener;
    }

    // Inflates the item layout and returns a new BorrowedViewHolder
    @NonNull
    @Override
    public BorrowedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_borrowed_book, parent, false);
        return new BorrowedViewHolder(view);
    }

    // Binds borrowed book title and author to holder views and attaches return button click listener
    @Override
    public void onBindViewHolder(@NonNull BorrowedViewHolder holder, int position) {
        final Book book = borrowedList.get(position);

        holder.textTitle.setText(book.getTitle());
        holder.textAuthor.setText("Author: " + book.getAuthor());

        holder.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReturnClick(book);
                }
            }
        });
    }

    // Returns total count of borrowed books in list
    @Override
    public int getItemCount() {
        return borrowedList.size();
    }

    // Updates dataset with new borrowed books list and calls notifyDataSetChanged
    public void updateData(ArrayList<Book> newBooks) {
        this.borrowedList.clear();
        if (newBooks != null) {
            this.borrowedList.addAll(newBooks);
        }
        notifyDataSetChanged();
    }
}
