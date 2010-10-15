package edu.sjtu.cse.codestorm.test;

import java.util.ArrayList;
import java.util.List;

import edu.sjtu.cse.codestorm.bean.Capital;
import edu.sjtu.cse.codestorm.bean.Order;
import edu.sjtu.cse.codestorm.bean.OrderType;
import edu.sjtu.cse.codestorm.bean.Product;
import edu.sjtu.cse.codestorm.db.CapitalDb;
import edu.sjtu.cse.codestorm.db.OrderDb;
import edu.sjtu.cse.codestorm.db.ProductDb;

public class DbTest {
    public static void testDb() {
        CapitalDb capitalDb = new CapitalDb();
        OrderDb orderDb = new OrderDb();
        ProductDb productDb = new ProductDb();
        
        Capital capital = new Capital();
        capital.setBalance(10);
        capitalDb.insertCapital(capital);
        
        Capital outCapital = capitalDb.getCapital();
        System.out.println(outCapital.getBalance());
        
        outCapital.setBalance(200);
        capitalDb.updateCapital(outCapital);
        
        outCapital = capitalDb.getCapital();
        System.out.println(outCapital.getBalance());
        
        Order order = new Order(1, "order", 10, 10, OrderType.Bid, true);
        List<Order> orders = new ArrayList<Order>();
        orders.add(order);
        
        orderDb.insertOrders(orders);
        
        List<Order> outOrders = orderDb.getOrders(1);
        for(Order item : outOrders) {
            System.out.println(item.getID());
            System.out.println(item.getName());
            System.out.println(item.getAmount());
            System.out.println(item.getRound());
            System.out.println(item.getOrderType());
        }
        
        Product product = new Product("lime", 10);
        List<Product> products = new ArrayList<Product>();
        products.add(product);
        
        productDb.insertProducts(products);
        
        List<Product> outProducts = productDb.getProducts();
        for(Product item : outProducts) {
            System.out.println(item.getName());
            System.out.println(item.getAmount());
        }
        
        outProducts.get(0).setAmount(1);
        
        productDb.updateProducts(outProducts);
        List<Product> result = productDb.getProducts();
        for(Product item1 : result) {
            System.out.println(item1.getName());
            System.out.println(item1.getAmount());
        }
        
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        testDb();
    }
}