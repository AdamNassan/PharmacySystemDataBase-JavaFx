package org.example.databasefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private int saleID;
    private int customerID;
    private Date saleDate;
    private Double totalAmount;

    // Constructors
    public Sale() {
        // Default constructor
    }

    public Sale(int saleID, int customerID, Date saleDate, Double totalAmount) {
        this.saleID = saleID;
        this.customerID = customerID;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Other methods...

    public static ArrayList<Sale> getAllSales(Connection connection) throws SQLException {
        ArrayList<Sale> sales = new ArrayList<>();
        String query = "SELECT * FROM Sales";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Sale sale = new Sale();
                sale.setSaleID(resultSet.getInt("SaleID"));
                sale.setCustomerID(resultSet.getInt("CustomerID"));
                sale.setSaleDate(resultSet.getDate("SaleDate"));
                sale.setTotalAmount(resultSet.getDouble("TotalAmount"));
                sales.add(sale);
            }
        }
        return sales;
    }

    public static void insertSale(int saleID, int customerID, Date saleDate, Double totalAmount, Connection connection) throws SQLException {
        String query = "INSERT INTO Sales (SaleID, CustomerID, SaleDate, TotalAmount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, saleID);
            preparedStatement.setInt(2, customerID);
            preparedStatement.setDate(3, new java.sql.Date(saleDate.getTime()));
            preparedStatement.setDouble(4, totalAmount);

            preparedStatement.executeUpdate();
        }
    }

    public static void deleteSale(int saleID, Connection connection) throws SQLException {
        String query = "DELETE FROM Sales WHERE SaleID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, saleID);

            preparedStatement.executeUpdate();
        }
    }

    public static void updateSale(int saleID, int customerID, Date saleDate, Double totalAmount, Connection connection) throws SQLException {
        String query = "UPDATE Sales SET CustomerID = ?, SaleDate = ?, TotalAmount = ? WHERE SaleID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);
            preparedStatement.setDate(2, new java.sql.Date(saleDate.getTime()));
            preparedStatement.setDouble(3, totalAmount);
            preparedStatement.setInt(4, saleID);

            preparedStatement.executeUpdate();
        }
    }
    public static ArrayList<Sale> getSalesByCustomer(int customerID, Connection connection) throws SQLException {
        ArrayList<Sale> sales = new ArrayList<>();
        String query = "SELECT * FROM Sales WHERE CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Sale sale = new Sale();
                sale.setSaleID(resultSet.getInt("SaleID"));
                sale.setCustomerID(resultSet.getInt("CustomerID"));
                sale.setSaleDate(resultSet.getDate("SaleDate"));
                sale.setTotalAmount(resultSet.getDouble("TotalAmount"));
                sales.add(sale);
            }
        }
        return sales;
    }

}
