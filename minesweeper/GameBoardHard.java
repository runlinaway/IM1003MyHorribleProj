package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public class GameBoardHard extends JPanel {
   // Name-constants for the game properties
   public static final int ROWS = 16;      // number of cells
   public static final int COLS = 30;

   // Name-constants for UI sizes
   public static final int CELL_SIZE = 40;  // Cell width and height, in pixels
   public static final int CANVAS_WIDTH = (CELL_SIZE * COLS); // Game board width/height
   public static final int CANVAS_HEIGHT = (CELL_SIZE * ROWS);
   
   Cell cells[][] = new Cell[ROWS][COLS];
   int numMines = 99;
   int numFlags = numMines;
   boolean isInit = true;
   public boolean lost=false;

   MineMapHard mines = new MineMapHard();
   

   // Constructor
   public GameBoardHard() {
      super.setLayout(new GridLayout(ROWS, COLS, 2, 2));  // JPanel

      // Allocate the 2D array of Cell, and added into content-pane.
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            cells[row][col] = new Cell(row, col);
            super.add(cells[row][col]);
         }
      }

      // [TODO 3] Allocate a common listener as the MouseEvent listener for all the
      //  Cells (JButtons)
      CellMouseListener listener = new CellMouseListener();

      // [TODO 4] Every cell adds this common listener
      // ......
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
         cells[row][col].addMouseListener(listener);   // For all rows and cols
         }
      }

      // Initialize for a new game
      init();

      // Set the size of the content-pane and pack all the components
      //  under this container.
      super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
   }

   // Initialize and re-initialize a new game
   
   
   public void init() {
      // Get a new mine map
      mines.newMineMap(numMines);
      numFlags = numMines;
      isInit = true;
      // Reset cells, mines, and flags
      for (int row = 0; row < ROWS; row++) {
         for (int col = 0; col < COLS; col++) {
            // Initialize each cell with/without mine
            cells[row][col].init(mines.isMined[row][col]);
         }
      }
      lost = false;
   }
   
  

   // Return the number of mines (0 - 8) in the 8 surrounding cells of the given cell.
   private int getSurroundingMines(Cell cell) {
      int numMines = 0;
      for (int row = cell.row - 1; row <= cell.row + 1; row++) {
         for (int col = cell.col - 1; col <= cell.col + 1; col++) {
            // Need to ensure valid row and column numbers too
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS && mines.isMined[row][col]) {
               numMines++;
            }
         }
      }
      return numMines;
   }

   // This cell has 0 surrounding mine. Reveal the 8 surrounding cells recursively

   private void revealSurrounding(Cell cell) {
	   		for (int row = cell.row - 1; row <= cell.row + 1; row++) {
                  for (int col = cell.col - 1; col <= cell.col + 1; col++) {
                  // Need to ensure valid row and column numbers too
                     if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                        if (mines.isMined[row][col]==false && cells[row][col].isRevealed==false){
                           cells[row][col].isRevealed=true;
                           cells[row][col].paint(getSurroundingMines(cells[row][col]));
                           if (getSurroundingMines(cells[row][col])==0){
                               revealSurrounding(cells[row][col]);
                           }
                        }
                     }
                  }
               }

   }   




   // Return true if the player has won (all cells have been revealed or were mined)
   public boolean hasWon() {
	  int cleared = 0;
      for (int row = 0; row < ROWS; row++) { 
         for (int col = 0; col < COLS ; col++) { 
            if (cells[row][col].isRevealed == true || cells[row][col].isMined == true) { 
            	cleared++; 
            } 
         } 
      }
      if (cleared == ROWS*COLS) {
      return true;
      }
      else return false;
   }
   

   // [TODO 2] Define a Listener Inner Class
   private class CellMouseListener extends MouseAdapter {
      @Override
      public void mouseClicked(MouseEvent e) {         // Get the source object that fired the Event
         Cell sourceCell = (Cell)e.getSource();
         // For debugging
         System.out.println("You clicked on (" + sourceCell.row + "," + sourceCell.col + ")");

         // Left-click to reveal a cell; Right-click to plant/remove the flag.
         if (e.getButton() == MouseEvent.BUTTON1) {  // Left-button clicked
            // [TODO 5] If you hit a mine, game over
        	 if (getSurroundingMines(sourceCell) != 0 &&isInit) {
            	 while (getSurroundingMines(sourceCell) != 0 ) {
            		 init();
            	 }
            	 isInit=false;
            	 sourceCell.isRevealed = true;
                 sourceCell.paint(0);
                 if(getSurroundingMines(sourceCell)==0) {
               	  revealSurrounding(sourceCell);
                 }
             }
        	 
             else if (mines.isMined[sourceCell.row][sourceCell.col] == true) {
            	 lost=true; 
            	 JOptionPane.showMessageDialog(null, "Game Over!");
            	 for (int row = 0; row < ROWS; row++) {
        			 for (int col = 0; col < COLS; col++) {
        				 if(cells[row][col].isMined) cells[row][col].gameover();
        			 }
        		 }
                               
               
            }
            // Otherwise, reveal the cell and display the number of surrounding mines
            else {
            	isInit=false;
              sourceCell.isRevealed = true;
              sourceCell.paint(getSurroundingMines(sourceCell));
              if(getSurroundingMines(sourceCell)==0) {
            	  revealSurrounding(sourceCell);
              }
              if (sourceCell.isFlagged==true){
                  sourceCell.isFlagged=false;
                  sourceCell.paint(0);
                  numFlags++;
               }
             
            }

         } else if (e.getButton() == MouseEvent.BUTTON3) { // right-button clicked
            // [TODO 6] If the location is flagged, remove the flag
            if (sourceCell.isFlagged==true){
               sourceCell.isFlagged=false;
               sourceCell.paint(0);
               numFlags++;
            }
            // Otherwise, plant a flag.
            else if (numFlags>0){
               sourceCell.isFlagged=true;
               sourceCell.paint(0);
               numFlags--;
            }
         }

         // [TODO 7] Check if the player has won, after revealing this cell
          if (hasWon()) {
            	  JOptionPane.showMessageDialog(null, "You Win!");
              }
      }
   }
}