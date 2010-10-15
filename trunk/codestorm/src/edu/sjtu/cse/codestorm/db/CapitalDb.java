package edu.sjtu.cse.codestorm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.sjtu.cse.codestorm.bean.Capital;
import edu.sjtu.cse.codestorm.bean.Order;
import edu.sjtu.cse.codestorm.bean.OrderType;

public class CapitalDb {
    private DbConnection dbConn;
    
    public Capital getCapital() {
        Capital capital = new Capital();
        dbConn = new DbConnection();
        String statement = "select * from capital";
        Connection conn = dbConn.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement(statement);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long balance = rs.getLong(1);
                capital.setBalance(balance);
            }

            rs.close();
            ps.close();
        } catch(Exception exp) {
            exp.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception exp) {}
            }
        }
        
        return capital;
    }
    
    public void insertCapital(Capital capital) {
        dbConn = new DbConnection();
        String statement = "insert into capital(balance) values (?);";
        Connection conn = dbConn.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setLong(1, capital.getBalance());

            ps.executeUpdate();
            ps.close();
        } catch(Exception exp) {
            exp.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception exp) {}
            }
        }
    }
    
    public void updateCapital(Capital capital) {
        dbConn = new DbConnection();
        String statement = "update capital set balance = ?";
        Connection conn = dbConn.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setLong(1, capital.getBalance());

            ps.executeUpdate();
            ps.close();
        } catch(Exception exp) {
            exp.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception exp) {}
            }
        }
    }
}