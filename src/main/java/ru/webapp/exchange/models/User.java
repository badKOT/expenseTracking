package ru.webapp.exchange.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Fetch;

import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    @NotEmpty(message = "Не может быть пустым")
    @Size(min = 5, max = 40, message = "Поле должно содержать от 5 до 40 знаков")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Не может быть пустым")
    @Size(min = 5, max = 100, message = "Поле должно содержать от 5 до 40 знаков")
    private String password;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Expense> expenseList;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }
}
