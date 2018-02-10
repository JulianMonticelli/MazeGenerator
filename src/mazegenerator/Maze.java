/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package mazegenerator;

import java.util.LinkedList;
import java.util.Random;

/**
 * @author Julian
 */
public class Maze {
    
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    
    
    private static Random rand;
    
    private final int mazeWidth;
    private final int mazeHeight;
    protected int mazeTileSize;
    
    public MazeTile[][] mazeTile;
    
    public LinkedList<MazeTile> tileStack;
    
    
    public Maze(int mazeWidth, int mazeHeight, int mazeTileSize,
            boolean usesSeed, long seed) {
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;
        this.mazeTileSize = mazeTileSize;
        
        if (usesSeed) {
            this.rand = new Random(seed);
        } else {
            this.rand = new Random();
        }
        
        tileStack = new LinkedList<>();
        
        // x, y coordinates
        mazeTile = new MazeTile[mazeWidth][mazeHeight];
        for (int x = 0; x < mazeTile.length; x++) {
            for (int y = 0; y < mazeTile[x].length; y++) {
                mazeTile[x][y] = new MazeTile(x, y);
            }
        }
    }
    
    
    /***************************************************************************
     * Generates a maze based on constructed object variables. Handles adding
     * the start and end of the maze on top of calling recursive generation.
     */
    public void generateMaze() {
        
        // Add start tile
        int xStart = 0;
        int yStart = 0;
        
        if (rand.nextBoolean()) {
            yStart = rand.nextInt(mazeHeight-1) + 1;
        } else {
            xStart = rand.nextInt(mazeWidth-1) + 1;
        }
        
        mazeTile[xStart][yStart].isPath = true;
        
        // Begin recursion from start tile
        generateMaze(xStart, yStart);
        
        
        // Generate an end tile
        int xEnd = mazeWidth-1;
        int yEnd = mazeHeight-1;
        
        boolean endIsValid = false;
        
        if (xStart == 0) {
            while (!endIsValid) {
                xEnd = rand.nextInt(mazeWidth-1) + 1;
                if (mazeTile[xEnd][yEnd-1].isPath) {
                    endIsValid = true;
                }
            }
        } else {
            while (!endIsValid) {
                yEnd = rand.nextInt(mazeHeight-1) + 1;
                if (mazeTile[xEnd-1][yEnd].isPath) {
                    endIsValid = true;
                }
            }
        }
        
        mazeTile[xEnd][yEnd].isPath = true;
        
    }
    
    
    /***************************************************************************
     * Uses a randomized DFS algorithm to generate a confusing maze
     * @param currentX Current path tile we're recursing from
     * @param currentY Current path tile we're recursing from
     */
    private void generateMaze(int startX, int startY) {
        
        tileStack.add(mazeTile[startX][startY]);
        
        // Iterative DFS approach - beats recursive because no need to set
        // a larger stack size for larger entries.
        
        while (!tileStack.isEmpty()) {
            // Set that current tile is a path (even if it already is)
            MazeTile tile = tileStack.peek();
            tile.isPath = true;
            
            int currentX = tile.xPos;
            int currentY = tile.yPos;
            
            // If we need to further check different directions, to expand our
            // path, then do so.
            if (!tile.checkedLeft || !tile.checkedRight
                    || !tile.checkedUp || !tile.checkedDown) {
                int wayToGo = rand.nextInt(4);
                switch(wayToGo) {
                    case LEFT:
                        if (isValidTile(currentX-1, currentY, wayToGo)) {
                            tileStack.push(mazeTile[currentX-1][currentY]);
                        }
                        tile.checkedLeft = true;
                        break;
                    case RIGHT:
                        if (isValidTile(currentX+1, currentY, wayToGo)) {
                            tileStack.push(mazeTile[currentX+1][currentY]);
                        }
                        tile.checkedRight = true;
                        break;
                    case UP:
                        if (isValidTile(currentX, currentY+1, wayToGo)) {
                            tileStack.push(mazeTile[currentX][currentY+1]);
                        }
                        tile.checkedUp = true;
                        break;
                    case DOWN:
                        if (isValidTile(currentX, currentY-1, wayToGo)) {
                            tileStack.push(mazeTile[currentX][currentY-1]);
                        }
                        tile.checkedDown = true;
                        break;
                }
            } else { // Otherwise, this tile can be removed from the stack
                tileStack.pop();
            }
        }
    }

    
    /***************************************************************************
     * Determines if a tile we are considering placing is valid
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @param wayToGo The direction we are heading
     * @return True if the tile is valid, otherwise false
     */
    private boolean isValidTile(int x, int y, int wayToGo) {
        if (x <= 0 || y <= 0)
            return false;
        if (x >= mazeWidth - 1 || y >= mazeHeight - 1)
            return false;
        if (mazeTile[x][y].isPath)
            return false;
        return isValidDirection(x, y, wayToGo);
    }

