package ru.webapp.exchange.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.webapp.exchange.models.Expense;
import ru.webapp.exchange.models.User;
import ru.webapp.exchange.repositories.ExpenseRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ExpenseService {
    private final ExpenseRepository repo;

    @Autowired
    public ExpenseService(ExpenseRepository repo) {
        this.repo = repo;
    }

    public List<Expense> findAll() {
        return repo.findAll();
    }

    public Optional<Expense> findById(int id) {
        return repo.findById(id);
    }

    public List<Expense> findByUser(User user) {
        return repo.findByOwner(user);
    }

    @Transactional
    public void save(Expense expense) {
        expense.setAddedAt(new Date());
        repo.save(expense);
    }

    @Transactional
    public void update(int id, Expense newExpense) {
        Optional<Expense> oldExpense = repo.findById(id);
        newExpense.setId(id);
        oldExpense.ifPresent(value -> newExpense.setAddedAt(value.getAddedAt()));
        repo.save(newExpense);
    }

    @Transactional
    public void delete(int id) {
        repo.deleteById(id);
    }
}
