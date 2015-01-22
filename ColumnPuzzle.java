/**
 * 
 * @author Bailiang Gong <bailiang>
 * @section B
 * @date 01/19/2015
 *
 */

// You may not import any additional classes or packages.
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Random;

public class ColumnPuzzle implements MouseListener {

    // Do not add, change, or delete any global variables or you will receive
    // a zero on this assignment.
	private GridGUI GUI;
    public int numMoves;
    public Color[][] grid;
    public Color[] colorsList;
    private int NUM_OF_COLUMNS = 6;

    // Do not change this method except to change the number of rows for
    // testing. It doesn't matter which number you leave in the main method
    // when you submit.
    public static void main(String[] args) {
        new ColumnPuzzle(3);
    }

    // Do not change this method.
    public ColumnPuzzle(Integer numRows) {
        numMoves = 0;
        setup(numRows);
        shuffle();
        GUI = new GridGUI(grid,this);
        Random digit = new Random();
        for(int i=0; i<=100; ++i){
        	System.out.println(digit.nextInt(10));
        }
    }

    /**
     * The setup method initializes the Column Puzzle.
     * 
     * 1. Initialize the grid array with the specified rows and 6 columns.
     * 2. Initialize each column with the color corresponding to the same index
     *    in colorsList.
     * 3. Set the bottom-right cell to BLACK.
     */
    public void setup(Integer numRows) {
        // Use only these colors.
        colorsList = new Color[] {Color.RED, Color.YELLOW, Color.GREEN,
                                  Color.BLUE, Color.CYAN, Color.MAGENTA};
        grid = new Color[numRows][NUM_OF_COLUMNS];
        for(int i=0; i<numRows; ++i){
        grid[i] = new Color[] {Color.RED, Color.YELLOW, Color.GREEN,
                               Color.BLUE, Color.CYAN, Color.MAGENTA}; 
        }   
        grid[numRows-1][NUM_OF_COLUMNS-1] = Color.BLACK;
    }

    /**
     * Returns the number of rows in the ColumnPuzzle.
     */
    public int getNumRows() {
        return grid.length;
    }

    /**
     * Returns the number of columns in the ColumnPuzzle.
     */
    public int getNumCols() {
        return grid[0].length;
    }

    /**
     * Returns the current grid.
     */
    public Color[][] getGrid() {
        return grid;
    }

    /**
     * Returns the number of moves.
     */
    public int getNumMoves() {
        return numMoves;
    }

    /**
     * Swaps the colors at the given points.
     */
    public void swap(Integer row1, Integer col1, Integer row2, Integer col2) {
        Color temp;
        temp = grid[row2][col2]; 
        grid[row2][col2] = grid[row1][col1];
        grid[row1][col1] = temp;
    }

    /**
     * Complete the shuffle method. You must use the following algorithm:
     * 
     * 1. Create a new Random object.
     * 2. Pick a random color location in the grid array (using a random row
     *    and random column)
     * 3. Swap the color with another color at a random location using the
     *    swap method you wrote
     * 4. Repeat this process until you have performed 100 color swaps.
     */
    public void shuffle() {
        Random rand = new Random();
        for(int i=0; i<=100; ++i){
            swap(rand.nextInt(getNumRows()),rand.nextInt(getNumCols()),
                 rand.nextInt(getNumRows()),rand.nextInt(getNumCols()));
        }
    }

    /**
     * Sets the title on the grid Window.
     * You should look at the API for JFrame to learn how to update the title.
     */
    public void setTitle(String title) {
        GUI.setTitle(title);
    }

    /** 
     * Gets the title on the Grid Window
     */
    public String getTitle() {
        return GUI.getTitle();
    }

    /**
     * Returns true if (and only if) the puzzle has been solved.
     * A puzzle is "solved" when each column consists of exactly one color,
     * or one color and the black square.
     */
    public boolean isSolved() {
        if(getNumRows() == 1) return true;
        // Compares each element with the first or second element from each column.
        for (int i = 0; i < getNumCols(); ++i) {
            Color comparedValue = grid[0][i];
            if (comparedValue != Color.BLACK) {
                for (int j = 1; j < getNumRows(); ++j) {
                    if (grid[j][i] != Color.BLACK && grid[j][i] != comparedValue) {
                        return false;
                    }
                }
            } else {
                comparedValue = grid[1][i];
                for (int j = 1; j < getNumRows(); ++j) {
                    if (grid[j][i] != comparedValue) {
                        return false;
                    }
                }
            }
        }
        return true;
    }	

    /**
     * Returns the index of the adjacent black square (horizontally or
     * vertically). It returns a int[] of (-1,-1) if no adjacent black square
     * is found.
     */
    public int[] adjacentBlackSquare(Integer row, Integer col) {
        int[] adjacentBlack = new int[2];
        boolean ifFind = false;
        // Finds the location of Black square.
        for(int i=0; i<getNumRows(); ++i){
            for(int j=0; j<getNumCols(); ++j){
                if(grid[i][j] == Color.BLACK){
                    adjacentBlack[0] = i;
                    adjacentBlack[1] = j;
                    ifFind = true;
                    break;
                }
            }
            if(ifFind) break;
        }
        // Tests if the Black square is adjacent to current square.
        if((Math.abs(adjacentBlack[0]-row)==1&&adjacentBlack[1]==col)
            ||(Math.abs(adjacentBlack[1]-col)==1&&adjacentBlack[0]==row)){
            return adjacentBlack;
        }else{
            adjacentBlack[0] = -1;
            adjacentBlack[1] = -1;
            return adjacentBlack;
        }
    }

    /**
     * Implement this mouseClicked method. Use the following algorithm:
     * 
     * 1. Find an adjacent black square to the point.
     * 2. If there is no black square, update the title to say "Illegal move"
     *    and return.
     * 3. If there is a black square, swap the black square and the clicked
     *    square (which shifts the clicked square into the blank space).
     * 4. Update the number of moves.
     * 5. Update the title. If this move solved the puzzle, update the title
     *    to say "You won!". Otherwise, update the title to say "X moves",
     *    where X is the number of moves so far.
     */
    public void mousePressed(MouseEvent e) {
        // Stops any moves from being made once the puzzle is solved.
        if (isSolved()){ setTitle("You won!");return;}
        // Gets the row and column of the clicked square
        int row = e.getY()/100;
        int col = e.getX()/100;
        int[] adjacentBlack = new int[2];
        adjacentBlack = adjacentBlackSquare(row,col);
        if(adjacentBlack[0] == -1 && adjacentBlack[1] == -1){
            setTitle("Illegal move");
            return;
        }
        swap(row,col,adjacentBlack[0],adjacentBlack[1]);
        ++numMoves;
        if(isSolved()){
            setTitle("You won!");
            return;
        }else{
            setTitle(numMoves + " moves");
            return;
        }
    }



    /*
     * Do not change anything below this line. If you do, you will receive a 0.
     *
     * You are, however, encouraged to read it. It's OK if you don't understand
     * every line, but you can probably understand what it's doing in general.
     */

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    	 GUI.paint(grid);
    }

    public void mouseClicked(MouseEvent e) {
    }

}
