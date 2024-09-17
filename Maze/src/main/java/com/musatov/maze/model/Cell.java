package com.musatov.maze.model;

public class Cell {
    private int set;
    private boolean rightWall;
    private boolean bottomWall;
    
    public Cell() {
        this.set = -1;
        this.rightWall = false;
        this.bottomWall = false;
    }
    
    public boolean hasRightWall() {
        return rightWall;
    }
    
    public boolean hasBottomWall() {
        return bottomWall;
    }
    
    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }
    
    public int getSet() {
        return this.set;
    }
    
    public void setSet(int set) {
        this.set = set;
    }
    
    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }
}
