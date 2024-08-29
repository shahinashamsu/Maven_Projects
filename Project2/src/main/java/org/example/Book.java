package org.example;

import jakarta.persistence.*;


@Entity
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    public int id;
    @Column(name="BookName",nullable = false)
    public String bookName;
    @Column(name="AuthorName",nullable = false)
    public String authorName;
    @Column(name="IsBorrowed",nullable = false)
    public boolean isBorrowed;
    @Column(name="BorrowedBy")
    public String borrowedBy;

    // Default constructor is needed by Hibernate
    public Book() {}

    public Book(String bookName, String authorName) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.isBorrowed = false;
       this.borrowedBy = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", isBorrowed=" + isBorrowed +
                ", borrowedBy='" + borrowedBy + '\'' +
                '}';
    }
}

