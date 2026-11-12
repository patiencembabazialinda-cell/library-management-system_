// Represents a registered library member. 
public class Reader {
    private int id;
    private String name;
    private int booksBorrowed;

    public Reader(int id, String name) {
        this.id = id;
        this.name = name;
        this.booksBorrowed = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getBooksBorrowed() { return booksBorrowed; }

    public void setName(String name) {
        this.name = name == null ? "" : name.trim();
    }

    public void incrementBorrowed() { booksBorrowed++; }

    public void decrementBorrowed() {
        if (booksBorrowed > 0) {
            booksBorrowed--;
        }
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name + " - Books borrowed: " + booksBorrowed;
    }
}



