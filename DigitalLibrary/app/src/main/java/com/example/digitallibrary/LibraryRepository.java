package com.example.digitallibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Repository class managing book catalog, search filtering, and borrow/return transactions.
// NOTE: In-memory/static data stored in ArrayList does NOT survive process death (when Android kills the application process). Persistence after process death is not required for this project.
public class LibraryRepository {

    // Singleton instance of LibraryRepository
    private static LibraryRepository instance;

    // List storing all 10 books in catalog
    private ArrayList<Book> allBooks;

    // List storing currently borrowed books (maximum 3)
    private ArrayList<Book> borrowedBooks;

    // Result codes for borrow and return operations
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_ERROR_MAX_LIMIT = 1;
    public static final int RESULT_ERROR_ALREADY_BORROWED = 2;
    public static final int RESULT_ERROR_NOT_FOUND = 3;
    public static final int RESULT_ERROR_NOT_BORROWED = 4;

    // Maximum allowed borrowed books per user
    public static final int MAX_BORROW_LIMIT = 3;

    // Private constructor initializing catalog with exactly 10 books (B101 to B110)
    private LibraryRepository() {
        allBooks = new ArrayList<>();
        borrowedBooks = new ArrayList<>();
        populateDefaultCatalog();
    }

    // Returns the singleton instance of LibraryRepository
    public static synchronized LibraryRepository getInstance() {
        if (instance == null) {
            instance = new LibraryRepository();
        }
        return instance;
    }

    // Populates the catalog with 10 books using the specified titles and categories
    private void populateDefaultCatalog() {
        allBooks.add(new Book("B101", "Harry Potter and Chamber of secerets", "J.K. Rowling", "Fiction", "Second year at Hogwarts featuring the mystery of the Chamber of Secrets.", true));
        allBooks.add(new Book("B102", "Goblet of fire", "J.K. Rowling", "Fiction", "Fourth year at Hogwarts and the perilous Triwizard Tournament.", true));
        allBooks.add(new Book("B103", "Harry Potter and the Half-Blood Prince", "J.K. Rowling", "Fiction", "Sixth year at Hogwarts exploring Lord Voldemort's mysterious past.", true));
        allBooks.add(new Book("B104", "One piece", "Eiichiro Oda", "Fiction", "Monkey D. Luffy's grand journey across the ocean to find the legendary treasure One Piece.", true));
        allBooks.add(new Book("B105", "No longer Human", "Osamu Dazai", "Fiction", "A classic Japanese novel analyzing alienation, identity, and modern society.", true));
        allBooks.add(new Book("B106", "Bleach", "Tite Kubo", "Programming", "A spiritual guide to cleaning bad code, refactoring legacy systems, and eliminating bugs like Soul Reapers.", true));
        allBooks.add(new Book("B107", "Attack on Titan", "Hajime Isayama", "Science", "Scientific and biological investigation of Titan physiology, wall structures, and physics.", true));
        allBooks.add(new Book("B108", "Rezero", "Tappei Nagatsuki", "Business", "Strategic business decision-making under high-risk environments and time-loop resource allocation.", true));
        allBooks.add(new Book("B109", "Fairytail", "Hiro Mashima", "Fiction", "Magical guild adventures, team building, and fantasy battles.", true));
        allBooks.add(new Book("B110", "Lucifer was Innocent", "Tirth Raj Parsana", "Business", "An intriguing analytical perspective on corporate leadership ethics, perception, and governance.", true));
    }

    // Returns all books in the catalog
    public ArrayList<Book> getAllBooks() {
        return allBooks;
    }

    // Returns all currently borrowed books
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    // Returns total count of currently borrowed books
    public int getBorrowedCount() {
        return borrowedBooks.size();
    }

