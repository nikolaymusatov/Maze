package com.musatov.maze.view;

import com.musatov.maze.model.Cell;
import com.musatov.maze.presenter.Presenter;
import javafx.geometry.Point2D;
import javafx.stage.Stage;

import java.util.List;

public interface MainWindowController {
    void setPresenter(Presenter presenter);
    void setStage(Stage stage);
    void drawMaze(Cell[][] maze, int rows, int cols);
    void drawRoute(List<Point2D> route);
}