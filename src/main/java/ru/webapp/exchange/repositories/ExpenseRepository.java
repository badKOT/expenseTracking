package ru.webapp.exchange.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.webapp.exchange.models.Expense;
import ru.webapp.exchange.models.User;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByOwner(User owner);
}
