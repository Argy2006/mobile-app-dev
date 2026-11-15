package com.example.digitallibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Adapter class to display a list of Book items in a RecyclerView
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private ArrayList<Book> bookList;

    // ViewHolder class holding references to views for each book item layout
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textAuthor;
        TextView textCategory;
        TextView textStatus;
        Button buttonViewDetails;

        // Constructor initializing item views from the inflated layout
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_book_title);
            textAuthor = itemView.findViewById(R.id.text_book_author);
            textCategory = itemView.findViewById(R.id.text_book_category);
            textStatus = itemView.findViewById(R.id.text_book_status);
            buttonViewDetails = itemView.findViewById(R.id.button_view_details);
        }
    }

    // Constructor for BookAdapter with Context and book list
    public BookAdapter(Context context, ArrayList<Book> bookList) {
        this.context = context;
        this.bookList = (bookList != null) ? bookList : new ArrayList<Book>();
    }

    // Inflates the item layout xml and returns a new BookViewHolder instance
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    // Binds data from the Book object at position to the views in holder
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        final Book book = bookList.get(position);

        holder.textTitle.setText(book.getTitle());
        holder.textAuthor.setText("Author: " + book.getAuthor());
        holder.textCategory.setText("Category: " + book.getCategory());

        if (book.isAvailable()) {
            holder.textStatus.setText("Available");
            holder.textStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.textStatus.setText("Borrowed");
            holder.textStatus.setTextColor(Color.parseColor("#C62828"));
        }

        // Sets click listener to open BookDetailsActivity passing the unique book ID explicitly
        holder.buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("BOOK_ID", book.getId());
                context.startActivity(intent);
            }
        });
    }

    // Returns the total number of items in the book list
    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // Updates the adapter dataset with a new list and calls notifyDataSetChanged
    public void updateData(ArrayList<Book> newBooks) {
        this.bookList.clear();
        if (newBooks != null) {
            this.bookList.addAll(newBooks);
        }
        notifyDataSetChanged();
    }
}
