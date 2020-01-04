/*
 * Copyright (c) 2019. UltraDev
 */

package net.ultradev.prisoncore.mysql;

import java.sql.*;
import java.util.UUID;

public class MySQLManager {
    private static Connection con = null;

    public static void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://149.56.29.148:3306/s890_ultraprison",
                    "u890_GyMLzVFV4E", "nWnxvZInMYhAzosM6dXwzuoF");
            Statement stmt = con.createStatement();
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS `players` ( `id` varchar(36) NOT NULL,  `balance` text NOT NULL, `tokens` text NOT NULL, `settings` text NOT NULL, `rank` int NOT NULL, `prestige` int NOT NULL, `mastery` int NOT NULL, `autoselltime` int, `gang` text, `miniminers` text, PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void execute(String query) {
        try {
            Statement stmt = con.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet executeQuery(String query) {
        try {
            Statement stmt = con.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getString(UUID id, String key) {
        ResultSet set = executeQuery("SELECT " + key + " FROM players WHERE id='" + id.toString() + "'");
        try {
            if (set.next()) {
                System.out.println(set.getString(1));
                set.getString(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
