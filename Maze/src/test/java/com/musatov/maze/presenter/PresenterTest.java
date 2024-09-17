package com.musatov.maze.presenter;

import com.musatov.maze.model.Model;
import com.musatov.maze.view.MainWindowController;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class PresenterTest {
    
    private Model model;
    private MainWindowController controller;
    private Presenter presenter;
    
    @BeforeEach
    void setUp() {
        model = mock(Model.class);
        controller = mock(MainWindowController.class);
        presenter = new Presenter(model, controller);
    }
    
    @Test
    void testLoadMaze() throws IOException {
        String path = "test_maze.txt";
        presenter.loadMaze(path);
        
        verify(model, times(1)).loadMaze(path);
        verify(controller, times(1)).drawMaze(any(), anyInt(), anyInt());
    }
    
    @Test
    void testGenerateMaze() {
        int rows = 5;
        int cols = 5;
        
        when(model.getHeight()).thenReturn(rows);
        when(model.getWidth()).thenReturn(cols);
        
        presenter.generateMaze(rows, cols);
        
        verify(model, times(1)).generateMaze(rows, cols);
        verify(controller, times(1)).drawMaze(any(), eq(rows), eq(cols));
    }
    
    @Test
    void testFindRoute() {
        Point2D start = new Point2D(0, 0);
        Point2D end = new Point2D(4, 4);
        List<Point2D> route = new ArrayList<>();
        when(model.findRoute(start, end)).thenReturn(route);
        presenter.findRoute(start, end);
        
        verify(model, times(1)).findRoute(start, end);
        verify(controller, times(1)).drawRoute(route);
    }
    
    @Test
    void testSaveMaze() throws IOException {
        String path = "test_maze.txt";
        presenter.saveMaze(path);
        
        verify(model, times(1)).saveMaze(path);
    }
}
