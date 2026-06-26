/**
 * INTERFACE: any report type must implement generateSummary().
 * This allows MonthlyReport and AnnualReport to be used polymorphically.
 */
public interface Reportable {
    String generateSummary();
}
