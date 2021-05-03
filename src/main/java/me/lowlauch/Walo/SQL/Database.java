package me.lowlauch.Walo.SQL;

import me.lowlauch.Walo.Main;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Database
{
    Main plugin = Main.getInstance();

    public void createTable()
    {
        PreparedStatement ps;
        try
        {
            String statement = "CREATE TABLE IF NOT EXISTS walo (name VARCHAR(100),uuid VARCHAR(100),kills INT(100)," +
                    "PRIMARY KEY (name) )";
            ps = plugin.sql.getConnection().prepareStatement(statement);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void createPlayer(Player p)
    {
        PreparedStatement ps;
        try
        {
            UUID uuid = p.getUniqueId();
            if(!exists(uuid))
            {
                String statement = "INSERT IGNORE INTO walo (name,uuid) VALUES (?,?)";
                ps = plugin.sql.getConnection().prepareStatement(statement);
                ps.setString(1, p.getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            }

        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid)
    {
        try
        {
            String statement = "SELECT * FROM walo WHERE uuid=?";
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(statement);
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            return results.next();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void addKill(UUID uuid)
    {
        try
        {
            String statement = "UPDATE walo SET kills=? WHERE uuid=?";
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(statement);
            ps.setInt(1, getKills(uuid)+1);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getKills(UUID uuid)
    {
        try
        {
            String statement = "SELECT kills FROM walo WHERE uuid=?";
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(statement);
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            int kills = 0;
            if(rs.next())
            {
                kills = rs.getInt("kills");
                return kills;
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }
}
