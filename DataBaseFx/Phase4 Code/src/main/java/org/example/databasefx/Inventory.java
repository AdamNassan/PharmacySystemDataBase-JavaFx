package org.example.databasefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int inventoryID;
    private int medicineID;
    private Date purchaseDate;
    private Double purchasePrice;
    private int quantityReceived;

    // Constructors
    public Inventory() {
        // Default constructor
    }

    public Inventory(int inventoryID, int medicineID, Date purchaseDate, Double purchasePrice, int quantityReceived) {
        this.inventoryID = inventoryID;
        this.medicineID = medicineID;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.quantityReceived = quantityReceived;
    }

    // Getters and setters
    public int getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(int inventoryID) {
        this.inventoryID = inventoryID;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(int quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    // Other methods...

    public static ArrayList<Inventory> getAllInventory(Connection connection) throws SQLException {
        ArrayList<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT * FROM Inventory";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Inventory inventory = new Inventory();
                inventory.setInventoryID(resultSet.getInt("InventoryID"));
                inventory.setMedicineID(resultSet.getInt("MedicineID"));
                inventory.setPurchaseDate(resultSet.getDate("PurchaseDate"));
                inventory.setPurchasePrice(resultSet.getDouble("PurchasePrice"));
                inventory.setQuantityReceived(resultSet.getInt("QuantityReceived"));
                inventoryList.add(inventory);
            }
        }
        return inventoryList;
    }

    public static void insertInventory(int inventoryID, int medicineID, Date purchaseDate, Double purchasePrice, int quantityReceived, Connection connection) throws SQLException {
        String query = "INSERT INTO Inventory (InventoryID, MedicineID, PurchaseDate, PurchasePrice, QuantityReceived) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, inventoryID);
            preparedStatement.setInt(2, medicineID);
            preparedStatement.setDate(3, new java.sql.Date(purchaseDate.getTime()));
            preparedStatement.setDouble(4, purchasePrice);
            preparedStatement.setInt(5, quantityReceived);

            preparedStatement.executeUpdate();
        }
    }

    public static void deleteInventory(int inventoryID, Connection connection) throws SQLException {
        String query = "DELETE FROM Inventory WHERE InventoryID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, inventoryID);

            preparedStatement.executeUpdate();
        }
    }

    public static void updateInventory(int inventoryID, int medicineID, Date purchaseDate, Double purchasePrice, int quantityReceived, Connection connection) throws SQLException {
        String query = "UPDATE Inventory SET MedicineID = COALESCE(?, MedicineID), PurchaseDate = COALESCE(?, PurchaseDate), PurchasePrice = COALESCE(?, PurchasePrice), QuantityReceived = COALESCE(?, QuantityReceived) WHERE InventoryID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, (medicineID == 0 ? 0 : medicineID)); // Assuming 0 is not a valid ID
            preparedStatement.setDate(2, (purchaseDate == null ? null : new java.sql.Date(purchaseDate.getTime())));
            preparedStatement.setDouble(3, (purchasePrice == null ? 0.0 : purchasePrice));
            preparedStatement.setInt(4, (quantityReceived == 0 ? 0 : quantityReceived)); // Assuming 0 is not a valid quantity
            preparedStatement.setInt(5, inventoryID);

            preparedStatement.executeUpdate();
        }
    }


}

