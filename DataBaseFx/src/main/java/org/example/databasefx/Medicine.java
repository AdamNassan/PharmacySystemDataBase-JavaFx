package org.example.databasefx;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Medicine {
    private int medicineID;
    private String name;
    private String manufacturer;
    private Date expiryDate;
    private double unitPrice;
    private int quantityInStock;

    // Constructors
    public Medicine() {
        // Default constructor
    }

    public Medicine(int medicineID, String name, String manufacturer, Date expiryDate, double unitPrice, int quantityInStock) {
        this.medicineID = medicineID;
        this.name = name;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
    }

    // Getters and setters
    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    // Get all medicines from the database
    public static ArrayList<Medicine> getAllMedicines(Connection connection) throws SQLException {
        ArrayList<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM Medicines";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Medicine medicine = new Medicine();
                // Populate the Medicine object with data from the result set
                medicine.setMedicineID(resultSet.getInt("MedicineID"));
                medicine.setName(resultSet.getString("Name"));
                medicine.setManufacturer(resultSet.getString("Manufacturer"));
                medicine.setExpiryDate(resultSet.getDate("ExpiryDate"));
                medicine.setUnitPrice(resultSet.getDouble("UnitPrice"));
                medicine.setQuantityInStock(resultSet.getInt("QuantityInStock"));
                medicines.add(medicine);
            }
        }
        return medicines;
    }
    // Insert a new medicine into the database
    // Insert a new medicine into the database with user-provided values
    // Insert a new medicine into the database with user-provided values
    public static void insertMedicine(int medicineID, String name, String manufacturer, Date expiryDate, double unitPrice, int quantityInStock, Connection connection) throws SQLException {
        String query = "INSERT INTO Medicines (MedicineID, Name, Manufacturer, ExpiryDate, UnitPrice, QuantityInStock) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, medicineID);
            if(!name.isEmpty())
                preparedStatement.setString(2, name);
            if(!manufacturer.isEmpty())
                preparedStatement.setString(3, manufacturer);
            preparedStatement.setDate(4, new java.sql.Date(expiryDate.getTime()));
            preparedStatement.setDouble(5, unitPrice);
            preparedStatement.setInt(6, quantityInStock);

            preparedStatement.executeUpdate();
        }
    }
    // Delete a medicine from the database by MedicineID
    public static void deleteMedicine(int medicineID, Connection connection) throws SQLException {
        String query = "DELETE FROM Medicines WHERE MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, medicineID);

            preparedStatement.executeUpdate();
        }
    }
    // Update a medicine in the database with user-provided values
    public static void updateMedicine(int medicineID, String name, String manufacturer, Date expiryDate, Double unitPrice, Integer quantityInStock, Connection connection) throws SQLException {
        String query = "UPDATE Medicines SET Name = COALESCE(?, Name), Manufacturer = COALESCE(?, Manufacturer), ExpiryDate = COALESCE(?, ExpiryDate), UnitPrice = COALESCE(?, UnitPrice), QuantityInStock = COALESCE(?, QuantityInStock) WHERE MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, (name.isEmpty() ? null : name));
            preparedStatement.setString(2, (manufacturer.isEmpty() ? null : manufacturer));
            preparedStatement.setDate(3, (expiryDate == null ? null : new java.sql.Date(expiryDate.getTime())));
            preparedStatement.setObject(4, (unitPrice == null ? null : unitPrice));
            preparedStatement.setObject(5, (quantityInStock == null ? null : quantityInStock));
            preparedStatement.setInt(6, medicineID);

            preparedStatement.executeUpdate();
        }
    }

    public static Medicine getMedicineByID(int medicineID, Connection connection) throws SQLException {
        Medicine medicine = null;
        String query = "SELECT * FROM Medicines WHERE MedicineID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, medicineID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                medicine = new Medicine();
                // Populate the Medicine object with data from the result set
                medicine.setMedicineID(resultSet.getInt("MedicineID"));
                medicine.setName(resultSet.getString("Name"));
                medicine.setManufacturer(resultSet.getString("Manufacturer"));
                medicine.setExpiryDate(resultSet.getDate("ExpiryDate"));
                medicine.setUnitPrice(resultSet.getDouble("UnitPrice"));
                medicine.setQuantityInStock(resultSet.getInt("QuantityInStock"));
            }
        }
        return medicine;
    }
    public static ArrayList<Medicine> getMedicineByName(String name, Connection connection) throws SQLException {
        ArrayList<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM Medicines WHERE Name LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Medicine medicine = new Medicine();
                // Populate the Medicine object with data from the result set
                medicine.setMedicineID(resultSet.getInt("MedicineID"));
                medicine.setName(resultSet.getString("Name"));
                medicine.setManufacturer(resultSet.getString("Manufacturer"));
                medicine.setExpiryDate(resultSet.getDate("ExpiryDate"));
                medicine.setUnitPrice(resultSet.getDouble("UnitPrice"));
                medicine.setQuantityInStock(resultSet.getInt("QuantityInStock"));
                medicines.add(medicine);
            }
        }
        return medicines;
    }
    public static ArrayList<Medicine> getMedicineByManufacturer(String manufacturer, Connection connection) throws SQLException {
        ArrayList<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM Medicines WHERE Manufacturer LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + manufacturer + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Medicine medicine = new Medicine();
                // Populate the Medicine object with data from the result set
                medicine.setMedicineID(resultSet.getInt("MedicineID"));
                medicine.setName(resultSet.getString("Name"));
                medicine.setManufacturer(resultSet.getString("Manufacturer"));
                medicine.setExpiryDate(resultSet.getDate("ExpiryDate"));
                medicine.setUnitPrice(resultSet.getDouble("UnitPrice"));
                medicine.setQuantityInStock(resultSet.getInt("QuantityInStock"));
                medicines.add(medicine);
            }
        }
        return medicines;
    }
    public void decrementQuantity(int quantityToDecrement, Connection connection) throws SQLException {
        if (quantityToDecrement > 0 && quantityInStock >= quantityToDecrement) {
            quantityInStock -= quantityToDecrement;

            // Update the database with the new quantity
            String query = "UPDATE Medicines SET QuantityInStock = ? WHERE MedicineID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, quantityInStock);
                preparedStatement.setInt(2, medicineID);

                preparedStatement.executeUpdate();
            }
        } else {
            System.out.println("Invalid quantity to decrement or insufficient stock.");
        }
    }


}

