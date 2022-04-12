package minesweeper;
import java.util.Random;
/**
 * Locations of Mines
 */
public class MineMapHard {
   // package access
   int numMines;
   boolean[][] isMined;

   Random randc = new Random();
   int upperboundc = GameBoardHard.COLS;
   int lowerbound = 0;
   
   Random randr = new Random();
   int upperboundr = GameBoardHard.ROWS;

   // Constructor
   public MineMapHard() {
      isMined = new boolean[GameBoardHard.ROWS][GameBoardHard.COLS];
   }

   // Allow user to change the rows and cols
   public void newMineMap(int numMines) {
      this.numMines = numMines;
      // Automatically generate new MineMap
      for (int i=0; i<GameBoardHard.ROWS; i++) {
         for (int j=0; j<GameBoardHard.COLS; j++) {
            isMined[i][j]=false;
         }
      }

      int i=1;
      while (i<=this.numMines){
         int row = randr.nextInt(upperboundr-lowerbound) + lowerbound;
         int col = randc.nextInt(upperboundc-lowerbound) + lowerbound;
         if (isMined[row][col] == false) {
        	 isMined[row][col] = true;
             ++i;
          }
      }
   }
}