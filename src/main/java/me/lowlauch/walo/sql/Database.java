package me.lowlauch.walo.sql;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Database {
    public boolean exists(String key, String value) {
        try {
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("SELECT * FROM " + MySQL.table + " WHERE " + key + "=?");
            statement.setString(1, value);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createTable(String keys) {
        try {
            // Create a table
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS " + MySQL.table + " (" + keys + ")");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player, String keys, String values) {
        try {
            // If playerdata from player doesn't exists create it
            if (!exists("UUID", player.getUniqueId().toString())) {
                PreparedStatement insert = MySQL.getConnection()
                        .prepareStatement("INSERT INTO " + MySQL.table + " (UUID,NAME,KILLS) VALUES (?,?,?)");
                insert.setString(1, player.getUniqueId().toString());
                insert.setString(2, player.getName());
                insert.setInt(3, 0);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setInt(UUID uuid, String key, int value) {
        try {
            // Set int of a element
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("UPDATE " + MySQL.table + " SET " + key + "=? WHERE UUID=?");
            statement.setInt(1, value);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getInt(UUID uuid, String key) {
        try {
            // Get int of a element
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("SELECT * FROM " + MySQL.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            return results.getInt(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void orderBy(String key) {
        // Order table by highest kills
        try {
            PreparedStatement statement = MySQL.getConnection()
                    .prepareStatement("ALTER TABLE " + MySQL.table + " ORDER BY " + key + " DESC");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
