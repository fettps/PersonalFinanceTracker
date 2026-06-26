import java.time.LocalDate;

/**
 * Base class for all financial transactions.
 * Demonstrates INHERITANCE: Income and Expense extend this class.
 * Demonstrates ENCAPSULATION: fields are private, accessed via getters.
 */
public abstract class Transaction {
    private final String description;
    private final double amount;
    private final LocalDate date;

    public Transaction(String description, double amount, LocalDate date) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Each subclass defines its own transaction type label.
     */
    public abstract String getType();

    @Override
    public String toString() {
        return String.format("[%s] %s: %s - %.2f", date, getType(), description, amount);
    }
}
