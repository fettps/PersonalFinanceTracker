import java.time.LocalDate;

/**
 * Represents money going out, always tied to a Category.
 * INHERITANCE: extends Transaction.
 */
public class Expense extends Transaction {
    private final String categoryName;

    public Expense(String description, double amount, LocalDate date, String categoryName) {
        super(description, amount, date);
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String getType() {
        return "Expense";
    }

    @Override
    public String toString() {
        return super.toString() + " (Category: " + categoryName + ")";
    }
}
