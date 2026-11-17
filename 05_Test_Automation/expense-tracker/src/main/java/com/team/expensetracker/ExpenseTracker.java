package com.team.expensetracker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Stores expenses and supports add, edit, delete and search. */
public class ExpenseTracker {
    private final ExpenseValidator validator;
    private final List<Expense> expenses = new ArrayList<>();
    private int nextId = 1;

    public ExpenseTracker(ExpenseValidator validator) {
        this.validator = validator;
    }

    public Expense addExpense(BigDecimal amount, Category category, String description, LocalDate date) {
        validator.validate(amount, category, description, date);
        Expense expense = new Expense(nextId++, amount, category, description.trim(), date);
        expenses.add(expense);
        return expense;
    }

    public Expense updateExpense(int id, BigDecimal amount, Category category, String description, LocalDate date) {
        Expense existing = findById(id).orElseThrow(() -> new ValidationException("Expense " + id + " not found"));
        validator.validate(amount, category, description, date);
        Expense updated = new Expense(existing.id(), amount, category, description.trim(), date);
        expenses.set(expenses.indexOf(existing), updated);
        return updated;
    }

    public void deleteExpense(int id) {
        Expense existing = findById(id).orElseThrow(() -> new ValidationException("Expense " + id + " not found"));
        expenses.remove(existing);
    }

    public Optional<Expense> findById(int id) {
        return expenses.stream().filter(e -> e.id() == id).findFirst();
    }

    public List<Expense> getAll() {
        return expenses.stream().sorted(Comparator.comparing(Expense::date)).toList();
    }

    public List<Expense> findByCategory(Category category) {
        return expenses.stream().filter(e -> e.category() == category).toList();
    }

    /** Returns expenses whose date is between start and end, both inclusive. */
    public List<Expense> findBetween(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start date must not be after end date");
        }
        return expenses.stream()
                .filter(e -> !e.date().isBefore(start) && !e.date().isAfter(end))
                .sorted(Comparator.comparing(Expense::date))
                .toList();
    }
}
