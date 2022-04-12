package minesweeper;
public class Player{
	public String name;
	private int score;
	
	public Player() {
		this.name = "";
		this.score = 32767; 
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public void setScore(int score) {
		this.score=score;
	}
	
	public String getScore() {
		if(this.score==32767) {
		return "  Best Time: 0 seconds";
		}
		else {
		return "  Best Time: "+this.score+" seconds";
		}
	}
	
	public int getScoreint() {
		return this.score;
	}
	
	public void setPlayer(Player another) {
			this.score=another.score;
			this.name=another.name;
	}
	
	public void reset() {
		this.name = "";
		this.score = 32767;
	}
}