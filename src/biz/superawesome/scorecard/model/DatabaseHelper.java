package biz.superawesome.scorecard.model;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "scoreCard.db";

	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Hole.class);
			TableUtils.createTable(connectionSource, Player.class);
			TableUtils.createTable(connectionSource, Round.class);
			TableUtils.createTable(connectionSource, Score.class);
			TableUtils.createTable(connectionSource, PlayerScore.class);
			TableUtils.createTable(connectionSource, PlayerRound.class);
			// insert some data
			RuntimeExceptionDao<Player, Integer> playerDao = getRuntimeExceptionDao(Player.class);
			RuntimeExceptionDao<Round, Integer> roundDao = getRuntimeExceptionDao(Round.class);
			playerDao.create(new Player("We"));
			playerDao.create(new Player("They"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {/*
		try {
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}*/
	}

	
	// move to model?
	public PreparedQuery<Player> getRoundPlayersPreparedQuery() throws SQLException { 

		RuntimeExceptionDao<PlayerRound, Integer> playerRoundDao = getRuntimeExceptionDao(PlayerRound.class);
		QueryBuilder<PlayerRound, Integer> qb_pr = playerRoundDao.queryBuilder();
		qb_pr.selectColumns("player_id");
		SelectArg userSelectArg = new SelectArg();
		qb_pr.where().eq("round_id", userSelectArg);

		RuntimeExceptionDao<Player, Integer> playerDao = getRuntimeExceptionDao(Player.class);
		QueryBuilder<Player, Integer> qb_p = playerDao.queryBuilder();
		qb_p.where().in("_id", qb_pr);
		return qb_p.prepare();
	}
}
