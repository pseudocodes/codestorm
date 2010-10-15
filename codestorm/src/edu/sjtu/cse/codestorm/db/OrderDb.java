package edu.sjtu.cse.codestorm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.sjtu.cse.codestorm.bean.Order;
import edu.sjtu.cse.codestorm.bean.OrderType;

public class OrderDb {
    private DbConnection dbConn;
    
    public List<Order> getOrders(int round) {
        List<Order> orders = new ArrayList<Order>();
        dbConn = new DbConnection();
        String statement = "select * from fruit where round = ?";
        Connection conn = dbConn.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.setInt(1, round);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                long price = rs.getLong(3);
                int amount = rs.getInt(4);
                OrderType orderType = ((rs.getInt(5) == OrderType.Bid.ordinal()) ?
                        OrderType.Bid : OrderType.Offer);
                boolean isDealed = rs.getBoolean(6);
                
                Order order = new Order(id, round, name, price, amount, orderType,
                        isDealed);
                orders.add(order);
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
        
        return orders;
    }
    
    public void insertOrders(List<Order> orders) {
        dbConn = new DbConnection();
        String statement = "insert into order(round, name, price, amount, orderType, isDealed) values (?, ?, ?, ?, ?, ?);";
        Connection conn = dbConn.getConnection();
        
        try {
            for (Order order : orders) {
                PreparedStatement ps = conn.prepareStatement(statement);
                ps.setInt(1, order.getRound());
                ps.setString(2, order.getName());
                ps.setLong(3, order.getPrice());
                ps.setInt(4, order.getAmount());
                ps.setInt(5, order.getOrderType().ordinal());
                ps.setBoolean(6, order.isDealed());

                ps.executeUpdate();
                ps.close();
            }
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