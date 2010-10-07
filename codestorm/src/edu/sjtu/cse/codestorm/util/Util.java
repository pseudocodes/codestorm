package edu.sjtu.cse.codestorm.util;

import edu.sjtu.cse.codestorm.bean.Message;

public class Util {
    public static final String HEADER = "";
    public static final String FAULT_CODE = "";
    
    public static String Msg2NetworkLevelStr(Message msg) {
        if (null == msg) {
            return HEADER;
        }
        
        return HEADER + msg.toString();
    }
    
    public static Message NetworkLevelStr2Msg(String str) {
        if (FAULT_CODE.equals(str)) {
            return null;
        }
        
        String id = str.split(";")[0];
        Message msg = new Message(id);
        
        return msg;
    }
}