import java.util.List;

/**
 * INTERFACE IMPLEMENTATION + POLYMORPHISM:
 * MonthlyReport implements Reportable, providing its own version
 * of generateSummary() for a specific month/year.
 */
public class MonthlyReport implements Reportable {
    private final Budget budget;
    private final int year;
    private final int month;

    public MonthlyReport(Budget budget, int year, int month) {
        this.budget = budget;
        this.year = year;
        this.month = month;
    }

    @Override
    public String generateSummary() {
        List<Income> incomes = budget.getIncomesInMonth(year, month);
        List<Expense> expenses = budget.getExpensesInMonth(year, month);

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Monthly Report: %d-%02d ===%n", year, month));
        sb.append(String.format("Total Income: %.2f%n", totalIncome));
        sb.append(String.format("Total Expenses: %.2f%n", totalExpense));
        sb.append(String.format("Net Balance: %.2f%n", totalIncome - totalExpense));
        sb.append("--- Transactions ---\n");
        for (Income i : incomes) sb.append(i).append("\n");
        for (Expense e : expenses) sb.append(e).append("\n");
        return sb.toString();
    }
}
