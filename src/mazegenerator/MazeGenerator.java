/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */
package mazegenerator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Julian
 */
public class MazeGenerator extends JFrame {

    private static final int DEFAULT_MAZE_SIZE = 100;
    private static final int DEFAULT_MAZE_TILE_SIZE = 5;
    
    public static MazePanel mazePanel;
    
    private int mazeSizeX;
    private int mazeSizeY;
    
    private Maze maze;
    
    public MazeGenerator(String[] args) {
        mazeSizeX = DEFAULT_MAZE_SIZE;
        mazeSizeY = DEFAULT_MAZE_SIZE;
        int tileSize = DEFAULT_MAZE_TILE_SIZE;
        boolean usesSeed = false;
        long seed = 0;
        
        handleArgs(args);
        
        
        maze = new Maze(mazeSizeY, mazeSizeX, tileSize, usesSeed, seed);
        maze.generateMaze();
        
        mazePanel = new MazePanel(maze);
        
        this.setTitle("Maze Generator");
        this.add(mazePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        mazePanel.repaint();
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // for GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MazeGenerator app = new MazeGenerator(args);
                app.setVisible(true);
            }
        });
    }

    private void handleArgs(String[] args) {
        if (args.length == 0) {
            return;
        }
        if (args.length == 1) {
            if (args[0].toLowerCase().equals("-help") 
                    || args[0].toLowerCase().equals("-h")
                    || args[0].toLowerCase().equals("--h")) {
                displayHelp();
            }
            else {
                try {
                    int size = Integer.parseInt(args[0]);
                    mazeSizeX = size;
                    mazeSizeY = size;
                } catch (NumberFormatException e) {
                    System.out.println("Could not parse maze size - are you entering an integer?");
                    System.exit(0);
                }
            }
        }
        
        if (args.length == 2) {
            // Args unfinished
        }
    }

    private void displayHelp() {
        System.out.println("");
        System.exit(0);
    }
    
}
