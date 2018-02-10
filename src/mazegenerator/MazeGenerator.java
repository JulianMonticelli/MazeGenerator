/*
 * This program, if distributed by its author to the public as source code,
 * can be used if credit is given to its author and any project or program
 * released with the source code is released under the same stipulations.
 */
package mazegenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;


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
        
        
        maze = new Maze(mazeSizeY, mazeSizeX, tileSize, usesSeed, seed);
        maze.generateMaze();
        
        mazePanel = new MazePanel(maze);
        
        addJMenuBar();
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
    
    /***************************************************************************
     * Creates a new maze by prompting the user for options about the new maze.
     * If an option is cancelled, no new maze is created.
     */
    private void newMaze() {
        // Local vars
        int mazeWidth = 0;
        int mazeHeight = 0;
        int tileSize = 0;
        boolean usesSeed = false;
        long seed = 0;
        
        boolean validSeed = false; // Used to control input seed since seed can be neg.
        
        // Handle new maze JOptionPanes
        while (mazeWidth <= 10) { 
            String input = JOptionPane.showInputDialog(null, 
                    "Maze tile width (minimum 10)", "New Maze", 
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            try {
                mazeWidth = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        while (mazeHeight <= 10) { 
            String input = JOptionPane.showInputDialog(null, 
                    "Maze tile height (minimum 10)", "New Maze", 
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            try {
                mazeHeight = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        while (tileSize <= 0) {
            String input = JOptionPane.showInputDialog(null, 
                    "Maze tile size in pixels (minimum 1)", "New Maze", 
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            try {
                tileSize = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        if (JOptionPane.showConfirmDialog(null, "Use a custom seed?",
                "New Maze", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            usesSeed = true;
        }
        while (!validSeed) {
            String input = JOptionPane.showInputDialog(null, 
                    "Enter a new seed", "New Maze", 
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            try {
                seed = Long.parseLong(input);
                validSeed = true;
            } catch (NumberFormatException e) {
                // do nothing
            }
        }
        
        renderNewMaze(mazeWidth, mazeHeight, tileSize, usesSeed, seed);
    }
    
    private void renderNewMaze(int mazeWidth, int mazeHeight, int tileSize, boolean usesSeed, long seed) {
        // Handle rendering the new maze
        this.remove(mazePanel);
        
        maze = new Maze(mazeWidth, mazeHeight, tileSize, usesSeed, seed);
        maze.generateMaze();
        mazePanel = new MazePanel(maze);
        
        this.add(mazePanel);
        this.pack();
    }
    
    /***************************************************************************
     * Provides a JFileChooser to pick a file to save to. If no file is selected,
     * the program will not save.
     */
    private void save() {
        JFrame frame = new JFrame("Select file...");
        JFileChooser jfc = new JFileChooser();
        
        FileNameExtensionFilter fef = new FileNameExtensionFilter("Portable "
                                    +" Network Graphics (*.png)", "png");
        jfc.setFileFilter(fef);
        
        jfc.setApproveButtonText("Save");
        
        frame.add(jfc);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(false);
        File file = null;

        if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile();
            // If the file isn't the appropriate filetype, prompt again to 
            // select a file after dumping a warning.
            if(!file.getPath().endsWith(".png")) {
                file = new File(file.getPath() + ".png");
            }
            frame.setVisible(false);
            frame.setEnabled(false);
            exportMazeAsPNG(file);
        } else {
            frame.setVisible(false);
            frame.setEnabled(false);
}
    }

    /***************************************************************************
     * Export the current maze as a PNG file.
     * @param file The file to export to.
     */
    private void exportMazeAsPNG(File file) {
        BufferedImage mazeImage = new BufferedImage(mazePanel.getWidth(),
            mazePanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        mazePanel.paint(mazeImage.createGraphics());
        try {
            ImageIO.write(mazeImage, "png", file);
        } catch (IOException ex) {
            System.err.println("There was an error writing the image to a"
                    + " file.");
        }
    }

    /***************************************************************************
     * Sets up menu bar for the current JFrame
     */
    private void addJMenuBar() {
        JMenuBar jmb = new JMenuBar();
        this.setJMenuBar(jmb);
        
        JMenu file = new JMenu("File");
        jmb.add(file);
        
        JMenuItem fileNew = new JMenuItem("New...");
        file.add(fileNew);
        fileNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newMaze();
            }
        });
        
        JMenuItem save = new JMenuItem("Save...");
        file.add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }

    
}
