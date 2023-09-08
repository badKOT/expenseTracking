package ru.webapp.exchange.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total")
    @Positive
    private double total;

    @Column(name = "message")
    @Size(max = 200, message = "Cannot be over 200 characters long")
    private String message;

    @Column(name = "added_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User owner;

    public Expense() {
    }

    public Expense(double total, String message, User owner) {
        this.total = total;
        this.message = message;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }
}
