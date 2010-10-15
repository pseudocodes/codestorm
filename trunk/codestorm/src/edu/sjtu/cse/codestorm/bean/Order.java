package edu.sjtu.cse.codestorm.bean;

import java.util.*;

public class Order {
	private int ID;
	private int round;
	private String name;
	private long price; // $1= 100
	private int amount;
	private OrderType orderType;
	private boolean isDealed;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	public boolean isDealed() {
		return isDealed;
	}
	public void setDealed(boolean isDealed) {
		this.isDealed = isDealed;
	}
	
	
}
