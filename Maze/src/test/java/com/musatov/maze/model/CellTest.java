package com.musatov.maze.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CellTest {
    private Cell cell = new Cell();
    
    @Test
    void setRightWallTest() {
        cell.setRightWall(true);
        assertTrue(cell.hasRightWall());
    }
    
    @Test
    void setBottomWallTest() {
        cell.setBottomWall(true);
        assertTrue(cell.hasBottomWall());
    }
    
    @Test
    void setSetTest() {
        cell.setSet(10);
        assertEquals(10, cell.getSet());
    }
}
