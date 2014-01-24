package biz.superawesome.scorecard.model;

import com.j256.ormlite.field.DatabaseField;

public class PlayerRound {
	
	@DatabaseField(generatedId = true)
	public int id;

	@DatabaseField(foreign = true)
	public Player player;

	@DatabaseField(foreign = true)
	public Round round;
	
	PlayerRound() {}
	
	public PlayerRound(Player player, Round round) {
		this.player = player;
		this.round = round;
	}

	public String toString() {
		//return "player " + player.id + "; round " + round.id + "\n";
		return "player " + player.id + "\n";
	}
}
