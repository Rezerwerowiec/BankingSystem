#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>

// Encryption key (simple method, shift of value)
const int ENCRYPTION_KEY = 3;

// Delimiters for serialization/deserialization for data reading/loading from/to file
const char DELIMITER = '|';
const char ACCOUNT_DELIMITER = ',';
const char ACCOUNT_FIELD_DELIMITER = ';';
const char CUSTOMER_DELIMITER = '\n';

// Global counter for generating unique account IDs
int globalAccountIdCounter = 0;

// Forward declaration of classes
class Customer;
class Account;

// Bank class
class Bank {
private:
    std::vector<Customer*> customers;
    int lastCustomerId = 0; // Last used customer ID

public:
    // Destructor
    ~Bank() {
        for (auto& customer : customers) {
            delete customer;
        }
        customers.clear();
    }

    // Method to save data to file
    void saveToFile(const std::string& filename) const;

    // Method to load data from file
    void loadFromFile(const std::string& filename);

    // Additional methods for managing customers and accounts
    Customer* getCustomerById(int customerId) const;
    Customer* getCustomerByName(const std::string& name) const;
    void addCustomer(const std::string& name, const std::string& password);
    void addAccount(int customerId, double balance);
    void displayCustomerAccounts(int customerId) const;
    void displayAllCustomers() const;
    void transferMoney(int fromAccountId, int toAccountId, double amount);

    // Method to display the menu and process user input
    void displayMenu();
};

// Account class
class Account {
private:
    int accountId;
    double balance;

public:
    Account(int accountId, double balance)
        : accountId(accountId), balance(balance) {}

    int getAccountId() const { return accountId; }
    double getBalance() const { return balance; }
    void deposit(double amount) { balance += amount; }
    void withdraw(double amount) { balance -= amount; }
};

// Customer class
class Customer {
private:
    int customerId;
    std::string name;
    std::string encryptedPassword; // Encrypted password for the customer
    std::vector<Account*> accounts; // Accounts associated with the customer

    // Simple encryption function
    std::string encryptPassword(const std::string& password) const {
        std::string encrypted = password;
        for (char& c : encrypted) {
            c += ENCRYPTION_KEY;
        }
        return encrypted;
    }

public:
    // Constructor
    Customer(int customerId, const std::string& name, const std::string& password)
        : customerId(customerId), name(name), encryptedPassword(encryptPassword(password)) {}

    // Destructor
    ~Customer() {
        for (auto& account : accounts) {
            delete account;
        }
        accounts.clear();
    }

    // Getters
    int getCustomerId() const { return customerId; }
    const std::string& getName() const { return name; }
    const std::string& getPassword() const { return encryptedPassword; }

    const std::vector<Account*>& getAccounts() const {
        return accounts;
    }

    // Methos used to set password for Customer
    void setPassword(const std::string& password) {
        encryptedPassword = encryptPassword(password);
    }

    // Method to add an account to the customer
    void addAccount(Account* account) {
        accounts.push_back(account);
    }
};

// Bank methods implementation
void Bank::saveToFile(const std::string& filename) const {
    std::ofstream outFile(filename);
    if (outFile.is_open()) {
        for (const auto& cust : customers) {
            outFile << cust->getCustomerId() << DELIMITER << cust->getName() << DELIMITER << cust->getPassword() << DELIMITER;
            for (const auto& account : cust->getAccounts()) {
                outFile << account->getAccountId() << ACCOUNT_FIELD_DELIMITER << account->getBalance() << ACCOUNT_DELIMITER;
            }
            outFile << CUSTOMER_DELIMITER;
        }
        outFile.close();
        std::cout << "Dane zostaly zapisane do pliku: " << filename << std::endl;
    }
    else {
        std::cerr << "Nie mozna zapisac pliku: " << filename << std::endl;
    }
}

void Bank::loadFromFile(const std::string& filename) {
    std::ifstream inFile(filename);
    if (inFile.is_open()) {
        std::string line;
        while (std::getline(inFile, line, CUSTOMER_DELIMITER)) {
            // Reading and processing data into the objects
            std::stringstream ss(line);
            std::string customerIdStr, name, password;
            std::getline(ss, customerIdStr, DELIMITER);
            std::getline(ss, name, DELIMITER);
            std::getline(ss, password, DELIMITER);
            Customer* customer = new Customer(std::stoi(customerIdStr), name, password);
            std::string accountData;
            while (std::getline(ss, accountData, ACCOUNT_DELIMITER)) {
                std::stringstream accStream(accountData);
                std::string accountIdStr, balanceStr;
                std::getline(accStream, accountIdStr, ACCOUNT_FIELD_DELIMITER);
                std::getline(accStream, balanceStr, ACCOUNT_FIELD_DELIMITER);
                Account* account = new Account(std::stoi(accountIdStr), std::stod(balanceStr));
                customer->addAccount(account);
                // Update global account ID counter if needed
                globalAccountIdCounter = std::max(globalAccountIdCounter, std::stoi(accountIdStr) + 1);
            }
            customers.push_back(customer);
        }
        inFile.close();
        std::cout << "Dane zaladowane z pliku: " << filename << std::endl;
    }
    else {
        std::cerr << "Nie mozna otworzyc pliku: " << filename << std::endl;
    }
}

