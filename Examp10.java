import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

//Adimn class for fix name and PASSWORD
class Admin {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    private static final String ADMIN_USERNAME = "Code Commandos";
    private static final String ADMIN_PASSWORD = "12345";

    public boolean login(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public void viewAllCustomerAccounts(CustomerManager customerManager) {
        Customer[] customers = customerManager.getAllCustomers();
        System.out.println("\n\n\t\t\t\t\t   " + BLUE + "All Customer Accounts" + RESET + "**\n");
        for (Customer customer : customers) {
            System.out.printf("\tAccount No. %-13s  " , customer.getUsername() );
			System.out.printf(" Name:  %-20s" , customer.getName() );
            System.out.printf("Contact:  %-15s" , customer.getContact() );
            System.out.printf("Balance: %.3f Rs." , customer.getBalance() );
            if (customer.isBlocked()) {
                System.out.print(RED + "\tblocked" + RESET);
            } else {
                // System.out.print(GREEN+"not blocked"+RESET);
            }
            System.out.println();
        }
    }

    public Customer viewCustomerAccount(CustomerManager customerManager, String username) {
        return customerManager.getCustomerByUsername(username);
    }

    public void blockCustomerAccount(CustomerManager customerManager, String username) {
        customerManager.blockCustomer(username);
        System.out.println(GREEN + "\t\t\t\tCustomer account blocked successfully!" + RESET);
    }

    public void createNewAccount(CustomerManager customerManager, String username, String password, String name,
            String contact, double balance) {
        customerManager.createCustomer(username, password, name, contact, balance);
       // System.out.println(GREEN + "\t\t\t\tNew customer account created successfully!" + RESET);
    }

    public void unblockCustomerAccount(CustomerManager customerManager, String username) {
        customerManager.unblockCustomer(username);
        System.out.println(GREEN + "\t\t\t\tCustomer account unblocked successfully!" + RESET);
    }
}

//Customer Operation class
class Customer {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";

    private String username;
    private String password;
    private String name;
    private String contact;
    private double balance;
    private boolean isBlocked;
    private List<Transaction> transactionHistory;
    private double loanBalance;

    public Customer(String username, String password, String name, String contact, double balance) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.contact = contact;
        this.balance = balance;
        this.isBlocked = false;
        this.transactionHistory = new ArrayList<>();
        this.loanBalance = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void updateContact(String newContact) {
        contact = newContact;
    }

     public void updateName(String newName) {
        name = newName;
    }
	
	public void updateAll(String newName,String newContact) {
        name = newName;
		contact = newContact;
    }


    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            recordTransaction("Deposit", amount);
        }
    }

    public boolean withdraw(double amount) {
        if (!isBlocked && amount > 0 && balance >= amount) {
            balance -= amount;
            recordTransaction("Withdrawal", -amount);
            return true;
        }
        return false;
    }

    public void block() {
        isBlocked = true;
    }

    public void unblock() {
        isBlocked = false;
    }

    public void transferMoney(Customer recipient, double amount) {
        if (!isBlocked && amount > 0 && balance >= amount) {
            balance -= amount;
            recipient.deposit(amount);
            recordTransaction("Transfer to " + recipient.getUsername(), -amount);
            recipient.recordTransaction("Received from " + getUsername(), amount);
        }
    }

    public void applyForLoan(double amount) {
        if (!isBlocked && amount > 0 && loanBalance == 0) {
            loanBalance += amount;
            balance += amount;
            recordTransaction("Loan Received", amount);
        }
    }

    public void repayLoan(double amount) {
        if (!isBlocked && amount > 0 && loanBalance > 0) {
            loanBalance -= amount;
            balance -= amount;
            recordTransaction("Loan Repayment", -amount);
        }
    }

    public void printTransactionHistory() {
        System.out.println("\t\t\t\t***" + BLUE + "Transaction History" + RESET + "***");
        for (Transaction transaction : transactionHistory) {
            System.out.print("\tDescription: " + transaction.getDescription());
            System.out.print("\t\tAmount: " + transaction.getAmount() + "rs.");
            System.out.print("\tTimestamp: " + transaction.getTimestamp());
            System.out.println();
        }
    }

    private void recordTransaction(String description, double amount) {
        Transaction transaction = new Transaction(description, amount);
        transactionHistory.add(transaction);
    }

    public String toFileString() {
        return username + "," + password + "," + name + "," + contact + "," + balance + "," + isBlocked + ","
                + loanBalance;
    }

    public void setBlocked(boolean isBlocked2) {
    }
}

