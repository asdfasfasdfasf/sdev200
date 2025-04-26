import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/database";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    
    private TextField tfId = new TextField();
    private TextField tfLastName = new TextField();
    private TextField tfFirstName = new TextField();
    private TextField tfMi = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfCity = new TextField();
    private TextField tfState = new TextField();
    private TextField tfTelephone = new TextField();
    private TextField tfEmail = new TextField();
    
    private Label lblStatus = new Label();
    
    private Connection connection;
    
    @Override
    public void start(Stage primaryStage) {
        initializeUI(primaryStage);
        initializeDB();
    }
    
    private void initializeUI(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(10);
        gridPane.setVgap(8);
        
        addFieldsToGrid(gridPane);
        
        HBox buttonBox = createButtonBox();
        gridPane.add(buttonBox, 1, 9);
        
        lblStatus.setStyle("-fx-text-fill: blue;");
        gridPane.add(lblStatus, 0, 10, 2, 1);
        
        Scene scene = new Scene(gridPane, 450, 400);
        primaryStage.setTitle("Staff Information");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void addFieldsToGrid(GridPane gridPane) {
        tfId.setPrefWidth(200);
        tfLastName.setPrefWidth(200);
        tfFirstName.setPrefWidth(200);
        tfMi.setPrefWidth(40);
        tfAddress.setPrefWidth(200);
        tfCity.setPrefWidth(200);
        tfState.setPrefWidth(50);
        tfTelephone.setPrefWidth(200);
        tfEmail.setPrefWidth(200);
        
        tfMi.setMaxWidth(40);
        tfState.setMaxWidth(50);

        gridPane.add(new Label("ID:"), 0, 0);
        gridPane.add(tfId, 1, 0);
        
        gridPane.add(new Label("Last Name:"), 0, 1);
        gridPane.add(tfLastName, 1, 1);
        
        gridPane.add(new Label("First Name:"), 0, 2);
        gridPane.add(tfFirstName, 1, 2);
        
        gridPane.add(new Label("MI:"), 0, 3);
        gridPane.add(tfMi, 1, 3);
        
        gridPane.add(new Label("Address:"), 0, 4);
        gridPane.add(tfAddress, 1, 4);
        
        gridPane.add(new Label("City:"), 0, 5);
        gridPane.add(tfCity, 1, 5);
        
        gridPane.add(new Label("State:"), 0, 6);
        gridPane.add(tfState, 1, 6);
        
        gridPane.add(new Label("Telephone:"), 0, 7);
        gridPane.add(tfTelephone, 1, 7);
        
        gridPane.add(new Label("Email:"), 0, 8);
        gridPane.add(tfEmail, 1, 8);
    }
    
    private HBox createButtonBox() {
        Button btView = new Button("View");
        Button btInsert = new Button("Insert");
        Button btUpdate = new Button("Update");
        Button btClear = new Button("Clear");
        
        btView.setPrefWidth(70);
        btInsert.setPrefWidth(70);
        btUpdate.setPrefWidth(70);
        btClear.setPrefWidth(70);
        
        btView.setOnAction(e -> viewStaff());
        btInsert.setOnAction(e -> insertStaff());
        btUpdate.setOnAction(e -> updateStaff());
        btClear.setOnAction(e -> clearFields());
        
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(btView, btInsert, btUpdate, btClear);
        hBox.setAlignment(Pos.CENTER);
        
        return hBox;
    }
    
    private void initializeDB() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            setStatus("Database connected successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database: " + e.getMessage());
            setStatus("Database connection failed");
        }
    }
    
    private void viewStaff() {
        String id = tfId.getText().trim();
        if (id.isEmpty()) {
            showAlert("Input Error", "Please enter an ID to view.");
            setStatus("Error: ID required for View operation");
            return;
        }
        
        try {
            String query = "SELECT * FROM Staff WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, id);
                
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        tfLastName.setText(resultSet.getString("lastName"));
                        tfFirstName.setText(resultSet.getString("firstName"));
                        tfMi.setText(resultSet.getString("mi"));
                        tfAddress.setText(resultSet.getString("address"));
                        tfCity.setText(resultSet.getString("city"));
                        tfState.setText(resultSet.getString("state"));
                        tfTelephone.setText(resultSet.getString("telephone"));
                        tfEmail.setText(resultSet.getString("email"));
                        
                        setStatus("Staff record found and displayed");
                    } else {
                        showAlert("No Record", "No staff found with ID: " + id);
                        clearFields();
                        setStatus("No staff record found with ID: " + id);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error viewing staff: " + e.getMessage());
            setStatus("Error: " + e.getMessage());
        }
    }
    
    private void insertStaff() {
        if (!validateInputs()) {
            return;
        }
        
        String id = tfId.getText().trim();
        String lastName = tfLastName.getText().trim();
        String firstName = tfFirstName.getText().trim();
        String mi = tfMi.getText().trim();
        String address = tfAddress.getText().trim();
        String city = tfCity.getText().trim();
        String state = tfState.getText().trim();
        String telephone = tfTelephone.getText().trim();
        String email = tfEmail.getText().trim();
        
        try {
            String checkQuery = "SELECT id FROM Staff WHERE id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, id);
                
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        showAlert("Duplicate ID", "A staff with ID " + id + " already exists.");
                        setStatus("Error: Duplicate ID");
                        return;
                    }
                }
            }
            
            String insertQuery = 
                "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                setStatementParameters(insertStatement, id, lastName, firstName, mi, 
                                      address, city, state, telephone, email);
                
                int rowsAffected = insertStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    showAlert("Success", "Staff inserted successfully.");
                    clearFields();
                    setStatus("Staff record inserted successfully");
                } else {
                    showAlert("Error", "Failed to insert staff.");
                    setStatus("Error: Insert operation failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error inserting staff: " + e.getMessage());
            setStatus("Error: " + e.getMessage());
        }
    }
    
    private void updateStaff() {
        String id = tfId.getText().trim();
        if (id.isEmpty()) {
            showAlert("Input Error", "Please enter an ID to update.");
            setStatus("Error: ID required for Update operation");
            return;
        }
        
        if (!validateInputs()) {
            return;
        }
        
        String lastName = tfLastName.getText().trim();
        String firstName = tfFirstName.getText().trim();
        String mi = tfMi.getText().trim();
        String address = tfAddress.getText().trim();
        String city = tfCity.getText().trim();
        String state = tfState.getText().trim();
        String telephone = tfTelephone.getText().trim();
        String email = tfEmail.getText().trim();
        
        try {
            String checkQuery = "SELECT id FROM Staff WHERE id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, id);
                
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        showAlert("No Record", "No staff found with ID: " + id);
                        setStatus("Error: No staff record found with ID: " + id);
                        return;
                    }
                }
            }
            
            String updateQuery = 
                "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, " +
                "address = ?, city = ?, state = ?, telephone = ?, email = ? " +
                "WHERE id = ?";
                
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, lastName);
                updateStatement.setString(2, firstName);
                updateStatement.setString(3, mi);
                updateStatement.setString(4, address);
                updateStatement.setString(5, city);
                updateStatement.setString(6, state);
                updateStatement.setString(7, telephone);
                updateStatement.setString(8, email);
                updateStatement.setString(9, id);
                
                int rowsAffected = updateStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    showAlert("Success", "Staff updated successfully.");
                    setStatus("Staff record updated successfully");
                } else {
                    showAlert("Error", "Failed to update staff.");
                    setStatus("Error: Update operation failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Error updating staff: " + e.getMessage());
            setStatus("Error: " + e.getMessage());
        }
    }
    
    private void setStatementParameters(PreparedStatement statement, String id, 
            String lastName, String firstName, String mi, 
            String address, String city, String state, 
            String telephone, String email) throws SQLException {
        
        statement.setString(1, id);
        statement.setString(2, lastName);
        statement.setString(3, firstName);
        statement.setString(4, mi);
        statement.setString(5, address);
        statement.setString(6, city);
        statement.setString(7, state);
        statement.setString(8, telephone);
        statement.setString(9, email);
    }
    
    private boolean validateInputs() {
        if (tfId.getText().trim().isEmpty()) {
            showAlert("Input Error", "ID cannot be empty.");
            setStatus("Error: ID is required");
            return false;
        }
        
        if (tfLastName.getText().trim().isEmpty()) {
            showAlert("Input Error", "Last Name cannot be empty.");
            setStatus("Error: Last Name is required");
            return false;
        }
        
        if (tfFirstName.getText().trim().isEmpty()) {
            showAlert("Input Error", "First Name cannot be empty.");
            setStatus("Error: First Name is required");
            return false;
        }
        
        String state = tfState.getText().trim();
        if (!state.isEmpty() && state.length() != 2) {
            showAlert("Input Error", "State must be 2 characters.");
            setStatus("Error: State must be 2 characters");
            return false;
        }
        
        String telephone = tfTelephone.getText().trim();
        if (!telephone.isEmpty() && telephone.length() != 10) {
            showAlert("Input Error", "Telephone must be 10 digits.");
            setStatus("Error: Telephone must be 10 digits");
            return false;
        }
        
        return true;
    }
    
    private void clearFields() {
        tfLastName.clear();
        tfFirstName.clear();
        tfMi.clear();
        tfAddress.clear();
        tfCity.clear();
        tfState.clear();
        tfTelephone.clear();
        tfEmail.clear();
        setStatus("Fields cleared");
    }
    
    private void setStatus(String message) {
        lblStatus.setText(message);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @Override
    public void stop() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
