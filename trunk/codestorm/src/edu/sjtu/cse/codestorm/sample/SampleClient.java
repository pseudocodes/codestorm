package edu.sjtu.cse.codestorm.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 * Sample ITRB Java Client
 */
public class SampleClient 
{       
    private final static String SERVERHOST = "ga209c1n13";
    private final static int SERVERPORT = 12297;    
    private final static String TEAMNAME = "there";
    private final static String TEAMKEY = "catch22";
    
    private final static String ORDERID_PREFIX = TEAMNAME;
    int orderid_counter = 1;
    String inputLine = "";
    BufferedReader in = null;
    Socket skt = null;
    PrintStream out = null;
    int current_round;
    int total_rounds;
    
    List<String> products = new ArrayList<String>();    
    Map<String, Properties> supply_product_price_quantity= new HashMap<String, Properties>();
    Map<String, Properties> demand_product_price_quantity= new HashMap<String, Properties>();

    
    public SampleClient()
    {
        try 
        {
            skt = new Socket(SERVERHOST, SERVERPORT);
            out = new PrintStream( skt.getOutputStream()) ;
            in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            System.out.println("Client connected to Sever: "+SERVERHOST+" : "+SERVERPORT);
        }
        catch(Exception e) 
        {
            System.out.print("Whoops! Error connecting to "+SERVERHOST+" : "+SERVERPORT);
            e.printStackTrace();
        }
    }

    
    /*
     * Main Controller
     */
    public static void main(String[] args) throws Exception
    {
        SampleClient myclient = new SampleClient();
        myclient.login();
        myclient.playgame();

    }   

    
    /*
     * Login to the server. Send the Team details and check for Welcome msg from Server
     */
    public void login()
    {
        try
        {
            String welcome_msg = "{\"Login\":{\"teamName\":\""+TEAMNAME+"\",\"key\":\""+TEAMKEY+"\"}}";     
            out.println(welcome_msg);
            inputLine = in.readLine();
            System.out.println("Input Received: "+inputLine);

            if(checkMessageType(inputLine, "Welcome"))
            {
                System.out.println("Welcome Message Received...");
            }
            else
            {
                System.out.println("Terminating client due to unsuccessful Welcome");
                System.exit(1);
            }
        }
        catch(Exception e) 
        {
            System.out.print("Whoops! Error connecting to "+SERVERHOST+" : "+SERVERPORT);
            e.printStackTrace();
        }
    }

    
    /*
     * Start playing the real game
     */
    public void playgame()
    {
        try 
        {
            while ((inputLine = in.readLine()) != null) 
            {
                System.out.println("Input Received: "+inputLine);

                if(checkMessageType(inputLine, "RoundStart"))
                {
                    handeRoundStart();
                }
                else if(checkMessageType(inputLine, "Ack"))
                {
                    handleOrderAck();                   
                }
                else if(checkMessageType(inputLine, "RoundEnd"))
                {
                    handeRoundEnd();
                }
                else if(checkMessageType(inputLine, "GameEnd"))
                {
                    handleGameEnd();
                }
            }
        }
        catch(Exception e) 
        {
            System.out.print("Whoops! It didn't work!\n");
            e.printStackTrace();
        }   
    }


