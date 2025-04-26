import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {

    private Connection connection;
    private Label statusLabel;
    private TextArea resultArea;

    @Override
    public void start(Stage primaryStage) {
        Button connectButton = new Button("Connect to Database");
        Button batchUpdateButton = new Button("Batch Update");
        Button nonBatchUpdateButton = new Button("Non-batch Update");
        statusLabel = new Label("No Connection");
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(10);

        VBox buttonBox = new VBox(10);
        buttonBox.getChildren().addAll(connectButton, batchUpdateButton, nonBatchUpdateButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        BorderPane mainPane = new BorderPane();
        mainPane.setLeft(buttonBox);
        mainPane.setCenter(resultArea);
        mainPane.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(10));

        connectButton.setOnAction(e -> showConnectionDialog(primaryStage));
        batchUpdateButton.setOnAction(e -> performBatchUpdate());
        nonBatchUpdateButton.setOnAction(e -> performNonBatchUpdate());

        Scene scene = new Scene(mainPane, 600, 400);
        primaryStage.setTitle("Database Batch Update Performance Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showConnectionDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("Database Connection");

        DBConnectionPanel connectionPanel = new DBConnectionPanel();
        
        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> {
            try {
                String driverClass = connectionPanel.getDriverTextField().getText();
                String url = connectionPanel.getUrlTextField().getText();
                String username = connectionPanel.getUsernameTextField().getText();
                String password = connectionPanel.getPasswordField().getText();
                
                Class.forName(driverClass);
                
                connection = DriverManager.getConnection(url, username, password);
                
                createTableIfNotExists();
                
                statusLabel.setText("Connected to: " + url);
                dialog.close();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText("Unable to connect to database");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(connectionPanel, connectButton);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(vbox);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void createTableIfNotExists() {
        try (Statement stmt = connection.createStatement()) {
            try {
                stmt.executeUpdate("DROP TABLE Temp");
            } catch (SQLException ex) {
            }
            
            String createTableSQL = "CREATE TABLE Temp(num1 DOUBLE, num2 DOUBLE, num3 DOUBLE)";
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error creating table", e.getMessage());
        }
    }

    private void performBatchUpdate() {
        if (connection == null) {
            showError("No Connection", "Please connect to the database first.");
            return;
        }

        try {
            long startTime = System.currentTimeMillis();
            
            String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            connection.setAutoCommit(false);
            
            for (int i = 0; i < 1000; i++) {
                pstmt.setDouble(1, Math.random());
                pstmt.setDouble(2, Math.random());
                pstmt.setDouble(3, Math.random());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
            
            long endTime = System.currentTimeMillis();
            double elapsedTime = (endTime - startTime) / 1000.0;
            
            resultArea.appendText("Batch Update: Inserted 1000 records in " + elapsedTime + " seconds\n");
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Batch Update Error", e.getMessage());
            
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void performNonBatchUpdate() {
        if (connection == null) {
            showError("No Connection", "Please connect to the database first.");
            return;
        }

        try {
            long startTime = System.currentTimeMillis();
            
            String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            for (int i = 0; i < 1000; i++) {
                pstmt.setDouble(1, Math.random());
                pstmt.setDouble(2, Math.random());
                pstmt.setDouble(3, Math.random());
                pstmt.executeUpdate();
            }
            
            long endTime = System.currentTimeMillis();
            double elapsedTime = (endTime - startTime) / 1000.0;
            
            resultArea.appendText("Non-batch Update: Inserted 1000 records in " + elapsedTime + " seconds\n");
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Non-batch Update Error", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class DBConnectionPanel extends GridPane {
    private TextField driverTextField;
    private TextField urlTextField;
    private TextField usernameTextField;
    private PasswordField passwordField;

    public DBConnectionPanel() {
        setHgap(5);
        setVgap(5);
        setPadding(new Insets(5));

        Label driverLabel = new Label("JDBC Driver");
        Label urlLabel = new Label("Database URL");
        Label usernameLabel = new Label("Username");
        Label passwordLabel = new Label("Password");

        driverTextField = new TextField("com.mysql.cj.jdbc.Driver");
        urlTextField = new TextField("jdbc:mysql://localhost:3306/test");
        usernameTextField = new TextField("root");
        passwordField = new PasswordField();

        add(driverLabel, 0, 0);
        add(driverTextField, 1, 0);
        add(urlLabel, 0, 1);
        add(urlTextField, 1, 1);
        add(usernameLabel, 0, 2);
        add(usernameTextField, 1, 2);
        add(passwordLabel, 0, 3);
        add(passwordField, 1, 3);

        driverTextField.setPrefWidth(300);
        urlTextField.setPrefWidth(300);
    }

    public TextField getDriverTextField() {
        return driverTextField;
    }

    public TextField getUrlTextField() {
        return urlTextField;
    }

    public TextField getUsernameTextField() {
        return usernameTextField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }
}
