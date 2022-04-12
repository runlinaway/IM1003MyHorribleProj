package minesweeper;
import java.awt.*;
import java.awt.event.*;        // Use AWT's Layout Manager
import java.awt.event.*;  // Use AWT's Event handlers
import javax.swing.*; // Use Swing's Containers and Components
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
/**
 * The Mine Sweeper Game.
 * Left-click to reveal a cell.
 * Right-click to plant/remove a flag for marking a suspected mine.
 * You win if all the cells not containing mines are revealed.
 * You lose if you reveal a cell containing a mine.
 */
@SuppressWarnings("serial")
public class MineSweeperMain extends JFrame {
   // private variables
   GameBoardEasy eboard = new GameBoardEasy();
   GameBoardIntermediate iboard = new GameBoardIntermediate();
   GameBoardHard hboard = new GameBoardHard();
   int currentBoard = 1;
   JButton btnNewGame = new JButton("New Game");
   JLabel Timer = new JLabel();
   TopBar bar = new TopBar();
   LoginBar usernameset = new LoginBar();
   JMenuBar menu;
   JMenu main, ldrBoard, difficulty;
   JMenuItem help, about, sound,initldr, demomode;
   JMenuItem easy, intermediate, hard, leasy, lintermediate, lhard;
   String playername;
   int time=0;
   Player players[][] = new Player[7][3];
   public Clip maintheme;
   public boolean soundON=true;
   public boolean demo=false;
   static final Color textcolor = new Color(117, 44, 24);
   static final Color bg = new Color(196, 77, 69);
   public boolean cheats=false;
   
   
   // Constructor to set up all the UI and game components
   public MineSweeperMain() {
      Container cp = this.getContentPane();           // JFrame's content-pane
      cp.setLayout(new BorderLayout()); // in 10x10 GridLayout
      cp.setBackground(Color.WHITE);
      setupMenuBar();
      for (int playernum =0 ; playernum < 7 ; playernum++) {
    	  for (int difficulty=0 ; difficulty < 3 ; difficulty++) {
    		  players[playernum][difficulty]=new Player(); 				//Initialize leaderboard array
    	  }
      }
      
      setJMenuBar(menu);
      cp.add(bar, BorderLayout.NORTH);
      cp.add(usernameset,BorderLayout.CENTER);
      cp.add(eboard, BorderLayout.SOUTH);
      eboard.init();
      
      
      try {
          // Open an audio input stream.
    	  File soundFile = new File("KleeOST.wav");
    	  AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
          // Get a sound clip resource.
    	  maintheme = AudioSystem.getClip();
          // Open audio clip and load samples from the audio input stream.
    	  maintheme.open(audioIn);
    	  maintheme.start();
    	  maintheme.loop(Clip.LOOP_CONTINUOUSLY);
       } catch (UnsupportedAudioFileException e) {
          e.printStackTrace();
       } catch (IOException e) {
          e.printStackTrace();
       } catch (LineUnavailableException e) {
          e.printStackTrace();
       }


      
      pack();// Pack the UI components, instead of setSize()
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // handle window-close button
      setTitle("Minesweeper");
      setVisible(true);   // show it
   }