    // This is one of the Strategies -Its really stupid right now :))
    public void takeOrderDecision()
    {
        String PRODUCT_ID = "";
        String QUANTITY = "";
        String PRICE = "";
        String one_buy_order = "";
        String one_sell_order = "";
        Random rand = new Random();     
        int random_product = rand.nextInt(products.size());
        System.out.println("Product Size :"+products.size());
        System.out.println("Random product :"+random_product);
        String orderId = TEAMNAME+orderid_counter;
        
        if(current_round == 1)
        {
            System.out.println("First Round..So buying");
            // Buy only
            String random_product_id = products.get(random_product);
            Properties pq = (Properties)demand_product_price_quantity.get(random_product_id);
            double quantity = Double.parseDouble(pq.getProperty("quantity"));
            double price = Double.parseDouble(pq.getProperty("price"));
            price = price + 0.1;
            
            PRODUCT_ID = random_product_id;
            QUANTITY = "1";
            PRICE = Double.toString(price);
            one_buy_order = "{\"side\":\"BUY\",\"teamName\":\""+TEAMNAME+"\",\"orderId\":\""+orderId+"\",\"productId\":\""+PRODUCT_ID+"\",\"price\":"+PRICE+",\"quantity\":"+QUANTITY+"}";
        }
        else if (current_round == total_rounds)
        {
            // sell only
            System.out.println("Last Round..So selling");
            String random_product_id = products.get(random_product);
            Properties pq = (Properties)supply_product_price_quantity.get(random_product_id);
            double quantity = Double.parseDouble(pq.getProperty("quantity"));
            double price = Double.parseDouble(pq.getProperty("price"));
            price = price - 0.1;
            
            PRODUCT_ID = random_product_id;
            QUANTITY = "1";
            PRICE = Double.toString(price); 
            one_sell_order = "{\"side\":\"SELL\",\"teamName\":\""+TEAMNAME+"\",\"orderId\":\""+orderId+"\",\"productId\":\""+PRODUCT_ID+"\",\"price\":"+PRICE+",\"quantity\":"+QUANTITY+"}";
        }
        else
        {
            if( (current_round % 2) == 0)
            {
                //buy
                System.out.println("Some Round..buying");
                
                String random_product_id = products.get(random_product);
                Properties pq = (Properties)demand_product_price_quantity.get(random_product_id);
                double quantity = Double.parseDouble(pq.getProperty("quantity"));
                double price = Double.parseDouble(pq.getProperty("price"));
                price = price + 0.1;
                
                PRODUCT_ID = random_product_id;
                QUANTITY = "1";
                PRICE = Double.toString(price); 
                one_buy_order = "{\"side\":\"BUY\",\"teamName\":\""+TEAMNAME+"\",\"orderId\":\""+orderId+"\",\"productId\":\""+PRODUCT_ID+"\",\"price\":"+PRICE+",\"quantity\":"+QUANTITY+"}";
            }
            else
            {
                System.out.println("Some Round..selling");
                
                //sell
                String random_product_id = products.get(random_product);
                Properties pq = (Properties)supply_product_price_quantity.get(random_product_id);
                double quantity = Double.parseDouble(pq.getProperty("quantity"));
                double price = Double.parseDouble(pq.getProperty("price"));
                price = price - 0.1;
                
                PRODUCT_ID = random_product_id;
                QUANTITY = "1";
                PRICE = Double.toString(price);
                one_sell_order = "{\"side\":\"SELL\",\"teamName\":\""+TEAMNAME+"\",\"orderId\":\""+orderId+"\",\"productId\":\""+PRODUCT_ID+"\",\"price\":"+PRICE+",\"quantity\":"+QUANTITY+"}";
                
            }           
        }
                        
        System.out.println("Supply Size: "+supply_product_price_quantity.size());
        System.out.println("Demand Size: "+demand_product_price_quantity.size());

        String order = "{\"Order\":"+ "{\"bids\":["+one_buy_order+"],"+  "\"asks\":["+one_sell_order+"]}}";

        System.out.println("Ther order sent is:\n"+order);

        out.println(order);
    }
    
    
    public void updateSupplyProductPriceQuantityMap(JSONArray supply)
    {
        try
        {
            for( int supply_count = 0; supply_count < supply.length(); supply_count++)
            {
                JSONObject supply_entry = supply.getJSONObject(supply_count);
                Properties pq = new Properties();
                pq.put("price", supply_entry.getString("price"));
                pq.put("quantity", supply_entry.getString("quantity"));
                supply_product_price_quantity.put(supply_entry.getString("productId"), pq);
                if(!products.contains(supply_entry.getString("productId")))
                {
                    products.add(supply_entry.getString("productId"));
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    
    public void updateDemandProductPriceQuantityMap(JSONArray demand)
    {
        try
        {
            for( int demand_count = 0; demand_count < demand.length(); demand_count++)
            {
                JSONObject demand_entry = demand.getJSONObject(demand_count);
                Properties pq = new Properties();
                pq.put("price", demand_entry.getString("price"));
                pq.put("quantity", demand_entry.getString("quantity"));
                demand_product_price_quantity.put(demand_entry.getString("productId"), pq);
                if(!products.contains(demand_entry.getString("productId")))
                {
                    products.add(demand_entry.getString("productId"));
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void handeRoundStart()
    {
        try
        {
            System.out.println("Round Start");
            JSONObject json = new JSONObject(inputLine);
            JSONObject round_start = json.getJSONObject("RoundStart");

            String rnumber = round_start.getString("roundNumber");
            String rtotal = round_start.getString("roundTotal");
            JSONArray supply = round_start.getJSONArray("supply");
            JSONArray demand = round_start.getJSONArray("demand");
            JSONObject portfolio = round_start.getJSONObject("portfolio");
            JSONArray positions = portfolio.getJSONArray("positions");


            System.out.println("Round Number: "+rnumber);
            System.out.println("Round Total: "+rtotal);
            orderid_counter = Integer.parseInt(rnumber);
            current_round = Integer.parseInt(rnumber);
            total_rounds = Integer.parseInt(rtotal);
            
            updateDemandProductPriceQuantityMap(demand);
            updateSupplyProductPriceQuantityMap(supply);
            takeOrderDecision();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void handeRoundEnd()
    {
        try
        {
            System.out.println("Round End");
            JSONObject json = new JSONObject(inputLine);
            JSONObject round_end = json.getJSONObject("RoundEnd");

            String rnumber = round_end.getString("roundNumber");
            String rtotal = round_end.getString("roundTotal");

            System.out.println("Round Number: "+rnumber);
            System.out.println("Round Total: "+rtotal);

            JSONArray win_asks = round_end.getJSONArray("winningAsks");
            JSONArray win_bids = round_end.getJSONArray("winningBids");
            JSONArray transactions = round_end.getJSONArray("marketTransactions");

            System.out.println("\nWinnind Asks- ");
            for( int win_asks_count = 0; win_asks_count < win_asks.length(); win_asks_count++)
            {
                JSONObject win_asks_entry = win_asks.getJSONObject(win_asks_count);
                System.out.println("side: "+win_asks_entry.getString("side"));
                System.out.println("teamname: "+win_asks_entry.getString("teamName"));
                System.out.println("orderId: "+win_asks_entry.getString("orderId"));
                System.out.println("productId: "+win_asks_entry.getString("productId"));
                System.out.println("price: "+win_asks_entry.getString("price"));
                System.out.println("quantity: "+win_asks_entry.getString("quantity"));
                System.out.println("\n");
            }
            System.out.println("\nWinnind Asks End- \n\n");


            System.out.println("\nWinnind Bids- ");
            for( int win_bids_count = 0; win_bids_count < win_bids.length(); win_bids_count++)
            {
                JSONObject win_bids_entry = win_bids.getJSONObject(win_bids_count);
                System.out.println("side: "+win_bids_entry.getString("side"));
                System.out.println("teamname: "+win_bids_entry.getString("teamName"));
                System.out.println("orderId: "+win_bids_entry.getString("orderId"));
                System.out.println("productId: "+win_bids_entry.getString("productId"));
                System.out.println("price: "+win_bids_entry.getString("price"));
                System.out.println("quantity: "+win_bids_entry.getString("quantity"));
                System.out.println("\n");
            }
            System.out.println("\nWinnind Bids End- \n\n");


            System.out.println("\nTransactions - ");
            for( int trans_count = 0; trans_count < transactions.length(); trans_count++)
            {
                JSONObject trans_entry = transactions.getJSONObject(trans_count);
                System.out.println("productId: "+trans_entry.getString("productId"));
                System.out.println("price: "+trans_entry.getString("price"));
                System.out.println("quantity: "+trans_entry.getString("quantity"));
                System.out.println("\n");
            }
            System.out.println("\nTransactions End- \n\n");         
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void handleGameEnd()
    {
        // Not doing anything at Game End right now
    }

    public  void handleOrderAck()
    {
        try
        {
            System.out.println("Order Ack");
            JSONObject ack = new JSONObject(inputLine);
            JSONObject acknowledge = ack.getJSONObject("Ack");
            JSONArray ack_array = acknowledge.getJSONArray("acknowledged");
            System.out.println("\nAcknowledge - ");
            for( int ack_count = 0; ack_count < ack_array.length(); ack_count++)
            {           
                String ack_entry = ack_array.getString(ack_count);
                System.out.println("Ack Order: "+ack_entry);
            }
            System.out.println("Acknowledge End- \n\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    private boolean checkMessageType(String inputline, String checkType)
    {
        try
        {
            JSONObject json = new JSONObject(inputline);
            JSONArray arr = json.names();
            if(arr.getString(0).equals(checkType))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


}

