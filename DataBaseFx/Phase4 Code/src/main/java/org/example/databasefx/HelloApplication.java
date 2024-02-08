package org.example.databasefx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class HelloApplication extends Application {

    private Connection connection;

    //Stage just to move to the main interface
    @Override
    public void start(Stage primaryStage) {
        try {
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "9177AdamShareef7719");
            start2();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    //Main interface
    public void start2() {
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "9177AdamShareef7719");
            ArrayList<Customer> allCustomers = new ArrayList<>(Customer.getAllCustomers(connection));
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Pharmacy Management System");

            Image image = new Image("D:\\JavaFiles\\DataBaseFx\\src\\main\\java\\org\\example\\databasefx\\Pharmacy1.jpg");
            //Setting the image view
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(1700);
            imageView.setFitHeight(850);

            // Create RadioButtons for Admin and User login
            RadioButton adminRadioButton = new RadioButton("Admin");
            RadioButton userRadioButton = new RadioButton("Customer");
            ToggleGroup roleToggleGroup = new ToggleGroup();
            Label userNotFound = new Label("This ID is not owned by any customer.");
            Label idHandling =  new Label("Wrong Format Please enter valid ID");
            adminRadioButton.setToggleGroup(roleToggleGroup);
            userRadioButton.setToggleGroup(roleToggleGroup);

            // Create TextField for entering user ID
            TextField userIdField = new TextField();
            userIdField.setPromptText("Enter Customer ID");
            userIdField.setMaxWidth(350);
            userIdField.setPrefHeight(40);
            // Create Next and Back buttons
            Button nextButton = new Button("Next");
            nextButton.setDisable(true); // Initially disabled

            Button backButton = new Button("Back");
            backButton.setDisable(true); // Initially disabled

            double buttonWidth = 200;
            double buttonHeight = 40;
            nextButton.setPrefSize(buttonWidth, buttonHeight);
            backButton.setPrefSize(buttonWidth, buttonHeight);

            // Set text sizes for labels and buttons
            double textSize = 35;
            adminRadioButton.setFont(Font.font("Century", FontWeight.BOLD, 40));
            userRadioButton.setFont(Font.font("Century", FontWeight.BOLD, 40));
            userNotFound.setFont(new Font(textSize));
            idHandling.setFont(new Font(textSize));
            userIdField.setFont(new Font(textSize));
            nextButton.setFont(Font.font("Arial", FontWeight.BOLD, textSize));
            backButton.setFont(Font.font("Arial", FontWeight.BOLD, textSize));

            // Create an HBox for the buttons with proper alignment
            HBox buttonBox = new HBox(80);
            //buttonBox.setAlignment(Pos.CENTER); // Align to the left
            buttonBox.getChildren().addAll(backButton,nextButton);

            nextButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;"); // Black background with white text
            backButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;"); // Black background with white text



            // Create a vertical layout for the main page
            VBox layout = new VBox(150);
            layout.setPadding(new Insets(100, 100, 100, 100));
            //layout.setAlignment(Pos.CENTER); // Align to the center
            layout.getChildren().addAll(adminRadioButton, userRadioButton,buttonBox);



            //admin configuration
            adminRadioButton.setOnAction(e -> {
                nextButton.setDisable(true);
                backButton.setDisable(true);
                if (adminRadioButton.isSelected()) {
                    nextButton.setDisable(false);
                }
            });

            //user configuration
            userRadioButton.setOnAction(e -> {
                nextButton.setDisable(true);
                backButton.setDisable(false);
                if (userRadioButton.isSelected()) {
                    layout.getChildren().removeAll(adminRadioButton, buttonBox);
                    layout.getChildren().addAll(userIdField, buttonBox);

                    userIdField.textProperty().addListener((observable, oldValue, newValue) -> {
                        nextButton.setDisable(newValue.isEmpty());
                    });
                }
            });

            backButton.setOnAction(e -> {
                adminRadioButton.setSelected(false);
                userRadioButton.setSelected(false);
                layout.getChildren().removeAll(adminRadioButton, userRadioButton,userIdField,buttonBox,userNotFound,idHandling);
                layout.getChildren().addAll(adminRadioButton, userRadioButton,buttonBox);
                userIdField.setText("");
                backButton.setDisable(true);
            });

            //Next config
            nextButton.setOnAction(e -> {
                if (adminRadioButton.isSelected()) {
                    openAdminDashboardStage();
                    primaryStage.close();
                } else if (userRadioButton.isSelected() && !userIdField.getText().isEmpty()) {

                    try {
                        int userId = Integer.parseInt(userIdField.getText());

                        for (int i = 0; i < allCustomers.size(); i++) {
                            Customer customer = allCustomers.get(i);
                            if (userIdField.getText().equals(String.valueOf(customer.getCustomerID()))) {
                                openUserDashboardStage(userIdField.getText());
                                primaryStage.close();
                            }
                        }
                        layout.getChildren().remove(idHandling);
                        layout.getChildren().remove(userNotFound);
                        layout.getChildren().add(userNotFound);
                    }
                    catch (NumberFormatException e2){
                        layout.getChildren().remove(userNotFound);
                        layout.getChildren().remove(idHandling);
                        layout.getChildren().add(idHandling);
                    }

                }
            });



            //add image and vbox into stack pane
            StackPane panee = new StackPane(imageView,layout);
            //add stack pane into scene
            Scene scene = new Scene(panee, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);

            primaryStage.show();

            }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //Launcher
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch();
    }

    //Admin Interface
    private void openAdminDashboardStage() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "9177AdamShareef7719");


            Stage adminStage = new Stage();

            Image image2 = new Image("D:\\JavaFiles\\DataBaseFx\\src\\main\\java\\org\\example\\databasefx\\adminPage.jpg");
            //Setting the image view
            ImageView imageView2 = new ImageView(image2);
            imageView2.setFitWidth(1600);
            imageView2.setFitHeight(800);


            // Create buttons
            Button btnManageMedicines = new Button("Manage Medicines");
            Button btnManageSuppliers = new Button("Manage Suppliers");
            Button btnManageCustomers = new Button("Manage Customers");
            Button btnManagePrescriptions = new Button("Manage Prescriptions");
            Button btnViewSalesReports = new Button("View Sales Reports");
            Button btnManageInventory = new Button("Manage Inventory");

            Button addMedicine = new Button("Add New Medicine");
            Button UpdateMedicine = new Button("Update Medicine");
            Button DeleteMedicine = new Button("Delete Medicine");
            Button ShowMedicine = new Button("Show All Medicines");
            Button ShowExpMed = new Button("Show Expired Medicines");
            Button Apply = new Button("Apply");
            Button MainBack = new Button("Back");
            Button SubBack = new Button("Back");
            Button SubBack2 = new Button("Back");


            TextField mid = new TextField();
            mid.setPromptText("Enter Medicine ID");
            mid.setMaxWidth(1000);
            mid.setPrefHeight(20);
            mid.setFont(new Font(30));
            TextField mname = new TextField();
            mname.setPromptText("Enter Medicine Name");
            mname.setMaxWidth(1000);
            mname.setPrefHeight(20);
            mname.setFont(new Font(30));
            TextField mmanufacturer = new TextField();
            mmanufacturer.setPromptText("Enter Medicine Manufacturer");
            mmanufacturer.setMaxWidth(1000);
            mmanufacturer.setPrefHeight(20);
            mmanufacturer.setFont(new Font(30));
            TextField mexpdate = new TextField();
            mexpdate.setPromptText("Enter Medicine Exp Date");
            mexpdate.setMaxWidth(1000);
            mexpdate.setPrefHeight(20);
            mexpdate.setFont(new Font(30));
            TextField munitprice = new TextField();
            munitprice.setPromptText("Enter Medicine Unit Price");
            munitprice.setMaxWidth(1000);
            munitprice.setPrefHeight(20);
            munitprice.setFont(new Font(30));
            TextField mquantity = new TextField();
            mquantity.setPromptText("Enter Medicine Quantity");
            mquantity.setMaxWidth(1000);
            mquantity.setPrefWidth(600);
            mquantity.setPrefHeight(20);
            mquantity.setFont(new Font(30));



            // Set preferred size for buttons
            double buttonWidth = 550;
            double buttonHeight = 200;
            btnManageMedicines.setPrefSize(buttonWidth, buttonHeight);
            btnManageSuppliers.setPrefSize(buttonWidth, buttonHeight);
            btnManageCustomers.setPrefSize(buttonWidth, buttonHeight);
            btnManagePrescriptions.setPrefSize(buttonWidth, buttonHeight);
            btnViewSalesReports.setPrefSize(buttonWidth, buttonHeight);
            btnManageInventory.setPrefSize(buttonWidth, buttonHeight);

            addMedicine.setPrefSize(buttonWidth, buttonHeight);
            UpdateMedicine.setPrefSize(buttonWidth, buttonHeight);
            DeleteMedicine.setPrefSize(buttonWidth, buttonHeight);
            ShowMedicine.setPrefSize(buttonWidth, buttonHeight);
            ShowExpMed.setPrefSize(buttonWidth, buttonHeight);
            Apply.setPrefSize(150, 40);
            MainBack.setPrefSize(150, 40);
            SubBack.setPrefSize(150, 40);
            SubBack2.setPrefSize(150, 40);




            //Buttons settings
            btnManageMedicines.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnManageSuppliers.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnManageCustomers.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnManagePrescriptions.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnViewSalesReports.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnManageInventory.setFont(Font.font("Arial", FontWeight.BOLD, 40));

            addMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            UpdateMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            DeleteMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            ShowMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            ShowExpMed.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            Apply.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            MainBack.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            SubBack.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            SubBack2.setFont(Font.font("Arial", FontWeight.BOLD, 20));




            btnManageMedicines.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            btnManageSuppliers.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            btnManageCustomers.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            btnManagePrescriptions.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            btnViewSalesReports.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            btnManageInventory.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            addMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            UpdateMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            DeleteMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            ShowMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            ShowExpMed.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            Apply.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            MainBack.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            SubBack.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text
            SubBack2.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text


            // Create a VBox (Vertical Box) for buttons
            VBox buttonBox = new VBox(40); // 10 is the spacing between buttons
            buttonBox.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the Center right

            // Add buttons to the VBox
            buttonBox.getChildren().addAll(
                    btnManageMedicines,
                    btnManageSuppliers,
                    btnManageCustomers,
                    btnManagePrescriptions,
                    btnViewSalesReports,
                    btnManageInventory
            );

            // Create a label
            Label welcomeLabel = new Label("   Welcome");
            welcomeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 100));
            // welcomeLabel.setFont(new Font(100)); // Set font size
            welcomeLabel.setTextFill(Color.WHITE);

            Label welcomeLabel2 = new Label("    Admin");
            welcomeLabel2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 100));
            //welcomeLabel2.setFont(new Font(100)); // Set font size
            welcomeLabel2.setTextFill(Color.WHITE);

            VBox WelcomeAdminVbox = new VBox(10);
            WelcomeAdminVbox.setAlignment(Pos.BASELINE_LEFT);
            WelcomeAdminVbox.getChildren().addAll(welcomeLabel, welcomeLabel2);

            Button signoutButton = new Button("Sign Out");
            signoutButton.setPrefSize(150, 30);
            signoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            signoutButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            VBox SignOutVbox = new VBox(10);
            SignOutVbox.setAlignment(Pos.BOTTOM_LEFT);
            SignOutVbox.getChildren().add(signoutButton);

            HBox HapplybackH = new HBox(240);
            HapplybackH.setAlignment(Pos.BOTTOM_LEFT);
            HapplybackH.getChildren().addAll(SubBack,Apply);

            //Start of the buttons

            btnManageMedicines.setOnAction(e -> {
                buttonBox.getChildren().removeAll(
                        btnManageMedicines,
                        btnManageSuppliers,
                        btnManageCustomers,
                        btnManagePrescriptions,
                        btnViewSalesReports,
                        btnManageInventory);
                buttonBox.getChildren().addAll(
                        addMedicine,
                        UpdateMedicine,
                        DeleteMedicine,
                        ShowMedicine,
                        ShowExpMed,
                        MainBack );
                MainBack.setOnAction(e1->{
                    buttonBox.getChildren().removeAll(
                            addMedicine,
                            UpdateMedicine,
                            DeleteMedicine,
                            ShowMedicine,
                            ShowExpMed,
                            MainBack );
                    buttonBox.getChildren().addAll(
                            btnManageMedicines,
                            btnManageSuppliers,
                            btnManageCustomers,
                            btnManagePrescriptions,
                            btnViewSalesReports,
                            btnManageInventory);

                });

                addMedicine.setOnAction(e2 ->{
                    mname.setDisable(false);
                    mmanufacturer.setDisable(false);
                    mexpdate.setDisable(false);
                    munitprice.setDisable(false);
                    mquantity.setDisable(false);
                    Apply.setDisable(false);
                    mid.setText("");
                    mname.setText("");
                    mmanufacturer.setText("");
                    mexpdate.setText("");
                    munitprice.setText("");
                    mquantity.setText("");

                    buttonBox.getChildren().removeAll(
                            addMedicine,
                            UpdateMedicine,
                            DeleteMedicine,
                            ShowMedicine,
                            ShowExpMed,
                            MainBack);
                    HapplybackH.getChildren().removeAll(SubBack,Apply);
                    HapplybackH.getChildren().addAll(SubBack,Apply);
                    buttonBox.getChildren().addAll(
                            mid,
                            mname,
                            mmanufacturer,
                            mexpdate,
                            munitprice,
                            mquantity,
                            HapplybackH);
                    Apply.setOnAction(e3->{
                        try {
                            //buttonBox.getChildren().remove(insertHandle);
                            Medicine.insertMedicine(Integer.parseInt(mid.getText()), mname.getText(), mmanufacturer.getText(), parseDate(mexpdate.getText()), Double.parseDouble(munitprice.getText()), Integer.parseInt(mquantity.getText()), connection);
                            mid.setText("");
                            mname.setText("");
                            mmanufacturer.setText("");
                            mexpdate.setText("");
                            munitprice.setText("");
                            mquantity.setText("");
                        }
                        catch (Exception e100){
                          /* // insertHandle.setText(e100.toString());
                            insertHandle.setText("Us");
                            buttonBox.getChildren().add(insertHandle);*/
                            System.out.println(e100);
                        }
                    });
                    SubBack.setOnAction(e4->{
                        buttonBox.getChildren().removeAll(
                                mid,
                                mname,
                                mmanufacturer,
                                mexpdate,
                                munitprice,
                                mquantity,
                                HapplybackH);
                        buttonBox.getChildren().addAll(
                                addMedicine,
                                UpdateMedicine,
                                DeleteMedicine,
                                ShowMedicine,
                                ShowExpMed,
                                MainBack );
                    });

                });

                TextField upadatemid = new TextField();
                upadatemid.setPromptText("Enter Medicine ID");
                upadatemid.setMaxWidth(1000);
                upadatemid.setPrefHeight(20);
                upadatemid.setFont(new Font(30));
                UpdateMedicine.setOnAction(e7->{
                    mname.setDisable(false);
                    mmanufacturer.setDisable(false);
                    mexpdate.setDisable(false);
                    munitprice.setDisable(false);
                    mquantity.setDisable(false);
                    Apply.setDisable(false);
                    upadatemid.setText("");
                    mname.setText("");
                    mmanufacturer.setText("");
                    mexpdate.setText("");
                    munitprice.setText("");
                    mquantity.setText("");

                    try {
                        buttonBox.getChildren().removeAll(
                                addMedicine,
                                UpdateMedicine,
                                DeleteMedicine,
                                ShowMedicine,
                                ShowExpMed,
                                MainBack);
                        HapplybackH.getChildren().removeAll(SubBack,Apply);
                        HapplybackH.getChildren().addAll(SubBack,Apply);
                        buttonBox.getChildren().addAll(
                                upadatemid,
                                mname,
                                mmanufacturer,
                                mexpdate,
                                munitprice,
                                mquantity,
                                HapplybackH);
                        mname.setDisable(true);
                        mmanufacturer.setDisable(true);
                        mexpdate.setDisable(true);
                        munitprice.setDisable(true);
                        mquantity.setDisable(true);
                        Apply.setDisable(true);
                        SubBack.setOnAction(e4->{
                            buttonBox.getChildren().removeAll(
                                    upadatemid,
                                    mname,
                                    mmanufacturer,
                                    mexpdate,
                                    munitprice,
                                    mquantity,
                                    HapplybackH);
                            buttonBox.getChildren().addAll(
                                    addMedicine,
                                    UpdateMedicine,
                                    DeleteMedicine,
                                    ShowMedicine,
                                    ShowExpMed,
                                    MainBack );

                        });
                        ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));

                        upadatemid.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int intnewValue = Integer.parseInt(newValue);
                                boolean isAllowed = allMedicines.stream().anyMatch(medicineInfo -> medicineInfo.getMedicineID() == intnewValue);

                                if (isAllowed) {
                                    mname.setDisable(false);
                                    mmanufacturer.setDisable(false);
                                    mexpdate.setDisable(false);
                                    munitprice.setDisable(false);
                                    mquantity.setDisable(false);
                                    Apply.setDisable(false);
                                } else {
                                    mname.setDisable(true);
                                    mmanufacturer.setDisable(true);
                                    mexpdate.setDisable(true);
                                    munitprice.setDisable(true);
                                    mquantity.setDisable(true);
                                    Apply.setDisable(true);
                                }
                                Apply.setOnAction(e3->{
                                    try {
                                        Medicine.updateMedicine(Integer.parseInt(upadatemid.getText()), mname.getText(), mmanufacturer.getText(), parseDate(mexpdate.getText()), Double.parseDouble(munitprice.getText()), Integer.parseInt(mquantity.getText()), connection);
                                        upadatemid.setText("");
                                        mname.setText("");
                                        mmanufacturer.setText("");
                                        mexpdate.setText("");
                                        munitprice.setText("");
                                        mquantity.setText("");
                                    }
                                    catch (Exception e100){
                                        System.out.println(e100);
                                    }
                                    });

                            }
                            catch (NumberFormatException e102) {
                                // Handle the case where newValue is not a valid integer
                                mname.setDisable(true);
                                mmanufacturer.setDisable(true);
                                mexpdate.setDisable(true);
                                munitprice.setDisable(true);
                                mquantity.setDisable(true);
                                Apply.setDisable(true);
                            }
                        });

                    }
                    catch (Exception e100){
                        System.out.println(e100);
                    }



                });

                TextField deletemid = new TextField();
                deletemid.setPromptText("Enter Medicine ID");
                deletemid.setMaxWidth(1000);
                deletemid.setPrefHeight(20);
                deletemid.setFont(new Font(30));

                DeleteMedicine.setOnAction(e11->{
                    mname.setDisable(false);
                    mmanufacturer.setDisable(false);
                    mexpdate.setDisable(false);
                    munitprice.setDisable(false);
                    mquantity.setDisable(false);
                    Apply.setDisable(false);
                    deletemid.setText("");
                    mname.setText("");
                    mmanufacturer.setText("");
                    mexpdate.setText("");
                    munitprice.setText("");
                    mquantity.setText("");

                    try{
                    buttonBox.getChildren().removeAll(
                            addMedicine,
                            UpdateMedicine,
                            DeleteMedicine,
                            ShowMedicine,
                            ShowExpMed,
                            MainBack);
                    HapplybackH.getChildren().removeAll(SubBack,Apply);
                    HapplybackH.getChildren().addAll(SubBack,Apply);
                    buttonBox.getChildren().addAll(deletemid,HapplybackH);
                        Apply.setDisable(true);

                        SubBack.setOnAction(e4->{
                            buttonBox.getChildren().removeAll(
                                    deletemid,
                                    HapplybackH);
                            buttonBox.getChildren().addAll(
                                    addMedicine,
                                    UpdateMedicine,
                                    DeleteMedicine,
                                    ShowMedicine,
                                    ShowExpMed,
                                    MainBack );

                        });
                        ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));

                        deletemid.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int intnewValue = Integer.parseInt(newValue);
                                boolean isAllowed = allMedicines.stream().anyMatch(medicineInfo -> medicineInfo.getMedicineID() == intnewValue);

                                if (isAllowed) {
                                    Apply.setDisable(false);
                                } else {
                                    Apply.setDisable(true);
                                }
                                Apply.setOnAction(e3->{
                                    try {
                                        Medicine.deleteMedicine(Integer.parseInt(deletemid.getText()),connection);
                                        deletemid.setText("");
                                    }
                                    catch (Exception e100){
                                        System.out.println(e100);
                                    }
                                });

                            }
                            catch (NumberFormatException e102) {
                                // Handle the case where newValue is not a valid integer
                                Apply.setDisable(true);
                            }
                        });

                    }
                    catch (Exception e100){
                        System.out.println(e100);
                    }

                });

                ShowMedicine.setOnAction(e8->{
                    buttonBox.getChildren().removeAll(
                            addMedicine,
                            UpdateMedicine,
                            DeleteMedicine,
                            ShowMedicine,
                            ShowExpMed,
                            MainBack );
                    try {
                        ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));
                        ObservableList<String> medicineInfoList = FXCollections.observableArrayList();
                        for (int i = 0; i < allMedicines.size(); i++) {
                            Medicine medicine = allMedicines.get(i);
                            // Append medicine information to the list
                            medicineInfoList.add(
                                        "Medicine ID: " + medicine.getMedicineID() + "\n" +
                                        "Name: " + medicine.getName() + "\n" +
                                        "Manufacturer: " + medicine.getManufacturer() + "\n" +
                                        "Expiry Date: " + medicine.getExpiryDate() + "\n" +
                                        "Unit Price: " + medicine.getUnitPrice() + "\n" +
                                        "Quantity in Stock: " + medicine.getQuantityInStock() + "\n" +
                                        "---------------"
                                );
                            }
                            // Create a ListView to display medicine information
                            ListView<String> listView = new ListView<>(medicineInfoList);
                        listView.setPrefSize(500,600);
                        buttonBox.getChildren().add(listView);
                        buttonBox.getChildren().add(SubBack);

                        SubBack.setOnAction(e9->{
                            buttonBox.getChildren().removeAll(listView,SubBack);
                            buttonBox.getChildren().addAll(
                                    addMedicine,
                                    UpdateMedicine,
                                    DeleteMedicine,
                                    ShowMedicine,
                                    ShowExpMed,
                                    MainBack );

                        });

                        }
                    catch (Exception e100){
                        System.out.println(e100);
                    }


                });

                ShowExpMed.setOnAction(e8->{
                    buttonBox.getChildren().removeAll(
                            addMedicine,
                            UpdateMedicine,
                            DeleteMedicine,
                            ShowMedicine,
                            ShowExpMed,
                            MainBack );
                    try {
                        // Get the current date
                        Date currentDate = new Date();
                        ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));
                        ObservableList<String> medicineInfoList = FXCollections.observableArrayList();
                        for (int i = 0; i < allMedicines.size(); i++) {
                            Medicine medicine = allMedicines.get(i);
                            // Check if the medicine's expiry date is before the current date
                            if (medicine.getExpiryDate().before(currentDate)) {
                                // Append medicine information to the list
                                medicineInfoList.add(
                                        "Medicine ID: " + medicine.getMedicineID() + "\n" +
                                                "Name: " + medicine.getName() + "\n" +
                                                "Manufacturer: " + medicine.getManufacturer() + "\n" +
                                                "Expiry Date: " + medicine.getExpiryDate() + "\n" +
                                                "Unit Price: " + medicine.getUnitPrice() + "\n" +
                                                "Quantity in Stock: " + medicine.getQuantityInStock() + "\n" +
                                                "---------------"
                                );
                            }
                        }
                        // Create a ListView to display medicine information
                        ListView<String> listView = new ListView<>(medicineInfoList);
                        listView.setPrefSize(500,600);
                        buttonBox.getChildren().add(listView);
                        buttonBox.getChildren().add(SubBack);

                        SubBack.setOnAction(e9->{
                            buttonBox.getChildren().removeAll(listView,SubBack);
                            buttonBox.getChildren().addAll(
                                    addMedicine,
                                    UpdateMedicine,
                                    DeleteMedicine,
                                    ShowMedicine,
                                    ShowExpMed,
                                    MainBack );

                        });

                    }
                    catch (Exception e100){
                        System.out.println(e100);
                    }


                });


            });


            //Supplier button config
            Button addSupplier = new Button("Add New Supplier");
            addSupplier.setPrefSize(buttonWidth, buttonHeight);
            addSupplier.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            addSupplier.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button UpdateSupplier = new Button("Update Supplier");
            UpdateSupplier.setPrefSize(buttonWidth, buttonHeight);
            UpdateSupplier.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            UpdateSupplier.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button DeleteSupplier = new Button("Delete Supplier");
            DeleteSupplier.setPrefSize(buttonWidth, buttonHeight);
            DeleteSupplier.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            DeleteSupplier.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button ShowSupplier = new Button("Show Suppliers");
            ShowSupplier.setPrefSize(buttonWidth, buttonHeight);
            ShowSupplier.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            ShowSupplier.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button AddSuppliedMedicine = new Button("Add Supplied Medicine");
            AddSuppliedMedicine.setPrefSize(buttonWidth, buttonHeight);
            AddSuppliedMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            AddSuppliedMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button ShowSuppliedMedicine = new Button("Show Supplied Medicines");
            ShowSuppliedMedicine.setPrefSize(buttonWidth, buttonHeight);
            ShowSuppliedMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            ShowSuppliedMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            TextField sname = new TextField();
            TextField sid = new TextField();
            TextField scontactPerson = new TextField();
            TextField scontactNumber = new TextField();

            sid.setPromptText("Enter Supplier ID");
            sid.setMaxWidth(1000);
            sid.setPrefHeight(20);
            sid.setFont(new Font(30));

            sname.setPromptText("Enter Supplier Name");
            sname.setMaxWidth(1000);
            sname.setPrefHeight(20);
            sname.setFont(new Font(30));

            scontactPerson.setPromptText("Enter Contact Person");
            scontactPerson.setMaxWidth(1000);
            scontactPerson.setPrefHeight(20);
            scontactPerson.setFont(new Font(30));

            scontactNumber.setPromptText("Enter Contact Number");
            scontactNumber.setMaxWidth(1000);
            scontactNumber.setPrefHeight(20);
            scontactNumber.setFont(new Font(30));


            btnManageSuppliers.setOnAction(e-> {
                        buttonBox.getChildren().removeAll(
                                btnManageMedicines,
                                btnManageSuppliers,
                                btnManageCustomers,
                                btnManagePrescriptions,
                                btnViewSalesReports,
                                btnManageInventory);
                        buttonBox.getChildren().addAll(
                                addSupplier,
                                UpdateSupplier,
                                DeleteSupplier,
                                ShowSupplier,
                                AddSuppliedMedicine,
                                ShowSuppliedMedicine,
                                MainBack);
                        MainBack.setOnAction(e1 -> {
                            buttonBox.getChildren().removeAll(
                                    addSupplier,
                                    UpdateSupplier,
                                    DeleteSupplier,
                                    ShowSupplier,
                                    AddSuppliedMedicine,
                                    ShowSuppliedMedicine,
                                    MainBack);
                            buttonBox.getChildren().addAll(
                                    btnManageMedicines,
                                    btnManageSuppliers,
                                    btnManageCustomers,
                                    btnManagePrescriptions,
                                    btnViewSalesReports,
                                    btnManageInventory);
                        });

                        addSupplier.setOnAction(e2 -> {
                            sname.setDisable(false);
                            scontactPerson.setDisable(false);
                            scontactNumber.setDisable(false);
                            Apply.setDisable(false);
                            sid.setText("");
                            sname.setText("");
                            scontactPerson.setText("");
                            scontactNumber.setText("");

                            buttonBox.getChildren().removeAll(
                                    addSupplier,
                                    UpdateSupplier,
                                    DeleteSupplier,
                                    ShowSupplier,
                                    AddSuppliedMedicine,
                                    ShowSuppliedMedicine,
                                    MainBack);
                            HapplybackH.getChildren().removeAll(SubBack, Apply);
                            HapplybackH.getChildren().addAll(SubBack, Apply);
                            buttonBox.getChildren().addAll(
                                    sid,
                                    sname,
                                    scontactPerson,
                                    scontactNumber,
                                    HapplybackH);
                            Apply.setOnAction(e3 -> {
                                try {
                                    Supplier.insertSupplier(
                                            Integer.parseInt(sid.getText()),
                                            sname.getText(),
                                            scontactPerson.getText(),
                                            scontactNumber.getText(),
                                            connection);

                                    sid.setText("");
                                    sname.setText("");
                                    scontactPerson.setText("");
                                    scontactNumber.setText("");
                                } catch (Exception e100) {
                                    System.out.println(e100);
                                }
                            });
                            SubBack.setOnAction(e4 -> {
                                buttonBox.getChildren().removeAll(
                                        sid,
                                        sname,
                                        scontactPerson,
                                        scontactNumber,
                                        HapplybackH);
                                buttonBox.getChildren().addAll(
                                        addSupplier,
                                        UpdateSupplier,
                                        DeleteSupplier,
                                        ShowSupplier,
                                        AddSuppliedMedicine,
                                        ShowSuppliedMedicine,
                                        MainBack);
                            });
                        });


                        TextField deleteSupplierID = new TextField();
                        deleteSupplierID.setPromptText("Enter Supplier ID");
                        deleteSupplierID.setMaxWidth(1000);
                        deleteSupplierID.setPrefHeight(20);
                        deleteSupplierID.setFont(new Font(30));
                        DeleteSupplier.setOnAction(e11 -> {
                            sname.setDisable(false);
                            scontactPerson.setDisable(false);
                            scontactNumber.setDisable(false);
                            Apply.setDisable(false);
                            deleteSupplierID.setText("");
                            sname.setText("");
                            scontactPerson.setText("");
                            scontactNumber.setText("");

                            try {
                                buttonBox.getChildren().removeAll(
                                        addSupplier,
                                        UpdateSupplier,
                                        DeleteSupplier,
                                        ShowSupplier,
                                        AddSuppliedMedicine,
                                        ShowSuppliedMedicine,
                                        MainBack);
                                HapplybackH.getChildren().removeAll(SubBack, Apply);
                                HapplybackH.getChildren().addAll(SubBack, Apply);
                                buttonBox.getChildren().addAll(deleteSupplierID, HapplybackH);
                                Apply.setDisable(true);

                                SubBack.setOnAction(e4 -> {
                                    buttonBox.getChildren().removeAll(
                                            deleteSupplierID,
                                            HapplybackH);
                                    buttonBox.getChildren().addAll(
                                            addSupplier,
                                            UpdateSupplier,
                                            DeleteSupplier,
                                            ShowSupplier,
                                            AddSuppliedMedicine,
                                            ShowSuppliedMedicine,
                                            MainBack);
                                });

                                ArrayList<Supplier> allSuppliers = new ArrayList<>(Supplier.getAllSuppliers(connection));

                                deleteSupplierID.textProperty().addListener((observable, oldValue, newValue) -> {
                                    try {
                                        int intNewValue = Integer.parseInt(newValue);
                                        boolean isAllowed = allSuppliers.stream().anyMatch(supplierInfo -> supplierInfo.getSupplierID() == intNewValue);

                                        if (isAllowed) {
                                            Apply.setDisable(false);
                                        } else {
                                            Apply.setDisable(true);
                                        }

                                        Apply.setOnAction(e3 -> {
                                            try {
                                                Supplier.deleteSupplier(Integer.parseInt(deleteSupplierID.getText()), connection);
                                                deleteSupplierID.setText("");
                                            } catch (Exception e100) {
                                                System.out.println(e100);
                                            }
                                        });

                                    } catch (NumberFormatException e102) {
                                        // Handle the case where newValue is not a valid integer
                                        Apply.setDisable(true);
                                    }
                                });

                            } catch (Exception e100) {
                                System.out.println(e100);
                            }

                        });


                        TextField updateSupplierID = new TextField();
                        updateSupplierID.setPromptText("Enter Supplier ID");
                        updateSupplierID.setMaxWidth(1000);
                        updateSupplierID.setPrefHeight(20);
                        updateSupplierID.setFont(new Font(30));
                        UpdateSupplier.setOnAction(e7 -> {
                            sname.setDisable(false);
                            scontactPerson.setDisable(false);
                            scontactNumber.setDisable(false);
                            Apply.setDisable(false);
                            updateSupplierID.setText("");
                            sname.setText("");
                            scontactPerson.setText("");
                            scontactNumber.setText("");

                            try {
                                buttonBox.getChildren().removeAll(
                                        addSupplier,
                                        UpdateSupplier,
                                        DeleteSupplier,
                                        ShowSupplier,
                                        AddSuppliedMedicine,
                                        ShowSuppliedMedicine,
                                        MainBack);
                                HapplybackH.getChildren().removeAll(SubBack, Apply);
                                HapplybackH.getChildren().addAll(SubBack, Apply);
                                buttonBox.getChildren().addAll(
                                        updateSupplierID,
                                        sname,
                                        scontactPerson,
                                        scontactNumber,
                                        HapplybackH);
                                sname.setDisable(true);
                                scontactPerson.setDisable(true);
                                scontactNumber.setDisable(true);
                                Apply.setDisable(true);

                                SubBack.setOnAction(e4 -> {
                                    buttonBox.getChildren().removeAll(
                                            updateSupplierID,
                                            sname,
                                            scontactPerson,
                                            scontactNumber,
                                            HapplybackH);
                                    buttonBox.getChildren().addAll(
                                            addSupplier,
                                            UpdateSupplier,
                                            DeleteSupplier,
                                            ShowSupplier,
                                            AddSuppliedMedicine,
                                            ShowSuppliedMedicine,
                                            MainBack);
                                });

                                ArrayList<Supplier> allSuppliers = new ArrayList<>(Supplier.getAllSuppliers(connection));

                                updateSupplierID.textProperty().addListener((observable, oldValue, newValue) -> {
                                    try {
                                        int intNewValue = Integer.parseInt(newValue);
                                        boolean isAllowed = allSuppliers.stream().anyMatch(supplierInfo -> supplierInfo.getSupplierID() == intNewValue);

                                        if (isAllowed) {
                                            sname.setDisable(false);
                                            scontactPerson.setDisable(false);
                                            scontactNumber.setDisable(false);
                                            Apply.setDisable(false);
                                        } else {
                                            sname.setDisable(true);
                                            scontactPerson.setDisable(true);
                                            scontactNumber.setDisable(true);
                                            Apply.setDisable(true);
                                        }

                                        Apply.setOnAction(e3 -> {
                                            try {
                                                Supplier.updateSupplier(Integer.parseInt(updateSupplierID.getText()), sname.getText(), scontactPerson.getText(), scontactNumber.getText(), connection);
                                                updateSupplierID.setText("");
                                                sname.setText("");
                                                scontactPerson.setText("");
                                                scontactNumber.setText("");
                                            } catch (Exception e100) {
                                                System.out.println(e100);
                                            }
                                        });

                                    } catch (NumberFormatException e102) {
                                        // Handle the case where newValue is not a valid integer
                                        sname.setDisable(true);
                                        scontactPerson.setDisable(true);
                                        scontactNumber.setDisable(true);
                                        Apply.setDisable(true);
                                    }
                                });

                            } catch (Exception e100) {
                                System.out.println(e100);
                            }
                        });


                        ShowSupplier.setOnAction(e8 -> {
                            buttonBox.getChildren().removeAll(
                                    addSupplier,
                                    UpdateSupplier,
                                    DeleteSupplier,
                                    ShowSupplier,
                                    AddSuppliedMedicine,
                                    ShowSuppliedMedicine,
                                    MainBack);
                            try {
                                ArrayList<Supplier> allSuppliers = new ArrayList<>(Supplier.getAllSuppliers(connection));
                                ObservableList<String> supplierInfoList = FXCollections.observableArrayList();
                                for (int i = 0; i < allSuppliers.size(); i++) {
                                    Supplier supplier = allSuppliers.get(i);
                                    // Append supplier information to the list
                                    supplierInfoList.add(
                                            "Supplier ID: " + supplier.getSupplierID() + "\n" +
                                                    "Name: " + supplier.getName() + "\n" +
                                                    "Contact Person: " + supplier.getContactPerson() + "\n" +
                                                    "Contact Number: " + supplier.getContactNumber() + "\n" +
                                                    "---------------"
                                    );
                                }
                                // Create a ListView to display supplier information
                                ListView<String> listView = new ListView<>(supplierInfoList);
                                listView.setPrefSize(500, 600);
                                buttonBox.getChildren().add(listView);
                                buttonBox.getChildren().add(SubBack);

                                SubBack.setOnAction(e9 -> {
                                    buttonBox.getChildren().removeAll(listView, SubBack);
                                    buttonBox.getChildren().addAll(
                                            addSupplier,
                                            UpdateSupplier,
                                            DeleteSupplier,
                                            ShowSupplier,
                                            AddSuppliedMedicine,
                                            ShowSuppliedMedicine,
                                            MainBack);
                                });
                            } catch (Exception e100) {
                                System.out.println(e100);
                            }
                        });

                        TextField addSuppliedMedicineSupplierID = new TextField();
                        addSuppliedMedicineSupplierID.setPromptText("Enter Supplier ID");
                        addSuppliedMedicineSupplierID.setMaxWidth(1000);
                        addSuppliedMedicineSupplierID.setPrefHeight(20);
                        addSuppliedMedicineSupplierID.setFont(new Font(30));

                        TextField addSuppliedMedicineMedicineID = new TextField();
                        addSuppliedMedicineMedicineID.setPromptText("Enter Medicine ID");
                        addSuppliedMedicineMedicineID.setMaxWidth(1000);
                        addSuppliedMedicineMedicineID.setPrefHeight(20);
                        addSuppliedMedicineMedicineID.setFont(new Font(30));

                        AddSuppliedMedicine.setOnAction(e14 -> {

                                // Enable necessary fields
                                addSuppliedMedicineSupplierID.setDisable(false);
                                addSuppliedMedicineMedicineID.setDisable(false);
                                Apply.setDisable(false);

                                // Clear existing values
                                addSuppliedMedicineSupplierID.setText("");
                                addSuppliedMedicineMedicineID.setText("");

                                // Remove existing buttons from the buttonBox
                                buttonBox.getChildren().removeAll(
                                        addSupplier,
                                        UpdateSupplier,
                                        DeleteSupplier,
                                        ShowSupplier,
                                        AddSuppliedMedicine,
                                        ShowSuppliedMedicine,
                                        MainBack);

                                HapplybackH.getChildren().removeAll(SubBack, Apply);
                                HapplybackH.getChildren().addAll(SubBack, Apply);

                                // Add necessary fields to the buttonBox
                                buttonBox.getChildren().addAll(
                                        addSuppliedMedicineSupplierID,
                                        addSuppliedMedicineMedicineID,
                                        HapplybackH);

                                Apply.setOnAction(e3 -> {
                                    try {
                                        ArrayList<Supplier> allSuppliers = new ArrayList<>(Supplier.getAllSuppliers(connection));
                                        // Check if addSuppliedMedicineSupplierID is in the supplier ArrayList
                                        boolean isSupplierIDValid = allSuppliers.stream()
                                                .anyMatch(supplier -> supplier.getSupplierID() == Integer.parseInt(addSuppliedMedicineSupplierID.getText()));

                                        ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));
                                        // Check if addSuppliedMedicineMedicineID is in the medicine ArrayList
                                        boolean isMedicineIDValid = allMedicines.stream()
                                                .anyMatch(medicine -> medicine.getMedicineID() == Integer.parseInt(addSuppliedMedicineMedicineID.getText()));

                                        // Perform the insertion only if both IDs are valid
                                        if (isSupplierIDValid && isMedicineIDValid) {

                                            // Insert supplied medicine into the database
                                            SuppliedMedicine.insertSuppliedMedicine(
                                                    Integer.parseInt(addSuppliedMedicineSupplierID.getText()),
                                                    Integer.parseInt(addSuppliedMedicineMedicineID.getText()),
                                                    connection);

                                            // Clear the text fields after successful insertion
                                            addSuppliedMedicineSupplierID.clear();
                                            addSuppliedMedicineMedicineID.clear();

                                        } else {
                                            // Handle the case where either supplier ID or medicine ID is not valid
                                            System.out.println("Invalid Supplier ID or Medicine ID");
                                        }
                                    } catch (Exception e101) {
                                        System.out.println(e101);
                                    }
                                });

                                SubBack.setOnAction(e4 -> {
                                    // Remove the input fields and buttons from the buttonBox
                                    buttonBox.getChildren().removeAll(
                                            addSuppliedMedicineSupplierID,
                                            addSuppliedMedicineMedicineID,
                                            HapplybackH);

                                    // Add back the original buttons to the buttonBox
                                    buttonBox.getChildren().addAll(
                                            addSupplier,
                                            UpdateSupplier,
                                            DeleteSupplier,
                                            ShowSupplier,
                                            AddSuppliedMedicine,
                                            ShowSuppliedMedicine,
                                            MainBack);
                                });

                        });

                        ShowSuppliedMedicine.setOnAction(e15 -> {
                            // Remove existing buttons from the buttonBox
                            buttonBox.getChildren().removeAll(
                                    addSupplier,
                                    UpdateSupplier,
                                    DeleteSupplier,
                                    ShowSupplier,
                                    AddSuppliedMedicine,
                                    ShowSuppliedMedicine,
                                    MainBack);

                            try {
                                // Fetch all supplied medicines from the database
                                ArrayList<SuppliedMedicine> allSuppliedMedicines = new ArrayList<>(SuppliedMedicine.getAllSuppliedMedicines(connection));

                                // Create an observable list to store supplied medicine information
                                ObservableList<String> suppliedMedicineInfoList = FXCollections.observableArrayList();

                                // Populate the observable list with supplied medicine information
                                for (int i = 0; i < allSuppliedMedicines.size(); i++) {
                                    SuppliedMedicine suppliedMedicine = allSuppliedMedicines.get(i);
                                    suppliedMedicineInfoList.add(
                                            "Supplier ID: " + suppliedMedicine.getSupplierID() + "\n" +
                                                    "Medicine ID: " + suppliedMedicine.getMedicineID() + "\n" +
                                                    "---------------"
                                    );
                                }

                                // Create a ListView to display supplied medicine information
                                ListView<String> listView = new ListView<>(suppliedMedicineInfoList);
                                listView.setPrefSize(500, 600);

                                // Add the ListView and SubBack button to the buttonBox
                                buttonBox.getChildren().addAll(listView, SubBack);

                                SubBack.setOnAction(e9 -> {
                                    // Remove the ListView and SubBack button from the buttonBox
                                    buttonBox.getChildren().removeAll(listView, SubBack);

                                    // Add back the original buttons to the buttonBox
                                    buttonBox.getChildren().addAll(
                                            addSupplier,
                                            UpdateSupplier,
                                            DeleteSupplier,
                                            ShowSupplier,
                                            AddSuppliedMedicine,
                                            ShowSuppliedMedicine,
                                            MainBack);
                                });
                            } catch (Exception e100) {
                                System.out.println(e100);
                            }
                        });

                    });



                //Customer Button Configuration
                Button addCustomer = new Button("Add New Customer");
                addCustomer.setPrefSize(buttonWidth, buttonHeight);
                addCustomer.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                addCustomer.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

                Button UpdateCustomer = new Button("Update Customer");
                UpdateCustomer.setPrefSize(buttonWidth, buttonHeight);
                UpdateCustomer.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                UpdateCustomer.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

                Button DeleteCustomer = new Button("Delete Customer");
                DeleteCustomer.setPrefSize(buttonWidth, buttonHeight);
                DeleteCustomer.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                DeleteCustomer.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

                Button ShowCustomers = new Button("Show Customers");
                ShowCustomers.setPrefSize(buttonWidth, buttonHeight);
                ShowCustomers.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                ShowCustomers.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

                Button searchCustomer = new Button("Search for Customer");
                searchCustomer.setPrefSize(buttonWidth, buttonHeight);
                searchCustomer.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                searchCustomer.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");

                Button searchCustomerByID = new Button("Search By ID");
                searchCustomerByID.setPrefSize(buttonWidth, buttonHeight);
                searchCustomerByID.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                searchCustomerByID.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");

                Button searchCustomerByName = new Button("Search By Name");
                searchCustomerByName.setPrefSize(buttonWidth, buttonHeight);
                searchCustomerByName.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                searchCustomerByName.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");

                TextField searchCustomerIDTextField = new TextField();
                searchCustomerIDTextField.setPromptText("Enter Customer ID");
                searchCustomerIDTextField.setMaxWidth(1000);
                searchCustomerIDTextField.setPrefHeight(20);
                searchCustomerIDTextField.setFont(new Font(30));

                TextField searchCustomerNameTextField = new TextField();
                searchCustomerNameTextField.setPromptText("Enter Customer Name");
                searchCustomerNameTextField.setMaxWidth(1000);
                searchCustomerNameTextField.setPrefHeight(20);
                searchCustomerNameTextField.setFont(new Font(30));



                TextField cid = new TextField();
                TextField cfirstName = new TextField();
                TextField clastName = new TextField();
                TextField caddress = new TextField();
                TextField ccontactNumber = new TextField();
                TextField cemail = new TextField();

                cid.setPromptText("Enter Customer ID");
                cid.setMaxWidth(1000);
                cid.setPrefHeight(20);
                cid.setFont(new Font(30));

                cfirstName.setPromptText("Enter First Name");
                cfirstName.setMaxWidth(1000);
                cfirstName.setPrefHeight(20);
                cfirstName.setFont(new Font(30));

                clastName.setPromptText("Enter Last Name");
                clastName.setMaxWidth(1000);
                clastName.setPrefHeight(20);
                clastName.setFont(new Font(30));

                caddress.setPromptText("Enter Address");
                caddress.setMaxWidth(1000);
                caddress.setPrefHeight(20);
                caddress.setFont(new Font(30));

                ccontactNumber.setPromptText("Enter Contact Number");
                ccontactNumber.setMaxWidth(1000);
                ccontactNumber.setPrefHeight(20);
                ccontactNumber.setFont(new Font(30));

                cemail.setPromptText("Enter Email");
                cemail.setMaxWidth(1000);
                cemail.setPrefHeight(20);
                cemail.setFont(new Font(30));



                btnManageCustomers.setOnAction(e16->{
                    buttonBox.getChildren().removeAll(
                            btnManageMedicines,
                            btnManageSuppliers,
                            btnManageCustomers,
                            btnManagePrescriptions,
                            btnViewSalesReports,
                            btnManageInventory);
                    buttonBox.getChildren().addAll(
                            addCustomer,
                            UpdateCustomer,
                            DeleteCustomer,
                            ShowCustomers,
                            searchCustomer,
                            MainBack);
                    MainBack.setOnAction(e1 -> {
                        buttonBox.getChildren().removeAll(
                                addCustomer,
                                UpdateCustomer,
                                DeleteCustomer,
                                ShowCustomers,
                                searchCustomer,
                                MainBack);
                        buttonBox.getChildren().addAll(
                                btnManageMedicines,
                                btnManageSuppliers,
                                btnManageCustomers,
                                btnManagePrescriptions,
                                btnViewSalesReports,
                                btnManageInventory);
                    });

                    HBox HapplybackH2 = new HBox(240);
                    HapplybackH2.setAlignment(Pos.BOTTOM_LEFT);
                    HapplybackH2.getChildren().addAll(SubBack,Apply);
                    HBox HapplybackH3 = new HBox(240);
                    HapplybackH3.setAlignment(Pos.BOTTOM_LEFT);
                    HapplybackH3.getChildren().addAll(SubBack,Apply);

                    searchCustomer.setOnAction(e28->{
                        Apply.setDisable(false);
                        buttonBox.getChildren().removeAll(
                                addCustomer,
                                UpdateCustomer,
                                DeleteCustomer,
                                ShowCustomers,
                                searchCustomer,
                                MainBack);
                        buttonBox.getChildren().addAll(
                                searchCustomerByID,
                                searchCustomerByName,
                                SubBack);
                        SubBack.setOnAction(e30->{
                            buttonBox.getChildren().addAll(
                                    addCustomer,
                                    UpdateCustomer,
                                    DeleteCustomer,
                                    ShowCustomers,
                                    searchCustomer,
                                    MainBack);
                            buttonBox.getChildren().removeAll(
                                    searchCustomerByID,
                                    searchCustomerByName,
                                    SubBack);
                        });
                        searchCustomerByID.setOnAction(e31 -> {
                            buttonBox.getChildren().removeAll(
                                    searchCustomerByID,
                                    searchCustomerByName,
                                    SubBack);
                            HapplybackH2.getChildren().removeAll(SubBack2, Apply);
                            HapplybackH2.getChildren().addAll(SubBack2, Apply);
                            ListView<String> resultListView = new ListView<>();
                            resultListView.setPrefSize(500,600);
                            buttonBox.getChildren().addAll(
                                    searchCustomerIDTextField,
                                    HapplybackH2
                            );

                            Apply.setOnAction(e32 -> {
                                try {
                                    ObservableList<String> searchResults = FXCollections.observableArrayList();

                                    int customerID = Integer.parseInt(searchCustomerIDTextField.getText());

                                    // Call the search by ID method
                                    Customer resultCustomer = Customer.getCustomerByID(customerID, connection);

                                    // Clear previous search results
                                    searchResults.clear();

                                    // Display the result in the ObservableList
                                    if (resultCustomer != null) {
                                        searchResults.add("Customer ID: " + resultCustomer.getCustomerID() + "\n" +
                                                "Name: " + resultCustomer.getFirstName() + " " + resultCustomer.getLastName() + "\n" +
                                                "Address: " + resultCustomer.getAddress() + "\n" +
                                                "Contact Number: " + resultCustomer.getContactNumber() + "\n" +
                                                "Email: " + resultCustomer.getEmail() + "\n" +
                                                "---------------");
                                    } else {
                                        searchResults.add("Customer not found");
                                    }
                                    resultListView.setItems(searchResults);
                                    HapplybackH2.getChildren().removeAll(SubBack2, Apply);
                                    HapplybackH2.getChildren().addAll(SubBack2, Apply);
                                    buttonBox.getChildren().removeAll(resultListView, HapplybackH2);
                                    buttonBox.getChildren().addAll(resultListView, HapplybackH2);
                                    searchCustomerIDTextField.setText("");
                                } catch (NumberFormatException | SQLException ex) {
                                    System.out.println("Error: " + ex.getMessage());
                                    HapplybackH2.getChildren().removeAll(SubBack2, Apply);
                                    HapplybackH2.getChildren().addAll(SubBack2, Apply);
                                    buttonBox.getChildren().removeAll(resultListView, HapplybackH2);
                                    buttonBox.getChildren().addAll(resultListView, HapplybackH2);
                                    searchCustomerIDTextField.setText("");
                                }
                            });

                            SubBack2.setOnAction(e30 -> {
                                buttonBox.getChildren().removeAll(
                                        searchCustomerIDTextField,
                                        HapplybackH2,
                                        resultListView
                                );
                                buttonBox.getChildren().addAll(
                                        searchCustomerByID,
                                        searchCustomerByName,
                                        SubBack);
                            });
                        });


                        searchCustomerByName.setOnAction(e31 -> {
                            buttonBox.getChildren().removeAll(
                                    searchCustomerByID,
                                    searchCustomerByName,
                                    SubBack);
                            HapplybackH2.getChildren().removeAll(SubBack2, Apply);
                            HapplybackH2.getChildren().addAll(SubBack2, Apply);
                            ListView<String> resultListView = new ListView<>();
                            resultListView.setPrefSize(500, 600);
                            buttonBox.getChildren().addAll(
                                    searchCustomerNameTextField,
                                    HapplybackH2
                            );

                            Apply.setOnAction(e32 -> {
                                try {
                                    ObservableList<String> searchResults = FXCollections.observableArrayList();

                                    String customerName = searchCustomerNameTextField.getText();

                                    // Call the search by Name method
                                    ArrayList<Customer> resultCustomers = new ArrayList<>(Customer.getCustomerByName(customerName, connection));

                                    // Clear previous search results
                                    searchResults.clear();

                                    if (customerName.equals(""))
                                        searchResults.add("Customer not found");
                                        // Display the result in the ObservableList
                                    else if (!resultCustomers.isEmpty()) {
                                        for (Customer resultCustomer : resultCustomers) {
                                            searchResults.add("Customer ID: " + resultCustomer.getCustomerID() + "\n" +
                                                    "Name: " + resultCustomer.getFirstName() + " " + resultCustomer.getLastName() + "\n" +
                                                    "Address: " + resultCustomer.getAddress() + "\n" +
                                                    "Contact Number: " + resultCustomer.getContactNumber() + "\n" +
                                                    "Email: " + resultCustomer.getEmail() + "\n" +
                                                    "____________________________________________");
                                        }

                                    } else {
                                        searchResults.add("Customer not found");
                                    }
                                    resultListView.setItems(searchResults);
                                    HapplybackH2.getChildren().removeAll(SubBack2, Apply);
                                    HapplybackH2.getChildren().addAll(SubBack2, Apply);
                                    buttonBox.getChildren().removeAll(resultListView, HapplybackH2);
                                    buttonBox.getChildren().addAll(resultListView, HapplybackH2);
                                    searchCustomerNameTextField.setText("");
                                } catch (SQLException ex) {
                                    System.out.println("Error: " + ex.getMessage());
                                    HapplybackH2.getChildren().removeAll(SubBack2, Apply);
                                    HapplybackH2.getChildren().addAll(SubBack2, Apply);
                                    buttonBox.getChildren().removeAll(resultListView, HapplybackH2);
                                    buttonBox.getChildren().addAll(resultListView, HapplybackH2);
                                    searchCustomerNameTextField.setText("");
                                }

                            });

                            SubBack2.setOnAction(e30 -> {
                                buttonBox.getChildren().removeAll(
                                        searchCustomerNameTextField,
                                        HapplybackH2,
                                        resultListView
                                );
                                buttonBox.getChildren().addAll(
                                        searchCustomerByID,
                                        searchCustomerByName,
                                        SubBack);
                            });
                        });


                    });

                    addCustomer.setOnAction(e2 -> {
                        cfirstName.setDisable(false);
                        clastName.setDisable(false);
                        caddress.setDisable(false);
                        ccontactNumber.setDisable(false);
                        cemail.setDisable(false);
                        Apply.setDisable(false);

                        cid.setText("");
                        cfirstName.setText("");
                        clastName.setText("");
                        caddress.setText("");
                        ccontactNumber.setText("");
                        cemail.setText("");

                        buttonBox.getChildren().removeAll(
                                addCustomer,
                                UpdateCustomer,
                                DeleteCustomer,
                                ShowCustomers,
                                searchCustomer,
                                MainBack);
                        HapplybackH.getChildren().removeAll(SubBack, Apply);
                        HapplybackH.getChildren().addAll(SubBack, Apply);
                        buttonBox.getChildren().addAll(
                                cid,
                                cfirstName,
                                clastName,
                                caddress,
                                ccontactNumber,
                                cemail,
                                HapplybackH);
                        Apply.setOnAction(e3 -> {
                            try {
                                Customer.insertCustomer(
                                        Integer.parseInt(cid.getText()),
                                        cfirstName.getText(),
                                        clastName.getText(),
                                        caddress.getText(),
                                        ccontactNumber.getText(),
                                        cemail.getText(),
                                        connection);

                                // Clear fields after successful insertion
                                cid.setText("");
                                cfirstName.setText("");
                                clastName.setText("");
                                caddress.setText("");
                                ccontactNumber.setText("");
                                cemail.setText("");
                            } catch (Exception e100) {
                                System.out.println(e100);
                            }
                        });
                        SubBack.setOnAction(e4 -> {
                            buttonBox.getChildren().removeAll(
                                    cid,
                                    cfirstName,
                                    clastName,
                                    caddress,
                                    ccontactNumber,
                                    cemail,
                                    HapplybackH);
                            buttonBox.getChildren().addAll(
                                    addCustomer,
                                    UpdateCustomer,
                                    DeleteCustomer,
                                    ShowCustomers,
                                    searchCustomer,
                                    MainBack);
                        });
                    });


                        TextField updateCustomerID = new TextField();
                        updateCustomerID.setPromptText("Enter Customer ID");
                        updateCustomerID.setMaxWidth(1000);
                        updateCustomerID.setPrefHeight(20);
                        updateCustomerID.setFont(new Font(30));

                        UpdateCustomer.setOnAction(e7 -> {
                            cfirstName.setDisable(false);
                            clastName.setDisable(false);
                            caddress.setDisable(false);
                            ccontactNumber.setDisable(false);
                            cemail.setDisable(false);
                            Apply.setDisable(false);

                            updateCustomerID.setText("");
                            cfirstName.setText("");
                            clastName.setText("");
                            caddress.setText("");
                            ccontactNumber.setText("");
                            cemail.setText("");

                            try {
                                buttonBox.getChildren().removeAll(
                                        addCustomer,
                                        UpdateCustomer,
                                        DeleteCustomer,
                                        ShowCustomers,
                                        searchCustomer,
                                        MainBack);
                                HapplybackH.getChildren().removeAll(SubBack, Apply);
                                HapplybackH.getChildren().addAll(SubBack, Apply);
                                buttonBox.getChildren().addAll(
                                        updateCustomerID,
                                        cfirstName,
                                        clastName,
                                        caddress,
                                        ccontactNumber,
                                        cemail,
                                        HapplybackH);

                                cfirstName.setDisable(true);
                                clastName.setDisable(true);
                                caddress.setDisable(true);
                                ccontactNumber.setDisable(true);
                                cemail.setDisable(true);
                                Apply.setDisable(true);

                                SubBack.setOnAction(e4 -> {
                                    buttonBox.getChildren().removeAll(
                                            updateCustomerID,
                                            cfirstName,
                                            clastName,
                                            caddress,
                                            ccontactNumber,
                                            cemail,
                                            HapplybackH);
                                    buttonBox.getChildren().addAll(
                                            addCustomer,
                                            UpdateCustomer,
                                            DeleteCustomer,
                                            ShowCustomers,
                                            searchCustomer,
                                            MainBack);
                                });

                                ArrayList<Customer> allCustomers = new ArrayList<>(Customer.getAllCustomers(connection));

                                updateCustomerID.textProperty().addListener((observable, oldValue, newValue) -> {
                                    try {
                                        int intNewValue = Integer.parseInt(newValue);
                                        boolean isAllowed = allCustomers.stream().anyMatch(customerInfo -> customerInfo.getCustomerID() == intNewValue);

                                        if (isAllowed) {
                                            cfirstName.setDisable(false);
                                            clastName.setDisable(false);
                                            caddress.setDisable(false);
                                            ccontactNumber.setDisable(false);
                                            cemail.setDisable(false);
                                            Apply.setDisable(false);
                                        } else {
                                            cfirstName.setDisable(true);
                                            clastName.setDisable(true);
                                            caddress.setDisable(true);
                                            ccontactNumber.setDisable(true);
                                            cemail.setDisable(true);
                                            Apply.setDisable(true);
                                        }

                                        Apply.setOnAction(e3 -> {
                                            try {
                                                Customer.updateCustomer(
                                                        Integer.parseInt(updateCustomerID.getText()),
                                                        cfirstName.getText(),
                                                        clastName.getText(),
                                                        caddress.getText(),
                                                        ccontactNumber.getText(),
                                                        cemail.getText(),
                                                        connection);

                                                updateCustomerID.setText("");
                                                cfirstName.setText("");
                                                clastName.setText("");
                                                caddress.setText("");
                                                ccontactNumber.setText("");
                                                cemail.setText("");
                                            } catch (Exception e100) {
                                                System.out.println(e100);
                                            }
                                        });

                                    } catch (NumberFormatException e102) {
                                        cfirstName.setDisable(true);
                                        clastName.setDisable(true);
                                        caddress.setDisable(true);
                                        ccontactNumber.setDisable(true);
                                        cemail.setDisable(true);
                                        Apply.setDisable(true);
                                    }
                                });

                            } catch (Exception e100) {
                                System.out.println(e100);
                            }
                        });




                    TextField deleteCustomerID = new TextField();
                    deleteCustomerID.setPromptText("Enter Customer ID");
                    deleteCustomerID.setMaxWidth(1000);
                    deleteCustomerID.setPrefHeight(20);
                    deleteCustomerID.setFont(new Font(30));

                    DeleteCustomer.setOnAction(e11 -> {
                        cfirstName.setDisable(false);
                        clastName.setDisable(false);
                        caddress.setDisable(false);
                        ccontactNumber.setDisable(false);
                        cemail.setDisable(false);
                        Apply.setDisable(false);
                        deleteCustomerID.setText("");
                        cfirstName.setText("");
                        clastName.setText("");
                        caddress.setText("");
                        ccontactNumber.setText("");
                        cemail.setText("");

                        try {
                            buttonBox.getChildren().removeAll(
                                    addCustomer,
                                    UpdateCustomer,
                                    DeleteCustomer,
                                    ShowCustomers,
                                    searchCustomer,
                                    MainBack);
                            HapplybackH.getChildren().removeAll(SubBack, Apply);
                            HapplybackH.getChildren().addAll(SubBack, Apply);
                            buttonBox.getChildren().addAll(deleteCustomerID, HapplybackH);
                            Apply.setDisable(true);

                            SubBack.setOnAction(e4 -> {
                                buttonBox.getChildren().removeAll(
                                        deleteCustomerID,
                                        HapplybackH);
                                buttonBox.getChildren().addAll(
                                        addCustomer,
                                        UpdateCustomer,
                                        DeleteCustomer,
                                        ShowCustomers,
                                        searchCustomer,
                                        MainBack);
                            });

                            ArrayList<Customer> allCustomers = new ArrayList<>(Customer.getAllCustomers(connection));

                            deleteCustomerID.textProperty().addListener((observable, oldValue, newValue) -> {
                                try {
                                    int intNewValue = Integer.parseInt(newValue);
                                    boolean isAllowed = allCustomers.stream().anyMatch(customerInfo -> customerInfo.getCustomerID() == intNewValue);

                                    if (isAllowed) {
                                        Apply.setDisable(false);
                                    } else {
                                        Apply.setDisable(true);
                                    }

                                    Apply.setOnAction(e3 -> {
                                        try {
                                            Customer.deleteCustomer(Integer.parseInt(deleteCustomerID.getText()), connection);
                                            deleteCustomerID.setText("");
                                        } catch (Exception e100) {
                                            System.out.println(e100);
                                        }
                                    });

                                } catch (NumberFormatException e102) {
                                    Apply.setDisable(true);
                                }
                            });

                        } catch (Exception e100) {
                            System.out.println(e100);
                        }

                    });
                        ShowCustomers.setOnAction(e8 -> {
                            buttonBox.getChildren().removeAll(
                                    addCustomer,
                                    UpdateCustomer,
                                    DeleteCustomer,
                                    ShowCustomers,
                                    searchCustomer,
                                    MainBack);
                            try {
                                ArrayList<Customer> allCustomers = new ArrayList<>(Customer.getAllCustomers(connection));
                                ObservableList<String> customerInfoList = FXCollections.observableArrayList();
                                for (int i = 0; i < allCustomers.size(); i++) {
                                    Customer customer = allCustomers.get(i);
                                    // Append customer information to the list
                                    customerInfoList.add(
                                            "Customer ID: " + customer.getCustomerID() + "\n" +
                                                    "Name: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
                                                    "Address: " + customer.getAddress() + "\n" +
                                                    "Contact Number: " + customer.getContactNumber() + "\n" +
                                                    "Email: " + customer.getEmail() + "\n" +
                                                    "---------------"
                                    );
                                }
                                // Create a ListView to display customer information
                                ListView<String> listView = new ListView<>(customerInfoList);
                                listView.setPrefSize(500, 600);
                                buttonBox.getChildren().add(listView);
                                buttonBox.getChildren().add(SubBack);

                                SubBack.setOnAction(e9 -> {
                                    buttonBox.getChildren().removeAll(listView, SubBack);
                                    buttonBox.getChildren().addAll(
                                            addCustomer,
                                            UpdateCustomer,
                                            DeleteCustomer,
                                            ShowCustomers,
                                            searchCustomer,
                                            MainBack);
                                });

                            } catch (Exception e100) {
                                System.out.println(e100);
                            }
                        });


                });


            //Prescription Button Config
            Button addPrescription = new Button("Add New Prescription");
            addPrescription.setPrefSize(buttonWidth, buttonHeight);
            addPrescription.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            addPrescription.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button UpdatePrescription = new Button("Update Prescription");
            UpdatePrescription.setPrefSize(buttonWidth, buttonHeight);
            UpdatePrescription.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            UpdatePrescription.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button DeletePrescription = new Button("Delete Prescription");
            DeletePrescription.setPrefSize(buttonWidth, buttonHeight);
            DeletePrescription.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            DeletePrescription.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button ShowPrescriptions = new Button("Show Prescriptions");
            ShowPrescriptions.setPrefSize(buttonWidth, buttonHeight);
            ShowPrescriptions.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            ShowPrescriptions.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            // Add Prescribed Medicine Button
            Button addPrescribedMedicine = new Button("Add Prescribed Medicine");
            addPrescribedMedicine.setPrefSize(buttonWidth, buttonHeight);
            addPrescribedMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            addPrescribedMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");

            // Show Prescribed Medicines Button
            Button showPrescribedMedicines = new Button("Show Prescribed Medicines");
            showPrescribedMedicines.setPrefSize(buttonWidth, buttonHeight);
            showPrescribedMedicines.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            showPrescribedMedicines.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;");


            TextField prescriptionID = new TextField();
            TextField customerID = new TextField();
            TextField doctorName = new TextField();
            TextField issueDate = new TextField();

            prescriptionID.setPromptText("Enter Prescription ID");
            prescriptionID.setMaxWidth(1000);
            prescriptionID.setPrefHeight(20);
            prescriptionID.setFont(new Font(30));

            customerID.setPromptText("Enter Customer ID");
            customerID.setMaxWidth(1000);
            customerID.setPrefHeight(20);
            customerID.setFont(new Font(30));

            doctorName.setPromptText("Enter Doctor Name");
            doctorName.setMaxWidth(1000);
            doctorName.setPrefHeight(20);
            doctorName.setFont(new Font(30));

            issueDate.setPromptText("Enter Issue Date");
            issueDate.setMaxWidth(1000);
            issueDate.setPrefHeight(20);
            issueDate.setFont(new Font(30));


            btnManagePrescriptions.setOnAction(e19->{

                buttonBox.getChildren().removeAll(
                        btnManageMedicines,
                        btnManageSuppliers,
                        btnManageCustomers,
                        btnManagePrescriptions,
                        btnViewSalesReports,
                        btnManageInventory);
                buttonBox.getChildren().addAll(
                        addPrescription,
                        UpdatePrescription,
                        DeletePrescription,
                        ShowPrescriptions,
                        addPrescribedMedicine,
                        showPrescribedMedicines,
                        MainBack);
                MainBack.setOnAction(e1 -> {
                    buttonBox.getChildren().removeAll(
                            addPrescription,
                            UpdatePrescription,
                            DeletePrescription,
                            ShowPrescriptions,
                            addPrescribedMedicine,
                            showPrescribedMedicines,
                            MainBack);
                    buttonBox.getChildren().addAll(
                            btnManageMedicines,
                            btnManageSuppliers,
                            btnManageCustomers,
                            btnManagePrescriptions,
                            btnViewSalesReports,
                            btnManageInventory);
                });


                addPrescription.setOnAction(e2 -> {
                    customerID.setDisable(false);
                    doctorName.setDisable(false);
                    issueDate.setDisable(false);
                    Apply.setDisable(false);

                    prescriptionID.setText("");
                    customerID.setText("");
                    doctorName.setText("");
                    issueDate.setText("");

                    buttonBox.getChildren().removeAll(
                            addPrescription,
                            UpdatePrescription,
                            DeletePrescription,
                            ShowPrescriptions,
                            addPrescribedMedicine,
                            showPrescribedMedicines,
                            MainBack);
                    HapplybackH.getChildren().removeAll(SubBack, Apply);
                    HapplybackH.getChildren().addAll(SubBack, Apply);
                    buttonBox.getChildren().addAll(
                            prescriptionID,
                            customerID,
                            doctorName,
                            issueDate,
                            HapplybackH);
                    Apply.setOnAction(e3 -> {
                        try {
                            Prescription.insertPrescription(
                                    Integer.parseInt(prescriptionID.getText()),
                                    Integer.parseInt(customerID.getText()),
                                    doctorName.getText(),
                                    parseDate(issueDate.getText()),
                                    connection);

                            // Clear fields after successful insertion
                            prescriptionID.setText("");
                            customerID.setText("");
                            doctorName.setText("");
                            issueDate.setText("");
                        } catch (Exception e100) {
                            System.out.println(e100);
                        }
                    });
                    SubBack.setOnAction(e4 -> {
                        buttonBox.getChildren().removeAll(
                                prescriptionID,
                                customerID,
                                doctorName,
                                issueDate,
                                HapplybackH);
                        buttonBox.getChildren().addAll(
                                addPrescription,
                                UpdatePrescription,
                                DeletePrescription,
                                ShowPrescriptions,
                                addPrescribedMedicine,
                                showPrescribedMedicines,
                                MainBack);
                    });
                });



                TextField updatePrescriptionID = new TextField();
                updatePrescriptionID.setPromptText("Enter Prescription ID");
                updatePrescriptionID.setMaxWidth(1000);
                updatePrescriptionID.setPrefHeight(20);
                updatePrescriptionID.setFont(new Font(30));

                //Update Given Prescription
                UpdatePrescription.setOnAction(e21 -> {
                    customerID.setDisable(false);
                    doctorName.setDisable(false);
                    issueDate.setDisable(false);
                    Apply.setDisable(false);

                    updatePrescriptionID.setText("");
                    customerID.setText("");
                    doctorName.setText("");
                    issueDate.setText("");

                    try {
                        buttonBox.getChildren().removeAll(
                                addPrescription,
                                UpdatePrescription,
                                DeletePrescription,
                                ShowPrescriptions,
                                addPrescribedMedicine,
                                showPrescribedMedicines,
                                MainBack);
                        HapplybackH.getChildren().removeAll(SubBack, Apply);
                        HapplybackH.getChildren().addAll(SubBack, Apply);
                        buttonBox.getChildren().addAll(
                                updatePrescriptionID,
                                customerID,
                                doctorName,
                                issueDate,
                                HapplybackH);

                        customerID.setDisable(true);
                        doctorName.setDisable(true);
                        issueDate.setDisable(true);
                        Apply.setDisable(true);

                        SubBack.setOnAction(e4 -> {
                            buttonBox.getChildren().removeAll(
                                    updatePrescriptionID,
                                    customerID,
                                    doctorName,
                                    issueDate,
                                    HapplybackH);
                            buttonBox.getChildren().addAll(
                                    addPrescription,
                                    UpdatePrescription,
                                    DeletePrescription,
                                    ShowPrescriptions,
                                    addPrescribedMedicine,
                                    showPrescribedMedicines,
                                    MainBack);
                        });

                        ArrayList<Prescription> allPrescriptions = new ArrayList<>(Prescription.getAllPrescriptions(connection));

                        updatePrescriptionID.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int intNewValue = Integer.parseInt(newValue);
                                boolean isAllowed = allPrescriptions.stream().anyMatch(prescriptionInfo -> prescriptionInfo.getPrescriptionID() == intNewValue);

                                if (isAllowed) {
                                    customerID.setDisable(false);
                                    doctorName.setDisable(false);
                                    issueDate.setDisable(false);
                                    Apply.setDisable(false);
                                } else {
                                    customerID.setDisable(true);
                                    doctorName.setDisable(true);
                                    issueDate.setDisable(true);
                                    Apply.setDisable(true);
                                }

                                Apply.setOnAction(e3 -> {
                                    try {
                                        Prescription.updatePrescription(
                                                Integer.parseInt(updatePrescriptionID.getText()),
                                                Integer.parseInt(customerID.getText()),
                                                doctorName.getText(),
                                                parseDate(issueDate.getText()),
                                                connection);

                                        updatePrescriptionID.setText("");
                                        customerID.setText("");
                                        doctorName.setText("");
                                        issueDate.setText("");
                                    } catch (Exception e100) {
                                        System.out.println(e100);
                                    }
                                });

                            } catch (NumberFormatException e102) {
                                customerID.setDisable(true);
                                doctorName.setDisable(true);
                                issueDate.setDisable(true);
                                Apply.setDisable(true);
                            }
                        });

                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });


                TextField deletePrescriptionID = new TextField();
                deletePrescriptionID.setPromptText("Enter Prescription ID");
                deletePrescriptionID.setMaxWidth(1000);
                deletePrescriptionID.setPrefHeight(20);
                deletePrescriptionID.setFont(new Font(30));

                DeletePrescription.setOnAction(e11 -> {
                    customerID.setDisable(false);
                    doctorName.setDisable(false);
                    issueDate.setDisable(false);
                    Apply.setDisable(false);
                    deletePrescriptionID.setText("");
                    customerID.setText("");
                    doctorName.setText("");
                    issueDate.setText("");

                    try {
                        buttonBox.getChildren().removeAll(
                                addPrescription,
                                UpdatePrescription,
                                DeletePrescription,
                                ShowPrescriptions,
                                addPrescribedMedicine,
                                showPrescribedMedicines,
                                MainBack);
                        HapplybackH.getChildren().removeAll(SubBack, Apply);
                        HapplybackH.getChildren().addAll(SubBack, Apply);
                        buttonBox.getChildren().addAll(deletePrescriptionID, HapplybackH);
                        Apply.setDisable(true);

                        SubBack.setOnAction(e4 -> {
                            buttonBox.getChildren().removeAll(
                                    deletePrescriptionID,
                                    HapplybackH);
                            buttonBox.getChildren().addAll(
                                    addPrescription,
                                    UpdatePrescription,
                                    DeletePrescription,
                                    ShowPrescriptions,
                                    addPrescribedMedicine,
                                    showPrescribedMedicines,
                                    MainBack);
                        });

                        ArrayList<Prescription> allPrescriptions = new ArrayList<>(Prescription.getAllPrescriptions(connection));

                        deletePrescriptionID.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int intNewValue = Integer.parseInt(newValue);
                                boolean isAllowed = allPrescriptions.stream().anyMatch(prescriptionInfo -> prescriptionInfo.getPrescriptionID() == intNewValue);

                                if (isAllowed) {
                                    Apply.setDisable(false);
                                } else {
                                    Apply.setDisable(true);
                                }

                                Apply.setOnAction(e3 -> {
                                    try {
                                        Prescription.deletePrescription(Integer.parseInt(deletePrescriptionID.getText()), connection);
                                        deletePrescriptionID.setText("");
                                    } catch (Exception e100) {
                                        System.out.println(e100);
                                    }
                                });

                            } catch (NumberFormatException e102) {
                                Apply.setDisable(true);
                            }
                        });

                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });

                //Show All Prescriptions
                ShowPrescriptions.setOnAction(e8 -> {
                    buttonBox.getChildren().removeAll(
                            addPrescription,
                            UpdatePrescription,
                            DeletePrescription,
                            ShowPrescriptions,
                            addPrescribedMedicine,
                            showPrescribedMedicines,
                            MainBack);
                    try {
                        ArrayList<Prescription> allPrescriptions = new ArrayList<>(Prescription.getAllPrescriptions(connection));
                        ObservableList<String> prescriptionInfoList = FXCollections.observableArrayList();
                        for (int i = 0; i < allPrescriptions.size(); i++) {
                            Prescription prescription = allPrescriptions.get(i);
                            // Append prescription information to the list
                            prescriptionInfoList.add(
                                    "Prescription ID: " + prescription.getPrescriptionID() + "\n" +
                                            "Customer ID: " + prescription.getCustomerID() + "\n" +
                                            "Doctor Name: " + prescription.getDoctorName() + "\n" +
                                            "Issue Date: " + prescription.getIssueDate() + "\n" +
                                            "---------------"
                            );
                        }
                        // Create a ListView to display prescription information
                        ListView<String> listView = new ListView<>(prescriptionInfoList);
                        listView.setPrefSize(500, 600);
                        buttonBox.getChildren().add(listView);
                        buttonBox.getChildren().add(SubBack);

                        SubBack.setOnAction(e9 -> {
                            buttonBox.getChildren().removeAll(listView, SubBack);
                            buttonBox.getChildren().addAll(
                                    addPrescription,
                                    UpdatePrescription,
                                    DeletePrescription,
                                    ShowPrescriptions,
                                    addPrescribedMedicine,
                                    showPrescribedMedicines,
                                    MainBack);
                        });

                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });

                // TextField for entering Prescription ID
                TextField addPrescribedMedicinePrescriptionID = new TextField();
                addPrescribedMedicinePrescriptionID.setPromptText("Enter Prescription ID");
                addPrescribedMedicinePrescriptionID.setMaxWidth(1000);
                addPrescribedMedicinePrescriptionID.setPrefHeight(20);
                addPrescribedMedicinePrescriptionID.setFont(new Font(30));

                // TextField for entering Medicine ID
                TextField addPrescribedMedicineMedicineID = new TextField();
                addPrescribedMedicineMedicineID.setPromptText("Enter Medicine ID");
                addPrescribedMedicineMedicineID.setMaxWidth(1000);
                addPrescribedMedicineMedicineID.setPrefHeight(20);
                addPrescribedMedicineMedicineID.setFont(new Font(30));

                // TextField for entering Quantity
                TextField addPrescribedMedicineQuantity = new TextField();
                addPrescribedMedicineQuantity.setPromptText("Enter Quantity");
                addPrescribedMedicineQuantity.setMaxWidth(1000);
                addPrescribedMedicineQuantity.setPrefHeight(20);
                addPrescribedMedicineQuantity.setFont(new Font(30));

                addPrescribedMedicine.setOnAction(e22 -> {
                    // Enable necessary fields
                    addPrescribedMedicinePrescriptionID.setDisable(false);
                    addPrescribedMedicineMedicineID.setDisable(false);
                    addPrescribedMedicineQuantity.setDisable(false);
                    Apply.setDisable(false);

                    // Clear existing values
                    addPrescribedMedicinePrescriptionID.setText("");
                    addPrescribedMedicineMedicineID.setText("");
                    addPrescribedMedicineQuantity.setText("");

                    // Remove existing buttons from the buttonBox
                    buttonBox.getChildren().removeAll(
                            addPrescription,
                            DeletePrescription,
                            UpdatePrescription,
                            addPrescribedMedicine,
                            ShowPrescriptions,
                            showPrescribedMedicines,
                            MainBack);

                    HapplybackH.getChildren().removeAll(SubBack, Apply);
                    HapplybackH.getChildren().addAll(SubBack, Apply);

                    // Add necessary fields to the buttonBox
                    buttonBox.getChildren().addAll(
                            addPrescribedMedicinePrescriptionID,
                            addPrescribedMedicineMedicineID,
                            addPrescribedMedicineQuantity,
                            HapplybackH);

                    Apply.setOnAction(e3 -> {
                        try {
                            ArrayList<Prescription> allPrescriptions = new ArrayList<>(Prescription.getAllPrescriptions(connection));
                            // Check if addPrescribedMedicinePrescriptionID is in the prescription ArrayList
                            boolean isPrescriptionIDValid = allPrescriptions.stream()
                                    .anyMatch(prescription -> prescription.getPrescriptionID() == Integer.parseInt(addPrescribedMedicinePrescriptionID.getText()));

                            ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));
                            // Check if addPrescribedMedicineMedicineID is in the medicine ArrayList
                            boolean isMedicineIDValid = allMedicines.stream()
                                    .anyMatch(medicine -> medicine.getMedicineID() == Integer.parseInt(addPrescribedMedicineMedicineID.getText()));

                            // Perform the insertion only if all IDs are valid
                            if (isPrescriptionIDValid && isMedicineIDValid) {
                                // Insert prescribed medicine into the database
                                PrescribedMedicine.insertPrescribedMedicine(
                                        Integer.parseInt(addPrescribedMedicinePrescriptionID.getText()),
                                        Integer.parseInt(addPrescribedMedicineMedicineID.getText()),
                                        Integer.parseInt(addPrescribedMedicineQuantity.getText()),
                                        connection);

                                // Clear the text fields after successful insertion
                                addPrescribedMedicinePrescriptionID.clear();
                                addPrescribedMedicineMedicineID.clear();
                                addPrescribedMedicineQuantity.clear();
                            } else {
                                // Handle the case where either prescription ID or medicine ID is not valid
                                System.out.println("Invalid Prescription ID or Medicine ID");
                            }
                        } catch (Exception e101) {
                            System.out.println(e101);
                        }
                    });

                    SubBack.setOnAction(e4 -> {
                        // Remove the input fields and buttons from the buttonBox
                        buttonBox.getChildren().removeAll(
                                addPrescribedMedicinePrescriptionID,
                                addPrescribedMedicineMedicineID,
                                addPrescribedMedicineQuantity,
                                HapplybackH);

                        // Add back the original buttons to the buttonBox
                        buttonBox.getChildren().addAll(
                                addPrescription,
                                UpdatePrescription,
                                DeletePrescription,
                                ShowPrescriptions,
                                addPrescribedMedicine,
                                showPrescribedMedicines,
                                MainBack);
                    });
                });

                //Show Prescibed Medicines
                showPrescribedMedicines.setOnAction(e15 -> {
                    // Remove existing buttons from the buttonBox
                    buttonBox.getChildren().removeAll(
                            addPrescription,
                            DeletePrescription,
                            UpdatePrescription,
                            addPrescribedMedicine,
                            ShowPrescriptions,
                            showPrescribedMedicines,
                            MainBack);

                    try {
                        // Fetch all prescribed medicines from the database
                        ArrayList<PrescribedMedicine> allPrescribedMedicines = new ArrayList<>(PrescribedMedicine.getAllPrescribedMedicines(connection));

                        // Create an observable list to store prescribed medicine information
                        ObservableList<String> prescribedMedicineInfoList = FXCollections.observableArrayList();

                        // Populate the observable list with prescribed medicine information
                        for (int i = 0; i < allPrescribedMedicines.size(); i++) {
                            PrescribedMedicine prescribedMedicine = allPrescribedMedicines.get(i);
                            prescribedMedicineInfoList.add(
                                    "Prescription ID: " + prescribedMedicine.getPrescriptionID() + "\n" +
                                            "Medicine ID: " + prescribedMedicine.getMedicineID() + "\n" +
                                            "Quantity: " + prescribedMedicine.getQuantity() + "\n" +
                                            "---------------"
                            );
                        }

                        // Create a ListView to display prescribed medicine information
                        ListView<String> listView = new ListView<>(prescribedMedicineInfoList);
                        listView.setPrefSize(500, 600);

                        // Add the ListView and SubBack button to the buttonBox
                        buttonBox.getChildren().addAll(listView, SubBack);

                        SubBack.setOnAction(e9 -> {
                            // Remove the ListView and SubBack button from the buttonBox
                            buttonBox.getChildren().removeAll(listView, SubBack);

                            // Add back the original buttons to the buttonBox
                            buttonBox.getChildren().addAll(
                                    addPrescription,
                                    UpdatePrescription,
                                    DeletePrescription,
                                    ShowPrescriptions,
                                    addPrescribedMedicine,
                                    showPrescribedMedicines,
                                    MainBack);
                        });
                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });

            });



            //Sales Button Configuration
            Button showSales = new Button("Show Sales");
            showSales.setPrefSize(buttonWidth, 100);
            showSales.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            showSales.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button showSoldMedicine = new Button("Show Sold Medicine");
            showSoldMedicine.setPrefSize(buttonWidth, 100);
            showSoldMedicine.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            showSoldMedicine.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            btnViewSalesReports.setOnAction(e23->{
                buttonBox.getChildren().removeAll(
                        btnManageMedicines,
                        btnManageSuppliers,
                        btnManageCustomers,
                        btnManagePrescriptions,
                        btnViewSalesReports,
                        btnManageInventory
                );
                buttonBox.getChildren().addAll(
                        showSales,
                        showSoldMedicine,
                        MainBack
                );

                MainBack.setOnAction(e1 -> {
                    buttonBox.getChildren().removeAll(
                            showSales,
                            showSoldMedicine,
                            MainBack);
                    buttonBox.getChildren().addAll(
                            btnManageMedicines,
                            btnManageSuppliers,
                            btnManageCustomers,
                            btnManagePrescriptions,
                            btnViewSalesReports,
                            btnManageInventory);
                });

                showSales.setOnAction(e24 -> {
                    // Remove existing buttons from the buttonBox
                    buttonBox.getChildren().removeAll(
                            showSales,
                            showSoldMedicine,
                            MainBack);

                    try {
                        // Fetch all sales from the database
                        ArrayList<Sale> allSales = new ArrayList<>(Sale.getAllSales(connection));

                        // Create an observable list to store sale information
                        ObservableList<String> saleInfoList = FXCollections.observableArrayList();

                        // Populate the observable list with sale information
                        for (int i = 0; i < allSales.size(); i++) {
                            Sale sale = allSales.get(i);
                            saleInfoList.add(
                                    "Sale ID: " + sale.getSaleID() + "\n" +
                                            "Customer ID: " + sale.getCustomerID() + "\n" +
                                            "Sale Date: " + sale.getSaleDate() + "\n" +
                                            "Total Amount: " + sale.getTotalAmount() + "\n" +
                                            "---------------"
                            );
                        }

                        // Create a ListView to display sale information
                        ListView<String> listView = new ListView<>(saleInfoList);
                        listView.setPrefSize(500, 600);

                        // Add the ListView and SubBack button to the buttonBox
                        buttonBox.getChildren().addAll(listView, SubBack);

                        SubBack.setOnAction(e9 -> {
                            // Remove the ListView and SubBack button from the buttonBox
                            buttonBox.getChildren().removeAll(listView, SubBack);

                            // Add back the original buttons to the buttonBox
                            buttonBox.getChildren().addAll(
                                    showSales,
                                    showSoldMedicine,
                                    MainBack);
                        });
                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });

                //Show Sold Medicines
                showSoldMedicine.setOnAction(e25 -> {
                    // Remove existing buttons from the buttonBox
                    buttonBox.getChildren().removeAll(
                            showSales,
                            showSoldMedicine,
                            MainBack);

                    try {
                        // Fetch all sold medicines from the database
                        ArrayList<SoldMedicine> allSoldMedicines = new ArrayList<>(SoldMedicine.getAllSoldMedicines(connection));

                        // Create an observable list to store sold medicine information
                        ObservableList<String> soldMedicineInfoList = FXCollections.observableArrayList();

                        // Populate the observable list with sold medicine information
                        for (int i = 0; i < allSoldMedicines.size(); i++) {
                            SoldMedicine soldMedicine = allSoldMedicines.get(i);
                            soldMedicineInfoList.add(
                                    "Sale ID: " + soldMedicine.getSaleID() + "\n" +
                                            "Medicine ID: " + soldMedicine.getMedicineID() + "\n" +
                                            "Quantity: " + soldMedicine.getQuantity() + "\n" +
                                            "Unit Price: " + soldMedicine.getUnitPrice() + "\n" +
                                            "---------------"
                            );
                        }

                        // Create a ListView to display sold medicine information
                        ListView<String> listView = new ListView<>(soldMedicineInfoList);
                        listView.setPrefSize(500, 600);

                        // Add the ListView and SubBack button to the buttonBox
                        buttonBox.getChildren().addAll(listView, SubBack);

                        SubBack.setOnAction(e9 -> {
                            // Remove the ListView and SubBack button from the buttonBox
                            buttonBox.getChildren().removeAll(listView, SubBack);

                            // Add back the original buttons to the buttonBox
                            buttonBox.getChildren().addAll(
                                    showSales,
                                    showSoldMedicine,
                                    MainBack);
                        });
                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });

            });


            //Inventory Button Configuration
            Button addInventory = new Button("Add New Item");
            addInventory.setPrefSize(buttonWidth, buttonHeight);
            addInventory.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            addInventory.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button UpdateInventory = new Button("Update Item");
            UpdateInventory.setPrefSize(buttonWidth, buttonHeight);
            UpdateInventory.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            UpdateInventory.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button DeleteInventory = new Button("Delete Item");
            DeleteInventory.setPrefSize(buttonWidth, buttonHeight);
            DeleteInventory.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            DeleteInventory.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            Button ShowInventory = new Button("Show Inventory");
            ShowInventory.setPrefSize(buttonWidth, buttonHeight);
            ShowInventory.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            ShowInventory.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white;"); // Red background with white text

            TextField inventoryID = new TextField();
            inventoryID.setPromptText("Enter Inventory ID");
            inventoryID.setMaxWidth(1000);
            inventoryID.setPrefHeight(20);
            inventoryID.setFont(new Font(30));

            TextField inMedicineID = new TextField();
            inMedicineID.setPromptText("Enter Medicine ID");
            inMedicineID.setMaxWidth(1000);
            inMedicineID.setPrefHeight(20);
            inMedicineID.setFont(new Font(30));

            TextField inPurchaseDate = new TextField();
            inPurchaseDate.setPromptText("Enter Purchase Date");
            inPurchaseDate.setMaxWidth(1000);
            inPurchaseDate.setPrefHeight(20);
            inPurchaseDate.setFont(new Font(30));

            TextField inPurchasePrice = new TextField();
            inPurchasePrice.setPromptText("Enter Purchase Price");
            inPurchasePrice.setMaxWidth(1000);
            inPurchasePrice.setPrefHeight(20);
            inPurchasePrice.setFont(new Font(30));

            TextField inQuantityReceived = new TextField();
            inQuantityReceived.setPromptText("Enter Quantity Received");
            inQuantityReceived.setMaxWidth(1000);
            inQuantityReceived.setPrefHeight(20);
            inQuantityReceived.setFont(new Font(30));

            btnManageInventory.setOnAction(e26->{
                buttonBox.getChildren().removeAll(
                        btnManageMedicines,
                        btnManageSuppliers,
                        btnManageCustomers,
                        btnManagePrescriptions,
                        btnViewSalesReports,
                        btnManageInventory);
                buttonBox.getChildren().addAll(
                        addInventory,
                        UpdateInventory,
                        DeleteInventory,
                        ShowInventory,
                        MainBack);
                MainBack.setOnAction(e1 -> {
                    buttonBox.getChildren().removeAll(
                            addInventory,
                            UpdateInventory,
                            DeleteInventory,
                            ShowInventory,
                            MainBack);
                    buttonBox.getChildren().addAll(
                            btnManageMedicines,
                            btnManageSuppliers,
                            btnManageCustomers,
                            btnManagePrescriptions,
                            btnViewSalesReports,
                            btnManageInventory);
                });

                //Add Inventory
                addInventory.setOnAction(e2 -> {
                    inMedicineID.setDisable(false);
                    inPurchaseDate.setDisable(false);
                    inPurchasePrice.setDisable(false);
                    inQuantityReceived.setDisable(false);
                    Apply.setDisable(false);

                    inventoryID.setText("");
                    inMedicineID.setText("");
                    inPurchaseDate.setText("");
                    inPurchasePrice.setText("");
                    inQuantityReceived.setText("");

                    buttonBox.getChildren().removeAll(
                            addInventory,
                            UpdateInventory,
                            DeleteInventory,
                            ShowInventory,
                            MainBack);
                    HapplybackH.getChildren().removeAll(SubBack, Apply);
                    HapplybackH.getChildren().addAll(SubBack, Apply);
                    buttonBox.getChildren().addAll(
                            inventoryID,
                            inMedicineID,
                            inPurchaseDate,
                            inPurchasePrice,
                            inQuantityReceived,
                            HapplybackH);
                    Apply.setOnAction(e3 -> {
                        try {
                            // Insert logic for inserting Inventory record into the database
                            // You need to implement the insertInventory method based on your requirements
                            Inventory.insertInventory(Integer.parseInt(inventoryID.getText()),Integer.parseInt(inMedicineID.getText()),parseDate(inPurchaseDate.getText()),Double.parseDouble(inPurchasePrice.getText()),Integer.parseInt(inQuantityReceived.getText()),connection);
                            // Clear fields after successful insertion
                            inventoryID.setText("");
                            inMedicineID.setText("");
                            inPurchaseDate.setText("");
                            inPurchasePrice.setText("");
                            inQuantityReceived.setText("");
                        } catch (Exception e100) {
                            System.out.println(e100);
                        }
                    });
                    SubBack.setOnAction(e4 -> {
                        buttonBox.getChildren().removeAll(
                                inventoryID,
                                inMedicineID,
                                inPurchaseDate,
                                inPurchasePrice,
                                inQuantityReceived,
                                HapplybackH);
                        buttonBox.getChildren().addAll(
                                addInventory,
                                UpdateInventory,
                                DeleteInventory,
                                ShowInventory,
                                MainBack);
                    });
                });

                TextField updateInventoryID = new TextField();
                updateInventoryID.setPromptText("Enter Inventory ID");
                updateInventoryID.setMaxWidth(1000);
                updateInventoryID.setPrefHeight(20);
                updateInventoryID.setFont(new Font(30));

                //Update Inventory
                UpdateInventory.setOnAction(e7 -> {
                    inMedicineID.setDisable(false);
                    inPurchaseDate.setDisable(false);
                    inPurchasePrice.setDisable(false);
                    inQuantityReceived.setDisable(false);
                    Apply.setDisable(false);

                    updateInventoryID.setText("");
                    inMedicineID.setText("");
                    inPurchaseDate.setText("");
                    inPurchasePrice.setText("");
                    inQuantityReceived.setText("");

                    try {
                        buttonBox.getChildren().removeAll(
                                addInventory,
                                UpdateInventory,
                                DeleteInventory,
                                ShowInventory,
                                MainBack);
                        HapplybackH.getChildren().removeAll(SubBack, Apply);
                        HapplybackH.getChildren().addAll(SubBack, Apply);
                        buttonBox.getChildren().addAll(
                                updateInventoryID,
                                inMedicineID,
                                inPurchaseDate,
                                inPurchasePrice,
                                inQuantityReceived,
                                HapplybackH);

                        inMedicineID.setDisable(true);
                        inPurchaseDate.setDisable(true);
                        inPurchasePrice.setDisable(true);
                        inQuantityReceived.setDisable(true);
                        Apply.setDisable(true);

                        SubBack.setOnAction(e4 -> {
                            buttonBox.getChildren().removeAll(
                                    updateInventoryID,
                                    inMedicineID,
                                    inPurchaseDate,
                                    inPurchasePrice,
                                    inQuantityReceived,
                                    HapplybackH);
                            buttonBox.getChildren().addAll(
                                    addInventory,
                                    UpdateInventory,
                                    DeleteInventory,
                                    ShowInventory,
                                    MainBack);
                        });

                        ArrayList<Inventory> allInventory = new ArrayList<>(Inventory.getAllInventory(connection));

                        updateInventoryID.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int intNewValue = Integer.parseInt(newValue);
                                boolean isAllowed = allInventory.stream().anyMatch(inventoryInfo -> inventoryInfo.getInventoryID() == intNewValue);

                                if (isAllowed) {
                                    inMedicineID.setDisable(false);
                                    inPurchaseDate.setDisable(false);
                                    inPurchasePrice.setDisable(false);
                                    inQuantityReceived.setDisable(false);
                                    Apply.setDisable(false);
                                } else {
                                    inMedicineID.setDisable(true);
                                    inPurchaseDate.setDisable(true);
                                    inPurchasePrice.setDisable(true);
                                    inQuantityReceived.setDisable(true);
                                    Apply.setDisable(true);
                                }

                                Apply.setOnAction(e3 -> {
                                    try {
                                        System.out.println("inv id=: "+intNewValue);
                                        // Update logic for updating Inventory record in the database
                                        // You need to implement the updateInventory method based on your requirements
                                        Inventory.updateInventory(intNewValue,Integer.parseInt(inMedicineID.getText()),parseDate(inPurchaseDate.getText()),Double.parseDouble(inPurchasePrice.getText()),Integer.parseInt(inQuantityReceived.getText()),connection);
                                        updateInventoryID.setText("");
                                        inMedicineID.setText("");
                                        inPurchaseDate.setText("");
                                        inPurchasePrice.setText("");
                                        inQuantityReceived.setText("");
                                    } catch (Exception e100) {
                                        System.out.println(e100);
                                    }
                                });

                            } catch (NumberFormatException e102) {
                                inMedicineID.setDisable(true);
                                inPurchaseDate.setDisable(true);
                                inPurchasePrice.setDisable(true);
                                inQuantityReceived.setDisable(true);
                                Apply.setDisable(true);
                            }
                        });

                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });

                TextField deleteInventoryID = new TextField();
                deleteInventoryID.setPromptText("Enter Inventory ID");
                deleteInventoryID.setMaxWidth(1000);
                deleteInventoryID.setPrefHeight(20);
                deleteInventoryID.setFont(new Font(30));

                //Delete Inventory
                DeleteInventory.setOnAction(e11 -> {
                    inMedicineID.setDisable(false);
                    inPurchaseDate.setDisable(false);
                    inPurchasePrice.setDisable(false);
                    inQuantityReceived.setDisable(false);
                    Apply.setDisable(false);
                    deleteInventoryID.setText("");
                    inMedicineID.setText("");
                    inPurchaseDate.setText("");
                    inPurchasePrice.setText("");
                    inQuantityReceived.setText("");

                    try {
                        buttonBox.getChildren().removeAll(
                                addInventory,
                                UpdateInventory,
                                DeleteInventory,
                                ShowInventory,
                                MainBack);
                        HapplybackH.getChildren().removeAll(SubBack, Apply);
                        HapplybackH.getChildren().addAll(SubBack, Apply);
                        buttonBox.getChildren().addAll(deleteInventoryID, HapplybackH);
                        Apply.setDisable(true);

                        SubBack.setOnAction(e4 -> {
                            buttonBox.getChildren().removeAll(
                                    deleteInventoryID,
                                    HapplybackH);
                            buttonBox.getChildren().addAll(
                                    addInventory,
                                    UpdateInventory,
                                    DeleteInventory,
                                    ShowInventory,
                                    MainBack);
                        });

                        ArrayList<Inventory> allInventory = new ArrayList<>(Inventory.getAllInventory(connection));

                        deleteInventoryID.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int intNewValue = Integer.parseInt(newValue);
                                boolean isAllowed = allInventory.stream().anyMatch(inventoryInfo -> inventoryInfo.getInventoryID() == intNewValue);

                                if (isAllowed) {
                                    Apply.setDisable(false);
                                } else {
                                    Apply.setDisable(true);
                                }

                                Apply.setOnAction(e3 -> {
                                    try {
                                        // Implement the logic for deleting an Inventory record
                                        Inventory.deleteInventory(intNewValue,connection);
                                        deleteInventoryID.setText("");
                                        inMedicineID.setText("");
                                        inPurchaseDate.setText("");
                                        inPurchasePrice.setText("");
                                        inQuantityReceived.setText("");
                                    } catch (Exception e100) {
                                        System.out.println("aha"+e100);
                                    }
                                });

                            } catch (NumberFormatException e102) {
                                Apply.setDisable(true);
                            }
                        });

                    } catch (Exception e100) {
                        System.out.println(e100);
                    }

                });

                //Show Inventory
                ShowInventory.setOnAction(e10 -> {
                    buttonBox.getChildren().removeAll(
                            addInventory,
                            UpdateInventory,
                            DeleteInventory,
                            ShowInventory,
                            MainBack);
                    try {
                        ArrayList<Inventory> allInventory = new ArrayList<>(Inventory.getAllInventory(connection));
                        ObservableList<String> inventoryInfoList = FXCollections.observableArrayList();
                        for (int i = 0; i < allInventory.size(); i++) {
                            Inventory inventory = allInventory.get(i);
                            // Append inventory information to the list
                            inventoryInfoList.add(
                                    "Inventory ID: " + inventory.getInventoryID() + "\n" +
                                            "Medicine ID: " + inventory.getMedicineID() + "\n" +
                                            "Purchase Date: " + inventory.getPurchaseDate() + "\n" +
                                            "Purchase Price: " + inventory.getPurchasePrice() + "\n" +
                                            "Quantity Received: " + inventory.getQuantityReceived() + "\n" +
                                            "---------------"
                            );
                        }
                        // Create a ListView to display inventory information
                        ListView<String> listView = new ListView<>(inventoryInfoList);
                        listView.setPrefSize(500, 600);
                        buttonBox.getChildren().add(listView);
                        buttonBox.getChildren().add(SubBack);

                        SubBack.setOnAction(e9 -> {
                            buttonBox.getChildren().removeAll(listView, SubBack);
                            buttonBox.getChildren().addAll(
                                    addInventory,
                                    UpdateInventory,
                                    DeleteInventory,
                                    ShowInventory,
                                    MainBack);
                        });

                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });


            });



            //Done
            //SignOut Button
            signoutButton.setOnAction(e -> {
                adminStage.close();
                start2();
            });
            //add image and vbox into stack pane
            GridPane gridpane = new GridPane();
            gridpane.setPadding(new Insets(10, 10, 10, 10));
            gridpane.setHgap(140);
            gridpane.add(SignOutVbox,0,6);
            gridpane.add(buttonBox,6,0);
            StackPane pane2 = new StackPane(imageView2, WelcomeAdminVbox, gridpane);

            //add stack pane into scene
            // Create the scene with the VBox
            Scene scene = new Scene(pane2, 300, 200);

            // Set the title of the window
            adminStage.setTitle("Admin Page");


            // Set the scene for the stage
            adminStage.setScene(scene);

            // Set the stage to full-screen mode
            adminStage.setMaximized(true);

            // Show the stage
            adminStage.show();
        }

        catch (Exception e100){
            System.out.println(e100);
        }
    }


    //Normal Customer Interface
    private void openUserDashboardStage(String userId) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "9177AdamShareef7719");

            Stage userStage = new Stage();

            Image image3 = new Image("D:\\JavaFiles\\DataBaseFx\\src\\main\\java\\org\\example\\databasefx\\UserPage.jpg");
            // Setting the image view
            ImageView imageView3 = new ImageView(image3);
            imageView3.setFitWidth(1600);
            imageView3.setFitHeight(800);

            // Create buttons
            Button btnBrowseMedicines = new Button("Browse Medicines");
            Button btnViewPrescriptionHistory = new Button("View Prescription History");
            Button btnPlaceOrders = new Button("Place Orders");
            Button btnViewSalesHistory = new Button("View Sales History");
            Button btnProfileManagement = new Button("Profile Management");

            // Set preferred size for buttons
            double buttonWidth1 = 550;
            double buttonHeight1 = 200;
            btnBrowseMedicines.setPrefSize(buttonWidth1, buttonHeight1);
            btnViewPrescriptionHistory.setPrefSize(buttonWidth1, buttonHeight1);
            btnPlaceOrders.setPrefSize(buttonWidth1, buttonHeight1);
            btnViewSalesHistory.setPrefSize(buttonWidth1, buttonHeight1);
            btnProfileManagement.setPrefSize(buttonWidth1, buttonHeight1);

            //Font Config
            btnBrowseMedicines.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnViewPrescriptionHistory.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnPlaceOrders.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnViewSalesHistory.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            btnProfileManagement.setFont(Font.font("Arial", FontWeight.BOLD, 40));

            //Color Config
            btnBrowseMedicines.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");
            btnViewPrescriptionHistory.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");
            btnPlaceOrders.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");
            btnViewSalesHistory.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");
            btnProfileManagement.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");

            // Create a VBox (Vertical Box) for buttons
            VBox buttonBox2 = new VBox(40); // 40 is the spacing between buttons
            buttonBox2.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the center right

            // Add buttons to the VBox
            buttonBox2.getChildren().addAll(
                    btnBrowseMedicines,
                    btnViewPrescriptionHistory,
                    btnPlaceOrders,
                    btnViewSalesHistory,
                    btnProfileManagement
            );

            // Create a label
            Label welcomeLabel3 = new Label("     Welcome");
            welcomeLabel3.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 100));
            welcomeLabel3.setTextFill(Color.BLACK);

            //Print Customer name in the page
            ArrayList<Customer> allCustomers = new ArrayList<>(Customer.getAllCustomers(connection));
            String customer_name=null;
            for (int i = 0; i < allCustomers.size(); i++) {
                Customer customer = allCustomers.get(i);
                if (userId.equals(String.valueOf(customer.getCustomerID()))) {
                    customer_name = customer.getFirstName();
                }
            }

            Label welcomeLabel4 = new Label("      "+customer_name);
            welcomeLabel4.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 100));
            welcomeLabel4.setTextFill(Color.BLACK);
            welcomeLabel4.setAlignment(Pos.CENTER);

            VBox welcomeUserVbox = new VBox(10);
            welcomeUserVbox.setAlignment(Pos.CENTER_LEFT);
            welcomeUserVbox.getChildren().addAll(welcomeLabel3, welcomeLabel4);

            Button signoutButton2 = new Button("Sign Out");
            signoutButton2.setPrefSize(150, 30);
            signoutButton2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            signoutButton2.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;"); // Red background with white text

            VBox SignOutVbox2 = new VBox(10);
            SignOutVbox2.setAlignment(Pos.BOTTOM_LEFT);
            SignOutVbox2.getChildren().add(signoutButton2);


            Button Apply = new Button("Apply");
            Button MainBack = new Button("Back");
            Button SubBack = new Button("Back");
            Button SubBack2 = new Button("Back");


            Apply.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            MainBack.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            SubBack.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            SubBack2.setFont(Font.font("Arial", FontWeight.BOLD, 20));


            Apply.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");
            MainBack.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");
            SubBack.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");
            SubBack2.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");


            Button viewMedicineButton = new Button("View Medicines");
            viewMedicineButton.setPrefSize(550, 150);
            viewMedicineButton.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            viewMedicineButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");

            Button searchMedicineButton = new Button("Search For Medicines");
            searchMedicineButton.setPrefSize(550, 150);
            searchMedicineButton.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            searchMedicineButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");

            HBox HapplybackH = new HBox(240);
            HapplybackH.setAlignment(Pos.BOTTOM_LEFT);
            HapplybackH.getChildren().addAll(MainBack,Apply);

            HBox HapplybackH2 = new HBox(240);
            HapplybackH2.setAlignment(Pos.BOTTOM_LEFT);
            HapplybackH2.getChildren().addAll(SubBack,Apply);

            HBox HapplybackH3 = new HBox(240);
            HapplybackH3.setAlignment(Pos.BOTTOM_LEFT);
            HapplybackH3.getChildren().addAll(SubBack2,Apply);


            //Medicine Button Config
            btnBrowseMedicines.setOnAction(e1->{
                buttonBox2.getChildren().removeAll(
                        btnBrowseMedicines,
                        btnPlaceOrders,
                        btnViewPrescriptionHistory,
                        btnViewSalesHistory,
                        btnProfileManagement
                );
                buttonBox2.getChildren().addAll(
                        viewMedicineButton,
                        searchMedicineButton,
                        MainBack
                );
                MainBack.setOnAction(e3->{
                    buttonBox2.getChildren().removeAll(
                            viewMedicineButton,
                            searchMedicineButton,
                            MainBack
                    );
                    buttonBox2.getChildren().addAll(
                            btnBrowseMedicines,
                            btnPlaceOrders,
                            btnViewPrescriptionHistory,
                            btnViewSalesHistory,
                            btnProfileManagement
                    );
                });

                viewMedicineButton.setOnAction(e2->{
                    buttonBox2.getChildren().removeAll(
                            viewMedicineButton,
                            searchMedicineButton,
                            MainBack
                    );
                    try {
                        ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));
                        ObservableList<String> medicineInfoList = FXCollections.observableArrayList();
                        for (int i = 0; i < allMedicines.size(); i++) {
                            Medicine medicine = allMedicines.get(i);
                            // Append medicine information to the list
                            medicineInfoList.add(
                                    "Medicine ID: " + medicine.getMedicineID() + "\n" +
                                            "Name: " + medicine.getName() + "\n" +
                                            "Manufacturer: " + medicine.getManufacturer() + "\n" +
                                            "Expiry Date: " + medicine.getExpiryDate() + "\n" +
                                            "Unit Price: " + medicine.getUnitPrice() + "\n" +
                                            "Quantity in Stock: " + medicine.getQuantityInStock() + "\n" +
                                            "---------------"
                            );
                        }
                        // Create a ListView to display medicine information
                        ListView<String> listView = new ListView<>(medicineInfoList);
                        listView.setPrefSize(500,600);
                        buttonBox2.getChildren().add(listView);
                        buttonBox2.getChildren().add(SubBack);

                        SubBack.setOnAction(e3->{
                            buttonBox2.getChildren().removeAll(listView,SubBack);
                            buttonBox2.getChildren().addAll(
                                    viewMedicineButton,
                                    searchMedicineButton,
                                    MainBack );

                        });

                    }
                    catch (Exception e100){
                        System.out.println(e100);
                    }
                });

                Button searchMedicineByID = new Button("Search By ID");
                searchMedicineByID.setPrefSize(550, 150);
                searchMedicineByID.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                searchMedicineByID.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");

                Button searchMedicineByName = new Button("Search By Name");
                searchMedicineByName.setPrefSize(550, 150);
                searchMedicineByName.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                searchMedicineByName.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");

                Button searchMedicineByManufacture = new Button("Search By Manufacture");
                searchMedicineByManufacture.setPrefSize(550, 150);
                searchMedicineByManufacture.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                searchMedicineByManufacture.setStyle("-fx-background-color: #4682B4; -fx-text-fill: gold;");


                TextField medicineIDTextField = new TextField();
                medicineIDTextField.setPromptText("Enter Supplier ID");
                medicineIDTextField.setMaxWidth(1000);
                medicineIDTextField.setPrefHeight(20);
                medicineIDTextField.setFont(new Font(30));

                searchMedicineButton.setOnAction(e4->{
                    Apply.setDisable(false);
                    buttonBox2.getChildren().removeAll(
                            viewMedicineButton,
                            searchMedicineButton,
                            MainBack
                    );
                    buttonBox2.getChildren().addAll(
                            searchMedicineByID,
                            searchMedicineByName,
                            searchMedicineByManufacture,
                            SubBack
                    );
                    SubBack.setOnAction(e5->{
                        buttonBox2.getChildren().removeAll(
                                searchMedicineByID,
                                searchMedicineByName,
                                searchMedicineByManufacture,
                                SubBack
                        );
                        buttonBox2.getChildren().addAll(
                                viewMedicineButton,
                                searchMedicineButton,
                                MainBack
                        );
                    });

                    searchMedicineByID.setOnAction(e6->{
                        buttonBox2.getChildren().removeAll(
                                searchMedicineByID,
                                searchMedicineByName,
                                searchMedicineByManufacture,
                                SubBack
                        );

                        HapplybackH3.getChildren().removeAll(SubBack2, Apply);
                        HapplybackH3.getChildren().addAll(SubBack2, Apply);
                        ListView<String> resultListView = new ListView<>();
                        resultListView.setPrefSize(500,600);
                        // Get the entered Medicine ID from a TextField (assuming you have a TextField for input)
                        buttonBox2.getChildren().addAll(medicineIDTextField,HapplybackH3);
                        Apply.setOnAction(e7->{
                            try {
                                ObservableList<String> searchResults = FXCollections.observableArrayList();

                                int medicineID = Integer.parseInt(medicineIDTextField.getText());

                                // Call the search by ID method
                                Medicine resultMedicine = Medicine.getMedicineByID(medicineID, connection);

                                // Clear previous search results
                                searchResults.clear();

                                // Display the result in the ObservableList
                                if (resultMedicine != null) {
                                    searchResults.add("Medicine ID: " + resultMedicine.getMedicineID() + "\n" +
                                            "Name: " + resultMedicine.getName() + "\n" +
                                            "Manufacturer: " + resultMedicine.getManufacturer() + "\n" +
                                            "Expiry Date: " + resultMedicine.getExpiryDate() + "\n" +
                                            "Unit Price: " + resultMedicine.getUnitPrice() + "\n" +
                                            "Quantity in Stock: " + resultMedicine.getQuantityInStock() + "\n" +
                                            "____________________________________________");
                                } else {
                                    searchResults.add("Medicine not found");
                                }
                                resultListView.setItems(searchResults);
                                HapplybackH3.getChildren().removeAll(SubBack2,Apply);
                                HapplybackH3.getChildren().addAll(SubBack2,Apply);
                                buttonBox2.getChildren().removeAll(resultListView,HapplybackH3);
                                buttonBox2.getChildren().addAll(resultListView ,HapplybackH3);
                                medicineIDTextField.setText("");
                            } catch (NumberFormatException | SQLException ex) {
                                System.out.println("Error: " + ex.getMessage());
                                HapplybackH3.getChildren().removeAll(SubBack2,Apply);
                                HapplybackH3.getChildren().addAll(SubBack2,Apply);
                                buttonBox2.getChildren().removeAll(resultListView,HapplybackH3);
                                buttonBox2.getChildren().addAll(resultListView ,HapplybackH3);
                                medicineIDTextField.setText("");
                            }
                        });
                        SubBack2.setOnAction(e8->{
                            buttonBox2.getChildren().removeAll(
                                    medicineIDTextField,
                                    resultListView,
                                    HapplybackH3
                            );
                            buttonBox2.getChildren().addAll(
                                    searchMedicineByID,
                                    searchMedicineByName,
                                    searchMedicineByManufacture,
                                    SubBack
                            );
                        });

                        //buttonBox2.getChildren().addAll(SubBack2);

                    });

                    TextField medicineNameTextField = new TextField();
                    medicineNameTextField.setPromptText("Enter Medicine Name");
                    medicineNameTextField.setMaxWidth(1000);
                    medicineNameTextField.setPrefHeight(20);
                    medicineNameTextField.setFont(new Font(30));

                    searchMedicineByName.setOnAction(e6 -> {
                        buttonBox2.getChildren().removeAll(
                                searchMedicineByID,
                                searchMedicineByName,
                                searchMedicineByManufacture,
                                SubBack
                        );

                        HapplybackH3.getChildren().removeAll(SubBack2, Apply);
                        HapplybackH3.getChildren().addAll(SubBack2, Apply);
                        ListView<String> resultListView = new ListView<>();
                        resultListView.setPrefSize(500,600);
                        // Get the entered Medicine Name from a TextField (assuming you have a TextField for input)
                        buttonBox2.getChildren().addAll(medicineNameTextField, HapplybackH3);
                        Apply.setOnAction(e7 -> {
                            try {
                                ObservableList<String> searchResults = FXCollections.observableArrayList();

                                String medicineName = medicineNameTextField.getText();

                                // Call the search by Name method
                                ArrayList<Medicine> resultMedicines = new ArrayList<>(Medicine.getMedicineByName(medicineName,connection));

                              //  ArrayList<Medicine> resultMedicines = Medicine.getMedicineByName(medicineName, connection);

                                // Clear previous search results
                                searchResults.clear();

                                if(medicineName.equals(""))
                                    searchResults.add("Medicine not found");
                                // Display the result in the ObservableList
                                else if (!resultMedicines.isEmpty()) {
                                    for (Medicine resultMedicine : resultMedicines) {
                                        searchResults.add("Medicine ID: " + resultMedicine.getMedicineID() + "\n" +
                                                "Name: " + resultMedicine.getName() + "\n" +
                                                "Manufacturer: " + resultMedicine.getManufacturer() + "\n" +
                                                "Expiry Date: " + resultMedicine.getExpiryDate() + "\n" +
                                                "Unit Price: " + resultMedicine.getUnitPrice() + "\n" +
                                                "Quantity in Stock: " + resultMedicine.getQuantityInStock() + "\n" +
                                                "____________________________________________");
                                    }

                                } else {
                                    searchResults.add("Medicine not found");
                                }
                                resultListView.setItems(searchResults);
                                HapplybackH3.getChildren().removeAll(SubBack2,Apply);
                                HapplybackH3.getChildren().addAll(SubBack2,Apply);
                                buttonBox2.getChildren().removeAll(resultListView,HapplybackH3);
                                buttonBox2.getChildren().addAll(resultListView ,HapplybackH3);
                                medicineNameTextField.setText("");
                            } catch (SQLException ex) {
                                System.out.println("Error: " + ex.getMessage());
                                HapplybackH3.getChildren().removeAll(SubBack2,Apply);
                                HapplybackH3.getChildren().addAll(SubBack2,Apply);
                                buttonBox2.getChildren().removeAll(resultListView,HapplybackH3);
                                buttonBox2.getChildren().addAll(resultListView, HapplybackH3);
                                medicineNameTextField.setText("");
                            }

                        });
                        SubBack2.setOnAction(e8 -> {
                            buttonBox2.getChildren().removeAll(
                                    medicineNameTextField,
                                    resultListView,
                                    HapplybackH3
                            );
                            buttonBox2.getChildren().addAll(
                                    searchMedicineByID,
                                    searchMedicineByName,
                                    searchMedicineByManufacture,
                                    SubBack
                            );
                        });

                        //buttonBox2.getChildren().addAll(SubBack2);
                    });

                    TextField medicineManufacturerTextField = new TextField();
                    medicineManufacturerTextField.setPromptText("Enter Manufacturer");
                    medicineManufacturerTextField.setMaxWidth(1000);
                    medicineManufacturerTextField.setPrefHeight(20);
                    medicineManufacturerTextField.setFont(new Font(30));

                    searchMedicineByManufacture.setOnAction(e9 -> {
                        buttonBox2.getChildren().removeAll(
                                searchMedicineByID,
                                searchMedicineByName,
                                searchMedicineByManufacture,
                                SubBack
                        );

                        HapplybackH3.getChildren().removeAll(SubBack2, Apply);
                        HapplybackH3.getChildren().addAll(SubBack2, Apply);
                        ListView<String> resultListView = new ListView<>();
                        resultListView.setPrefSize(500,600);
                        // Get the entered Medicine Manufacturer from a TextField (assuming you have a TextField for input)
                        buttonBox2.getChildren().addAll(medicineManufacturerTextField, HapplybackH3);
                        Apply.setOnAction(e10 -> {
                            try {
                                ObservableList<String> searchResults = FXCollections.observableArrayList();

                                String medicineManufacturer = medicineManufacturerTextField.getText();

                                // Call the search by Manufacturer method
                                ArrayList<Medicine> resultMedicines = new ArrayList<>(Medicine.getMedicineByManufacturer(medicineManufacturer, connection));

                                // Clear previous search results
                                searchResults.clear();

                                if (medicineManufacturer.equals(""))
                                    searchResults.add("Medicine not found");
                                    // Display the result in the ObservableList
                                else if (!resultMedicines.isEmpty()) {
                                    for (Medicine resultMedicine : resultMedicines) {
                                        searchResults.add("Medicine ID: " + resultMedicine.getMedicineID() + "\n" +
                                                "Name: " + resultMedicine.getName() + "\n" +
                                                "Manufacturer: " + resultMedicine.getManufacturer() + "\n" +
                                                "Expiry Date: " + resultMedicine.getExpiryDate() + "\n" +
                                                "Unit Price: " + resultMedicine.getUnitPrice() + "\n" +
                                                "Quantity in Stock: " + resultMedicine.getQuantityInStock() + "\n" +
                                                "____________________________________________");
                                    }

                                } else {
                                    searchResults.add("Medicine not found");
                                }
                                resultListView.setItems(searchResults);
                                HapplybackH3.getChildren().removeAll(SubBack2, Apply);
                                HapplybackH3.getChildren().addAll(SubBack2, Apply);
                                buttonBox2.getChildren().removeAll(resultListView, HapplybackH3);
                                buttonBox2.getChildren().addAll(resultListView, HapplybackH3);
                                medicineManufacturerTextField.setText("");
                            } catch (SQLException ex) {
                                System.out.println("Error: " + ex.getMessage());
                                HapplybackH3.getChildren().removeAll(SubBack2, Apply);
                                HapplybackH3.getChildren().addAll(SubBack2, Apply);
                                buttonBox2.getChildren().removeAll(resultListView, HapplybackH3);
                                buttonBox2.getChildren().addAll(resultListView, HapplybackH3);
                                medicineManufacturerTextField.setText("");
                            }

                        });
                        SubBack2.setOnAction(e11 -> {
                            buttonBox2.getChildren().removeAll(
                                    medicineManufacturerTextField,
                                    resultListView,
                                    HapplybackH3
                            );
                            buttonBox2.getChildren().addAll(
                                    searchMedicineByID,
                                    searchMedicineByName,
                                    searchMedicineByManufacture,
                                    SubBack
                            );
                        });

                        //buttonBox2.getChildren().addAll(SubBack2);
                    });



                });

            });



            //Prescription History Button
            btnViewPrescriptionHistory.setOnAction(e9 -> {
                buttonBox2.getChildren().removeAll(
                        btnBrowseMedicines,
                        btnViewPrescriptionHistory,
                        btnPlaceOrders,
                        btnViewSalesHistory,
                        btnProfileManagement
                );



                    ListView<String> resultListView = new ListView<>();
                    resultListView.setPrefSize(500,600);


                        try {

                            ObservableList<String> searchResults = FXCollections.observableArrayList();

                            // Call the search by Manufacturer method
                            ArrayList<Prescription> resultPrescriptions = new ArrayList<>(Prescription.getPrescriptionsByCustomer(Integer.parseInt(userId), connection));

                            // Clear previous search results
                            searchResults.clear();


                            // Display the result in the ObservableList
                            if (!resultPrescriptions.isEmpty()) {
                                for (Prescription resultPrescription : resultPrescriptions) {
                                    searchResults.add("Prescription ID: " + resultPrescription.getPrescriptionID() +"\n"+
                                            "Customer ID: " + resultPrescription.getCustomerID() +"\n"+
                                            "Doctor Name: " + resultPrescription.getDoctorName() +"\n"+
                                            "Issue Date: " + resultPrescription.getIssueDate()+"\n"+
                                            "____________________________________________");
                                }
                            } else {
                                searchResults.add("Prescription not found");
                            }


                            resultListView.setItems(searchResults);
                            buttonBox2.getChildren().removeAll(resultListView, MainBack);
                            buttonBox2.getChildren().addAll(resultListView, MainBack);
                        } catch (NumberFormatException | SQLException ex) {
                            System.out.println("Error: " + ex.getMessage());
                            buttonBox2.getChildren().removeAll(resultListView, MainBack);
                            buttonBox2.getChildren().addAll(resultListView, MainBack);
                        }
                MainBack.setOnAction(e10 -> {
                    buttonBox2.getChildren().removeAll(
                            resultListView,
                            MainBack
                    );
                    buttonBox2.getChildren().addAll(
                            btnBrowseMedicines,
                            btnViewPrescriptionHistory,
                            btnPlaceOrders,
                            btnViewSalesHistory,
                            btnProfileManagement
                    );
                });
            });


            btnPlaceOrders.setOnAction(e12->{

            });



            //Sales Button
            btnViewSalesHistory.setOnAction(e11 -> {
                buttonBox2.getChildren().removeAll(
                        btnBrowseMedicines,
                        btnViewPrescriptionHistory,
                        btnPlaceOrders,
                        btnViewSalesHistory,
                        btnProfileManagement
                );

                ListView<String> resultListView = new ListView<>();
                resultListView.setPrefSize(500,600);

                try {
                    ObservableList<String> searchResults = FXCollections.observableArrayList();

                    // Call the method to get sales history
                    ArrayList<Sale> resultSales = new ArrayList<>(Sale.getSalesByCustomer(Integer.parseInt(userId), connection));

                    // Clear previous search results
                    searchResults.clear();

                    // Display the result in the ObservableList
                    if (!resultSales.isEmpty()) {
                        for (Sale resultSale : resultSales) {
                            searchResults.add("Sale ID: " + resultSale.getSaleID() + "\n" );
                            ArrayList<SoldMedicine> resultSoldMedicines = SoldMedicine.getSoldMedicinesBySaleID(resultSale.getSaleID(), connection);
                            if (!resultSoldMedicines.isEmpty()) {
                                for (SoldMedicine resultSoldMedicine : resultSoldMedicines) {
                                    searchResults.add(
                                            "Medicine ID: " + resultSoldMedicine.getMedicineID() + "\n" +
                                            "Quantity: " + resultSoldMedicine.getQuantity() + "\n" +
                                            "Unit Price: " + resultSoldMedicine.getUnitPrice() + "\n" +
                                            "____________________________________________");
                                }
                            } else {
                                searchResults.add("No sold medicines found for this sale");
                            }
                            searchResults.add(
                                    "Sale Date: " + resultSale.getSaleDate() + "\n" +
                                    "Total Amount: " + resultSale.getTotalAmount() + "\n" +
                                    "____________________________________________");
                        }
                    } else {
                        searchResults.add("Sales history not found");
                    }

                    resultListView.setItems(searchResults);
                    buttonBox2.getChildren().removeAll(resultListView, MainBack);
                    buttonBox2.getChildren().addAll(resultListView, MainBack);
                } catch (NumberFormatException | SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                    buttonBox2.getChildren().removeAll(resultListView, MainBack);
                    buttonBox2.getChildren().addAll(resultListView, MainBack);
                }

                MainBack.setOnAction(e10 -> {
                    buttonBox2.getChildren().removeAll(
                            resultListView,
                            MainBack
                    );
                    buttonBox2.getChildren().addAll(
                            btnBrowseMedicines,
                            btnViewPrescriptionHistory,
                            btnPlaceOrders,
                            btnViewSalesHistory,
                            btnProfileManagement
                    );
                });
            });


            //New text field for place order
            TextField cid = new TextField();
            TextField cfirstName = new TextField();
            TextField clastName = new TextField();
            TextField caddress = new TextField();
            TextField ccontactNumber = new TextField();
            TextField cemail = new TextField();

            cid.setPromptText("Enter Customer ID");
            cid.setMaxWidth(1000);
            cid.setPrefHeight(20);
            cid.setFont(new Font(30));

            cfirstName.setPromptText("Enter First Name");
            cfirstName.setMaxWidth(1000);
            cfirstName.setPrefHeight(20);
            cfirstName.setFont(new Font(30));

            clastName.setPromptText("Enter Last Name");
            clastName.setMaxWidth(1000);
            clastName.setPrefHeight(20);
            clastName.setFont(new Font(30));

            caddress.setPromptText("Enter Address");
            caddress.setMaxWidth(1000);
            caddress.setPrefHeight(20);
            caddress.setFont(new Font(30));

            ccontactNumber.setPromptText("Enter Contact Number");
            ccontactNumber.setMaxWidth(1000);
            ccontactNumber.setPrefHeight(20);
            ccontactNumber.setFont(new Font(30));

            cemail.setPromptText("Enter Email");
            cemail.setMaxWidth(1000);
            cemail.setPrefHeight(20);
            cemail.setFont(new Font(30));


            //Manage Profile Button
            btnProfileManagement.setOnAction(e15 -> {

                cfirstName.setText("");
                clastName.setText("");
                caddress.setText("");
                ccontactNumber.setText("");
                cemail.setText("");

                try {
                    buttonBox2.getChildren().removeAll(
                            btnBrowseMedicines,
                            btnViewPrescriptionHistory,
                            btnPlaceOrders,
                            btnViewSalesHistory,
                            btnProfileManagement
                            );
                    HapplybackH.getChildren().removeAll(MainBack, Apply);
                    HapplybackH.getChildren().addAll(MainBack, Apply);
                    buttonBox2.getChildren().addAll(
                            cfirstName,
                            clastName,
                            caddress,
                            ccontactNumber,
                            cemail,
                            HapplybackH);

                    MainBack.setOnAction(e4 -> {
                        buttonBox2.getChildren().removeAll(
                                cfirstName,
                                clastName,
                                caddress,
                                ccontactNumber,
                                cemail,
                                HapplybackH);
                        buttonBox2.getChildren().addAll(
                                btnBrowseMedicines,
                                btnViewPrescriptionHistory,
                                btnPlaceOrders,
                                btnViewSalesHistory,
                                btnProfileManagement);
                    });

                            Apply.setOnAction(e3 -> {
                                try {
                                    Customer.updateCustomer(
                                            Integer.parseInt(userId),
                                            cfirstName.getText(),
                                            clastName.getText(),
                                            caddress.getText(),
                                            ccontactNumber.getText(),
                                            cemail.getText(),
                                            connection);
                                    cfirstName.setText("");
                                    clastName.setText("");
                                    caddress.setText("");
                                    ccontactNumber.setText("");
                                    cemail.setText("");
                                } catch (Exception e100) {
                                    System.out.println(e100);
                                }
                            });

                } catch (Exception e100) {
                    System.out.println(e100);
                }
            });




            TextField medID = new TextField();
            medID.setPromptText("Enter Medicine ID");
            medID.setMaxWidth(1000);
            medID.setPrefHeight(20);
            medID.setFont(new Font(30));

            TextField Quantity = new TextField();
            Quantity.setPromptText("Enter The Quantity");
            Quantity.setMaxWidth(1000);
            Quantity.setPrefHeight(20);
            Quantity.setFont(new Font(30));

            double[] totalResult = {0.0}; // Using an array to make it effectively final


            //Config for place order button
            btnPlaceOrders.setOnAction(e25->{
                    medID.setDisable(false);
                    Quantity.setDisable(false);
                    Apply.setDisable(false);
                    medID.setText("");
                    Quantity.setText("");

                    try {
                        buttonBox2.getChildren().removeAll(
                                btnBrowseMedicines,
                                btnViewPrescriptionHistory,
                                btnPlaceOrders,
                                btnViewSalesHistory,
                                btnProfileManagement);
                        HapplybackH.getChildren().removeAll(MainBack, Apply);
                        HapplybackH.getChildren().addAll(MainBack, Apply);
                        buttonBox2.getChildren().addAll(
                                medID,
                                Quantity,
                                HapplybackH);
                        Apply.setDisable(true);
                        Quantity.setDisable(true);

                        MainBack.setOnAction(e4->{
                            buttonBox2.getChildren().removeAll(
                                    medID,
                                    Quantity,
                                    HapplybackH);
                            buttonBox2.getChildren().addAll(
                                    btnBrowseMedicines,
                                    btnViewPrescriptionHistory,
                                    btnPlaceOrders,
                                    btnViewSalesHistory,
                                    btnProfileManagement);
                        });

                        ArrayList<Medicine> allMedicines = new ArrayList<>(Medicine.getAllMedicines(connection));

                        medID.textProperty().addListener((observable, oldValue, newValue) -> {
                            try {
                                int intnewValue = Integer.parseInt(newValue);
                                boolean isAllowed = allMedicines.stream().anyMatch(medicineInfo -> medicineInfo.getMedicineID() == intnewValue);

                                if (isAllowed) {
                                    Quantity.setDisable(false);
                                    Apply.setDisable(false);
                                } else {
                                    Quantity.setDisable(true);
                                    Apply.setDisable(true);
                                }
                                Apply.setOnAction(e23->{
                                    try {
                                        double medPrice=0;
                                        int CurrentQuantity=0;
                                        int newId=0;
                                        int flag=0;
                                        ArrayList<Sale> allSales = Sale.getAllSales(connection);
                                        Sale lastSale = allSales.get(allSales.size() - 1);
                                        ArrayList<Medicine> allMedicines2 = new ArrayList<>(Medicine.getAllMedicines(connection));
                                        for (int i = 0; i < allMedicines2.size(); i++) {
                                            Medicine medicine = allMedicines2.get(i);
                                            if(Integer.parseInt(medID.getText())==medicine.getMedicineID()){
                                                System.out.println("Aha");
                                                medPrice = medicine.getUnitPrice();
                                                CurrentQuantity = medicine.getQuantityInStock();

                                                Date currentDate = new Date();
                                                if(CurrentQuantity>=Integer.parseInt(Quantity.getText()) && medicine.getExpiryDate().compareTo(currentDate)>0){
                                                    medicine.decrementQuantity(Integer.parseInt(medID.getText()),connection);
                                                    flag = 1;
                                                }
                                            }
                                        }
                                        if(CurrentQuantity>=Integer.parseInt(Quantity.getText()) && flag==1) {
                                            double price = medPrice * Integer.parseInt(Quantity.getText());
                                            newId = lastSale.getSaleID() + 1;
                                            SoldMedicine.insertSoldMedicine(newId, Integer.parseInt(medID.getText()), Integer.parseInt(Quantity.getText()), medPrice, connection);
                                            totalResult[0] += price;
                                            medID.setText("");
                                            Quantity.setText("");
                                            flag=0;
                                        }
                                        else
                                            System.out.println("a");

                                    } catch (Exception e100) {
                                        System.out.println(e100);
                                    }

                                });

                                MainBack.setOnAction(e26->{
                                    try {
                                        int newId = 0;
                                        ArrayList<Sale> allSales = Sale.getAllSales(connection);
                                        Sale lastSale = allSales.get(allSales.size() - 1);
                                        newId= lastSale.getSaleID()+1;
                                        Sale.insertSale(newId,Integer.parseInt(userId),new Date(),totalResult[0],connection);
                                        totalResult[0]=0;

                                        buttonBox2.getChildren().removeAll(
                                                medID,
                                                Quantity,
                                                HapplybackH);
                                        buttonBox2.getChildren().addAll(
                                                btnBrowseMedicines,
                                                btnViewPrescriptionHistory,
                                                btnPlaceOrders,
                                                btnViewSalesHistory,
                                                btnProfileManagement);

                                    }
                                    catch (Exception e100) {
                                        System.out.println(e100);
                                    }
                                });

                            } catch (NumberFormatException e102) {
                                // Handle the case where newValue is not a valid integer
                                Quantity.setDisable(true);
                                Apply.setDisable(true);
                            }
                        });
                    } catch (Exception e100) {
                        System.out.println(e100);
                    }
                });


                signoutButton2.setOnAction(e -> {
                userStage.close();
                start2();
            });

            //add image and vbox into stack pane
            GridPane gridpane2 = new GridPane();
            gridpane2.setPadding(new Insets(10, 10, 10, 10));
            gridpane2.setHgap(140);
            gridpane2.add(SignOutVbox2,0,6);
            gridpane2.add(buttonBox2,6,0);
            StackPane pane3 = new StackPane(imageView3, welcomeUserVbox, gridpane2);


            // Create the scene with the VBox
            Scene scene = new Scene(pane3, 300, 200);

            // Set the title of the window
            userStage.setTitle("Customer Page");

            // Set the scene for the stage
            userStage.setScene(scene);

            // Set the stage to full-screen mode
            userStage.setMaximized(true);

            // Show the stage
            userStage.show();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    //Check Date Validity Button
    public static Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the pattern accordingly
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e); // Print the exception details for debugging purposes
            return null; // Or throw an exception or return a default value
        }
    }
}