   // The entry main() method
   public static void main(String[] args) {
      // [TODO 1] Check Swing program template on how to run the constructor
       SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new MineSweeperMain(); // Let the constructor do the job
         }
      });
   }
   
   
  
   private void setupMenuBar() {
		menu = new JMenuBar();
	   help = new JMenuItem("Help");
	   about = new JMenuItem("About");
	   sound = new JMenuItem("Sound On");
	   demomode = new JMenuItem("DemoMode");
	   easy = new JMenuItem("Easy");
	   intermediate = new JMenuItem("Intermediate");
	   hard = new JMenuItem("Hard");
	   leasy = new JMenuItem("Easy");
	   lintermediate = new JMenuItem("Intermediate");
	   lhard = new JMenuItem("Hard");
	   initldr = new JMenuItem("Reset Leaderboards");
	   menu = new JMenuBar();
	   main = new JMenu("Menu");
	   ldrBoard = new JMenu("LeaderBoards");
	   difficulty = new JMenu("Select Difficulty");
	   Container cp = this.getContentPane();
	   difficulty.add(easy);
	   easy.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	           
	        	 cp.remove(iboard);
	        	 cp.remove(hboard);
	        	 cp.add(eboard, BorderLayout.SOUTH);
	        	 currentBoard = 1;
	        	 eboard.init();
	             pack();
	         }
	      });
	   
	   difficulty.add(intermediate);
	   intermediate.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	           
	        	 cp.remove(eboard);
	        	 cp.remove(hboard);
	        	 cp.add(iboard, BorderLayout.SOUTH);
	        	 currentBoard = 2;
	        	 iboard.init();
	             pack();
	         }
	      });
	   
	   difficulty.add(hard);
	   hard.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	           
	        	 cp.remove(eboard);
	        	 cp.remove(iboard);
	        	 cp.add(hboard, BorderLayout.SOUTH);
	        	 currentBoard = 3;
	        	 hboard.init();
	             pack();
	         }
	      });
	   
	   main.add(difficulty);
	   main.add(help);
	   help.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 String msg = "Click New Game to begin timer\n"
	                     + "Left click on a square to begin\n"
	                     + "Right Click to plant a flag\n"
	        	 		 + "Don't click on mines!\n"
	                     + 	"Bonne Chance!\n";
	               JOptionPane.showMessageDialog(MineSweeperMain.this, 
	                     msg, "Instructions", JOptionPane.PLAIN_MESSAGE);
	         }
	      });
	   main.add(about);
	   about.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 String msg = "A little Java Project\n"
	                     + "By Runlin and Shannen\n"
	                     + "Please give us an A prof....\n";
	               JOptionPane.showMessageDialog(MineSweeperMain.this, 
	                     msg, "About", JOptionPane.PLAIN_MESSAGE);
	         }
	      });
	   main.add(sound);
	   sound.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 if (maintheme.isRunning()) {
	        		 maintheme.stop();
	        		 soundON=false;
	        		 sound.setText("Sound Off");
	        	 }
	        	 else {
	        		 maintheme.start();
	        		 maintheme.loop(Clip.LOOP_CONTINUOUSLY);
	        		 soundON=true;
	        		 sound.setText("Sound On");
	        	 }
	         }
	      });
	   
	   main.add(initldr);
	   initldr.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 for (int playernum =0 ; playernum < 6 ; playernum++) {
	           	  for (int difficulty=0 ; difficulty < 3 ; difficulty++) {
	           		  players[playernum][difficulty].reset(); 				//Initialize leaderboard array
	           	  }
	             }
	         }
	      });
	   main.add(demomode);
	   demomode.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	if(cheats==false) {
	        		cheats=true;
	        		eboard.cheater=true;
	        		 String msg = "Cheats are now on.\n";
		               JOptionPane.showMessageDialog(MineSweeperMain.this, 
		                     msg, "Pepeg Mode", JOptionPane.PLAIN_MESSAGE);
	        	}
	        	else {
	        		cheats=false;
	        		eboard.cheater=false;
	        		String msg = "Cheats are now off.\n";
		               JOptionPane.showMessageDialog(MineSweeperMain.this, 
		                     msg, "Pepeg Mode", JOptionPane.PLAIN_MESSAGE);
	        	}
	        	
	         }
	      });
	   ldrBoard.add(leasy);
	   leasy.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 String msg = "1. "+players[0][0].name+players[0][0].getScore()+"\n"
	                     + "2. "+players[1][0].name+players[1][0].getScore()+"\n"
	                     + "3. "+players[2][0].name+players[2][0].getScore()+"\n"
	        	 		 + "4. "+players[3][0].name+players[3][0].getScore()+"\n"
	        	 		 + "5. "+players[4][0].name+players[4][0].getScore()+"\n";
	               JOptionPane.showMessageDialog(MineSweeperMain.this, 
	                     msg, "Easy Leaderboard", JOptionPane.PLAIN_MESSAGE);
	         }
	      });
	   ldrBoard.add(lintermediate);
	   lintermediate.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 String msg = "1. "+players[0][1].name+players[0][1].getScore()+"\n"
	                     + "2. "+players[1][1].name+players[1][1].getScore()+"\n"
	                     + "3. "+players[2][1].name+players[2][1].getScore()+"\n"
	        	 		 + "4. "+players[3][1].name+players[3][1].getScore()+"\n"
	        	 		 + "5. "+players[4][1].name+players[4][1].getScore()+"\n";
	               JOptionPane.showMessageDialog(MineSweeperMain.this, 
	                     msg, "Intermediate Leaderboard", JOptionPane.PLAIN_MESSAGE);
	         }
	      });
	   ldrBoard.add(lhard);
	   lhard.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 String msg = "1. "+players[0][2].name+players[0][2].getScore()+"\n"
	                     + "2. "+players[1][2].name+players[1][2].getScore()+"\n"
	                     + "3. "+players[2][2].name+players[2][2].getScore()+"\n"
	        	 		 + "4. "+players[3][2].name+players[3][2].getScore()+"\n"
	        	 		 + "5. "+players[4][2].name+players[4][2].getScore()+"\n";
	               JOptionPane.showMessageDialog(MineSweeperMain.this, 
	                     msg, "Hard Leaderboard", JOptionPane.PLAIN_MESSAGE);
	         }
	      });

	   menu.add(main);menu.add(ldrBoard);
	   
	   
   }
