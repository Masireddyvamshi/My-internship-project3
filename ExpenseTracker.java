import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Expense {
    private String date;
    private String category;
    private double amount;

    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    
    public String toString() {
        return "Date: " + date + ", Category: " + category + ", Amount: $" + amount;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String username;
    private String password;
    private ArrayList<Expense> expenses;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.expenses = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}

public class ExpenseTracker {
    private ArrayList<User> users;
    private User currentUser;

    public ExpenseTracker() {
        users = new ArrayList<>();
    }

    public void registerUser(String username, String password) {
        User newUser = new User(username, password);
        users.add(newUser);
        currentUser = newUser;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void addExpense(String date, String category, double amount) {
        Expense newExpense = new Expense(date, category, amount);
        currentUser.addExpense(newExpense);
    }

    public void listExpenses() {
        for (Expense expense : currentUser.getExpenses()) {
            System.out.println(expense);
        }
    }

    public void listExpensesSortedByCategory() {
        Collections.sort(currentUser.getExpenses(), Comparator.comparing(Expense::getCategory));
        listExpenses();
    }

    public double getTotalExpenseForCategory(String category) {
        double totalExpense = 0;
        for (Expense expense : currentUser.getExpenses()) {
            if (expense.getCategory().equals(category)) {
                totalExpense += expense.getAmount();
            }
        }
        return totalExpense;
    }

    public void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("expenses.txt", true))) {
            for (Expense expense : currentUser.getExpenses()) {
                writer.println(expense.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving expenses: " + e.getMessage());
        }
    }

    public void loadExpenses() {
        try (Scanner reader = new Scanner(new File("expenses.txt"))) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String date = parts[0].trim();
                    String category = parts[1].trim();
    
                    
                    double amount = Double.parseDouble(parts[2].trim().replace("Amount: $", ""));
    
                    Expense expense = new Expense(date, category, amount);
                    currentUser.addExpense(expense);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading expenses: " + e.getMessage());
        }
    }
    

    public static void main(String[] args) {
        ExpenseTracker expenseTracker = new ExpenseTracker();

        
        expenseTracker.registerUser("user", "Mypass@123");
        expenseTracker.login("user1", "Mypass@123");

        
        expenseTracker.addExpense("2024-01-01", "Food", 20.50);
        expenseTracker.addExpense("2024-02-01", "Utilities", 50.00);
        expenseTracker.addExpense("2024-02-02", "movies", 30.25);

        
        System.out.println("All Expenses:");
        expenseTracker.listExpenses();

        
        System.out.println("\nExpenses Sorted by Category:");
        expenseTracker.listExpensesSortedByCategory();

        
        String categoryToCheck = "Food";
        double totalExpense = expenseTracker.getTotalExpenseForCategory(categoryToCheck);
        System.out.println("\nTotal Expense for Category '" + categoryToCheck + "': $" + totalExpense);

        
        expenseTracker.saveExpenses();

        
        expenseTracker.loadExpenses();
    }
}
