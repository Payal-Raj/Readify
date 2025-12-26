package com.example.readify.Manager;

import com.example.readify.Models.Book;
import java.util.ArrayList;
import java.util.HashMap;

public class BookManager {

    private static ArrayList<Book> bookList = new ArrayList<>();
    private static HashMap<String, Book> bookMap = new HashMap<>();

    public static boolean addBook(Book book) {

        if (bookMap.containsKey(book.getBookId())) {
            return false;
        }

        bookList.add(book);
        bookMap.put(book.getBookId(), book);
        return true;
    }

    /*DSA
        public static ArrayList<Book> getAllBooks() {
            return new ArrayList<>(bookList);
        }

        public static boolean removeBook(String bookId) {
            Book book = bookMap.remove(bookId);
            if (book != null) {
                bookList.remove(book);
                return true;
            }
            return false;
        }



     */
}
