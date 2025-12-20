package com.example.readify.Manager;

import com.example.readify.Models.Book;
import java.util.ArrayList;
import java.util.HashMap;

public class BookManager {

    private static ArrayList<Book> bookList = new ArrayList<>();
    private static HashMap<String, Book> bookMap = new HashMap<>();

    public static boolean addBook(Book book) {

        if (bookMap.containsKey(book.getBookId())) {
            return false; // duplicate
        }

        bookList.add(book);                    // ArrayList ADD
        bookMap.put(book.getBookId(), book);   // HashMap ADD
        return true;
    }
}
