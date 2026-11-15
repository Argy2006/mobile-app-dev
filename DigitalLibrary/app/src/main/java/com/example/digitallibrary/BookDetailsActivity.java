package com.example.digitallibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// Activity displaying full details of a Book with availability info banner and borrow/return dialogs
public class BookDetailsActivity extends AppCompatActivity {

    // Logcat tag for monitoring BookDetailsActivity lifecycle
    private static final String TAG = "DL_BookDetailsActivity";

    private TextView textId;
    private TextView textTitle;
    private TextView textAuthor;
    private TextView textCategory;
    private TextView textStatus;
    private TextView textUnavailableNotice;
    private TextView bannerTitleTextView;
    private TextView bannerMessageTextView;
    private TextView textDescription;
    private Button buttonBorrow;
    private Button buttonReturn;
    private Button buttonBack;

    private String bookId;
    private Book currentBook;

    // Field holding reference to active dialog for safe dismissal on rotation/pause
    private AlertDialog currentDialog;

    // Called when activity is created; initializes views, loads book data, and logs lifecycle event
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_book_details);

        setupToolbar();
        initViews();
        readIntentData();
        setupClickListeners();
    }

    // Called when activity is becoming visible to user
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    // Called when activity starts interacting with user; refreshes book details and UI
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        refreshBookData();
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

    // Called when activity is no longer visible to user
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

    // Called before activity destruction
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
        outState.putString("BOOK_ID", bookId);
    }

    // Restores UI state from savedInstanceState bundle after screen rotation
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState called");
        if (savedInstanceState.containsKey("BOOK_ID")) {
            bookId = savedInstanceState.getString("BOOK_ID");
        }
    }

    // Configures action bar toolbar with back arrow
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Book Details");
            }
        }
    }

    // Initializes view references using findViewById
    private void initViews() {
        textId = findViewById(R.id.text_detail_id);
        textTitle = findViewById(R.id.text_detail_title);
        textAuthor = findViewById(R.id.text_detail_author);
        textCategory = findViewById(R.id.text_detail_category);
        textStatus = findViewById(R.id.text_detail_status);
        textUnavailableNotice = findViewById(R.id.text_unavailable_notice);
        bannerTitleTextView = findViewById(R.id.bannerTitleTextView);
        bannerMessageTextView = findViewById(R.id.bannerMessageTextView);
        textDescription = findViewById(R.id.text_detail_description);
        buttonBorrow = findViewById(R.id.button_borrow);
        buttonReturn = findViewById(R.id.button_return);
        buttonBack = findViewById(R.id.button_back);
    }

    // Reads the book ID from Intent extra and handles missing ID error case
    private void readIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("BOOK_ID")) {
            bookId = intent.getStringExtra("BOOK_ID");
        }

        if (bookId == null || bookId.trim().isEmpty()) {
            showErrorDialogAndFinish(getString(R.string.dialog_book_not_found));
        }
    }

    // Re-fetches current book data from LibraryRepository and updates UI elements
    private void refreshBookData() {
        if (bookId == null) {
            return;
        }

        currentBook = LibraryRepository.getInstance().getBookById(bookId);
        if (currentBook == null) {
            showErrorDialogAndFinish(getString(R.string.dialog_book_not_found));
            return;
        }

        updateUI();
    }

    // Updates TextViews, buttons, availability banner, and notices based on book details
    private void updateUI() {
        if (currentBook == null) {
            return;
        }

        textId.setText("ID: " + currentBook.getId());
        textTitle.setText(currentBook.getTitle());
        textAuthor.setText("Author: " + currentBook.getAuthor());
        textCategory.setText("Category: " + currentBook.getCategory());
        textDescription.setText(currentBook.getDescription());

        if (currentBook.isAvailable()) {
            textStatus.setText("Status: Available");
            textStatus.setTextColor(Color.parseColor("#2E7D32"));
            textUnavailableNotice.setVisibility(View.GONE);
            buttonBorrow.setVisibility(View.VISIBLE);
            buttonReturn.setVisibility(View.GONE);
        } else {
            textStatus.setText("Status: Borrowed");
            textStatus.setTextColor(Color.parseColor("#C62828"));
            textUnavailableNotice.setVisibility(View.VISIBLE);
            buttonBorrow.setVisibility(View.GONE);
            buttonReturn.setVisibility(View.VISIBLE);
        }

        // FEATURE 3: Update availability info banner title and message
        updateBanner(currentBook);
    }

    // Updates the availability info banner title and message based on the book's current status
    private void updateBanner(Book book) {
        if (book == null || bannerTitleTextView == null || bannerMessageTextView == null) {
            return;
        }

        if (book.isAvailable()) {
            bannerTitleTextView.setText("In Stock & Ready");
            bannerMessageTextView.setText("Available for immediate 14-day campus loan");
        } else {
            bannerTitleTextView.setText("Borrowed by you");
            bannerMessageTextView.setText("Return it from here or from My Borrowed Books");
        }
    }

    // Sets up click listeners for borrow button, return button, and back button
    private void setupClickListeners() {
        buttonBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBorrowButtonClicked();
            }
        });

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReturnButtonClicked();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Validates availability and borrow limits before showing borrow confirmation dialog
    private void onBorrowButtonClicked() {
        if (currentBook == null) {
            return;
        }

        if (!currentBook.isAvailable()) {
            showAlertDialog(getString(R.string.dialog_error_title), getString(R.string.dialog_unavailable_msg));
            return;
        }

        if (LibraryRepository.getInstance().getBorrowedCount() >= LibraryRepository.MAX_BORROW_LIMIT) {
            showAlertDialog(getString(R.string.dialog_limit_reached_title), getString(R.string.dialog_limit_reached_msg));
            return;
        }

        showBorrowConfirmDialog();
    }

    // Shows a confirmation dialog for borrowing a book with Cancel and Confirm buttons
    private void showBorrowConfirmDialog() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
        String message = getString(R.string.dialog_confirm_borrow_msg, currentBook.getTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_confirm_borrow_title);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentDialog = null;
                executeBorrow();
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

    // Executes the borrow operation in LibraryRepository and updates UI
    private void executeBorrow() {
        int resultCode = LibraryRepository.getInstance().borrowBook(bookId);
        if (resultCode == LibraryRepository.RESULT_SUCCESS) {
            String successMsg = getString(R.string.toast_borrow_success, currentBook.getTitle());
            Toast.makeText(BookDetailsActivity.this, successMsg, Toast.LENGTH_SHORT).show();
            refreshBookData();
        } else {
            String errorMsg = LibraryRepository.getInstance().getBorrowErrorMessage(resultCode);
            showAlertDialog(getString(R.string.dialog_error_title), errorMsg);
        }
    }

    // Validates borrowed status before showing return confirmation dialog
    private void onReturnButtonClicked() {
        if (currentBook == null) {
            return;
        }

        if (currentBook.isAvailable()) {
            showAlertDialog(getString(R.string.dialog_error_title), "This book is not currently borrowed.");
            return;
        }

        showReturnConfirmDialog();
    }

    // Shows a confirmation dialog for returning a book with Cancel and Confirm buttons
    private void showReturnConfirmDialog() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
        String message = getString(R.string.dialog_confirm_return_msg, currentBook.getTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_confirm_return_title);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.button_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentDialog = null;
                executeReturn();
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

    // Executes the return operation in LibraryRepository and updates UI
    private void executeReturn() {
        int resultCode = LibraryRepository.getInstance().returnBook(bookId);
        if (resultCode == LibraryRepository.RESULT_SUCCESS) {
            String successMsg = getString(R.string.toast_return_success, currentBook.getTitle());
            Toast.makeText(BookDetailsActivity.this, successMsg, Toast.LENGTH_SHORT).show();
            refreshBookData();
        } else {
            String errorMsg = LibraryRepository.getInstance().getReturnErrorMessage(resultCode);
            showAlertDialog(getString(R.string.dialog_error_title), errorMsg);
        }
    }

    // Displays an alert dialog with title and message
    private void showAlertDialog(String title, String message) {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
            }
        });
        currentDialog = builder.create();
        currentDialog.show();
    }

    // Shows an error dialog and finishes the activity when OK is clicked
    private void showErrorDialogAndFinish(String message) {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_error_title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentDialog = null;
                finish();
            }
        });
        currentDialog = builder.create();
        currentDialog.show();
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
