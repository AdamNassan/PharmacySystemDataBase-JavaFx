package org.example.databasefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private int supplierID;
    private String name;
    private String contactPerson;
    private String contactNumber;

    // Constructors
    public Supplier() {
        // Default constructor
    }

    public Supplier(int supplierID, String name, String contactPerson, String contactNumber) {
        this.supplierID = supplierID;
        this.name = name;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
    }

    // Getters and setters
    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    // Other methods...

    public static ArrayList<Supplier> getAllSuppliers(Connection connection) throws SQLException {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM Suppliers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierID(resultSet.getInt("SupplierID"));
                supplier.setName(resultSet.getString("Name"));
                supplier.setContactPerson(resultSet.getString("ContactPerson"));
                supplier.setContactNumber(resultSet.getString("ContactNumber"));
                suppliers.add(supplier);
            }
        }
        return suppliers;
    }

    public static void insertSupplier(int supplierID, String name, String contactPerson, String contactNumber, Connection connection) throws SQLException {
        String query = "INSERT INTO Suppliers (SupplierID, Name, ContactPerson, ContactNumber) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, supplierID);
            if(!name.isEmpty())
                preparedStatement.setString(2, name);
            if(!contactPerson.isEmpty())
                preparedStatement.setString(3, contactPerson);
            if(!contactNumber.isEmpty())
                preparedStatement.setString(4, contactNumber);

            preparedStatement.executeUpdate();
        }
    }

    public static void deleteSupplier(int supplierID, Connection connection) throws SQLException {
        String query = "DELETE FROM Suppliers WHERE SupplierID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, supplierID);

            preparedStatement.executeUpdate();
        }
    }

    public static void updateSupplier(int supplierID, String name, String contactPerson, String contactNumber, Connection connection) throws SQLException {
        String query = "UPDATE Suppliers SET Name = COALESCE(?, Name), ContactPerson = COALESCE(?, ContactPerson), ContactNumber = COALESCE(?, ContactNumber) WHERE SupplierID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, (name.isEmpty() ? null : name));
            preparedStatement.setString(2, (contactPerson.isEmpty() ? null : contactPerson));
            preparedStatement.setString(3, (contactNumber.isEmpty() ? null : contactNumber));
            preparedStatement.setInt(4, supplierID);

            preparedStatement.executeUpdate();
        }
    }

}
