package biz.superawesome.scorecard.model;

import com.j256.ormlite.field.DatabaseField;

public class Player {
	
	public final static String COLUMN_NAME = "name";

	@DatabaseField(generatedId = true, columnName = "_id")
	public int id;
	
	@DatabaseField(columnName = COLUMN_NAME)
	public String name;
	
	Player() {}
	
	public Player(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Player))
            return false;

        Player rhs = (Player) obj;
        return (id == rhs.id);
    }

}