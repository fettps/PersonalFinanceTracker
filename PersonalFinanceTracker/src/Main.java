import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Main entry point + GUI for the Personal Finance Tracker.
 * Built with Swing so it runs with plain Java, no extra libraries needed.
 */
public class Main extends JFrame {
    private final Budget budget = new Budget();

    private final JComboBox<String> categoryBox = new JComboBox<>();
    private final JTextArea outputArea = new JTextArea();
    private final JTextField descField = new JTextField(10);
    private final JTextField amountField = new JTextField(6);

    public Main() {
        super("Personal Finance Tracker");

        // Pre-populate a few categories so the app is usable immediately
        budget.addCategory("Food", 500);
        budget.addCategory("Rent", 1000);
        budget.addCategory("Entertainment", 200);
        for (Category c : budget.getCategories()) {
            categoryBox.addItem(c.getName());
        }

        setLayout(new BorderLayout(10, 10));
        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildOutputPanel(), BorderLayout.CENTER);
        add(buildSummaryPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 520);
        setLocationRelativeTo(null);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add Transaction"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(descField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 3;
        panel.add(amountField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Category (for expenses):"), gbc);
        gbc.gridx = 1;
        panel.add(categoryBox, gbc);

        JButton addIncomeBtn = new JButton("Add Income");
        JButton addExpenseBtn = new JButton("Add Expense");
        addIncomeBtn.addActionListener(e -> onAddIncome());
        addExpenseBtn.addActionListener(e -> onAddExpense());

        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(addIncomeBtn, gbc);
        gbc.gridx = 3;
        panel.add(addExpenseBtn, gbc);

        return panel;
    }

    private JPanel buildOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Log / Reports"));
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildSummaryPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton monthlyBtn = new JButton("Generate Monthly Report (current month)");
        JButton annualBtn = new JButton("Generate Annual Report (current year)");
        JButton balanceBtn = new JButton("Show Balance & Categories");

        monthlyBtn.addActionListener(e -> {
            LocalDate now = LocalDate.now();
            Reportable report = new MonthlyReport(budget, now.getYear(), now.getMonthValue());
            outputArea.setText(report.generateSummary());
        });

        annualBtn.addActionListener(e -> {
            LocalDate now = LocalDate.now();
            Reportable report = new AnnualReport(budget, now.getYear());
            outputArea.setText(report.generateSummary());
        });

        balanceBtn.addActionListener(e -> showBalance());

        panel.add(monthlyBtn);
        panel.add(annualBtn);
        panel.add(balanceBtn);
        return panel;
    }

    private void onAddIncome() {
        try {
            String desc = descField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            Income income = new Income(desc.isEmpty() ? "Income" : desc, amount, LocalDate.now());
            budget.addIncome(income);
            log("Added: " + income);
            clearInputs();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddExpense() {
        try {
            String desc = descField.getText().trim();
            double amount = Double.parseDouble(amountField.getText().trim());
            String category = (String) categoryBox.getSelectedItem();
            if (category == null) {
                JOptionPane.showMessageDialog(this, "No category selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Expense expense = new Expense(desc.isEmpty() ? "Expense" : desc, amount, LocalDate.now(), category);
            boolean accepted = budget.addExpense(expense);
            if (accepted) {
                log("Added: " + expense);
            } else {
                Category cat = budget.getCategory(category);
                log(String.format(
                    "REJECTED: '%s' (%.2f) would exceed the '%s' limit. Remaining budget: %.2f",
                    desc, amount, category, cat.getRemaining()));
                JOptionPane.showMessageDialog(this,
                    "This expense would exceed the limit for category '" + category + "'.\n" +
                    "Remaining in this category: " + String.format("%.2f", cat.getRemaining()),
                    "Overspending blocked", JOptionPane.WARNING_MESSAGE);
            }
            clearInputs();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBalance() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Total Income: %.2f%n", budget.getTotalIncome()));
        sb.append(String.format("Total Expenses: %.2f%n", budget.getTotalExpenses()));
        sb.append(String.format("Balance: %.2f%n%n", budget.getBalance()));
        sb.append("--- Categories ---\n");
        List<Category> cats = budget.getCategories();
        for (Category c : cats) {
            sb.append(c).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void log(String message) {
        outputArea.append(message + "\n");
    }

    private void clearInputs() {
        descField.setText("");
        amountField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
