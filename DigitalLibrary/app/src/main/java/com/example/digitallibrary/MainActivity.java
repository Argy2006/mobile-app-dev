package com.example.digitallibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// Main activity handling catalog view, stats cards (Total, Remaining, Interactive Borrowed tile), search bar, category & availability filtering, and options menu
public class MainActivity extends AppCompatActivity {

    // Logcat tag for monitoring activity lifecycle callbacks
    private static final String TAG = "DL_MainActivity";

    // Keys for saving instance state on configuration change / rotation
    private static final String KEY_SEARCH_QUERY = "key_search_query";
    private static final String KEY_CATEGORY_POSITION = "key_category_position";
    private static final String KEY_AVAILABILITY_POSITION = "key_availability_position";
    private static final String KEY_SORT_AZ = "key_sort_az";

    private Toolbar toolbar;
    private TextView totalBooksTextView;
    private TextView remainingBooksTextView;
    private TextView borrowedNumberTextView;
    private LinearLayout cardBorrowedTile;
    private EditText editSearch;
    private ImageButton clearSearchButton;
    private Spinner spinnerCategory;
    private Spinner spinnerAvailability;
    private RecyclerView recyclerViewBooks;
    private TextView textNoBooks;

    private BookAdapter bookAdapter;
    private ArrayList<Book> displayedBooks;
    private boolean isSortedAZ = false;

    // Field holding reference to active dialog for safe dismissal on rotation/pause
    private AlertDialog currentDialog;

    // Spinner option arrays
    private String[] categoriesArray = new String[]{
            "All Categories", "Programming", "Fiction", "Science", "Business"
    };

    private String[] availabilityArray = new String[]{
            "All Books", "Available", "Borrowed"
    };

