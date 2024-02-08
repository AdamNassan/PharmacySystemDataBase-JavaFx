-- Medicines Table
CREATE TABLE Medicines (
    MedicineID INT PRIMARY KEY,
    Name VARCHAR(255),
    Manufacturer VARCHAR(255),
    ExpiryDate DATE,
    UnitPrice DECIMAL(10, 2),
    QuantityInStock INT
);

-- Suppliers Table
CREATE TABLE Suppliers (
    SupplierID INT PRIMARY KEY,
    Name VARCHAR(255),
    ContactPerson VARCHAR(255),
    ContactNumber VARCHAR(20)
);

-- Customers Table
CREATE TABLE Customers (
    CustomerID INT PRIMARY KEY,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    Address VARCHAR(255),
    ContactNumber VARCHAR(20),
    Email VARCHAR(255)
);

-- Prescriptions Table
CREATE TABLE Prescriptions (
    PrescriptionID INT PRIMARY KEY,
    CustomerID INT REFERENCES Customers(CustomerID),
    DoctorName VARCHAR(255),
    IssueDate DATE
);

-- PrescribedMedicines Table (Many-to-Many Relationship)
CREATE TABLE PrescribedMedicines (
    PrescriptionID INT REFERENCES Prescriptions(PrescriptionID),
    MedicineID INT REFERENCES Medicines(MedicineID),
    Quantity INT,
    PRIMARY KEY (PrescriptionID, MedicineID)
);

-- Sales Table
CREATE TABLE Sales (
    SaleID INT PRIMARY KEY,
    CustomerID INT REFERENCES Customers(CustomerID),
    SaleDate DATE,
    TotalAmount DECIMAL(10, 2)
);

-- SoldMedicines Table (Many-to-Many Relationship)
CREATE TABLE SoldMedicines (
    SaleID INT REFERENCES Sales(SaleID),
    MedicineID INT REFERENCES Medicines(MedicineID),
    Quantity INT,
    UnitPrice DECIMAL(10, 2),
    PRIMARY KEY (SaleID, MedicineID)
);

-- Inventory Table
CREATE TABLE Inventory (
    InventoryID INT PRIMARY KEY,
    MedicineID INT REFERENCES Medicines(MedicineID),
    PurchaseDate DATE,
    PurchasePrice DECIMAL(10, 2),
    QuantityReceived INT
);
CREATE TABLE SuppliedMedicines (
    SupplierID INT REFERENCES Suppliers(SupplierID),
    MedicineID INT REFERENCES Medicines(MedicineID),
    PRIMARY KEY (SupplierID, MedicineID)
);

-- Insert data into Medicines Table
INSERT INTO Medicines (MedicineID, Name, Manufacturer, ExpiryDate, UnitPrice, QuantityInStock)
VALUES
    (1, 'Ibuprofen', 'PharmaPal', '2023-12-31', 10.99, 80),
    (2, 'Amoxicillin', 'Generic', '2023-12-31', 8.49, 120),
    (3, 'Paracetamol', 'Palestine Pharma', '2024-06-30', 6.99, 60);

-- Insert data into Suppliers Table
INSERT INTO Suppliers (SupplierID, Name, ContactPerson, ContactNumber)
VALUES
    (1, 'Al-Quds Pharma', 'Mohammed Supplier', '059-1234567'),
    (2, 'Nablus Suppliers', 'Aisha Contact', '056-9876543');

-- Insert data into Customers Table
INSERT INTO Customers (CustomerID, FirstName, LastName, Address, ContactNumber, Email)
VALUES
    (1, 'Mariam', 'Abu Zahra', '123 Salah al-Din St, Ramallah', '059-1234567', 'mariam@email.com'),
    (2, 'Ahmed', 'Saadeh', '456 Omar Mukhtar St, Nablus', '056-9876543', 'ahmed@email.com');

-- Insert data into Prescriptions Table
INSERT INTO Prescriptions (PrescriptionID, CustomerID, DoctorName, IssueDate)
VALUES
    (1, 1, 'Dr. Khalid Ibrahim', '2023-01-15'),
    (2, 2, 'Dr. Rana Al-Qasem', '2023-02-20');

-- Insert data into PrescribedMedicines Table
INSERT INTO PrescribedMedicines (PrescriptionID, MedicineID, Quantity)
VALUES
    (1, 1, 2),
    (1, 2, 1),
    (2, 3, 3);

-- Insert data into Sales Table
INSERT INTO Sales (SaleID, CustomerID, SaleDate, TotalAmount)
VALUES
    (1, 1, '2023-03-10', 34.47),
    (2, 2, '2023-04-22', 22.97);

-- Insert data into SoldMedicines Table
INSERT INTO SoldMedicines (SaleID, MedicineID, Quantity, UnitPrice)
VALUES
    (1, 1, 2, 10.99),
    (1, 2, 1, 8.49),
    (2, 3, 3, 6.99);

-- Insert data into Inventory Table
INSERT INTO Inventory (InventoryID, MedicineID, PurchaseDate, PurchasePrice, QuantityReceived)
VALUES
    (1, 1, '2022-12-01', 8.50, 100),
    (2, 2, '2023-01-05', 5.75, 150),
    (3, 3, '2023-02-10', 12.00, 50);

-- Insert data into SuppliedMedicines Table
INSERT INTO SuppliedMedicines (SupplierID, MedicineID)
VALUES
    (1, 1),  -- Supplier 1 supplies Medicine 1 (Ibuprofen)
    (1, 2),  -- Supplier 1 supplies Medicine 2 (Amoxicillin)
    (2, 3);  -- Supplier 2 supplies Medicine 3 (Paracetamol)


