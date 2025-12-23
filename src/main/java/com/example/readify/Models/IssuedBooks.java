package com.example.readify.Models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class IssuedBooks {

    private StringProperty bookId;
    private IntegerProperty memberId;
    private StringProperty bookName;
    private StringProperty memberName;
    private ObjectProperty<LocalDate> issuedDate;
    private ObjectProperty<LocalDate> dueDate;
    private IntegerProperty penalty;

    public IssuedBooks(String bookId, int memberId, String bookName, String memberName,
                       LocalDate issuedDate, LocalDate dueDate, int penalty) {
        this.bookId = new SimpleStringProperty(bookId);
        this.memberId = new SimpleIntegerProperty(memberId);
        this.bookName = new SimpleStringProperty(bookName);
        this.memberName = new SimpleStringProperty(memberName);
        this.issuedDate = new SimpleObjectProperty<>(issuedDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.penalty = new SimpleIntegerProperty(penalty);
    }

    // Getters for TableView
    public StringProperty bookIdProperty() { return bookId; }
    public IntegerProperty memberIdProperty() { return memberId; }
    public StringProperty bookNameProperty() { return bookName; }
    public StringProperty memberNameProperty() { return memberName; }
    public ObjectProperty<LocalDate> issuedDateProperty() { return issuedDate; }
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }
    public IntegerProperty penaltyProperty() { return penalty; }

    // Standard getters (optional)
    public String getBookName() { return bookName.get(); }
    public String getMemberName() { return memberName.get(); }
    public LocalDate getIssuedDate() { return issuedDate.get(); }
}
