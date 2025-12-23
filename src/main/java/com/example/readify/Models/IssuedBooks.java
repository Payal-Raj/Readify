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
    public IssuedBooks(String bookId, String bookName,
                       LocalDate issuedDate, LocalDate dueDate) {
        this.bookId = new SimpleStringProperty(bookId);
        this.bookName = new SimpleStringProperty(bookName);
        this.issuedDate = new SimpleObjectProperty<>(issuedDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
    }

    // Getters for TableView
    public StringProperty bookIdProperty() { return bookId; }
    public IntegerProperty memberIdProperty() { return memberId; }
    public StringProperty bookNameProperty() { return bookName; }
    public StringProperty memberNameProperty() { return memberName; }
    public ObjectProperty<LocalDate> issuedDateProperty() { return issuedDate; }
    public ObjectProperty<LocalDate> dueDateProperty() { return dueDate; }
    public IntegerProperty penaltyProperty() { return penalty; }

    public String getBookId() {
        return bookId.get();
    }

    public void setBookId(String bookId) {
        this.bookId.set(bookId);
    }

    public int getMemberId() {
        return memberId.get();
    }

    public void setMemberId(int memberId) {
        this.memberId.set(memberId);
    }

    public String getBookName() {
        return bookName.get();
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
    }

    public String getMemberName() {
        return memberName.get();
    }

    public void setMemberName(String memberName) {
        this.memberName.set(memberName);
    }

    public LocalDate getIssuedDate() {
        return issuedDate.get();
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate.set(issuedDate);
    }

    public LocalDate getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate.set(dueDate);
    }

    public int getPenalty() {
        return penalty.get();
    }

    public void setPenalty(int penalty) {
        this.penalty.set(penalty);
    }
}
