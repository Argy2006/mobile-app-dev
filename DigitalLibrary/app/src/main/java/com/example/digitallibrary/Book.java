package com.example.digitallibrary;

// Model class representing a Book in the Digital Library catalog
public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private String description;
    private boolean isAvailable;

    // Constructor to initialize a Book object
    public Book(String id, String title, String author, String category, String description, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    // Returns the unique ID of the book
    public String getId() {
        return id;
    }

    // Sets the unique ID of the book
    public void setId(String id) {
        this.id = id;
    }

    // Returns the title of the book
    public String getTitle() {
        return title;
    }

    // Sets the title of the book
    public void setTitle(String title) {
        this.title = title;
    }

    // Returns the author of the book
    public String getAuthor() {
        return author;
    }

    // Sets the author of the book
    public void setAuthor(String author) {
        this.author = author;
    }

    // Returns the category of the book
    public String getCategory() {
        return category;
    }

    // Sets the category of the book
    public void setCategory(String category) {
        this.category = category;
    }

    // Returns the detailed description of the book
    public String getDescription() {
        return description;
    }

    // Sets the description of the book
    public void setDescription(String description) {
        this.description = description;
    }

    // Returns true if the book is available for borrowing, false otherwise
    public boolean isAvailable() {
        return isAvailable;
    }

    // Sets the availability status of the book
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