//Customer class for read write data in to file
class CustomerManager {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
   
    private Customer[] customers;
    private int customerCount;
    private static final String CUSTOMER_FILE = "customers.txt";

    public CustomerManager() {
        customers = new Customer[300];
        customerCount = 0;
        loadCustomersFromFile();
    }

    private void loadCustomersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    Customer customer = new Customer(data[0], data[1], data[2], data[3], Double.parseDouble(data[4]));
                    customer.block();
                    customer.applyForLoan(Double.parseDouble(data[6]));
                    customers[customerCount++] = customer;
                } else if (data.length == 6) {
                    Customer customer = new Customer(data[0], data[1], data[2], data[3], Double.parseDouble(data[4]));
                    boolean isBlocked = Boolean.parseBoolean(data[5]);
                    customer.setBlocked(isBlocked);
                    customers[customerCount++] = customer;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCustomer(String username, String password, String name, String contact, double balance) {
        if (customerCount < 300) {
            Customer newCustomer = new Customer(username, password, name, contact, balance);
            customers[customerCount++] = newCustomer;
            saveCustomersToFile();
        } else {
            System.out.println(RED + "\t\t\t\tCannot create a new customer limit FULL" + RESET);
        }
    }

    public Customer[] getAllCustomers() {
        Customer[] allCustomers = new Customer[customerCount];
        System.arraycopy(customers, 0, allCustomers, 0, customerCount);
        return allCustomers;
    }

    private void saveCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (int i = 0; i < customerCount; i++) {
                writer.write(customers[i].toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerByUsername(String usernameToFind) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getUsername().equals(usernameToFind)) {
                return customers[i];
            }
        }
        return null;
    }

    public void blockCustomer(String username

    ) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getUsername().equals(username)) {
                customers[i].block();
                saveCustomersToFile();
                return;
            }
        }
    }

    public void unblockCustomer(String username) {
        for (int i = 0; i < customerCount; i++) {
            if (customers[i].getUsername().equals(username)) {
                customers[i].unblock();
                saveCustomersToFile();
                return;
            }
        }
    }

    public boolean transferMoney(String senderUsername, String recipientUsername, double amount) {
        Customer sender = getCustomerByUsername(senderUsername);
        Customer recipient = getCustomerByUsername(recipientUsername);

        if (sender != null && recipient != null) {
            sender.transferMoney(recipient, amount);
            saveCustomersToFile();
            return true;
        }

        return false;
    }

    public void applyForLoan(String username, double amount) {
        Customer customer = getCustomerByUsername(username);
        if (customer != null) {
            customer.applyForLoan(amount);
            saveCustomersToFile();
        }
    }

    public void repayLoan(String username, double amount) {
        Customer customer = getCustomerByUsername(username);
        if (customer != null) {
            customer.repayLoan(amount);
            saveCustomersToFile();
        }
    }
}
//Transcation History class
class Transaction {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";

    private String description;
    private double amount;
    private Date timestamp;

    public Transaction(String description, double amount) {
        this.description = description;
        this.amount = amount;
        this.timestamp = new Date();
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(timestamp);
    }
}

//Main Class
public class BankApp{
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
 