// Searching for Customer by its Id
Customer* Bank::getCustomerById(int customerId) const {
    for (const auto& cust : customers) {
        if (cust->getCustomerId() == customerId) {
            return cust;
        }
    }
    return nullptr; // Customer not found
}

// Searching for Customer by its Name(Login)
Customer* Bank::getCustomerByName(const std::string& name) const {
    for (const auto& cust : customers) {
        if (cust->getName() == name) {
            return cust;
        }
    }
    return nullptr; // Customer not found
}

// Add Customer
void Bank::addCustomer(const std::string& name, const std::string& password) {
    Customer* customer = new Customer(++lastCustomerId, name, password);
    customers.push_back(customer);
}

// Add Account to the Customer with given Id
void Bank::addAccount(int customerId, double balance) {
    Customer* customer = getCustomerById(customerId);
    if (customer) {
        Account* account = new Account(++globalAccountIdCounter, balance); // Increase the global tracker for unique Id
        customer->addAccount(account);
    }
}

// Display all Accounts of the certain Customer
void Bank::displayCustomerAccounts(int customerId) const {
    Customer* customer = getCustomerById(customerId);
    if (customer) {
        std::cout << "Lista kont bankowych klienta [" << customer->getName() << "]:\n";
        for (const auto& account : customer->getAccounts()) {
            std::cout << "Numer konta bankowego: " << account->getAccountId() << ", Saldo: " << account->getBalance() << " PLN" << std::endl;
        }
    }
    else {
        std::cerr << "Klient o podanym Id nie istnieje." << std::endl;
    }
}

// Display all customers and their accounts
void Bank::displayAllCustomers() const {
    std::cout << "Wszyscy klienci banku:\n";
    for (const auto& cust : customers) {
        std::cout << "Identyfikator klienta: " << cust->getCustomerId() << ", Nazwa klienta(Login): " << cust->getName() << std::endl;
        displayCustomerAccounts(cust->getCustomerId());
        std::cout << std::endl;
    }
}

// Transfer money between Accounts
void Bank::transferMoney(int fromAccountId, int toAccountId, double amount) {
    for (auto& cust : customers) {
        for (auto& account : cust->getAccounts()) {
            if (account->getAccountId() == fromAccountId) {
                account->withdraw(amount);
            }
            else if (account->getAccountId() == toAccountId) {
                account->deposit(amount);
            }
        }
    }
}

// Method to display the menu and process user input
void Bank::displayMenu() {
    int choice;
    do {
        std::cout << "\n=== Menu Operatora Bankowego ===\n";
        std::cout << "1. Dodaj klienta\n";
        std::cout << "2. Dodaj konto bankowe\n";
        std::cout << "3. Wyswietl konta bankowego danego klienta\n";
        std::cout << "4. Wyswietl wszystkich klientow wraz z ich kontami bankowymi\n";
        std::cout << "5. Utworz transakcje pomiedzy kontami bankowymi\n";
        std::cout << "6. Wyjdz i zapisz wszystkie zmiany\n";
        std::cout << "Wybierz numer z listy powyzej: ";
        std::cin >> choice;

        switch (choice) {
        case 1: {
            std::string name, password;
            std::cout << "Wpisz nazwe klienta(Login): ";
            std::cin >> name;
            std::cout << "Wpisz podane przez klienta haslo: ";
            std::cin >> password;
            addCustomer(name, password);
            break;
        }
        case 2: {
            std::string customerName;
            int customerId;
            double balance;
            std::cout << "Podaj nazwe klienta: ";
            std::cin >> customerName;
            std::cout << "Podaj poczatkowe saldo: ";
            std::cin >> balance;
            customerId = getCustomerByName(customerName)->getCustomerId();
            addAccount(customerId, balance);
            break;
        }
        case 3: {
            std::string customerName;
            int customerId;
            std::cout << "Podaj nazwe klienta: ";
            std::cin >> customerName;
            customerId = getCustomerByName(customerName)->getCustomerId();
            displayCustomerAccounts(customerId);
            break;
        }
        case 4: {
            displayAllCustomers();
            break;
        }
        case 5: {
            int fromAccountId, toAccountId;
            double amount;
            std::cout << "Podaj numer zrodlowego konta bankowego: ";
            std::cin >> fromAccountId;
            std::cout << "Podaj numer docelowego konta bankowego: ";
            std::cin >> toAccountId;
            std::cout << "Podaj kwote transakcji: ";
            std::cin >> amount;
            transferMoney(fromAccountId, toAccountId, amount);
            std::cout << "Pomyslnie wykonano transakcje." << std::endl;
            break;
        }
        case 6:
            std::cout << "Wyjscie z programu i zapisywanie zmian..." << std::endl;
            break;
        default:
            std::cout << "Nieprawidlowy wybor. Sprobuj ponownie..." << std::endl;
        }
    } while (choice != 6);
}

int main() {
    // Initiate Bank object
    Bank bank;
    // Load data from file
    bank.loadFromFile("banksystem.csv");

    // Display menu for bank operator
    bank.displayMenu();

    // Save all changes
    bank.saveToFile("banksystem.csv");

    // Exit program
    return 0;
}
