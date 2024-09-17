package com.musatov.maze.view;

import com.musatov.maze.model.Cell;
import com.musatov.maze.presenter.Presenter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.util.List;

public class MainWindowControllerImpl implements MainWindowController {
    private Presenter presenter;
    
    private Stage stage;
    
    private GraphicsContext graphicsContext;
    
    private Point2D startPoint;
    
    private Point2D endPoint;
    
    private double scaleHeight;
    
    private double scaleWidth;
    
    private double offsetX;
    
    private double offsetY;
    
    @FXML
    private Canvas canvas;
    
    @FXML
    private Button saveButton;
    
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
    
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void drawMaze(Cell[][] maze, int rows, int cols) {
        this.graphicsContext.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        this.graphicsContext.setStroke(Color.DARKGREY);
        this.graphicsContext.setLineWidth(2);
        this.scaleHeight = this.canvas.getHeight() / rows;
        this.scaleWidth = this.canvas.getWidth() / cols;
        this.offsetX = this.scaleWidth / 2;
        this.offsetY = this.scaleHeight / 2;
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col].hasRightWall()) {
                    this.graphicsContext.strokeLine(
                            (col + 1) * this.scaleWidth, row * this.scaleHeight,
                            (col + 1) * this.scaleWidth, (row + 1) * this.scaleHeight);
                }
                if (maze[row][col].hasBottomWall()) {
                    this.graphicsContext.strokeLine(
                            col * this.scaleWidth, (row + 1) * this.scaleHeight,
                            (col + 1) * this.scaleWidth, (row + 1) * this.scaleHeight);
                }
            }
        }
        
        this.graphicsContext.strokeRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
    }
    
    @Override
    public void drawRoute(List<Point2D> route) {
        this.graphicsContext.setStroke(Color.BLACK);
        this.graphicsContext.setLineWidth(2);
        for (int i = 0; i < route.size() - 1; i++) {
            Point2D start = route.get(i);
            Point2D end = route.get(i + 1);
            this.graphicsContext.strokeLine(
                    start.getX() * this.scaleWidth + this.offsetX,
                    start.getY() * this.scaleHeight + this.offsetY,
                    end.getX() * this.scaleWidth + this.offsetX,
                    end.getY() * this.scaleHeight + this.offsetY);
        }
    }
    
    @FXML
    private void initialize() {
        this.saveButton.setDisable(true);
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.startPoint = null;
        this.endPoint = null;
    }
    
    @FXML
    private void loadButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .txt file");
        fileChooser.getExtensionFilters()
                   .add(new FileChooser.ExtensionFilter("Txt Files", "*.txt"));
        File file = fileChooser.showOpenDialog(this.stage);
        
        if (file != null) {
            String path = file.getAbsolutePath();
            try {
                this.presenter.loadMaze(path);
                this.saveButton.setDisable(false);
            } catch (Exception e) {
                showAlert("Error during file reading");
            }
        }
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void saveButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save .txt file:");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Txt Files", "*.txt"));
        File file = fileChooser.showSaveDialog(this.stage);
        if (file != null) {
            String path = file.getAbsolutePath();
            try {
                this.presenter.saveMaze(path);
            } catch (Exception e) {
                showAlert("Error during file saving");
            }
        }
    }
    
    @FXML
    private void generateButtonClicked() {
        Pair<Integer, Integer> size = showMazeSizeDialog();
        if (size != null) {
            int rows = size.getKey();
            int cols = size.getValue();
            this.presenter.generateMaze(rows, cols);
            this.saveButton.setDisable(false);
        }
    }
    
    private Pair<Integer, Integer> showMazeSizeDialog() {
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Set size");
        dialog.setHeaderText("Enter the number of rows and columns for the maze");
        dialog.getDialogPane().setPrefWidth(240);
        dialog.getDialogPane().setPrefHeight(180);
        
        ButtonType okButtonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        
        TextField rowsInput = new TextField();
        TextField columnsInput = new TextField();
        rowsInput.setPrefSize(200, 20);
        columnsInput.setPrefSize(200, 20);
        Label rowsLabel = new Label("Rows:");
        Label colsLabel = new Label("Cols:");
        rowsLabel.setPrefSize(80, 20);
        colsLabel.setPrefSize(80, 20);
        
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 20, 10));
        
        grid.add(rowsLabel, 0, 0);
        grid.add(rowsInput, 1, 0);
        grid.add(colsLabel, 0, 1);
        grid.add(columnsInput, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        
        BooleanBinding validInputBinding = Bindings.createBooleanBinding(() -> {
            return isInputValid(rowsInput.getText()) && isInputValid(columnsInput.getText());
        }, rowsInput.textProperty(), columnsInput.textProperty());
        
        okButton.disableProperty().bind(validInputBinding.not());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    int rows = Integer.parseInt(rowsInput.getText());
                    int columns = Integer.parseInt(columnsInput.getText());
                    return new Pair<>(rows, columns);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
        
        return dialog.showAndWait().orElse(null);
    }
    
    private boolean isInputValid(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            int value = Integer.parseInt(input);
            return value > 0 && value <= 50;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    @FXML
    private void handleCanvasClick(MouseEvent event) {
        if (this.startPoint == null) {
            this.startPoint = new Point2D(event.getX(), event.getY());
            this.drawPoint(this.startPoint);
        } else if (this.endPoint == null) {
            this.endPoint = new Point2D(event.getX(), event.getY());
            this.drawPoint(this.endPoint);
            Point2D startCell = getCell(startPoint);
            Point2D endCell = getCell(endPoint);
            this.presenter.findRoute(startCell, endCell);
        } else {
            this.clearRoute();
            this.startPoint = new Point2D(event.getX(), event.getY());
            this.endPoint = null;
            this.drawPoint(this.startPoint);
        }
    }
    
    private void drawPoint(Point2D point) {
        Point2D cell = getCell(point);
        double radius = 5;
        
        this.graphicsContext.setFill(Color.BLACK);
        this.graphicsContext.fillOval(
                (cell.getX() * this.scaleWidth + this.offsetX) - radius / 2,
                (cell.getY() * this.scaleHeight + this.offsetY) - radius / 2,
                radius, radius);
    }
    
    private Point2D getCell(Point2D point) {
        return new Point2D((int) (point.getX() / this.scaleWidth),
                           (int) (point.getY() / this.scaleHeight));
    }
    
    private void clearRoute() {
        this.graphicsContext.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        this.presenter.drawMaze();
        this.startPoint = null;
        this.endPoint = null;
    }
}
