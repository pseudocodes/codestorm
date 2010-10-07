package edu.sjtu.cse.codestorm.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import edu.sjtu.cse.codestorm.util.Util;

import edu.sjtu.cse.codestorm.bean.Message;

public class TradingClient {
    private static final String SERVER_ADDR = "127.0.0.1";
    private static final int PORT = 7777;
    
    public Message communicate(Message sendMsg) {
        Message replyMsg = null;
        
        if (null == sendMsg) {
            return null;
        }
        
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        
        try {
            socket = new Socket(SERVER_ADDR, PORT);
            
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(Util.Msg2NetworkLevelStr(sendMsg));
            
            ois = new ObjectInputStream(socket.getInputStream());
            String msg = (String)ois.readObject();
            replyMsg = Util.NetworkLevelStr2Msg(msg); 
            
        } catch (IOException exp) {
            exp.printStackTrace();
        } catch (ClassNotFoundException exp) {
            exp.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                
                if (oos != null) {
                    oos.close();
                }
                
                if (socket != null) {
                    socket.close();
                }
                
            } catch (IOException exp) {}
        }
        
        return replyMsg;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TradingClient client = new TradingClient();
        Message msg = new Message("ms-register");
        Message result = client.communicate(msg);
        System.out.println(result);
    }
}