///////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) Sep 19, 2008 Morgan Stanley & Co. Incorporated, All Rights Reserved
//
// Unpublished copyright.  All rights reserved.  This material contains
// proprietary information that shall be used or copied only within Morgan
// Stanley, except with written permission of Morgan Stanley.
//
// $Id: //depot/dpg/tradeone_core/trunk/src/build/TradeOneCodeTemplates.xml#2 $
// $Author: samshen $
// $DateTime: 2007/06/14 18:52:05 $
///////////////////////////////////////////////////////////////////////////////

package edu.sjtu.cse.codestorm.client;

import org.json.simple.JSONArray;

import edu.sjtu.cse.codestorm.networking.SocketGameIO;
import edu.sjtu.cse.codestorm.strategy.ITradingStrategy;
import edu.sjtu.cse.codestorm.strategy.RandomTradingStrategy;

/**
 * Plays the game
 * 
 * @author brunk on Sep 19, 2008 @ 2:58:17 PM
 * @version $Revision:$, submitted by $Author:$
 */
public class GameClientMain 
{
  public static void main(String[] args) throws Exception
  {
    String teamName = "Jeff";
    String teamKey = "abc123";
    String host = "vps2.mscc08.com";
    String portStr = "8931";

    if (args.length > 0)
    {
      host = args[0];

      if (args.length > 1)
      {
        portStr = args[1];
      }
    }

    int port = Integer.parseInt(portStr);

    SocketGameIO io = new SocketGameIO(host, port);
    GameClient client = new GameClient();

    client.setIo(io);

    ITradingStrategy strategy = new RandomTradingStrategy();

    client.sendLogin(teamName, teamKey);
    if (client.readWelcome())
    {
      int roundNum;
      while ((roundNum = client.getCurrentRound()) <= client.getFinalRound())
      {
        client.readRoundStartMessage();
        JSONArray buyOffers = strategy.calculateBuyOffers(roundNum, client.getCurrentSupply());
        JSONArray sellOffers = strategy.calculateSellOffers(roundNum, client.getCurrentDemand());
        client.sendOrder(buyOffers, sellOffers);
        client.readAck();
        client.readRoundEndMessage();
        strategy.updateWinningBuyOffers(roundNum, client.getWinningBuyOffers());
        strategy.updateWinningSellOffers(roundNum, client.getWinningSellOffers());
        strategy.updateMarketTransactions(roundNum, client.getMarketTransactions());
      }
    }
    client.readGameEndMessage();
  }
  

}
