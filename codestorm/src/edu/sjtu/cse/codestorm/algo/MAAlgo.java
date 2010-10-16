package edu.sjtu.cse.codestorm.algo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.sjtu.cse.codestorm.db.DbConnection;

public class MAAlgo {

    public static void calBenefit(int day) throws Exception {
        double[] price = new double[1894];
        double[] MA = new double[1894];
        String statement = "select EndPrice, MA" + day
                + " from stockinfo_day order by Date asc";
        DbConnection dbConnection = new DbConnection();
        Connection conn = dbConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(statement);

        ResultSet rs = ps.executeQuery();
        int i = 0;
        while (rs.next()) {
            price[i] = rs.getDouble(1);
            MA[i] = rs.getDouble(2);
            i++;
        }

        rs.close();
        ps.close();
        conn.close();

        double money = 100;
        double stock = 0;
        i = 0;

        stock = money / price[i];
        money = 0;
        i++;

        while (i < price.length - 1) {
            int j = i;
            if (money == 0) {
                while (j < price.length - 2 && MA[j] < MA[j + 1]) {
                    j++;
                }
                money = stock * price[++j];
                stock = 0;
            } else if (stock == 0) {
                while (j < price.length - 2 && MA[j] > MA[j + 1]) {
                    j++;
                }
                stock = money / price[++j];
                money = 0;
            } else {
                throw new Exception("stock and money all == 0");
            }
            i = ++j;
        }
        System.out.print("Result with MA" + day + ": ");
        if (money != 0) {
            System.out.println(money);
        } else {
            System.out.println(stock * price[price.length - 1]);
        }
        System.out.println("Expected:         " + price[price.length - 1] * 100
                / price[0] + "\n");
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        MAAlgo.calBenefit(5);
        MAAlgo.calBenefit(10);
        MAAlgo.calBenefit(20);
        MAAlgo.calBenefit(60);
    }

}
