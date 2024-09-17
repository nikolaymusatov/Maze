package com.musatov.maze.presenter;

import com.musatov.maze.model.Model;
import com.musatov.maze.view.MainWindowController;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.List;

public class Presenter {
    private final Model model;
    private final MainWindowController controller;
    
    public Presenter(Model model, MainWindowController controller) {
        this.model = model;
        this.controller = controller;
    }
    
    public void loadMaze(String path) throws IOException {
        this.model.loadMaze(path);
        this.drawMaze();
    }
    
    public void saveMaze(String path) throws IOException {
        this.model.saveMaze(path);
    }
    
    public void generateMaze(int rows, int cols) {
        this.model.generateMaze(rows, cols);
        this.drawMaze();
    }
    
    public void drawMaze() {
        this.controller.drawMaze(this.model.getMaze(),
                                 this.model.getHeight(),
                                 this.model.getWidth());
    }
    
    public void findRoute(Point2D start, Point2D end) {
        List<Point2D> route = this.model.findRoute(start, end);
        this.controller.drawRoute(route);
    }
}