    /***************************************************************************
     * Determines if the tile is valid to place, considering we are going in a
     * certain direction.
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @param wayToGo The direction we are heading
     * @return True if the tile is valid, otherwise false
     */
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
    
    /***************************************************************************
     * Determines if left center of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validLeftCenter(int x, int y) {
        if (x > 0) {
            if (mazeTile[x-1][y].isPath) {
                return false;
            }
        }
        return true;
    }
    
    /***************************************************************************
     * Determines if left side of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validLeft(int x, int y) {
        // Take care of left side
        if (x > 0) {
            if (mazeTile[x-1][y].isPath) {
                return false;
            }
            if (y > 0) {
                if (mazeTile[x-1][y-1].isPath) {
                    return false;
                }
            }
            if (y < mazeHeight-1) {
                if (mazeTile[x-1][y+1].isPath) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /***************************************************************************
     * Determines if right center of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validRightCenter(int x, int y) {
        if (x < mazeWidth-1) {
            if (mazeTile[x+1][y].isPath) {
                return false;
            }
        }
        return true;
    }
    
    /***************************************************************************
     * Determines if right side of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validRight(int x, int y) {
        // Take care of right side
        if (x < mazeWidth-1) {
            if (mazeTile[x+1][y].isPath) {
                return false;
            }
            if (y > 0) {
                if (mazeTile[x+1][y-1].isPath) {
                    return false;
                }
            }
            if (y < mazeHeight-1) {
                if (mazeTile[x+1][y+1].isPath) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /***************************************************************************
     * Determines if upper center of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validUpCenter(int x, int y) {
        if (y < mazeHeight - 1) {
            if (mazeTile[x][y+1].isPath) {
                return false;
            }
        }
        return true;
    }
    
    /***************************************************************************
     * Determines if upper side of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validUp(int x, int y) {
        // Take care of up and down
        if (y < mazeHeight - 1) {
            if (mazeTile[x][y+1].isPath) {
                return false;
            }
            if (x < mazeWidth-1) {
                if (mazeTile[x+1][y+1].isPath) {
                    return false;
                }
            }
            if (x > 0) {
                if (mazeTile[x-1][y+1].isPath) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /***************************************************************************
     * Determines if lower center of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validDownCenter(int x, int y) {
        if (y > 0) {
            if (mazeTile[x][y-1].isPath) {
                return false;
            }
        }
        return true;
    }
    
    /***************************************************************************
     * Determines if lower side of the tile has no path
     * @param x The x value of the tile that is being considered
     * @param y The y value of the tile that is being considered
     * @return True if there are no conflicting tiles
     */
    private boolean validDown(int x, int y) {
        // Take care of up and down
        if (y > 0) {
            if (mazeTile[x][y-1].isPath) {
                return false;
            }
            if (x < mazeWidth-1) {
                if (mazeTile[x+1][y-1].isPath) {
                    return false;
                }
            }
            if (x > 0) {
                if (mazeTile[x-1][y-1].isPath) {
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
