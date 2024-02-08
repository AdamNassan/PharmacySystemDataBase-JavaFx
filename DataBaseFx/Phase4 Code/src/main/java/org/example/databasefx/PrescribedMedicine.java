package org.example.databasefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrescribedMedicine {
    private int prescriptionID;
    private int medicineID;
    private int quantity;

    // Constructors, getters, setters...

    public PrescribedMedicine() {
        // Default constructor
    }

    public PrescribedMedicine(int prescriptionID, int medicineID, int quantity) {
        this.prescriptionID = prescriptionID;
        this.medicineID = medicineID;
        this.quantity = quantity;
    }


    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
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


    public static ArrayList<PrescribedMedicine> getAllPrescribedMedicines(Connection connection) throws SQLException {
        ArrayList<PrescribedMedicine> prescribedMedicines = new ArrayList<>();
        String query = "SELECT * FROM PrescribedMedicines";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PrescribedMedicine prescribedMedicine = new PrescribedMedicine();
                prescribedMedicine.setPrescriptionID(resultSet.getInt("PrescriptionID"));
                prescribedMedicine.setMedicineID(resultSet.getInt("MedicineID"));
                prescribedMedicine.setQuantity(resultSet.getInt("Quantity"));
                prescribedMedicines.add(prescribedMedicine);
            }
        }
        return prescribedMedicines;
    }


    public static void insertPrescribedMedicine(int prescriptionID, int medicineID, int quantity, Connection connection) throws SQLException {
        String query = "INSERT INTO PrescribedMedicines (PrescriptionID, MedicineID, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, prescriptionID);
            preparedStatement.setInt(2, medicineID);
            preparedStatement.setInt(3, quantity);

            preparedStatement.executeUpdate();
        }
    }

    public static void updatePrescribedMedicine(int prescriptionID, int medicineID, int quantity, Connection connection) throws SQLException {
        String query = "UPDATE PrescribedMedicines SET Quantity = ? WHERE PrescriptionID = ? AND MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, prescriptionID);
            preparedStatement.setInt(3, medicineID);

            preparedStatement.executeUpdate();
        }
    }

    public static void deletePrescribedMedicine(int prescriptionID, int medicineID, Connection connection) throws SQLException {
        String query = "DELETE FROM PrescribedMedicines WHERE PrescriptionID = ? AND MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, prescriptionID);
            preparedStatement.setInt(2, medicineID);

            preparedStatement.executeUpdate();
        }
    }

}

