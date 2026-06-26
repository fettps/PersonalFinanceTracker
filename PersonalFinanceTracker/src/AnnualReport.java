import java.util.List;

/**
 * Another implementation of Reportable, for a full year.
 * Demonstrates POLYMORPHISM: code that works with Reportable
 * does not need to know whether it holds a MonthlyReport or AnnualReport.
 */
public class AnnualReport implements Reportable {
    private final Budget budget;
    private final int year;

    public AnnualReport(Budget budget, int year) {
        this.budget = budget;
        this.year = year;
    }

    @Override
    public String generateSummary() {
        List<Income> incomes = budget.getIncomesInYear(year);
        List<Expense> expenses = budget.getExpensesInYear(year);

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Annual Report: %d ===%n", year));
        sb.append(String.format("Total Income: %.2f%n", totalIncome));
        sb.append(String.format("Total Expenses: %.2f%n", totalExpense));
        sb.append(String.format("Net Balance: %.2f%n", totalIncome - totalExpense));
        sb.append("--- By Category ---\n");
        for (Category c : budget.getCategories()) {
            sb.append(c).append("\n");
        }
        return sb.toString();
    }
}
