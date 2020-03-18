package bettercommands.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import bettercommands.objects.entries.CoordsEntry;
import bettercommands.objects.entries.TeleportHistoryEntry;
import bettercommands.utils.Logger;

public class DatabaseManager {
	
	private ConnectionSource source;
	private Dao<CoordsEntry, Integer> coordsDao;
	private Dao<TeleportHistoryEntry, Integer> historyDao;
	
	public DatabaseManager(Database database) throws SQLException {
		this.source = database.getConnection();
		this.coordsDao = DaoManager.createDao(source, CoordsEntry.class);
		this.historyDao = DaoManager.createDao(source, TeleportHistoryEntry.class);
	}
	
	public void shutdown() {
		try {
			source.close();
			Logger.info("Database connection closed.");
		} catch (IOException e) {
			Logger.error("Failed close database connection: " + e.getLocalizedMessage());
		}
	}
	
	/*
	 * Coords
	 */
	
	public List<CoordsEntry> getCoords(String name) {
		try {
			QueryBuilder<CoordsEntry, Integer> builder = coordsDao.queryBuilder();
			Where<CoordsEntry, Integer> where = builder.where();
			
			where.eq("name", name);
			
			return builder.query();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveCoords(CoordsEntry entry) {
		try {
			coordsDao.create(entry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public CoordsEntry removeCoords(CoordsEntry entry) {
		try {
			QueryBuilder<CoordsEntry, Integer> builder = coordsDao.queryBuilder();
			Where<CoordsEntry, Integer> where = builder.where();
			
			where.eq("name", entry.getName()).and();
			where.eq("x", entry.getX()).and();
			where.eq("y", entry.getY()).and();
			where.eq("z", entry.getZ());
			
			CoordsEntry full = builder.queryForFirst();
			if(full == null) return null;
			
			DeleteBuilder<CoordsEntry, Integer> builder2 = coordsDao.deleteBuilder();
			builder2.setWhere(where);
			
			builder2.delete();
			return full;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Teleports history
	 */
	
	public List<TeleportHistoryEntry> getHistory(String name) {
		try {
			QueryBuilder<TeleportHistoryEntry, Integer> builder = historyDao.queryBuilder();
			Where<TeleportHistoryEntry, Integer> where = builder.where();
			
			where.eq("name", name);
			
			return builder.query();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveHistory(TeleportHistoryEntry entry) {
		try {
			historyDao.create(entry);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
