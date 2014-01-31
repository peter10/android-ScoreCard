package biz.superawesome.scorecard.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class Hole {

	@DatabaseField(generatedId = true)
	public int id;

	@DatabaseField(foreign = true, foreignAutoRefresh=true, maxForeignAutoRefreshLevel=3)
	public Round round;
	
	@ForeignCollectionField(eager = true)
	public ForeignCollection<Score> scores;

	@DatabaseField
	public String name;

	@DatabaseField
	public int order;

	@DatabaseField
	public int par;
	
	Hole() {}
	
	public Hole(Round round, String name, int order) {
		this.round = round;
		this.name = name;
		this.order = order;
	}

	public String toString() {
		String returnValue = name;
		// add scores if they have been scored
		for (Score s : scores) {
			if (s.score > 0) {
				returnValue += " " + s.player.toString() + ": " + s.score; 
			}
		}
		return returnValue;
	}
}
