package edu.sjtu.cse.codestorm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.sjtu.cse.codestorm.bean.Product;

public class ProductDb {
    private DbConnection dbConn;
    
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<Product>();
        dbConn = new DbConnection();
        String statement = "select * from product where amount > 0";
        Connection conn = dbConn.getConnection();
        
        try {
            PreparedStatement ps = conn.prepareStatement(statement);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                int amount = rs.getInt(2);
                
                Product product = new Product(name, amount);
                products.add(product);
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
        
        return products;
    }
    
    public void insertProducts(List<Product> products) {
        dbConn = new DbConnection();
        String statement = "insert into product(name, amount) values (?, ?);";
        Connection conn = dbConn.getConnection();
        
        try {
            for (Product product : products) {
                PreparedStatement ps = conn.prepareStatement(statement);
                ps.setString(1, product.getName());
                ps.setInt(2, product.getAmount());

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
    
    public void updateProducts(List<Product> products) {
        dbConn = new DbConnection();
        String statement = "update product set amount = ? where name = ?";
        Connection conn = dbConn.getConnection();
        
        try {
            for (Product product : products) {
                PreparedStatement ps = conn.prepareStatement(statement);
                ps.setInt(1, product.getAmount());
                ps.setString(2, product.getName());

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