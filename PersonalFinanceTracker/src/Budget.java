import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all transactions and budget categories.
 * COMPOSITION: Budget "has-a" collection of Category objects. Categories
 * cannot meaningfully exist without a Budget managing them in this design,
 * which is the hallmark of composition (as opposed to a looser aggregation).
 */
public class Budget {
    private final Map<String, Category> categories = new LinkedHashMap<>();
    private final List<Income> incomes = new ArrayList<>();

    public void addCategory(String name, double limit) {
        categories.put(name, new Category(name, limit));
    }

    public Category getCategory(String name) {
        return categories.get(name);
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories.values());
    }

    public void addIncome(Income income) {
        incomes.add(income);
    }

    public List<Income> getIncomes() {
        return new ArrayList<>(incomes);
    }

    /**
     * Attempts to register an expense against its category.
     * Returns true if accepted, false if rejected due to overspending.
     */
    public boolean addExpense(Expense expense) {
        Category category = categories.get(expense.getCategoryName());
        if (category == null) {
            throw new IllegalArgumentException("Unknown category: " + expense.getCategoryName());
        }
        return category.addExpense(expense);
    }

    public double getTotalIncome() {
        return incomes.stream().mapToDouble(Income::getAmount).sum();
    }

    public double getTotalExpenses() {
        return categories.values().stream().mapToDouble(Category::getCurrentSpent).sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    public List<Expense> getExpensesInMonth(int year, int month) {
        List<Expense> result = new ArrayList<>();
        for (Category c : categories.values()) {
            for (Expense e : c.getExpenses()) {
                LocalDate d = e.getDate();
                if (d.getYear() == year && d.getMonthValue() == month) {
                    result.add(e);
                }
            }
        }
        return result;
    }

    public List<Income> getIncomesInMonth(int year, int month) {
        List<Income> result = new ArrayList<>();
        for (Income i : incomes) {
            if (i.getDate().getYear() == year && i.getDate().getMonthValue() == month) {
                result.add(i);
            }
        }
        return result;
    }

    public List<Expense> getExpensesInYear(int year) {
        List<Expense> result = new ArrayList<>();
        for (Category c : categories.values()) {
            for (Expense e : c.getExpenses()) {
                if (e.getDate().getYear() == year) {
                    result.add(e);
                }
            }
        }
        return result;
    }

    public List<Income> getIncomesInYear(int year) {
        List<Income> result = new ArrayList<>();
        for (Income i : incomes) {
            if (i.getDate().getYear() == year) {
                result.add(i);
            }
        }
        return result;
    }
}
