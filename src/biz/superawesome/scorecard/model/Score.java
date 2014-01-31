package biz.superawesome.scorecard.model;

import com.j256.ormlite.field.DatabaseField;

public class Score {

	@DatabaseField(generatedId = true)
	public int id;

	@DatabaseField(foreign = true)
	public Hole hole;

	@DatabaseField(foreign = true, foreignAutoRefresh=true, maxForeignAutoRefreshLevel=3)
	public Player player;

	@DatabaseField
	public int score = 0;

	@DatabaseField
	public int circles = 0;
	
	Score() {}
	
	public Score(Hole h, Player p) {
		hole = h;
		player = p;
	}
}