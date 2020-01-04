package com.onica;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookManager {

  private BufferedReader bufferedReader;
  private Map<Long, Book> books;

  public BookManager() {
    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    books = new HashMap<>();
  }

  public void loadFileToBookMap() {
    try {
      FileInputStream inputStream = new FileInputStream("books.txt");
      BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = fileReader.readLine()) != null) {
        String[] elements = line.split(";");
        Long bookId = Long.parseLong(elements[0]);
        String title = elements[1];
        String author = elements[2];
        String description = elements[3];
        Book book = createBook(title, author, description);
        book.setId(bookId);
        books.put(bookId, book);
      }
    } catch(Exception ex) {

    }

  }

  public void manageAction() throws Exception {
    System.out.println("==== Book Manager ====");
    System.out.println("\t1)View all books");
    System.out.println("\t2)Add a book");
    System.out.println("\t3)Edit a book");
    System.out.println("\t4)Search for a book");
    System.out.println("\t5)Save and exit");
    System.out.println("Choose [1-5]: ");
    int action = Integer.parseInt(bufferedReader.readLine());
    switch (action) {
      case 1:
        viewAllBooksOption();
        break;
      case 2:
        addBook();
        break;
      case 3:
        editBookOption();
        break;
      case 4:
        searchBookOption();
        break;
      case 5:
        saveAndExit();
        break;
    }
  }

  private void saveAndExit() throws Exception {
    FileOutputStream outputStream = new FileOutputStream("books.txt");
    for (Long bookId: books.keySet()) {
      Book book = books.get(bookId);
      writeBookToFile(book, outputStream);
    }
    outputStream.close();
  }

  private void writeBookToFile(Book book, FileOutputStream outputStream) throws Exception {
    String bookId = book.getId() + ";";
    String title = book.getTitle() + ";";
    String author = book.getAuthor() + ";";
    String description = book.getDescription() + ";\n";
    outputStream.write(bookId.getBytes());
    outputStream.write(title.getBytes());
    outputStream.write(author.getBytes());
    outputStream.write(description.getBytes());
  }

  private void searchBookOption() throws Exception {
    System.out.println("==== Search ====");
    System.out.println("Type in one or more keywords to search for");
    System.out.print("\tSearch: ");
    String key = bufferedReader.readLine();
    List<Book> searchedBooks = new ArrayList<>();
    filterBooks(key, searchedBooks);
    System.out.println("The following books matched your query. Enter the book ID to see more details, or <Enter> to return.");
    for (Book book: searchedBooks) {
      System.out.println("[" + book.getId() + "]" + book.getTitle());
    }
    String action = bufferedReader.readLine();
    if (action.isEmpty()) {
      manageAction();
    } else {
      viewBookDetails(action);
    }
  }

  private void filterBooks(String key, List<Book> searchedBooks) {
    for (Long bookId: books.keySet()) {
      Book book = books.get(bookId);
      String title = book.getTitle();
      if (title.contains(key)) {
        searchedBooks.add(book);
      }
    }
  }

  private void editBookOption() throws Exception {
    System.out.println("==== Edit a Book ====");
    viewAllBooks();
    editBook();
  }

  private void editBook() throws Exception {
    System.out.println("Enter the book ID of the book you want to edit; to return press <Enter>.");
    System.out.print("Book ID: ");
    String action = bufferedReader.readLine();
    if (action.isEmpty()) {
      manageAction();
    } else {
      Long bookId = Long.parseLong(action);
      Book book = books.get(bookId);
      System.out.print("Title [" + book.getTitle() + "]: ");
      String title = getInputIfUpdated(book.getTitle());
      System.out.print("Author [" + book.getAuthor() + "]: ");
      String author = getInputIfUpdated(book.getAuthor());
      System.out.print("Description [" + book.getDescription() + "]: ");
      String description = getInputIfUpdated(book.getDescription());
      updateBookDetails(book, title, author, description);
      books.put(bookId, book);
      editBook();
    }
  }

  private String getInputIfUpdated(String element) throws Exception {
    String input = bufferedReader.readLine();
    if (input.isEmpty()) {
      return element;
    } else {
      return input;
    }
  }

  private void updateBookDetails(Book book, String title, String author, String description) {
    book.setTitle(title);
    book.setAuthor(author);
    book.setDescription(description);
  }

  private void addBook() throws Exception {
    System.out.println("==== Add a Book ====");
    System.out.println("Please enter the following information:");
    System.out.print("Title: ");
    String title = bufferedReader.readLine();
    System.out.print("Author: ");
    String author = bufferedReader.readLine();
    System.out.print("Description: ");
    String description = bufferedReader.readLine();
    Book book = createBook(title, author, description);
    books.put(book.getId(), book);
    System.out.println("Book [" + book.getId() + "] Saved");
    manageAction();
  }

  private Book createBook(String title, String author, String description) {
    Book book = new Book();
    updateBookDetails(book, title, author, description);
    Long lastID = getaLastBookId();
    book.setId(lastID + 1);
    return book;
  }

  private void viewAllBooksOption() throws Exception {
    System.out.println("==== View Books ====");
    viewAllBooks();
    System.out.println("To view details enter the book ID, to return press <Enter>.");
    System.out.print("Book ID: ");
    String action = bufferedReader.readLine();
    if (action.isEmpty()) {
      manageAction();
    } else {
      viewBookDetails(action);
    }
  }

  private void viewBookDetails(String action) throws Exception {
    Book book = books.get(Long.parseLong(action));
    System.out.println();
    System.out.println("\tID: " + book.getId());
    System.out.println("\tTitle: " + book.getTitle());
    System.out.println("\tAuthor: " + book.getAuthor());
    System.out.println("\tDescription: " + book.getDescription());
    System.out.println();
    System.out.println("To view details enter the book ID, to return press <Enter>.");
    String newAction = bufferedReader.readLine();
    if (newAction.isEmpty()) {
      manageAction();
    } else {
      viewBookDetails(newAction);
    }
  }

  private void viewAllBooks() {
    for (Long bookId : books.keySet()) {
      Book book = books.get(bookId);
      System.out.println("[" + book.getId() + "]" + book.getTitle());
    }
  }

  private Long getaLastBookId() {
    if (books.size() > 0) {
      Book lastBook = books.get(Long.valueOf(books.size()));
      return lastBook.getId();
    } else {
      return 0L;
    }
  }
}
