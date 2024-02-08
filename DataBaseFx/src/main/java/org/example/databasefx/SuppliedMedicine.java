package org.example.databasefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuppliedMedicine {
    private int supplierID;
    private int medicineID;

    // Constructors, getters, setters...

    public SuppliedMedicine() {
        // Default constructor
    }

    public SuppliedMedicine(int supplierID, int medicineID) {
        this.supplierID = supplierID;
        this.medicineID = medicineID;
    }

    // Getters and setters

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }


    public static ArrayList<SuppliedMedicine> getAllSuppliedMedicines(Connection connection) throws SQLException {
        ArrayList<SuppliedMedicine> suppliedMedicines = new ArrayList<>();
        String query = "SELECT * FROM SuppliedMedicines";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SuppliedMedicine suppliedMedicine = new SuppliedMedicine();
                suppliedMedicine.setSupplierID(resultSet.getInt("SupplierID"));
                suppliedMedicine.setMedicineID(resultSet.getInt("MedicineID"));
                suppliedMedicines.add(suppliedMedicine);
            }
        }
        return suppliedMedicines;
    }


    public static void insertSuppliedMedicine(int supplierID, int medicineID, Connection connection) throws SQLException {
        String query = "INSERT INTO SuppliedMedicines (SupplierID, MedicineID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, supplierID);
            preparedStatement.setInt(2, medicineID);

            preparedStatement.executeUpdate();
        }
    }

    public static void updateSuppliedMedicine(int supplierID, int medicineID, Connection connection) throws SQLException {
        String query = "UPDATE SuppliedMedicines SET MedicineID = ? WHERE SupplierID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, medicineID);
            preparedStatement.setInt(2, supplierID);

            preparedStatement.executeUpdate();
        }
    }

    public static void deleteSuppliedMedicine(int supplierID, int medicineID, Connection connection) throws SQLException {
        String query = "DELETE FROM SuppliedMedicines WHERE SupplierID = ? AND MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, supplierID);
            preparedStatement.setInt(2, medicineID);

            preparedStatement.executeUpdate();
        }
    }

}
