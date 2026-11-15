package com.example.digitallibrary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Activity displaying user's currently borrowed books with capability to return items via confirmation dialogs
public class BorrowedBooksActivity extends AppCompatActivity {

    // Logcat tag for monitoring BorrowedBooksActivity lifecycle callbacks
    private static final String TAG = "DL_BorrowedBooksActivity";

    private RecyclerView recyclerView;
    private TextView textNoBorrowed;
    private BorrowedBookAdapter adapter;

    // Field holding reference to active dialog for safe dismissal on rotation/pause
    private AlertDialog currentDialog;

    // Called when activity is created; initializes toolbar, views, and adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_borrowed_books);

        setupToolbar();
        initViews();
        setupRecyclerView();
    }

    // Called when activity becomes visible
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    // Called when activity starts user interaction; refreshes borrowed books list
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        refreshBorrowedList();
    }

    // Called when activity is going into background; dismisses active dialog to prevent window leak on rotation
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
            currentDialog = null;
        }
    }

    // Called when activity is no longer visible
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    // Called when activity is restarted after stopping
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called");
    }

    // Called before activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
            currentDialog = null;
        }
    }

    // Called before activity destruction to save instance state
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState called");
    }

    // Restores UI state from savedInstanceState bundle after screen rotation
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState called");
    }

    // Configures action bar toolbar with back arrow
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_borrowed);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(R.string.title_borrowed_books);
            }
        }
    }

    // Initializes view references using findViewById
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_borrowed);
        textNoBorrowed = findViewById(R.id.text_no_borrowed);
    }

    // Sets up RecyclerView layout manager and adapter with return click listener
    private void setupRecyclerView() {
        ArrayList<Book> initialList = new ArrayList<>();
        adapter = new BorrowedBookAdapter(this, initialList, new BorrowedBookAdapter.OnReturnClickListener() {
            @Override
            public void onReturnClick(Book book) {
                showReturnConfirmDialog(book);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // Re-fetches current borrowed books list from LibraryRepository and updates UI
    private void refreshBorrowedList() {
        ArrayList<Book> currentBorrowed = LibraryRepository.getInstance().getBorrowedBooks();
        adapter.updateData(currentBorrowed);

        if (currentBorrowed == null || currentBorrowed.isEmpty()) {
            textNoBorrowed.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoBorrowed.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // Shows a confirmation dialog for returning a borrowed book
    private void showReturnConfirmDialog(final Book book) {
        if (book == null) {
            return;
        }

        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }

        String message = getString(R.string.dialog_confirm_return_msg, book.getTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_confirm_return_title);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentDialog = null;
                executeReturn(book);
            }
        });

        builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
            }
        });

        currentDialog = builder.create();
        currentDialog.show();
    }

    // Executes return in LibraryRepository, shows Toast message, and refreshes list
    private void executeReturn(Book book) {
        if (book == null) {
            return;
        }

        int resultCode = LibraryRepository.getInstance().returnBook(book.getId());
        if (resultCode == LibraryRepository.RESULT_SUCCESS) {
            String successMsg = getString(R.string.toast_return_success, book.getTitle());
            Toast.makeText(BorrowedBooksActivity.this, successMsg, Toast.LENGTH_SHORT).show();
            refreshBorrowedList();
        } else {
            String errorMsg = LibraryRepository.getInstance().getReturnErrorMessage(resultCode);
            Toast.makeText(BorrowedBooksActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    // Handles toolbar home back button click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