    // Called when the activity is starting; sets up UI, listeners, and restores saved state
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        setupSpinners();
        setupRecyclerView();
        setupSearchAndFilterListeners();

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    // Called after onCreate or onRestart, when activity becomes visible
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    // Called when activity starts interacting with user; re-applies filters and updates stats numbers
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        applyFilters();
    }

    // Called when system is about to resume another activity; dismisses active dialog to prevent window leak on rotation
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

    // Called after onStop when activity is about to be restarted
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart called");
    }

    // Called before activity destruction for clean up
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
            currentDialog = null;
        }
    }

    // Saves current UI input states before activity rotation or destruction
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState called");

        if (editSearch != null) {
            outState.putString(KEY_SEARCH_QUERY, editSearch.getText().toString());
        }
        if (spinnerCategory != null) {
            outState.putInt(KEY_CATEGORY_POSITION, spinnerCategory.getSelectedItemPosition());
        }
        if (spinnerAvailability != null) {
            outState.putInt(KEY_AVAILABILITY_POSITION, spinnerAvailability.getSelectedItemPosition());
        }
        outState.putBoolean(KEY_SORT_AZ, isSortedAZ);
    }

    // Restores UI state from savedInstanceState bundle after screen rotation
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState called");
        restoreInstanceState(savedInstanceState);
    }

    // Helper method to restore input states from savedInstanceState bundle
    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        String savedQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, "");
        int savedCategoryPos = savedInstanceState.getInt(KEY_CATEGORY_POSITION, 0);
        int savedAvailabilityPos = savedInstanceState.getInt(KEY_AVAILABILITY_POSITION, 0);
        isSortedAZ = savedInstanceState.getBoolean(KEY_SORT_AZ, false);

        if (editSearch != null) {
            editSearch.setText(savedQuery);
        }
        if (spinnerCategory != null && savedCategoryPos < categoriesArray.length) {
            spinnerCategory.setSelection(savedCategoryPos);
        }
        if (spinnerAvailability != null && savedAvailabilityPos < availabilityArray.length) {
            spinnerAvailability.setSelection(savedAvailabilityPos);
        }
        if (isSortedAZ) {
            sortCurrentListAZ();
        }
    }

    // Binds view references using findViewById
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        totalBooksTextView = findViewById(R.id.totalBooksTextView);
        remainingBooksTextView = findViewById(R.id.remainingBooksTextView);
        borrowedNumberTextView = findViewById(R.id.borrowedNumberTextView);
        cardBorrowedTile = findViewById(R.id.cardBorrowedTile);
        editSearch = findViewById(R.id.edit_search);
        clearSearchButton = findViewById(R.id.clearSearchButton);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerAvailability = findViewById(R.id.spinner_availability);
        recyclerViewBooks = findViewById(R.id.recycler_view_books);
        textNoBooks = findViewById(R.id.text_no_books);
    }

    // Configures action bar toolbar
    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    // Populates and sets up adapters for category and availability spinners
    private void setupSpinners() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categoriesArray);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter<String> availabilityAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, availabilityArray);
        availabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvailability.setAdapter(availabilityAdapter);
    }

    // Initializes RecyclerView layout manager and BookAdapter
    private void setupRecyclerView() {
        displayedBooks = new ArrayList<>();
        bookAdapter = new BookAdapter(this, displayedBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBooks.setAdapter(bookAdapter);
    }

    // Sets listeners for search text, interactive tiles, and filter spinners without using lambdas
    private void setupSearchAndFilterListeners() {
        // Interactive click listener for the Currently Borrowed tile to launch BorrowedBooksActivity
        if (cardBorrowedTile != null) {
            cardBorrowedTile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBorrowedBooksScreen();
                }
            });
        }

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Show clear X button only when text is entered
                if (clearSearchButton != null) {
                    if (s != null && s.length() > 0) {
                        clearSearchButton.setVisibility(View.VISIBLE);
                    } else {
                        clearSearchButton.setVisibility(View.GONE);
                    }
                }
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Click listener for clear search X button
        if (clearSearchButton != null) {
            clearSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editSearch != null) {
                        editSearch.setText("");
                    }
                }
            });
        }

        // Action search editor listener for soft keyboard
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    applyFilters();
                    return true;
                }
                return false;
            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerAvailability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Reads user inputs, executes search via LibraryRepository, updates list and stats numbers
    private void applyFilters() {
        String query = editSearch.getText().toString();
        String selectedCategory = (String) spinnerCategory.getSelectedItem();
        String selectedAvailability = (String) spinnerAvailability.getSelectedItem();

        displayedBooks = LibraryRepository.getInstance().searchBooks(query, selectedCategory, selectedAvailability);
        if (isSortedAZ) {
            LibraryRepository.getInstance().sortBooksAZ(displayedBooks);
        }
        bookAdapter.updateData(displayedBooks);

        // Toggle "No books found" visibility
        if (displayedBooks == null || displayedBooks.isEmpty()) {
            textNoBooks.setVisibility(View.VISIBLE);
            recyclerViewBooks.setVisibility(View.GONE);
        } else {
            textNoBooks.setVisibility(View.GONE);
            recyclerViewBooks.setVisibility(View.VISIBLE);
        }

        // Update stats card numbers
        updateBorrowedCounter();
    }

    // Updates stats card numbers for total books, remaining available books, and currently borrowed books
    private void updateBorrowedCounter() {
        int totalCount = LibraryRepository.getInstance().getAllBooks().size();
        int borrowedCount = LibraryRepository.getInstance().getBorrowedCount();
        int remainingCount = totalCount - borrowedCount;

        if (totalBooksTextView != null) {
            totalBooksTextView.setText(String.valueOf(totalCount));
        }
        if (remainingBooksTextView != null) {
            remainingBooksTextView.setText(String.valueOf(remainingCount));
        }
        if (borrowedNumberTextView != null) {
            borrowedNumberTextView.setText(borrowedCount + " / " + LibraryRepository.MAX_BORROW_LIMIT);
        }
    }

    // Inflates the options menu for main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handles selection of options menu items (My Borrowed Books, Sort A-Z, About, Exit)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_my_borrowed) {
            openBorrowedBooksScreen();
            return true;
        } else if (id == R.id.action_sort_az) {
            isSortedAZ = true;
            sortCurrentListAZ();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.action_exit) {
            showExitConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Opens the BorrowedBooksActivity via explicit intent
    private void openBorrowedBooksScreen() {
        Intent intent = new Intent(MainActivity.this, BorrowedBooksActivity.class);
        startActivity(intent);
    }

    // Sorts the currently displayed books list A to Z by title
    private void sortCurrentListAZ() {
        LibraryRepository.getInstance().sortBooksAZ(displayedBooks);
        bookAdapter.notifyDataSetChanged();
    }

    // Displays an About dialog with application information
    private void showAboutDialog() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_title);
        builder.setMessage(R.string.about_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
            }
        });
        currentDialog = builder.create();
        currentDialog.show();
    }

    // Displays a confirmation dialog before exiting the application
    private void showExitConfirmationDialog() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit_title);
        builder.setMessage(R.string.exit_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentDialog = null;
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                currentDialog = null;
            }
        });
        currentDialog = builder.create();
        currentDialog.show();
    }
}
