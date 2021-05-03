package me.lowlauch.Walo.SQL;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Database
{
    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("SELECT * FROM " + MySQL.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createTable() {
        try {
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS " + MySQL.table + " (UUID VARCHAR(100),NAME VARCHAR(100),KILLS INT(100))");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(final UUID uuid, Player player) {
        try {
            /*
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("SELECT * FROM " + MySQL.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();*/
            if (!playerExists(uuid)) {
                PreparedStatement insert = MySQL.getConnection()
                        .prepareStatement("INSERT INTO " + MySQL.table + " (UUID,NAME,KILLS) VALUES (?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setInt(3, 0);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKills(UUID uuid) {
        try {
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("UPDATE " + MySQL.table + " SET KILLS=? WHERE UUID=?");
            statement.setInt(1, getKills(uuid)+1);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getKills(UUID uuid) {
        try {
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("SELECT * FROM " + MySQL.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt("KILLS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
