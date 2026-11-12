// Represents a single book in the library. 
public class Book {
    private int id;
    private String title;
    private String author;
    private boolean available;
    private int borrowedBy; 

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = true;
        this.borrowedBy = 0;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return available; }
    public int getBorrowedBy() { return borrowedBy; }

    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }

    public void setAuthor(String author) {
        this.author = author == null ? "" : author.trim();
    }

    public void setAvailable(boolean available) { this.available = available; }
    public void setBorrowedBy(int borrowedBy) { this.borrowedBy = borrowedBy; }

    @Override
    public String toString() {
        String status = available ? "Available" : "Borrowed by Reader #" + borrowedBy;
        return "[" + id + "] " + title + " by " + author + " - " + status;
    }
}



