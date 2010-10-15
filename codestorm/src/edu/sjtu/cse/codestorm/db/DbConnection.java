package edu.sjtu.cse.codestorm.db;

import java.sql.*;

/**
 * DbConn class Generate the db connection
 * 
 * @author <a href="mailto:weichenaccount@hotmail.com">Wei Chen</a>
 * 
 */
public class DbConnection {
    private Connection conn = null;
    private final static String usrName = "mysql";
    private final static String pwd = "mysql";
    private final static String dbUrl = "jdbc:mysql://127.0.0.1:3306/citistar";

    /**
     * Return the connection of db
     * 
     * @return Db connection
     */
    public Connection getConnection() {
        this.initConn();
        return conn;
    }

    /**
     * Initialize the db connection
     */
    public void initConn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbUrl, usrName, pwd);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        String statement = "select ID from stock where StockCode = ?";
        DbConnection dbConnection = new DbConnection();
        Connection conn = dbConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(statement);
        ps.setString(1, "600036");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }

        rs.close();
        ps.close();
        conn.close();
    }
}
