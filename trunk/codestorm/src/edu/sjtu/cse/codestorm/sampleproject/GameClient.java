///////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) Sep 18, 2008 Morgan Stanley & Co. Incorporated, All Rights Reserved
//
// Unpublished copyright.  All rights reserved.  This material contains
// proprietary information that shall be used or copied only within Morgan
// Stanley, except with written permission of Morgan Stanley.
//
// $Id: //depot/dpg/tradeone_core/trunk/src/build/TradeOneCodeTemplates.xml#2 $
// $Author: samshen $
// $DateTime: 2007/06/14 18:52:05 $
///////////////////////////////////////////////////////////////////////////////

package edu.sjtu.cse.codestorm.sampleproject;

import java.io.IOException;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * TODO: type comment.
 * 
 * @author brunk on Sep 18, 2008 @ 11:14:44 PM
 * @version $Revision:$, submitted by $Author:$
 */
public class GameClient implements IGameConstants {
	private final static Logger LOGGER = Logger.getLogger(GameClient.class
			.getName());
	private IGameIO io;
	private JSONArray currentSupply;
	private JSONArray currentDemand;
	private JSONArray winningBuyOffers;
	private JSONArray winningSellOffers;
	private JSONArray marketTransactions;
	// this is such a hack, just to make sure the first round gets played - the
	// real currentRound and finalRound are set when handling each RoundStart
	// message but we have to make sure the first round actually gets played to
	// get one!
	private int currentRound = 1;
	private int finalRound;
	
	private double cash;

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public int getFinalRound() {
		return finalRound;
	}

	public JSONArray getCurrentSupply() {
		return currentSupply;
	}

	public JSONArray getCurrentDemand() {
		return currentDemand;
	}

	public JSONArray getWinningBuyOffers() {
		return winningBuyOffers;
	}

	public JSONArray getWinningSellOffers() {
		return winningSellOffers;
	}

	public JSONArray getMarketTransactions() {
		return marketTransactions;
	}

	/**
	 * {"Login":{"teamName":"brunk1","key":"abc123"}}
	 * 
	 * @param teamName
	 * @param teamKey
	 */
	@SuppressWarnings("unchecked")
	public void sendLogin(String teamName, String teamKey) throws IOException {
		JSONObject body = new JSONObject();
		body.put(TEAM_NAME, teamName);
		body.put(KEY, teamKey);
		sendMsg(LOGIN, body);
	}

	public boolean readWelcome() {
		JSONObject msg = readMsg(WELCOME);
		if (msg == null)
		{
			return false;
		}
		finalRound = ((Long) msg.get(ROUND_TOTAL)).intValue();
		cash = ((Long) msg.get(INITIAL_CASH)).intValue();
		return true;
	}

	public boolean readAck() {
		// note this ignores the acknowledged order ids
		return null != readMsg(ACK);
	}

	private void clearPreviousRoundState() {
		currentSupply = currentDemand = winningBuyOffers = winningSellOffers = marketTransactions = null;
	}

	@SuppressWarnings("unchecked")
	public void sendOrder(JSONArray buyOffers, JSONArray sellOffers) {
		JSONObject body = new JSONObject();
		body.put(BIDS, buyOffers);
		body.put(ASKS, sellOffers);

		sendMsg(ORDER, body);
	}

	public JSONObject readRoundStartMessage() {
		JSONObject msg = readMsg(ROUND_START);
		// clear after reading to preserve state in case an exception is thrown
		// from readMsg
		clearPreviousRoundState();
		currentSupply = (JSONArray) msg.get(SUPPLY);
		currentDemand = (JSONArray) msg.get(DEMAND);
		//just part of the same hack
		currentRound = ((Long) msg.get(ROUND_NUMBER)).intValue()+1;
		// note this ignores the portfolio attribute
		return msg;
	}

	public JSONObject readRoundEndMessage() {
		JSONObject msg = readMsg(ROUND_END);
		winningBuyOffers = (JSONArray) msg.get(WINNING_BIDS);
		winningSellOffers = (JSONArray) msg.get(WINING_ASKS);
		marketTransactions = (JSONArray) msg.get(MARKET_TRANSACTIONS);
		return msg;
	}

	public JSONObject readGameEndMessage() {
		JSONObject msg = readMsg("GameEnd");
		// note this ignores the portfolio attribute
		return msg;
	}

	@SuppressWarnings("unchecked")
	private void sendMsg(String root, JSONObject body) {
		JSONObject msg = new JSONObject();
		msg.put(root, body);

		try {
			io.send(msg.toString());
			LOGGER.info(String.format("Sent message '%s'", msg));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("caught IOException", e);
		}
	}

	private JSONObject readMsg(String root) {
		JSONObject o;
		try {
			o = (JSONObject) JSONValue.parse(io.read());
			LOGGER.info(String.format("Read message '%s'", o));
		} catch (IOException e) {
			throw new RuntimeException("caught IOException trying to read "
					+ root, e);
		}
		return (JSONObject) o.get(root);
	}

	/**
	 * @return the io
	 */
	public IGameIO getIo() {
		return io;
	}

	/**
	 * @param io
	 *            the io to set
	 */
	public void setIo(IGameIO io) {
		this.io = io;
	}
}
