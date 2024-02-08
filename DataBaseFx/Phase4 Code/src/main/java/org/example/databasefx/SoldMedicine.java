package org.example.databasefx;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SoldMedicine {
    private int saleID;
    private int medicineID;
    private int quantity;
    private Double unitPrice;

    // Constructors

    public SoldMedicine() {
        // Default constructor
    }

    public SoldMedicine(int saleID, int medicineID, int quantity, Double unitPrice) {
        this.saleID = saleID;
        this.medicineID = medicineID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and setters

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }


    public static ArrayList<SoldMedicine> getAllSoldMedicines(Connection connection) throws SQLException {
        ArrayList<SoldMedicine> soldMedicines = new ArrayList<>();
        String query = "SELECT * FROM SoldMedicines";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SoldMedicine soldMedicine = new SoldMedicine();
                soldMedicine.setSaleID(resultSet.getInt("SaleID"));
                soldMedicine.setMedicineID(resultSet.getInt("MedicineID"));
                soldMedicine.setQuantity(resultSet.getInt("Quantity"));
                soldMedicine.setUnitPrice(resultSet.getDouble("UnitPrice"));
                soldMedicines.add(soldMedicine);
            }
        }
        return soldMedicines;
    }


    public static void insertSoldMedicine(int saleID, int medicineID, int quantity, Double unitPrice, Connection connection) throws SQLException {
        String query = "INSERT INTO SoldMedicines (SaleID, MedicineID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, saleID);
            preparedStatement.setInt(2, medicineID);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setDouble(4, unitPrice);

            preparedStatement.executeUpdate();
        }
    }

    public static void updateSoldMedicine(int saleID, int medicineID, int quantity, Double unitPrice, Connection connection) throws SQLException {
        String query = "UPDATE SoldMedicines SET Quantity = ?, UnitPrice = ? WHERE SaleID = ? AND MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setDouble(2, unitPrice);
            preparedStatement.setInt(3, saleID);
            preparedStatement.setInt(4, medicineID);

            preparedStatement.executeUpdate();
        }
    }

    public static void deleteSoldMedicine(int saleID, int medicineID, Connection connection) throws SQLException {
        String query = "DELETE FROM SoldMedicines WHERE SaleID = ? AND MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, saleID);
            preparedStatement.setInt(2, medicineID);

            preparedStatement.executeUpdate();
        }
    }

    public static ArrayList<SoldMedicine> getSoldMedicinesBySaleID(int saleID, Connection connection) throws SQLException {
        ArrayList<SoldMedicine> soldMedicines = new ArrayList<>();
        String query = "SELECT * FROM SoldMedicines WHERE SaleID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, saleID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SoldMedicine soldMedicine = new SoldMedicine();
                soldMedicine.setSaleID(resultSet.getInt("SaleID"));
                soldMedicine.setMedicineID(resultSet.getInt("MedicineID"));
                soldMedicine.setQuantity(resultSet.getInt("Quantity"));
                soldMedicine.setUnitPrice(resultSet.getDouble("UnitPrice"));
                soldMedicines.add(soldMedicine);
            }
        }
        return soldMedicines;
    }



}