    public static void main(String[] args) {
		//hide the password
        Console console = System.console();
        Scanner scanner = new Scanner(System.in);
        Admin admin = new Admin();
        CustomerManager customerManager = new CustomerManager();

        while (true) {
        System.out.println("\n");
         System.out.println(BLUE+"\t\t\t\t\t  ___ _                 _  "+RESET);
        System.out.println(YELLOW+"\t\t\t\t\t |__   __| |             | |    "+RESET);
        System.out.println(RED+"\t\t\t\t\t    | |  | |__   _ _ _ _ | | __"+RESET);
        System.out.println(PURPLE+"\t\t\t\t\t    | |  | '_ \\ / ` | ' \\| |/ /"+RESET);
        System.out.println(GREEN+"\t\t\t\t\t    | |  | | | | (_| | | | |   < "+RESET);
        System.out.println(CYAN+"\t\t\t\t\t    |_|  |_| |_|\\__,_|_| |_|_|\\_\\"+RESET);
        System.out.println("\t\t\t\t\t                                 ");
        System.out.println(GREEN+"\t\t\t\t\t\t   Your Trusted Bank     \n\n"+RESET);
        
            System.out.println("\t\t\t\t\tWELCOME TO YOUR TRUSTED BANK APPLICATION\n\n");
            System.out.println(
                    "\t\033[0;30m\033[47m-----------------\t\t\t-------------------\t\t\t-----------------\033[0m");
            System.out.println(YELLOW
                    + "\t\033[0;30m\033[47m\u001B[34m 1) Admin Login  \t\t\t 2) Customer Login \t\t\t   0) Exit       \033[0m"
                    + RESET);
            System.out.println(
                    "\t\033[0;30m\033[47m-----------------\t\t\t-------------------\t\t\t-----------------\033[0m\n");

            System.out.println(
                    "\n\n\t..................................................................................................");
            System.out.print("\t\t\t\t\t\tEnter the choice: ");
            int ch = scanner.nextInt();

            switch (ch) {
                case 1:
                    System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
                    System.out.println(YELLOW + "\t\t\t\t\t\tWELCOME TO ADMIN PAGE" + RESET);
                    System.out.println("\t\t\t\t-----------------------------------------------------\n");
                    System.out.print("\t\t\t\tEnter admin name: ");
                    scanner.nextLine();
                    String adminUsername = scanner.nextLine();
                    System.out
                            .println(YELLOW + "\t\t\t\t-----------------------------------------------------" + RESET);
	    char[] passwordArray = console.readPassword("\t\t\t\tEnter admin password: ");
                   String adminPassword = new String(passwordArray);
                    if (admin.login(adminUsername, adminPassword)) {
                        System.out.println(GREEN
                                + "\n\t\t\t------------------------YOU ARE SUCCESSFULLY ADMIN LOGIN---------------------------"
                                + RESET);
                        adminMenu(admin, customerManager, scanner);
                    } else {
                        System.out.println(RED + "\n\t\t\t\tWARNING:Login Failed WRONG PASSWORD OR WRONG NAME" + RESET);
                    }
                    break;

                case 2:
                    System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
                    System.out.println(YELLOW + "\t\t\t\t\t\tWELCOME TO CUSTOMER PAGE" + RESET);
                    System.out.println("\t\t\t\t-----------------------------------------------------");

                    System.out.print("\t\t\t\tenter account number: ");
                    scanner.nextLine();
                    String customerUsername = scanner.nextLine();
                    System.out
                            .println(YELLOW + "\t\t\t\t-----------------------------------------------------" + RESET);
                   char[] passwordArrayC = console.readPassword("\t\t\t\tEnter customer password: ");
        String customerPassword = new String(passwordArrayC);
                    Customer customer = customerManager.getCustomerByUsername(customerUsername);
                    if (customer != null && customer.getPassword().equals(customerPassword)) {
                          System.out.println(GREEN
                                + "\n\t\t\t------------------------YOU ARE SUCCESSFULLY CUSTOMER LOGIN---------------------------"
                                + RESET);
						customerMenu(customerManager, customer, scanner);
                    } else {
                        System.out.println(RED + "\n\t\t\t\tWARNING:Login Failed WRONG PASSWORD OR WRONG NAME" + RESET);
                    }
                    break;

                case 0:
                    System.out.println(GREEN + "\t\t\t\t\t\t---SUCCESSFULLY EXIT---" + RESET);
                    System.out.println(GREEN + "\t\t\t\t\t\t---THANKS FOR VISIT---" + RESET);
                    System.exit(0);

                default:
                    System.out.println(RED + "\t\t\t\t\t\t---Invalid choise---" + RESET);
            }
        }
    }

