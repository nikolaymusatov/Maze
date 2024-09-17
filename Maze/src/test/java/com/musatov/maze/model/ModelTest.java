package com.musatov.maze.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.geometry.Point2D;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    private Model model;
    
    @BeforeEach
    void setUp() {
        model = new Model();
    }
    
    @Test
    void testInitializeMaze() {
        model.generateMaze(5, 5);
        assertNotNull(model.getMaze());
        assertEquals(5, model.getWidth());
        assertEquals(5, model.getHeight());
        assertNotNull(model.getMaze()[0][0]);
    }
    
    @Test
    void testFindRoute() {
        model.generateMaze(5, 5);
        Point2D start = new Point2D(0, 0);
        Point2D end = new Point2D(4, 4);
        var route = model.findRoute(start, end);
        
        assertNotNull(route);
        assertFalse(route.isEmpty());
        assertEquals(start, route.get(0));
        assertEquals(end, route.get(route.size() - 1));
    }
    
    @Test
    void testSaveAndLoadMaze() throws Exception {
        model.generateMaze(5, 5);
        String path = "target/generated-test-sources/test_maze.txt";
        model.saveMaze(path);
        
        Model newModel = new Model();
        newModel.loadMaze(path);
        
        assertEquals(model.getWidth(), newModel.getWidth());
        assertEquals(model.getHeight(), newModel.getHeight());
        assertEquals(model.getMaze()[0][0].hasRightWall(), newModel.getMaze()[0][0].hasRightWall());
    }
}