    // Null-safe lookup to find a book by its unique ID
    public Book getBookById(String id) {
        if (id == null) {
            return null;
        }
        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);
            if (book.getId() != null && book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    // Searches and filters books combining case-insensitive title text, category, and availability
    public ArrayList<Book> searchBooks(String query, String category, String availability) {
        ArrayList<Book> result = new ArrayList<>();
        String trimmedQuery = (query != null) ? query.trim().toLowerCase() : "";

        for (int i = 0; i < allBooks.size(); i++) {
            Book book = allBooks.get(i);

            // Title filter (case-insensitive substring match)
            boolean matchesTitle = true;
            if (!trimmedQuery.isEmpty()) {
                if (book.getTitle() == null || !book.getTitle().toLowerCase().contains(trimmedQuery)) {
                    matchesTitle = false;
                }
            }

            // Category filter
            boolean matchesCategory = true;
            if (category != null && !category.equalsIgnoreCase("All Categories") && !category.trim().isEmpty()) {
                if (book.getCategory() == null || !book.getCategory().equalsIgnoreCase(category.trim())) {
                    matchesCategory = false;
                }
            }

            // Availability filter
            boolean matchesAvailability = true;
            if (availability != null && !availability.equalsIgnoreCase("All Books") && !availability.trim().isEmpty()) {
                if (availability.equalsIgnoreCase("Available")) {
                    matchesAvailability = book.isAvailable();
                } else if (availability.equalsIgnoreCase("Borrowed")) {
                    matchesAvailability = !book.isAvailable();
                }
            }

            if (matchesTitle && matchesCategory && matchesAvailability) {
                result.add(book);
            }
        }
        return result;
    }

    // Attempts to borrow a book by its ID and returns an integer result code
    public int borrowBook(String bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            return RESULT_ERROR_NOT_FOUND;
        }
        if (!book.isAvailable()) {
            return RESULT_ERROR_ALREADY_BORROWED;
        }
        if (borrowedBooks.size() >= MAX_BORROW_LIMIT) {
            return RESULT_ERROR_MAX_LIMIT;
        }

        book.setAvailable(false);
        borrowedBooks.add(book);
        return RESULT_SUCCESS;
    }

    // Attempts to return a borrowed book by its ID and returns an integer result code
    public int returnBook(String bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            return RESULT_ERROR_NOT_FOUND;
        }
        if (book.isAvailable()) {
            return RESULT_ERROR_NOT_BORROWED;
        }

        book.setAvailable(true);
        borrowedBooks.remove(book);
        return RESULT_SUCCESS;
    }

    // Returns a human-readable error or success message for a borrow result code
    public String getBorrowErrorMessage(int resultCode) {
        if (resultCode == RESULT_SUCCESS) {
            return "Book borrowed successfully!";
        } else if (resultCode == RESULT_ERROR_MAX_LIMIT) {
            return "Cannot borrow more than 3 books at a time.";
        } else if (resultCode == RESULT_ERROR_ALREADY_BORROWED) {
            return "This book is currently borrowed by another user.";
        } else if (resultCode == RESULT_ERROR_NOT_FOUND) {
            return "Book not found.";
        }
        return "Unknown error occurred.";
    }

    // Returns a human-readable error or success message for a return result code
    public String getReturnErrorMessage(int resultCode) {
        if (resultCode == RESULT_SUCCESS) {
            return "Book returned successfully!";
        } else if (resultCode == RESULT_ERROR_NOT_BORROWED) {
            return "This book is not currently borrowed.";
        } else if (resultCode == RESULT_ERROR_NOT_FOUND) {
            return "Book not found.";
        }
        return "Unknown error occurred.";
    }

    // Sorts a list of books alphabetically by title (A to Z) without using lambdas
    public void sortBooksAZ(ArrayList<Book> list) {
        if (list == null) {
            return;
        }
        Collections.sort(list, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                if (b1 == null || b1.getTitle() == null) return -1;
                if (b2 == null || b2.getTitle() == null) return 1;
                return b1.getTitle().compareToIgnoreCase(b2.getTitle());
            }
        });
    }
}
