package biz.superawesome.scorecard.model;

import com.j256.ormlite.field.DatabaseField;

public class PlayerScore {
	
	@DatabaseField(generatedId = true)
	public int id;

	@DatabaseField(foreign = true)
	public Player player;

	@DatabaseField(foreign = true)
	public Score score;
	
	PlayerScore() {}
	
	public PlayerScore(Player player, Score score) {
		this.player = player;
		this.score = score;
	}

}