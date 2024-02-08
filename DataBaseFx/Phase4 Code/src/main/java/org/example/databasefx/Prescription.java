package org.example.databasefx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private int prescriptionID;
    private int customerID;
    private String doctorName;
    private Date issueDate;

    // Constructors
    public Prescription() {
        // Default constructor
    }

    public Prescription(int prescriptionID, int customerID, String doctorName, Date issueDate) {
        this.prescriptionID = prescriptionID;
        this.customerID = customerID;
        this.doctorName = doctorName;
        this.issueDate = issueDate;
    }

    // Getters and setters
    public int getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(int prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    // Other methods...

    public static ArrayList<Prescription> getAllPrescriptions(Connection connection) throws SQLException {
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT * FROM Prescriptions";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Prescription prescription = new Prescription();
                prescription.setPrescriptionID(resultSet.getInt("PrescriptionID"));
                prescription.setCustomerID(resultSet.getInt("CustomerID"));
                prescription.setDoctorName(resultSet.getString("DoctorName"));
                prescription.setIssueDate(resultSet.getDate("IssueDate"));
                prescriptions.add(prescription);
            }
        }
        return prescriptions;
    }

    public static void insertPrescription(int prescriptionID, int customerID, String doctorName, Date issueDate, Connection connection) throws SQLException {
        String query = "INSERT INTO Prescriptions (PrescriptionID, CustomerID, DoctorName, IssueDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, prescriptionID);
            preparedStatement.setInt(2, customerID);
            if(!doctorName.isEmpty())
                preparedStatement.setString(3, doctorName);
            preparedStatement.setDate(4, new java.sql.Date(issueDate.getTime()));

            preparedStatement.executeUpdate();
        }
    }

    public static void deletePrescription(int prescriptionID, Connection connection) throws SQLException {
        String query = "DELETE FROM Prescriptions WHERE PrescriptionID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, prescriptionID);

            preparedStatement.executeUpdate();
        }
    }

    public static void updatePrescription(int prescriptionID, Integer customerID, String doctorName, Date issueDate, Connection connection) throws SQLException {
        String query = "UPDATE Prescriptions SET CustomerID = COALESCE(?, CustomerID), DoctorName = COALESCE(?, DoctorName), IssueDate = COALESCE(?, IssueDate) WHERE PrescriptionID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);
            preparedStatement.setString(2, (doctorName.isEmpty() ? null : doctorName));
            preparedStatement.setDate(3, (issueDate == null ? null : new java.sql.Date(issueDate.getTime())));
            preparedStatement.setInt(4, prescriptionID);

            preparedStatement.executeUpdate();
        }
    }

    public static ArrayList<Prescription> getPrescriptionsByCustomer(int customerID, Connection connection) throws SQLException {
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT * FROM Prescriptions WHERE CustomerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Prescription prescription = new Prescription();
                prescription.setPrescriptionID(resultSet.getInt("PrescriptionID"));
                prescription.setCustomerID(resultSet.getInt("CustomerID"));
                prescription.setDoctorName(resultSet.getString("DoctorName"));
                prescription.setIssueDate(resultSet.getDate("IssueDate"));
                prescriptions.add(prescription);
            }
        }
        return prescriptions;
    }
}