    private static void adminMenu(Admin admin, CustomerManager customerManager, Scanner scanner) {
Console console = System.console();
     
       
	   while (true) {
            System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
            System.out.println(YELLOW + "\t\t\t\t\t\t WELCOME TO ADMIN PAGE" + RESET);
            System.out.println("\t\t\t\t-----------------------------------------------------");

            System.out.println(
                    "\n\n\t\033[0;30m\033[47m-----------------\t-----------------\t-----------------\t---------------------\033[0m");
            System.out.println(
                    "\t\033[0;30m\033[47m\u001B[36m1)Create Customer\t 2)View Account  \t 3)Block Account \t 4)Unblock Account   \033[0m"
                            + RESET);
            System.out.println(
                    "\t\033[0;30m\033[47m-----------------\t-----------------\t-----------------\t---------------------\033[0m");
            System.out.println("\n\n\t\t\t\t\033[0;30m\033[47m-----------------\t-----------------\033[0m");
            System.out
                    .println("\t\t\t\t\033[0;30m\033[47m\u001B[36m   5)View ALL    \t   0)Exit/Logout \033[0m" + RESET);
            System.out.println("\t\t\t\t\033[0;30m\033[47m-----------------\t-----------------\033[0m");

            System.out.println(
                    "\n\n\t..................................................................................................");
            System.out.print("\t\t\t\t\t\tEnter the choice: ");
            int adminChoice = scanner.nextInt();
			
switch (adminChoice) {

                case 1:
                    System.out.println(BLUE+"\t\t\t\t-------------------------------------------------------"+RESET);
                    System.out.println(YELLOW + "\t\t\t\t1)Current Account(opening amount Minimum is 1000)" + RESET);
                     System.out.println(BLUE+"\t\t\t\t-------------------------------------------------------"+RESET);
                    System.out.println(YELLOW + "\t\t\t\t2)Saving Account(opening amount Minimum is 5000)" + RESET);
                    System.out.println(BLUE+"\t\t\t\t-------------------------------------------------------"+RESET);
                    System.out.println(YELLOW + "\t\t\t\t0)Do not open Account" + RESET);
                     System.out.println(BLUE+"\t\t\t\t-------------------------------------------------------"+RESET);
                   System.out.print("\t\t\t\tEnter the choice: ");
                         int choice = scanner.nextInt();
                           
                          switch (choice) {
                            case 1:
                                
                                 double initialBalance = 0;
                    try {
                        Random random = new Random();
                        int accountNumber = random.nextInt(100000000);
                        String newUsername = String.valueOf(accountNumber);
                        System.out
                                .println(YELLOW + "\n\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                        System.out.println(BLUE + "\t\t\t\tOpening balance is 1000" + RESET);
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                        System.out.println("\t\t\t\tYour Account Number is: " + newUsername);

                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
								char[] pA = console.readPassword("\t\t\t\tEnter new customer's password: ");
                       String newPassword = new String(pA);
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                            System.out.print("\t\t\t\tEnter customer's name: ");
							scanner.nextLine();
                            String newName = scanner.nextLine();
                           
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                        System.out.print("\t\t\t\tEnter customer's contact: ");
                        String newContact = scanner.nextLine();
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
										
                        System.out.print("\t\t\t\tEnter Customer GmailID: ");
                        String gM=scanner.nextLine();
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);		
                        while (true) {

                            try {
                                Scanner input1 = new Scanner(System.in);
                                System.out.print("\t\t\t\tEnter initial balance for the new customer: ");
                                initialBalance = input1.nextDouble();
                                if (initialBalance <1000) {
                                    System.out.println(RED + "\t\t\t\tYour balance is less than 1000.." + RESET);
                                    continue;
                                } else {
                                            System.out.println(GREEN+" \t\t\t\tCongratulations.. your current account has been opened Successfully..."+RESET);
                                    admin.createNewAccount(customerManager, newUsername, newPassword, newName,   newContact, initialBalance);
                                }
                            } catch (Exception e) {
                                System.out.println(RED + "\t\t\t\twrong" + RESET);
                                continue;
                            }
                            break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("\t\t\t\tsomething went wrong");
                    }
                    break;
                          case 2:
                                double Balance = 0;
                         try {
                        Random random = new Random();
                        int accountNumber = random.nextInt(100000000);
                        String newUsername = String.valueOf(accountNumber);
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                        System.out.println(BLUE + "\t\t\t\tOpening balance is 5000" + RESET);
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                        System.out.println("\t\t\t\tYour Account Number is: " + newUsername);

                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
								char[] pA = console.readPassword("\t\t\t\tEnter new customer's password: ");
                       String newPassword = new String(pA);
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                            System.out.print("\t\t\t\tEnter customer's name: ");
							scanner.nextLine();
                            String newName = scanner.nextLine();
                           
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
                        System.out.print("\t\t\t\tEnter customer's contact: ");
                        String newContact = scanner.nextLine();
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);
										
                        System.out.print("\t\t\t\tEnter Customer GmailID: ");
                        String gM=scanner.nextLine();
                        System.out
                                .println(YELLOW + "\t\t\t\t-----------------------------------------------------"
                                        + RESET);		
                        while (true) {

                            try {
                                Scanner input1 = new Scanner(System.in);
                                System.out.print("\t\t\t\tEnter initial balance for the new customer: ");
                                Balance = input1.nextDouble();
                                if (Balance < 5000) {
                                    System.out.println(RED + "\t\t\t\tYour balance is less than 5000.." + RESET);
                                    continue;
                                } else {
                                         System.out.println(GREEN+"\t\t\t\t Congratulations.. your  Saving Account has been opened Successfully..."+RESET);
                                    admin.createNewAccount(customerManager, newUsername, newPassword, newName, newContact,Balance);
                                }
                            } catch (Exception e) {
                                System.out.println(RED + "\t\t\t\twrong" + RESET);
                                continue;
                            }
                            break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(RED+"\t\t\t\tsomething went wrong"+RESET);
                    }
                    break;
                       case 0:
					    System.out.println(GREEN + "\t\t\t\t\t   SUCCESSFULLY EXIT CREATE CUSTOMER PAGE" + RESET);
                    System.out.println(GREEN + "\t\t\t\t\t\tTHANKS FOR VISIT" + RESET);
                      break;					   
						   default:
                                System.out.println(RED+"\t\t\t\tInvaild choice..."+RESET);
                                break;
                          }
                          break;
                case 2:
                    System.out.print("\t\t\t\tEnter the account number of customer's to view: ");
                    scanner.nextLine();
                    String usernameToView = scanner.nextLine();
                    Customer accountToView = admin.viewCustomerAccount(customerManager, usernameToView);
                    if (accountToView != null) {
                        System.out.println("\n\t\t\t\t\t\tAccount Information:");
                        System.out.println(
                                "\t..................................................................................................");
                        System.out.print("\tAccount Number: " + accountToView.getUsername() + "\t");
                        System.out.print("Name: " + accountToView.getName() + "\t");
                        System.out.print("Contact: " + accountToView.getContact() + "\t");
                        System.out.print("Balance: Rs. " + accountToView.getBalance() + "\t");
                        if (accountToView.isBlocked()) {
                            System.out.println(RED + "blocked" + RESET);
                        } else {
                           // System.out.println(GREEN + "not blocked" + RESET);
                        }
                        // accountToView.printTransactionHistory();
                    } else {
                        System.out.println(RED + "\t\t\t\tAccount not found." + RESET);
                    }
                    break;

                case 3:
                    System.out.print("\t\t\t\tEnter the Account Number of customer to block: ");
                    scanner.nextLine();
                    String usernameToBlock = scanner.nextLine();
                    admin.blockCustomerAccount(customerManager, usernameToBlock);
                    break;

                case 4:
                    System.out.print("\t\t\t\tEnter the Account Number of customer to unblock: ");
                    scanner.nextLine();
                    String usernameToUnblock = scanner.nextLine();
                    admin.unblockCustomerAccount(customerManager, usernameToUnblock);
                    break;

                case 5:
                    admin.viewAllCustomerAccounts(customerManager);
                    break;

                case 0:
                    System.out.println(GREEN + "\t\t\t\t\t   SUCCESSFULLY EXIT ADMIN PAGE" + RESET);
                    System.out.println(GREEN + "\t\t\t\t\t\tTHANKS FOR VISIT" + RESET);
                    return;

                default:
                    System.out.println(RED + "\t\t\t\t\t\tInvalid choice" + RESET);
            }
        }
    }

    private static void customerMenu(CustomerManager customerManager, Customer customer, Scanner scanner) {

while (true) {
            Scanner sc = new Scanner(System.in);

            System.out.println(
                    "\n\n\t\033[0;30m\033[47m------------------\t-----------------\t----------------------\t---------------------\033[0m");
            System.out.println(
                    "\t\033[0;30m\033[47m\u001B[36m1)Update Details  \t 2)View Balance  \t3)Transaction History \t 4)Transfer Money    \033[0m"
                            + RESET);
            System.out.println(
                    "\t\033[0;30m\033[47m------------------\t-----------------\t----------------------\t---------------------\033[0m");
            System.out.println("\n\n\t\t\t\t\033[0;30m\033[47m-----------------\t-----------------\033[0m");
            System.out
                    .println("\t\t\t\t\033[0;30m\033[47m\u001B[36m 5)Apply for Loan\t   0)Exit/Logout \033[0m" + RESET);
            System.out.println("\t\t\t\t\033[0;30m\033[47m-----------------\t-----------------\033[0m");
            System.out.println(
                    "\n\n\t..................................................................................................");
            System.out.print("\t\t\t\t\t\tEnter the choice: ");

            int customerChoice = scanner.nextInt();
            scanner.nextLine();

            switch (customerChoice) {
                case 1:

                    System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
                    System.out.println(YELLOW + "\t\t\t\t\t\t    Update Details" + RESET);
                    System.out.println("\t\t\t\t-----------------------------------------------------\n");
                   System.out.println("\t\t-----------------\t-----------------\t-----------------\t-----------------");
              System.out.println(YELLOW+"\t\t1)Customer Name\t\t2)MobileNo.\t\t3)Update All\t\t0)No Change"+RESET);
              System.out.println("\t\t-----------------\t-----------------\t-----------------\t-----------------");
              System.out.print("\t\t\t\tEnter the choice: ");
              int co=scanner.nextInt();
              switch(co){
               
			   case 1:
                System.out.print("\t\t\t\tEnter the customer name: ");
				scanner.nextLine();
                String newName=scanner.nextLine();
				 customer.updateName(newName);
                 System.out.println(GREEN + "\t\t\t\tCustomer Name updated successfully!" + RESET);               
			   break;
               case 2:
			    System.out.print("\t\t\t\tEnter the mobile number: ");
				scanner.nextLine();
                    String newContact = scanner.nextLine();
                    customer.updateContact(newContact);
                    System.out.println(GREEN + "\t\t\t\tContact information updated successfully!" + RESET);
                break;
	           case 3:
	              System.out.print("\tEnter the customer name: ");
				  scanner.nextLine();
                  newName=scanner.nextLine();
                  System.out.println(YELLOW+"\t---------------------------------------------------"+RESET);
	              System.out.print("\tEnter the mobile number: ");
                  newContact=scanner.next();
                  customer.updateAll(newName,newContact);    
				  System.out.println(GREEN + "\t\t\t\tALL information updated successfully!" + RESET);
                  break;
              case 0:
                  System.out.println(GREEN+"\t\t\t\t---No Updation ---"+RESET);
                  System.out.println(GREEN+"\t\t\t\t---THANK U---"+RESET);
                  break;
      
             default:
                 System.out.println(RED+"\t\t\t\t---Invalid choise---"+RESET);
           }
                    break;

                case 2:
                    System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
                    System.out.println(YELLOW + "\t\t\t\t\t\t    View Balance " + RESET);
                    System.out.println("\t\t\t\t-----------------------------------------------------\n");
                    System.out.println("\t\t\t\tYour balance: Rs. " + customer.getBalance());
                    /*
                     * if (customer.isBlocked()) {
                     * System.out.println(RED + "\t\t\t\tYour account is blocked." + RESET);
                     * } else {
                     * System.out.println(GREEN + "\t\t\t\tYour account is not blocked." + RESET);
                     * }
                     */
                    break;

                case 3:
                    System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
                    System.out.println(YELLOW + "\t\t\t\t\t\tTransaction History " + RESET);
                    System.out.println("\t\t\t\t-----------------------------------------------------\n");
                    customer.printTransactionHistory();
                    break;

                case 4:
                    System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
                    System.out.println(YELLOW + "\t\t\t\t\t\t    Transfer Money  " + RESET);
                    System.out.println("\t\t\t\t-----------------------------------------------------\n");

                    System.out.println(
                            "\n\n\t\033[0;30m\033[47m-----------------\t-------------------\t-------------------\t---------------------\033[0m");
                    System.out.println(
                            "\t\033[0;30m\033[47m\u001B[36m1)Deposite money \t 2)Withrawal money \t 3)Transfer money  \t 0)Exit/Logout       \033[0m"
                                    + RESET);
                    System.out.println(
                            "\t\033[0;30m\033[47m-----------------\t-------------------\t-------------------\t---------------------\033[0m");

                    System.out.println(
                            "\n\n\t..................................................................................................");
                    System.out.print("\t\t\t\t\t\tEnter the choice: ");

                    int ch = sc.nextInt();
                    switch (ch) {

                        case 1:
                            System.out.print("\t\t\t\tEnter the amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            scanner.nextLine(); // enter consumed
                            customer.deposit(depositAmount);
                            System.out.println(GREEN + "\t\t\t\tDeposit successful!" + RESET);
                            break;

                        case 2:
                            System.out.print("\t\t\t\tEnter the amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            scanner.nextLine(); // enter consumed
                            if (customer.withdraw(withdrawAmount)) {
                                System.out.println(GREEN + "\t\t\t\tWithdrawal successful!" + RESET);
                            } else {
                                System.out.println(
                                        RED + "\t\t\t\tWithdrawal failed. Insufficient balance or account is blocked."
                                                + RESET);
                            }
                            break;

                        case 3:

                            System.out.print("\t\t\t\tEnter account number for recipient : ");
                            String recipientUsername = scanner.nextLine();
                            System.out.print("\t\t\t\tEnter the amount to transfer: ");
                            double transferAmount = scanner.nextDouble();
                            scanner.nextLine(); // enter consumed
                            boolean success = customerManager.transferMoney(customer.getUsername(), recipientUsername,
                                    transferAmount);
                            if (success) {
                                System.out.println(GREEN + "\t\t\t\tTransfer successful!" + RESET);
                            } else {
                                System.out.println(RED
                                        + "\t\t\t\tTransfer failed. Check recipient username or insufficient balance."
                                        + RESET);
                            }
                            break;
                        case 0:
                            System.out.println(
                                    RED + "\t\t\t\t\t\tExit from transfer money" + RESET);
                            break;
                        default:
                            System.out.println(
                                    RED + "\t\t\t\t\tInvalid choice" + RESET);
                    }
                    if (customerChoice == 4)
                        break;
                case 5:
                    System.out.println("\n\n\t\t\t\t-----------------------------------------------------");
                    System.out.println(YELLOW + "\t\t\t\t\t\t   Apply for Loan  " + RESET);
                    System.out.println("\t\t\t\t-----------------------------------------------------\n");
                    System.out.print("\t\t\t\tenter account number: ");
                    double loanAmount = scanner.nextDouble();
                    customerManager.applyForLoan(customer.getUsername(), loanAmount);
                    System.out.println("\t\t\t-----------------\t-----------------\t-----------------");
                    System.out.println(YELLOW + "\t\t\t1)Personal Loan\t\t2)Education Loan\t   0) Exit  " + RESET);
                    System.out.println("\t\t\t-----------------\t-----------------\t-----------------");
                    System.out.print("\t\t\t\tEnter the choice: ");
                    int n = sc.nextInt();
                    switch (n) {
                        case 1:
                            System.out.println("\n\n\t\t\t\t\t\t-----------------");
                            System.out.println(YELLOW + "\t\t\t\t\t\t  Personal Loan" + RESET);
                            System.out.println("\t\t\t\t\t\t-----------------");
                            System.out.println(
                                    "\t\t\t*Loan offer Starting from Min.RS 50,001/- to Max.RS 5,00,000/-\n\n\t\t\t* Interest rate Starting from Min.12.40% to Max.15.90%\n\n\t\t\t* Age between 21 t0 57 \n\n\t\t\t* Loan Repayment period from Min.18 months tp Max.36 months\n\n\t\t\t* Processing charges: 2% of the amount (Min.RS 1000/- Max.RS 10,000)\n");
                            System.out.println("\t\t\t\t-----------------\t-----------------");
                            System.out.println("\t\t\t\t     (1)YES  \t\t     (2)NO");
                            System.out.println("\t\t\t\t-----------------\t-----------------");
                            System.out.print("\t\t\t\tEnter the choice: ");
                            int n1 = sc.nextInt();
                            switch (n1) {
                                case 1:
                                    System.out.println(YELLOW + "\t\t\t\tApply for loan Enter the details: " + RESET);
                                    System.out.print("\t\t\t\tEnter the moblie number: ");
                                    String mb = sc.next();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the Adhar Number: ");
                                    String ad = sc.next();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the PIN card Number: ");
                                    String pn = sc.next();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the  age: ");
                                    int ag = sc.nextInt();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the your salary:");
                                    int salary = sc.nextInt();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the Amount for loan: ");
                                    double am = sc.nextDouble();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the todays date: ");
                                    sc.nextLine();
                                    String date = sc.nextLine();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the Duration(number of year) of loan:");
                                    int Duration = sc.nextInt();
                                   
                                    double Interest = (am * 12) / 100;
                                    double m = Duration * 12;
                                    double emi = (am + Interest) / m;
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                     System.out.print("\t\t\t\tyour loan monthly EMI: ");
                                        System.out.printf("%.3f", emi);
                                    if ((ag >= 21 && ag <= 57) && (am >= 50001 && am <= 500000) && emi < salary) {
                                        System.out.println(GREEN + "\n\t\t\t\tLOAN IS SUCCESSFULLY PASSED... " + RESET);
                                        break;
                                    } else
                                        System.out.println(RED + "\t\t\t\tLOAN IS FAILED... " + RESET);
                                    break;

                                case 2:
                                    System.out.println(RED + "\t\t\t\tYour are Exit personal loan page.." + RESET);
                                    break;
                                default:
                                    System.out.println(
                            RED + "\t\t\t\tInvalid choice" + RESET);
                                    break;

                            }
                            break;
                        case 2:
                            System.out.println("\n\n\t\t\t\t\t\t-----------------");
                            System.out.println(YELLOW + "\t\t\t\t\t\t  Education Loan" + RESET);
                            System.out.println("\t\t\t\t\t\t-----------------");
                            System.out.println(
                                    "\t\t\t*Loan offer Starting from Min.RS 50,000/- to Max.RS 1Cr./-\n\n\t\t\t* Age 18 to 25 \n\n\t\t\t* Interest rate Starting from Min.10.10% to Max.12.00%\n\n\t\t\t* Loan Repayment period from Min.18 months tp Max.36 months\n\n\t\t\t* Processing charges: 2% of the amount (Min.RS 1000/- Max.RS 10,000)");
                            System.out.println("\t\t\t\t-----------------\t-----------------");
                            System.out.println("\t\t\t\t     (1)YES  \t\t     (2)NO");
                            System.out.println("\t\t\t\t-----------------\t-----------------");
                            System.out.print("\t\t\t\tEnter the choice: ");
                            int n2 = sc.nextInt();

                            switch (n2) {

                                case 1:
                                    System.out.println(YELLOW + "\t\t\t\tApply for loan Enter the details " + RESET);
                                    System.out.print("\t\t\t\tEnter the moblie number: ");
                                    String mb = sc.next();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the Adhar Number: ");
                                    String ad = sc.next();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tLetter for principle: ");
                                    char ch1 = sc.next().charAt(0);
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the  age: ");
                                    int ag = sc.nextInt();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the Amount for loan: ");
                                    double am = sc.nextDouble();
                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                    System.out.print("\t\t\t\tEnter the Duration(number of year) of loan:");
                                    int Duration = sc.nextInt();

                                    System.out.println(
                                            YELLOW + "\t\t\t\t-------------------------------------------" + RESET);
                                      double Interest = (am * 10) / 100;
                                        double m = Duration * 12;
                                        double emi = (am + Interest) / m;
                                       System.out.print("\t\t\t\tyour loan monthly EMI: ");
                                        System.out.printf("%.3f", emi);
                                    if ((ag >= 18 && ag <= 25) && (am >= 50000 && am <= 10000000)
                                            && (ch1 == 'y' || ch1 == 'Y')) {
                                        System.out
                                                .println(GREEN + "\n\t\t\t\t LOAN IS SUCCESSFULLY PASSED... " + RESET);
                                      
                                        
                                        
                                        
                                        break;
                                    } else
                                        System.out.println(RED + "\t\t\t\tLOAN IS FAILED... " + RESET);
                                    break;

                                case 2:
                                    System.out.println(RED + "\t\t\t\tYour are Exit Education loan page.." + RESET);
                                    break;
                                default:
                                    System.out.println(
                            RED + "\t\t\t\tInvalid choice" + RESET);
                                    break;

                            }
                    }
                    break;
              
			  case 0:
                    System.out.println(GREEN + "\t\t\t\t\t   SUCCESSFULLY EXIT CUSTOMER PAGE" + RESET);
                    System.out.println(GREEN + "\t\t\t\t\t\tTHANKS FOR VISIT" + RESET);
                    return;

                default:
                    System.out.println(
                            RED + "\t\t\t\tInvalid choice" + RESET);
            }
            if (customerChoice == 6)
                break;
        }
    }
}















