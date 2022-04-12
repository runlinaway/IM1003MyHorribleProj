package minesweeper;
import java.util.Random;
/**
 * Locations of Mines
 */
public class MineMapInt {
   // package access
   int numMines;
   boolean[][] isMined;

   Random rand = new Random();
   int upperbound = GameBoardIntermediate.COLS;
   int lowerbound = 0;

   // Constructor
   public MineMapInt() {
      isMined = new boolean[GameBoardIntermediate.ROWS][GameBoardIntermediate.COLS];
   }

   // Allow user to change the rows and cols
   public void newMineMap(int numMines) {
      this.numMines = numMines;
      // Automatically generate new MineMap
      for (int i=0; i<GameBoardIntermediate.ROWS; i++) {
         for (int j=0; j<GameBoardIntermediate.COLS; j++) {
            isMined[i][j]=false;
         }
      }

      int i=1;
      while (i<=this.numMines){
         int row = rand.nextInt(upperbound-lowerbound) + lowerbound;
         int col = rand.nextInt(upperbound-lowerbound) + lowerbound;
         if (isMined[row][col] == false) {
        	 isMined[row][col] = true;
             ++i;
          }
      }
   }
}