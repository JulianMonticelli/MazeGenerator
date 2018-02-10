/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package mazegenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * @author Julian
 */
class MazePanel extends JPanel {
    
    Maze maze;
    
    public MazePanel(Maze maze) {
        this.maze = maze; 
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(maze.getMazeSizeX(),
                maze.getMazeSizeY()));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = 0; x < maze.mazeTile.length; x++) {
            for (int y = 0; y < maze.mazeTile[x].length; y++) {
                if (maze.mazeTile[x][y].isPath) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x*maze.mazeTileSize, y*maze.mazeTileSize, 
                        maze.mazeTileSize, maze.mazeTileSize);
            }
        }
    }
    
    
}
