package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection  {

    private Connection con  = null;
    private String table = "";

    public DBConnection() {
        connect();
    }

    public DBConnection(String table) {
        this.table = table;
        connect();
    }

    public void connect(){
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + table, "postgres", "postgres");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return this.con;
    }
}
