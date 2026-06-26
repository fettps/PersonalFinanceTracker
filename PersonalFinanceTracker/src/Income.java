import java.time.LocalDate;

/**
 * Represents money coming in (salary, gifts, etc).
 * INHERITANCE: extends Transaction.
 */
public class Income extends Transaction {

    public Income(String description, double amount, LocalDate date) {
        super(description, amount, date);
    }

    @Override
    public String getType() {
        return "Income";
    }
}
