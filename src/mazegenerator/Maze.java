/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package mazegenerator;

import java.util.Random;

/**
 * @author Julian
 */
public class Maze {
    
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    
    
    private static Random rand = new Random();
    
    private int mazeWidth;
    private int mazeHeight;
    protected int mazeTileSize;
    
    public boolean[][] isPath; 
    
    
    public Maze(int mazeWidth, int mazeHeight, int mazeTileSize,
            boolean usesSeed, long seed) {
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;
        this.mazeTileSize = mazeTileSize;
        
        // x, y coordinates
        isPath = new boolean[mazeWidth][mazeHeight];
    }
    
    public void generateMaze() {
        int xStart = 0;
        int yStart = 0;
        
        if (rand.nextBoolean()) {
            yStart = rand.nextInt(mazeHeight-1) + 1;
        } else {
            xStart = rand.nextInt(mazeWidth-1) + 1;
        }
        
        isPath[xStart][yStart] = true;
        
        generateMaze(xStart, yStart);
        
        int xEnd = mazeWidth-1;
        int yEnd = mazeHeight-1;
        
        boolean endIsValid = false;
        
        if (xStart == 0) {
            while (!endIsValid) {
                xEnd = rand.nextInt(mazeWidth-1) + 1;
                if (isPath[xEnd][yEnd-1]) {
                    endIsValid = true;
                }
            }
        } else {
            while (!endIsValid) {
                yEnd = rand.nextInt(mazeHeight-1) + 1;
                if (isPath[xEnd-1][yEnd]) {
                    endIsValid = true;
                }
            }
        }
        
        isPath[xEnd][yEnd] = true;
        
    }
    
    private void generateMaze(int currentX, int currentY) {
        isPath[currentX][currentY] = true;
        
        boolean checkedLeft = false;
        boolean checkedRight = false;
        boolean checkedUp = false;
        boolean checkedDown = false;
        
        
        // Generate, randomly, next path piece
        while(!checkedLeft || !checkedRight || !checkedUp || !checkedDown) {
            int wayToGo = rand.nextInt(4);
            switch(wayToGo) {
                case LEFT:
                    if (isValidTile(currentX-1, currentY, wayToGo)) {
                        generateMaze(currentX-1, currentY);
                    }
                    checkedLeft = true;
                    break;
                case RIGHT:
                    if (isValidTile(currentX+1, currentY, wayToGo)) {
                        generateMaze(currentX+1, currentY);
                    }
                    checkedRight = true;
                    break;
                case UP:
                    if (isValidTile(currentX, currentY+1, wayToGo)) {
                        generateMaze(currentX, currentY+1);
                    }
                    checkedUp = true;
                    break;
                case DOWN:
                    if (isValidTile(currentX, currentY-1, wayToGo)) {
                        generateMaze(currentX, currentY-1);
                    }
                    checkedDown = true;
                    break;
            }
        }
        // Bactrack if there is no way to go
    }

    private boolean isValidTile(int x, int y, int wayToGo) {
        if (x <= 0 || y <= 0)
            return false;
        if (x >= mazeWidth - 1 || y >= mazeHeight - 1)
            return false;
        if (isPath[x][y])
            return false;
        return isValidDirection(x, y, wayToGo);
    }

    private boolean isValidDirection(int x, int y, int wayToGo) {
        int directNeighborCount = 0;   
        switch (wayToGo) {
            case LEFT:
                return (validLeft(x, y) && validUpCenter(x, y)
                        && validDownCenter(x, y));
            case RIGHT:
                return (validRight(x, y) && validUpCenter(x, y)
                        && validDownCenter(x, y));
            case UP:
                return (validUp(x, y) && validRightCenter(x, y)
                        && validLeftCenter(x, y));
            case DOWN:
                return (validDown(x, y) && validLeftCenter(x, y)
                        && validRightCenter(x, y));
            default:
                return true;
        }
    }
    
    private boolean validLeftCenter(int x, int y) {
        if (x > 0) {
            if (isPath[x-1][y]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean validLeft(int x, int y) {
        // Take care of left side
        if (x > 0) {
            if (isPath[x-1][y]) {
                return false;
            }
            if (y > 0) {
                if (isPath[x-1][y-1]) {
                    return false;
                }
            }
            if (y < mazeHeight-1) {
                if (isPath[x-1][y+1]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean validRightCenter(int x, int y) {
        if (x < mazeWidth-1) {
            if (isPath[x+1][y]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean validRight(int x, int y) {
        // Take care of right side
        if (x < mazeWidth-1) {
            if (isPath[x+1][y]) {
                return false;
            }
            if (y > 0) {
                if (isPath[x+1][y-1]) {
                    return false;
                }
            }
            if (y < mazeHeight-1) {
                if (isPath[x+1][y+1]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean validUpCenter(int x, int y) {
        if (y < mazeHeight - 1) {
            if (isPath[x][y+1]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean validUp(int x, int y) {
        // Take care of up and down
        if (y < mazeHeight - 1) {
            if (isPath[x][y+1]) {
                return false;
            }
            if (x < mazeWidth-1) {
                if (isPath[x+1][y+1]) {
                    return false;
                }
            }
            if (x > 0) {
                if (isPath[x-1][y+1]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean validDownCenter(int x, int y) {
        if (y > 0) {
            if (isPath[x][y-1]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean validDown(int x, int y) {
        // Take care of up and down
        if (y > 0) {
            if (isPath[x][y-1]) {
                return false;
            }
            if (x < mazeWidth-1) {
                if (isPath[x+1][y-1]) {
                    return false;
                }
            }
            if (x > 0) {
                if (isPath[x-1][y-1]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public int getMazeSizeX() {
        return mazeWidth * mazeTileSize;
    }
    
    public int getMazeSizeY() {
        return mazeHeight * mazeTileSize;
    }
    
}