//CLASSES
   //Lower Bar
   public void play(String filename) {
	   try {
	          // Open an audio input stream.
	    	  File soundFile = new File(filename+".wav");
	    	  AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
	          // Get a sound clip resource.
	    	  Clip clip = AudioSystem.getClip();
	          // Open audio clip and load samples from the audio input stream.
	    	  clip.open(audioIn);
	    	  clip.start();
	       } catch (UnsupportedAudioFileException e1) {
	          e1.printStackTrace();
	       } catch (IOException e1) {
	          e1.printStackTrace();
	       } catch (LineUnavailableException e1) {
	          e1.printStackTrace();
	       }
	}
   
   public class TopBar extends JPanel{

	   Icon reset = new ImageIcon("HappyKlee.PNG");
	   Icon pause = new ImageIcon("SpeakerKlee.PNG");
	   Icon resume = new ImageIcon("SadKlee.PNG");
	   Dimension dim = new Dimension(80,80);
	   JButton btnNewGame = new JButton(reset);
	   JButton pauseButton = new JButton(pause);
	   JLabel TimeElapsed = new JLabel("",SwingConstants.CENTER);
	   JLabel FlagCount = new JLabel("",SwingConstants.CENTER);
	   boolean timerRunning = false;
	   ActionListener updateTimer = new ActionListener() {
			    		  public void actionPerformed(ActionEvent e) {			    			  
			    			  TimeElapsed.setText("Time:"+time+"s");  
			    			  time++;
			    			  if (currentBoard == 1) {
			    				  if(eboard.lost&&soundON) {
			    					  maintheme.stop();
			    					  play("consequences");
			    					  eboard.lost=false;
			    					  if(cheats==false) {
			    						t1.stop();  
			    					  }
			    					  
			    				  }
			    				  if (eboard.hasWon()) {
			    					  t1.stop();
			    					  players[6][currentBoard-1].setScore(time);
				    				  setldrs(currentBoard);
				    				  if(soundON) {
				    					  maintheme.stop();
				    					  play("bombombakudan");
				    					  maintheme.start();
				    				  }
			    				  }
			    			  }
			    			  
			    			  if (currentBoard == 2) {
			    				  if(iboard.lost&&soundON) {
			    					  maintheme.stop();
			    					  play("consequences");
			    					  iboard.lost=false;
			    					  t1.stop();
			    				  }
			    				  if (iboard.hasWon()) { 
			    					  t1.stop();
			    					  players[6][currentBoard-1].setScore(time);
				    				  setldrs(currentBoard);
				    				  if(soundON) {
				    					  maintheme.stop();
				    					  play("bombombakudan");
				    					  maintheme.start();
				    				  }
			    				  }
			    			  }
			    			  
			    			  if (currentBoard == 3) {
			    				  if(hboard.lost&&soundON) {
			    					  maintheme.stop();
			    					  play("consequences");
			    					  hboard.lost=false;
			    					  t1.stop();
			    				  }
			    				  if (hboard.hasWon()) {
			    					  t1.stop();
			    					  players[6][currentBoard-1].setScore(time);
			    					  setldrs(currentBoard);
				    				  if(soundON) {
				    					  maintheme.stop();
				    					  play("bombombakudan");
				    					  maintheme.start();
				    				  }
			    				  }
			    			  }
			    		  
			    		  
			    		  }
			    	  };
			    	  Timer t1 = new Timer(1000, updateTimer);
	   
	   public TopBar() {
		   super.setLayout(new FlowLayout(FlowLayout.CENTER));
		   super.add(FlagCount);
		   super.setBackground(Color.WHITE);
		   FlagCount.setOpaque(true);
		   FlagCount.setForeground(textcolor);
		   FlagCount.setBackground(Color.WHITE);
		   FlagCount.setPreferredSize(new Dimension(150, 50));
		   
		   ActionListener updateFlag = new ActionListener() {
	    		  public void actionPerformed(ActionEvent e) {
	    			  if (currentBoard == 1) {
	    				  FlagCount.setText("Flags Left:"+eboard.numFlags);
	    			  }
	    			  
	    			  if (currentBoard == 2) {
	    				  FlagCount.setText("Flags Left:"+iboard.numFlags);
	    			  }
	    			  
	    			  if (currentBoard == 3) {
	    				  FlagCount.setText("Flags Left:"+hboard.numFlags);
	    			  }
	    		  }
	    	  };
	    	  Timer t2 = new Timer(150, updateFlag);
	    	  t2.start();
		   
		   super.add(btnNewGame);
		   btnNewGameListener resetListener = new btnNewGameListener();
		   btnNewGame.addActionListener(resetListener);
		   btnNewGame.setPreferredSize(dim);
		   btnNewGame.setBackground(Color.WHITE);
		   super.add(pauseButton);
		   pauseButtonListener pauseListener = new pauseButtonListener();
		   pauseButton.addActionListener(pauseListener);
		   pauseButton.setPreferredSize(dim);
		   pauseButton.setBackground(Color.WHITE);
		   super.add(TimeElapsed);
		   TimeElapsed.setOpaque(true);
		   TimeElapsed.setForeground(textcolor);
		   TimeElapsed.setBackground(Color.WHITE);
		   TimeElapsed.setText("Time:   "); 
		   TimeElapsed.setPreferredSize(new Dimension(150, 50));   
	   }
	   
	   public void setldrs(int diff) {
		   int i =0;
			  while (players[i][diff-1].getScoreint()<players[6][diff-1].getScoreint()) i++;
			  System.out.printf("%d",i);
			      for (int x = 4; x>=i;x--){
			          players[x+1][diff-1].setPlayer(players[x][diff-1]);
			      }
			      players[i][diff-1].setPlayer(players[6][diff-1]);
	   }
	   
	   //Listener for Reset Button
	   private class btnNewGameListener implements ActionListener {			
		
			      @Override
			      public void actionPerformed(ActionEvent evt) {
			    	  if (currentBoard == 1) {
			    		  eboard.init();
	    			  }
	    			  
	    			  if (currentBoard == 2) {
	    				  iboard.init();
	    			  }
	    			  
	    			  if (currentBoard == 3) {
	    				  hboard.init();
	    			  }
			    	  time = 0;
			    	  t1.restart();
			    	  timerRunning = true;
			    	  if(soundON) {
			    		  maintheme.start();
			        	  maintheme.loop(Clip.LOOP_CONTINUOUSLY);
			    	  }
			      }
	   }
	   
	   //Listener for the Pause Button
	   private class pauseButtonListener implements ActionListener {
		   @Override
		      public void actionPerformed(ActionEvent evt) {
			  if (timerRunning==true) {  
				  t1.stop();
				  timerRunning = false;
				  pauseButton.setIcon(resume);
			  }
			  else {
				  t1.start();
				  timerRunning = true;
				  pauseButton.setIcon(pause);
			  }
		   }
	   }
	   

   }
   
   public class LoginBar extends JPanel{
	   JTextField playerSet = new JTextField("Player1");
	   JButton confirm = new JButton("Confirm name");
	   JLabel username = new JLabel("Enter Player Name:");
	   public LoginBar() {
		   super.setLayout(new FlowLayout(FlowLayout.LEFT));
		   super.setBackground(Color.WHITE);
		   super.add(username);
		   username.setForeground(textcolor);
		   super.add(playerSet);
		   playerSet.setEditable(true);
		   playerSet.setPreferredSize(new Dimension(70,20));
		   super.add(confirm);
		   confirm.setBackground(bg);
		   confirm.setForeground(Color.WHITE);
		   
		   confirm.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e) {
		        	 players[6][currentBoard-1].setName(playerSet.getText());
		         }
		      });
	   }
	   
   }
	   
}