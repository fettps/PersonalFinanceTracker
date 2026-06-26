import java.util.ArrayList;
import java.util.List;

/**
 * Represents a budget category (e.g., Food, Rent, Entertainment).
 * ENCAPSULATION: the spending limit and current total are private;
 * the only way to add an expense is through addExpense(), which
 * enforces the "no overspending" rule internally.
 */
public class Category {
    private final String name;
    private double limit;
    private double currentSpent;
    private final List<Expense> expenses = new ArrayList<>();

    public Category(String name, double limit) {
        this.name = name;
        this.limit = limit;
        this.currentSpent = 0.0;
    }

    public String getName() {
        return name;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getCurrentSpent() {
        return currentSpent;
    }

    public double getRemaining() {
        return limit - currentSpent;
    }

    public List<Expense> getExpenses() {
        return new ArrayList<>(expenses); // defensive copy
    }

    /**
     * Tries to add an expense to this category.
     * Returns true if it was added, false if it was rejected
     * because it would exceed the category's limit (overspending guard).
     */
    public boolean addExpense(Expense expense) {
        if (currentSpent + expense.getAmount() > limit) {
            return false; // reject: would overspend
        }
        expenses.add(expense);
        currentSpent += expense.getAmount();
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s (Spent: %.2f / Limit: %.2f)", name, currentSpent, limit);
    }
}
