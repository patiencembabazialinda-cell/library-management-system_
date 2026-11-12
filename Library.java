
    import javax.swing.JOptionPane;
    
    // Manages arrays of books and readers and the borrow/return logic. 
    public class Library {
        private static final int MAX = 100;
        private static final int MAX_LOANS = 3;
    
        private Book[] books = new Book[MAX];
        private Reader[] readers = new Reader[MAX];
        private int bookCount = 0;
        private int readerCount = 0;
    
        public void addBook() {
            // Ask for Book ID
            String bookId = JOptionPane.showInputDialog(null, "Enter numeric Book ID:");
    
            if (bookId == null || bookId.trim().isEmpty()) {
                return;
            }
    
            bookId = bookId.trim();
    
            // Book IDs are numeric in the Book model.
            if (!bookId.matches("[0-9]+")) {
                JOptionPane.showMessageDialog(null, "Book ID must contain only numbers.");
                return;
            }
    
            int numericBookId = Integer.parseInt(bookId);
    
            // Check for duplicate ID
            if (findBook(numericBookId) != null) {
                JOptionPane.showMessageDialog(null, "This Book ID already exists!");
                return;
            }
    
            // Ask for book title
            String title = JOptionPane.showInputDialog(null, "Enter Book Title:");
    
            if (title == null || title.trim().isEmpty()) {
                return;
            }
    
            // Ask for author
            String author = JOptionPane.showInputDialog(null, "Enter Author's Name:");
    
            if (author == null || author.trim().isEmpty()) {
                return;
            }
    
            if (bookCount >= MAX) {
                JOptionPane.showMessageDialog(null, "Library is full.");
                return;
            }
    
            // Create the new book
            Book newBook = new Book(numericBookId, title.trim(), author.trim());
    
            books[bookCount] = newBook;
            bookCount++;
    
            JOptionPane.showMessageDialog(null,
                "Book added successfully!\n"
                + "Book ID: " + numericBookId
                + "\nTitle: " + title.trim()
                + "\nAuthor: " + author.trim());
    
        }
        
    
    
        public boolean registerReader(String name) {
            if (name == null) {
                return false;
            }
            String cleanName = name.trim();
            if (cleanName.isEmpty() || readerCount >= MAX) {
                return false;
            }
            readers[readerCount] = new Reader(nextReaderId(), cleanName);
            readerCount++;
            return true;
        }
    
        private int nextReaderId() {
            int id = 0;
            do {
                id++;
            } while (findReader(id) != null);
            return id;
        }
    
        private int nextBookId() {
            int id = 0;
            do {
                id++;
            } while (findBook(id) != null);
            return id;
        }
    
        // ---------- Searching ----------
        public Book findBook(int id) {
            for (int i = 0; i < bookCount; i++) {
                if (books[i].getId() == id) {
                    return books[i];
                }
            }
            return null;
        }
    
        public boolean addBook(String title, String author) {
            if (title == null || author == null) {
                return false;
            }
            String cleanTitle = title.trim();
            String cleanAuthor = author.trim();
            if (cleanTitle.isEmpty() || cleanAuthor.isEmpty() || bookCount >= MAX) {
                return false;
            }
            books[bookCount] = new Book(nextBookId(), cleanTitle, cleanAuthor);
            bookCount++;
            return true;
        }
        public int getBorrowedBookCount(String readerId) {
    int count = 0;

    for (int i = 0; i < bookCount; i++) {
        Book book = books[i];

        if (!book.isAvailable()
            && readerId != null
            && readerId.equals(String.valueOf(book.getBorrowedBy()))) {
            count++;
        }
    }

    return count;
}
        
        public Reader findReader(int id) {
            int i = 0;
            while (i < readerCount) {
                if (readers[i].getId() == id) {
                    return readers[i];
                }
                i++;
            }
            return null;
        }
    
        // ---------- Borrow / Return ----------
        //Returns: 0 success, 1 book not found, 2 reader not found, 3 book unavailable, 4 loan limit reached. 
        public int borrowBook(int bookId, int readerId) {
            Book book = findBook(bookId);
            Reader reader = findReader(readerId);
    
            if (book == null) return 1;
            if (reader == null) return 2;
            if (!book.isAvailable()) return 3;
            if (reader.getBooksBorrowed() >= MAX_LOANS) return 4;
    
            book.setAvailable(false);
            book.setBorrowedBy(readerId);
            reader.incrementBorrowed();
            return 0;
        }
    
        // Returns: 0 success, 1 book not found, 2 book was not borrowed. 
        public int returnBook(int bookId) {
            Book book = findBook(bookId);
            if (book == null) return 1;
            if (book.isAvailable()) return 2;
    
            Reader reader = findReader(book.getBorrowedBy());
            if (reader != null) {
                reader.decrementBorrowed();
            }
            book.setAvailable(true);
            book.setBorrowedBy(0);
            return 0;
        }
    
        // ---------- Display ----------
        // filter: "All", "Available" or "Borrowed". 
        public String getBooksText(String filter) {
            String text = "";
            for (int i = 0; i < bookCount; i++) {
                boolean show = false;
                switch (filter) {
                    case "Available": show = books[i].isAvailable(); break;
                    case "Borrowed":  show = !books[i].isAvailable(); break;
                    default:          show = true;
                }
                if (show) {
                    text += books[i].toString() + "\n";
                }
            }
            if (text.isEmpty()) {
                text = "No books to display.\n";
            }
            return text;
        }
    
       public String getBorrowedBooksText(String readerId) {
    StringBuilder text = new StringBuilder();

    for (int i = 0; i < bookCount; i++) {
        Book book = books[i];

        if (!book.isAvailable()
                && readerId != null
                && readerId.equals(String.valueOf(book.getBorrowedBy()))) {
            text.append(book).append("\n");
        }
    }

    return text.length() == 0
            ? "This reader has no borrowed books."
            : text.toString();
}
       
       
       
        public String getReadersText() {
            String text = "";
            for (int i = 0; i < readerCount; i++) {
                text += readers[i].toString() + "\n";
            }
            if (text.isEmpty()) {
                text = "No readers registered.\n";
            }
            return text;
        }
    
        public int getBookCount() { return bookCount; }
        public int getReaderCount() { return readerCount; }
    }




