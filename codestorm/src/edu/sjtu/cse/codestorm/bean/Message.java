package edu.sjtu.cse.codestorm.bean;

import java.util.List;

public class Message {
    private String id;
    private List<Order> orders;
    //to store message type
    //out message or in message
    private short type;

    public Message(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}
}