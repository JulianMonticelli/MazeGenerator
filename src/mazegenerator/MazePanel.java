/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */

package mazegenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * @author Julian
 */
class MazePanel extends JPanel {
    
    private final BufferedImage bi;
    
    private Maze maze;
    
    public MazePanel(Maze maze) {
        this.maze = maze; 
        bi = renderMazeImage();
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(maze.getMazeSizeX(),
                maze.getMazeSizeY()));
    }
    
    /***************************************************************************
     * Renders the mage to a BufferedImage object and returns it.
     * @return a BufferedImage of the maze
     */
    private BufferedImage renderMazeImage() {
        BufferedImage bi = new BufferedImage(maze.getMazeSizeX(), maze.getMazeSizeY(),
            BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
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
        return bi;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bi, 0, 0, this);
    }
    
    /***************************************************************************
     * Returns the maze width in pixels
     * @return the maze width in pixels
     */
    public int getMazeWidth() {
        return maze.getMazeSizeX();
    }
    
    /***************************************************************************
     * Returns the maze height in pixels
     * @return the maze height in pixels
     */
    public int getMazeHeight() {
        return maze.getMazeSizeY();
    }
    
    /***************************************************************************
     * Returns a freshly drawn buffered image of the maze. The buffered image
     * must be re-drawn every time it is called for since after use, a buffered
     * image 
     * @return A freshly drawn buffered image
     */
    public BufferedImage getBufferedImage() {
        return renderMazeImage();
    }
}
