package com.musatov.maze.model;

import javafx.geometry.Point2D;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Model {
    private int width;
    private int height;
    private Cell[][] maze;
    private final Random random;
    
    public Model() {
        this.random = new Random();
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Cell[][] getMaze() {
        return this.maze;
    }
    
    void initializeMaze() {
        this.maze = new Cell[this.height][this.width];
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                this.maze[row][col] = new Cell();
            }
        }
    }
    
    public void loadMaze(String path) throws IOException {
        try (Scanner scanner = new Scanner(new File(path))) {
            if (scanner.hasNextInt()) {
                this.height = scanner.nextInt();
                this.width = scanner.nextInt();
                this.initializeMaze();
                
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        maze[row][col].setRightWall(scanner.nextInt() == 1);
                    }
                }
                
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        maze[row][col].setBottomWall(scanner.nextInt() == 1);
                    }
                }
            }
        }
    }
    
    public void saveMaze(String path) throws FileNotFoundException {
        File file = new File(path);
        PrintWriter writer = new PrintWriter(file);
        
        writer.println(this.height + " " + this.width);
        
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                writer.print((this.maze[row][col].hasRightWall() ? 1 : 0) + " ");
            }
            writer.println();
        }
        
        writer.println();
        
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                writer.print((this.maze[row][col].hasBottomWall() ? 1 : 0) + " ");
            }
            writer.println();
        }
        
        writer.close();
    }
    
    public void generateMaze(int rows, int cols) {
        this.height = rows;
        this.width = cols;
        this.initializeMaze();
        this.initializeFirstRow();
        for (int row = 0; row < height - 1; row++) {
            createVerticalWalls(row);
            createHorizontalWalls(row);
            prepareNextRow(row);
        }
        createLastRow();
    }
    
    public List<Point2D> findRoute(Point2D start, Point2D end) {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        Queue<Pair<Point2D, List<Point2D>>> queue = new LinkedList<>();
        Set<Point2D> visited = new HashSet<>();
        List<Point2D> initialPath = new ArrayList<>();
        queue.add(new Pair<>(start, initialPath));
        visited.add(start);
        
        while (!queue.isEmpty()) {
            Pair<Point2D, List<Point2D>> current = queue.poll();
            Point2D currentPoint = current.getKey();
            List<Point2D> path = new ArrayList<>(current.getValue());
            path.add(currentPoint);
            
            if (currentPoint.equals(end)) {
                return path;
            }
            
            for (int[] direction : directions) {
                int newX = (int) currentPoint.getX() + direction[0];
                int newY = (int) currentPoint.getY() + direction[1];
                Point2D neighbor = new Point2D(newX, newY);
                
                if (isValid(currentPoint, neighbor) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(new Pair<>(neighbor, path));
                }
            }
        }
        
        return null;
    }
    
    private boolean isValid(Point2D current, Point2D neighbor) {
        int currentY = (int) current.getX();
        int currentX = (int) current.getY();
        int neighborY = (int) neighbor.getX();
        int neighborX = (int) neighbor.getY();
        
        if (neighborX < 0 || neighborX >= this.getHeight() ||
                neighborY < 0 || neighborY >= this.getWidth()) {
            return false;
        }
        
        if (neighborX == currentX + 1) {
            return !this.maze[currentX][currentY].hasBottomWall();
        } else if (neighborX == currentX - 1) {
            return !this.maze[neighborX][neighborY].hasBottomWall();
        } else if (neighborY == currentY + 1) {
            return !this.maze[currentX][currentY].hasRightWall();
        } else if (neighborY == currentY - 1) {
            return !this.maze[neighborX][neighborY].hasRightWall();
        }
        
        return false;
    }
    
    private void initializeFirstRow() {
        for (int col = 0; col < this.width; col++) {
            this.maze[0][col].setSet(col);
        }
    }
    
    private void createVerticalWalls(int row) {
        for (int col = 0; col < this.width - 1; col++) {
            boolean isWallSet = this.random.nextBoolean();
            if (this.maze[row][col].getSet()
                    != this.maze[row][col + 1].getSet() && !isWallSet) {
                mergeSets(row, col, col + 1);
            } else {
                this.maze[row][col].setRightWall(true);
            }
        }
        this.maze[row][width - 1].setRightWall(true);
    }
    
    private void mergeSets(int row, int from, int to) {
        int oldSet = this.maze[row][from].getSet();
        int newSet = this.maze[row][to].getSet();
        for (int col = 0; col < this.width; col++) {
            if (this.maze[row][col].getSet() == oldSet) {
                this.maze[row][col].setSet(newSet);
            }
        }
    }
    
    private void createHorizontalWalls(int row) {
        for (int col = 0; col < this.width; col++) {
            boolean isWallSet = this.random.nextBoolean();
            if (isWallSet && isBottomWallNeeded(row, col)) {
                this.maze[row][col].setBottomWall(true);
            }
        }
    }
    
    private boolean isBottomWallNeeded(int row, int col) {
        int cellsWithoutBottomWall = 0;
        int cellsInSet = 0;
        int set = this.maze[row][col].getSet();
        for (int c = 0; c < this.width; c++) {
            if (this.maze[row][c].getSet() == set
                    && !this.maze[row][c].hasBottomWall()) {
                cellsWithoutBottomWall++;
                cellsInSet++;
            }
        }
        return cellsWithoutBottomWall > 1 && cellsInSet > 1;
    }
    
    private void prepareNextRow(int row) {
        for (int col = 0; col < this.width; col++) {
            if (!this.maze[row][col].hasBottomWall()) {
                int newSet = this.maze[row][col].getSet();
                this.maze[row + 1][col].setSet(newSet);
            } else {
                this.maze[row + 1][col].setSet(width * (row + 1) + col);
            }
            this.maze[row + 1][col].setRightWall(false);
            this.maze[row + 1][col].setBottomWall(false);
        }
    }
    
    private void createLastRow() {
        int row = this.height - 1;
        for (int col = 0; col < this.width - 1; col++) {
            this.maze[row][col].setBottomWall(true);
            if (this.maze[row][col].getSet()
                    != this.maze[row][col + 1].getSet()) {
                this.maze[row][col].setRightWall(false);
                mergeSets(row, col, col + 1);
            } else {
                this.maze[row][col].setRightWall(true);
            }
        }
        this.maze[row][width - 1].setBottomWall(true);
        this.maze[row][width - 1].setRightWall(true);
    }
}
