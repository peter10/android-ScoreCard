package biz.superawesome.scorecard.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class Round {

	@DatabaseField(generatedId = true)
	public int id;
	
	@DatabaseField
	public String name;

	@DatabaseField
	public Date date;
	
	@ForeignCollectionField(eager = true)
    public ForeignCollection<Hole> holes;
	
	Round() {}
	
	public Round(String name) {
		this.name = name;
		this.date = new Date();
	}

	public String toString() {
		String returnValue = "";
	    SimpleDateFormat sdf = new SimpleDateFormat ("E yyyy.MM.dd");
	    returnValue += sdf.format(date);
	    //returnValue += ", " + getOverUnder() + " after " + getHolesPlayed(); 
	    return returnValue;
	}
	/*
	public int getOverUnder() {
		int overUnder = 0;
		for (Score s : scores.toArray(new Score[]{})) {
			if (s.score > 0) {
				overUnder = overUnder + s.score - 3;
			}
		}
		return overUnder;
	}
	
	public int getHolesPlayed() {
		int holesPlayed = 0;
		for (Score s : scores.toArray(new Score[]{})) {
			if (s.score > 0) {
				holesPlayed++;
			}
		}
		return holesPlayed;
	}
	
	public int getTotal() {
		int total = 0;
		for (Score s : scores.toArray(new Score[]{})) {
			total += s.score;
		}
		return total;
	}*/
	
}