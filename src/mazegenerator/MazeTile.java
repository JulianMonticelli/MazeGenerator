/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package mazegenerator;

/**
 * @author Julian
 */
public class MazeTile {
    public int xPos;
    public int yPos;
    public boolean checkedLeft;
    public boolean checkedRight;
    public boolean checkedUp;
    public boolean checkedDown;
    
    public boolean isPath;
    
    public MazeTile(int x, int y) {
        xPos = x;
        yPos = y;
        checkedLeft = false;
        checkedRight = false;
        checkedUp = false;
        checkedDown = false;
        
        isPath = false;
    }
}
