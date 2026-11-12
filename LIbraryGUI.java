/*  CSC 1102: Structured and Object-Oriented Programming - Practical Exam Project
 * Project: Library Management System
 * Group Name:    Group 13
 * Members:       
 * ALINDA PATIENCE MBABAZI-26/U/03481/PSA
 * EZATI MARY IMMACULATE-26/U/03504/PS
 * TEBANDEKE ALLAN-26/U/3656/PS
 * ARATUSINGUZA JACOB-26/U/03490/PS
 * WASIKE EMMANUEL ERISA-26/U/0318
 * Session:       Day
 * Github link: https://github.com/patiencembabazialinda-cell/library-management-system_*/
 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

class LibraryGUI extends JFrame {

    private Library library = new Library();

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private JLabel totalLabel;
    private JLabel availableLabel;
    private JLabel borrowedLabel;

    Color peach = new Color(246, 191, 174);
    Color butterYellow = new Color(255, 241, 184);
    Color cream = new Color(255, 249, 235);
    Color terracotta = new Color(155, 89,71);
    Color white = Color.WHITE;

    public LibraryGUI() {

        setTitle(" The Nook of Knowledge");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(terracotta);
        header.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel heading = new JLabel("📚 BOOK SHIRE");
        heading.setForeground(white);
        heading.setFont(new Font("Arial", Font.BOLD, 25));

        header.add(heading, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(terracotta);
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setLayout(new GridLayout(7, 1, 5, 10));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] menu = {
            "Dashboard",
            "All Books",
            "Search Books",
            "Readers",
            "Borrow Book",
            "Return Book",
            "Exit"
        };

        for (String item : menu) {
            JButton button = new JButton(item);
            button.setBackground(white);
            button.setForeground(terracotta);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));

            if (item.equals("Exit")) {
                button.addActionListener(e -> System.exit(0));
            } else if (item.equals("Search Books")) {
                button.addActionListener(e -> searchField.requestFocus());
            } else if (item.equals("Readers")) {
                button.addActionListener(e -> {
                    JDialog readersDialog = new JDialog(this, "Readers", true);
                    readersDialog.setContentPane(buildReadersPanel());
                    readersDialog.pack();
                    readersDialog.setLocationRelativeTo(this);
                    readersDialog.setVisible(true);
                });
            } else if (item.equals("All Books")) {
                button.addActionListener(e -> {
                    JDialog booksDialog = new JDialog(this, "Books", true);
                    booksDialog.setContentPane(buildBooksPanel());
                    refresh();
                    booksDialog.pack();
                    booksDialog.setLocationRelativeTo(this);
                    booksDialog.setVisible(true);
                });
            } else if (item.equals("Borrow Book")) {
                button.addActionListener(e -> borrowBook());
            } else if (item.equals("Return Book")) {
                button.addActionListener(e -> returnBook());
            } else {
                button.addActionListener(e -> {
                    searchField.setText("");
                    loadBooks("");
                });
            }

            sidebar.add(button);
        }

        add(sidebar, BorderLayout.WEST);

        // Main content
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(cream);

        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcome = new JLabel("Welcome to your Library Dashboard");
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        welcome.setForeground(terracotta);

        // Search section
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);
        top.add(welcome, BorderLayout.NORTH);

        // Dashboard cards
JPanel dashboard = new JPanel(new GridLayout(1, 3, 15, 15));
dashboard.setBackground(cream);

int totalBooks = 0;
int availableBooks = 0;
int borrowedBooks = 0;

for (Book book : getBooks()) {
    totalBooks++;
    if (isBookAvailable(book)) {
        availableBooks++;
    } else {
        borrowedBooks++;
    }
}

totalLabel = new JLabel(String.valueOf(totalBooks));
availableLabel = new JLabel(String.valueOf(availableBooks));
borrowedLabel = new JLabel(String.valueOf(borrowedBooks));

dashboard.add(createCard("TOTAL BOOKS", totalLabel, peach));

