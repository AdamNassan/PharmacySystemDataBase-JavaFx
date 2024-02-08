package org.example.databasefx;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private String address;
    private String contactNumber;
    private String email;

    // Constructors
    public Customer() {
        // Default constructor
    }

    public Customer(int customerID, String firstName, String lastName, String address, String contactNumber, String email) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // Getters and setters
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Other methods...

    public static ArrayList<Customer> getAllCustomers(Connection connection) throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(resultSet.getInt("CustomerID"));
                customer.setFirstName(resultSet.getString("FirstName"));
                customer.setLastName(resultSet.getString("LastName"));
                customer.setAddress(resultSet.getString("Address"));
                customer.setContactNumber(resultSet.getString("ContactNumber"));
                customer.setEmail(resultSet.getString("Email"));
                customers.add(customer);
            }
        }
        return customers;
    }

    public static void insertCustomer(int customerID, String firstName, String lastName, String address, String contactNumber, String email, Connection connection) throws SQLException {
        String query = "INSERT INTO Customers (CustomerID, FirstName, LastName, Address, ContactNumber, Email) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);
            if(!firstName.isEmpty())
                preparedStatement.setString(2, firstName);
            if(!lastName.isEmpty())
                preparedStatement.setString(3, lastName);
            if(!address.isEmpty())
                preparedStatement.setString(4, address);
            if(!contactNumber.isEmpty())
                preparedStatement.setString(5, contactNumber);
            if(!email.isEmpty())
                preparedStatement.setString(6, email);

            preparedStatement.executeUpdate();
        }
    }

    public static void deleteCustomer(int customerID, Connection connection) throws SQLException {
        String query = "DELETE FROM Customers WHERE CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);

            preparedStatement.executeUpdate();
        }
    }

    public static void updateCustomer(int customerID, String firstName, String lastName, String address, String contactNumber, String email, Connection connection) throws SQLException {
        String query = "UPDATE Customers SET FirstName = COALESCE(?, FirstName), LastName = COALESCE(?, LastName), Address = COALESCE(?, Address), ContactNumber = COALESCE(?, ContactNumber), Email = COALESCE(?, Email) WHERE CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, (firstName.isEmpty() ? null : firstName));
            preparedStatement.setString(2, (lastName.isEmpty() ? null : lastName));
            preparedStatement.setString(3, (address.isEmpty() ? null : address));
            preparedStatement.setString(4, (contactNumber.isEmpty() ? null : contactNumber));
            preparedStatement.setString(5, (email.isEmpty() ? null : email));
            preparedStatement.setInt(6, customerID);

            preparedStatement.executeUpdate();
        }
    }

    public static Customer getCustomerByID(int customerID, Connection connection) throws SQLException {
        Customer customer = null;
        String query = "SELECT * FROM Customers WHERE CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customer = new Customer();
                // Populate the Customer object with data from the result set
                customer.setCustomerID(resultSet.getInt("CustomerID"));
                customer.setFirstName(resultSet.getString("FirstName"));
                customer.setLastName(resultSet.getString("LastName"));
                customer.setAddress(resultSet.getString("Address"));
                customer.setContactNumber(resultSet.getString("ContactNumber"));
                customer.setEmail(resultSet.getString("Email"));
            }
        }
        return customer;
    }
    public static ArrayList<Customer> getCustomerByName(String name, Connection connection) throws SQLException {
        ArrayList<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customers WHERE FirstName LIKE ? OR LastName LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setString(2, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer();
                // Populate the Customer object with data from the result set
                customer.setCustomerID(resultSet.getInt("CustomerID"));
                customer.setFirstName(resultSet.getString("FirstName"));
                customer.setLastName(resultSet.getString("LastName"));
                customer.setAddress(resultSet.getString("Address"));
                customer.setContactNumber(resultSet.getString("ContactNumber"));
                customer.setEmail(resultSet.getString("Email"));
                customers.add(customer);
            }
        }
        return customers;
    }

}