dashboard.add(createCard("AVAILABLE BOOKS", availableLabel, butterYellow));

dashboard.add(createCard("BORROWED BOOKS", borrowedLabel, peach));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);

        searchField = new JTextField(25);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e ->
            loadBooks(searchField.getText())
        );

        searchPanel.add(new JLabel("Search book:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        top.add(searchPanel, BorderLayout.CENTER);

        JPanel topContent = new JPanel(new BorderLayout(10, 15));
topContent.setOpaque(false);

topContent.add(welcome, BorderLayout.NORTH);
topContent.add(dashboard, BorderLayout.CENTER);
topContent.add(searchPanel, BorderLayout.SOUTH);

top.add(topContent, BorderLayout.CENTER);
        content.add(top, BorderLayout.NORTH);

        // Book table
        String[] columns = {
            "Book ID", "Title", "Author", "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setBackground(terracotta);
        table.getTableHeader().setForeground(white);
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        loadBooks("");

        setVisible(true);
    }

    private JPanel createCard(String title, JLabel numberLabel, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 10));
        card.setBackground(color);
        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(peach, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setForeground(terracotta);

        numberLabel.setFont(new Font("Serif", Font.BOLD, 30));
        numberLabel.setForeground(terracotta);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(numberLabel, BorderLayout.CENTER);
        return card;
    }

    private void loadBooks(String search) {

        model.setRowCount(0);

        for (Book book : getBooks()) {
            String title = getBookField(book, "title");
            String author = getBookField(book, "author");

            String bookId = getBookId(book);
            if (bookId.toLowerCase().contains(search.toLowerCase())
                || title.toLowerCase().contains(search.toLowerCase())
                || author.toLowerCase().contains(search.toLowerCase())) {

                String status = isBookAvailable(book) ? "Available" : "Borrowed";

                model.addRow(new Object[] {
                    bookId,
                    title,
                    author,
                    status
                });
            }
        }
    }

    private void borrowBook() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a book first.");
            return;
        }

        String bookId = model.getValueAt(row, 0).toString();
        String selectedBookTitle = model.getValueAt(row, 1).toString();
        String enteredBookTitle = JOptionPane.showInputDialog(
            this, "Enter the book title:", "Borrow Book",
            JOptionPane.QUESTION_MESSAGE);
        if (enteredBookTitle == null || enteredBookTitle.trim().isEmpty()) return;

        if (!selectedBookTitle.equalsIgnoreCase(enteredBookTitle.trim())) {
            JOptionPane.showMessageDialog(this,
                "The book title does not match the selected book. Borrowing canceled.");
            return;
        }

        String readerName = JOptionPane.showInputDialog(
            this, "Enter the registered reader's name:", "Borrow Book",
            JOptionPane.QUESTION_MESSAGE);
        if (readerName == null || readerName.trim().isEmpty()) return;

        String readerIdText = JOptionPane.showInputDialog(
            this, "Enter the registered reader's ID:", "Borrow Book",
            JOptionPane.QUESTION_MESSAGE);

        if (readerIdText == null || readerIdText.trim().isEmpty()) return;

        final int readerId;
        try {
            readerId = Integer.parseInt(readerIdText.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Reader ID must be a number.");
            return;
        }

        Reader reader = library.findReader(readerId);
        if (reader == null
                || !reader.getName().equalsIgnoreCase(readerName.trim())) {
            JOptionPane.showMessageDialog(this,
                "The reader name and ID do not match a registered reader.");
            return;
        }

        int result = library.borrowBook(Integer.parseInt(bookId), readerId);
        if (result == 0) {
            JOptionPane.showMessageDialog(this, "Book borrowed successfully!");
        } else if (result == 2) {
            JOptionPane.showMessageDialog(this, "Reader ID was not found. Register the reader first.");
        } else if (result == 3) {
            JOptionPane.showMessageDialog(this, "This book is already borrowed.");
        } else if (result == 4) {
            JOptionPane.showMessageDialog(this, "This reader has reached the borrowing limit.");
        } else {
            JOptionPane.showMessageDialog(this, "Book ID was not found.");
        }

        loadBooks(searchField.getText());
        updateDashboard();
        refresh();
    }

    private void returnBook() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a book first.");
            return;
        }

        String id = model.getValueAt(row, 0).toString();

        int result = library.returnBook(Integer.parseInt(id));
        if (result == 0) {
            JOptionPane.showMessageDialog(this, "Book returned successfully!");
        } else if (result == 2) {
            JOptionPane.showMessageDialog(this, "This book has not been borrowed.");
        } else {
            JOptionPane.showMessageDialog(this, "Book ID was not found.");
        }

        loadBooks(searchField.getText());
        updateDashboard();
        refresh();
    }

    private JTextArea booksArea = new JTextArea();
    private JTextArea readersArea = new JTextArea();
    private JComboBox<String> filterBox = new JComboBox<>(new String[]{"All", "Available", "Borrowed"});

    // ---------- Books tab ----------
    private JPanel buildBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JTextField titleField = new JTextField(12);
        JTextField authorField = new JTextField(12);
        JButton addBtn = new JButton("Add Book");

        JPanel top = new JPanel();
        top.add(new JLabel("Title:"));
        top.add(titleField);
        top.add(new JLabel("Author:"));
        top.add(authorField);
        top.add(addBtn);
        top.add(new JLabel("View:"));
        top.add(filterBox);

        booksArea.setEditable(false);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(booksArea), BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            if (title.isEmpty() || author.isEmpty()) {
                showMessage("Please enter both title and author.");
            } else {
            if (addBookToLibrary(createBook(title, author), title, author)) {
    titleField.setText("");
    authorField.setText("");

    // Refresh the main books table
    loadBooks("");

    // Update dashboard numbers
    updateDashboard();

    // Refresh the books display
    refresh();

    showMessage("Book added successfully!");
}else {
                    showMessage("Library is full.");
                }
            }
        });
        filterBox.addActionListener(e -> refresh());
        return panel;
    }

    // ---------- Readers tab ----------
    private JPanel buildReadersPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JTextField nameField = new JTextField(15);
        JButton regBtn = new JButton("Register Reader");

        JPanel top = new JPanel();
        top.add(new JLabel("Name:"));
        top.add(nameField);
        top.add(regBtn);

        readersArea.setEditable(false);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(readersArea), BorderLayout.CENTER);

        regBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showMessage("Please enter the reader's name.");
            } else if (library.registerReader(name)) {
                nameField.setText("");
                refresh();
            } else {
                showMessage("Reader limit reached.");
            }
        });
        return panel;
    }

    private void refresh() {
        booksArea.setText(library.getBooksText((String) filterBox.getSelectedItem()));
        readersArea.setText(library.getReadersText());
    }

    private Book createBook(String title, String author) {
        try {
            for (Constructor<?> constructor : Book.class.getDeclaredConstructors()) {
                Class<?>[] types = constructor.getParameterTypes();
                int strings = 0;
                for (Class<?> type : types) {
                    if (type == String.class) strings++;
                }
                if (strings < 2) continue;

                Object[] args = new Object[types.length];
                int stringIndex = 0;
                for (int i = 0; i < types.length; i++) {
                    Class<?> type = types[i];
                    if (type == String.class) args[i] = stringIndex++ == 0 ? title : author;
                    else if (type == boolean.class) args[i] = false;
                    else if (type == char.class) args[i] = '\0';
                    else if (type.isPrimitive()) args[i] = 0;
                    else args[i] = null;
                }
                constructor.setAccessible(true);
                return (Book) constructor.newInstance(args);
            }

            Constructor<Book> constructor = Book.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Book book = constructor.newInstance();
            setBookField(book, "title", title);
            setBookField(book, "author", author);
            return book;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to create a book.", ex);
        }
    }

    private boolean addBookToLibrary(Book book, String title, String author) {
        for (Class<?> type = library.getClass(); type != null; type = type.getSuperclass()) {
            for (Method method : type.getDeclaredMethods()) {
                String name = method.getName().toLowerCase();
                if (!(name.contains("add") || name.contains("register") || name.contains("store"))
                        || !name.contains("book")) {
                    continue;
                }

                Class<?>[] parameters = method.getParameterTypes();
                Object[] arguments;
                if (parameters.length == 1 && parameters[0].isAssignableFrom(book.getClass())) {
                    arguments = new Object[]{book};
                } else if (parameters.length == 2
                        && parameters[0] == String.class && parameters[1] == String.class) {
                    arguments = new Object[]{title, author};
                } else {
                    continue;
                }

                try {
                    method.setAccessible(true);
                    Object result = method.invoke(library, arguments);
                    return !(result instanceof Boolean) || (Boolean) result;
                } catch (ReflectiveOperationException ex) {
                    throw new IllegalStateException("Unable to add a book.", ex);
                }
            }
        }
        throw new IllegalStateException("No book-registration method is available.");
    }

    private void setBookField(Book book, String name, String value)
            throws ReflectiveOperationException {
        for (Class<?> type = Book.class; type != null; type = type.getSuperclass()) {
            try {
                Field field = type.getDeclaredField(name);
                if (field.getType() == String.class) {
                    field.setAccessible(true);
                    field.set(book, value);
                }
                return;
            } catch (NoSuchFieldException ignored) {
                // Search the superclass.
            }
        }
    }

    private String getBookField(Book book, String name) {
        for (Class<?> type = Book.class; type != null; type = type.getSuperclass()) {
            try {
                Field field = type.getDeclaredField(name);
                field.setAccessible(true);
                Object value = field.get(book);
                return value == null ? "" : value.toString();
            } catch (NoSuchFieldException ignored) {
                // Search the superclass.
            } catch (ReflectiveOperationException ex) {
                throw new IllegalStateException("Unable to read book details.", ex);
            }
        }
        throw new IllegalStateException("Book field not found: " + name);
    }

    private boolean isBookAvailable(Book book) {
        for (Class<?> type = Book.class; type != null; type = type.getSuperclass()) {
            try {
                Field field = type.getDeclaredField("available");
                field.setAccessible(true);
                return field.getBoolean(book);
            } catch (NoSuchFieldException ignored) {
                // Search the superclass.
            } catch (ReflectiveOperationException ex) {
                throw new IllegalStateException("Unable to read book availability.", ex);
            }
        }
        throw new IllegalStateException("Book availability field not found.");
    }

    private String getBookId(Book book) {
        for (String name : new String[] {"bookId", "id"}) {
            try {
                return getBookField(book, name);
            } catch (IllegalStateException ignored) {
                // Try the alternate identifier field name.
            }
        }
        throw new IllegalStateException("Book identifier field not found.");
    }

    private Iterable<Book> getBooks() {
        try {
            Field field = Library.class.getDeclaredField("books");
            field.setAccessible(true);
            Object value = field.get(Modifier.isStatic(field.getModifiers()) ? null : library);

            List<Book> books = new ArrayList<>();
            if (value instanceof Iterable<?>) {
                for (Object item : (Iterable<?>) value) {
                    if (item instanceof Book) books.add((Book) item);
                }
            } else if (value != null && value.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(value); i++) {
                    Object item = Array.get(value, i);
                    if (item instanceof Book) books.add((Book) item);
                }
            } else {
                throw new IllegalStateException("Library books are not iterable.");
            }
            return books;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to access library books.", ex);
        }
    }

private void updateDashboard() {
    Iterable<Book> books = getBooks();
    int total = 0;
    int available = 0;
    int borrowed = 0;

    for (Book book : books) {
        total++;
        if (isBookAvailable(book)) {
            available++;
        } else {
            borrowed++;
        }
    }

    totalLabel.setText(String.valueOf(total));
    availableLabel.setText(String.valueOf(available));
    borrowedLabel.setText(String.valueOf(borrowed));
}


    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.refresh();
            gui.setVisible(true);
        });
    }
}



