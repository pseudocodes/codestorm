///////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) Oct 16, 2008 Morgan Stanley & Co. Incorporated, All Rights Reserved
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * TODO: type comment.
 * 
 * @author brunk on Oct 16, 2008 @ 5:50:50 PM
 * @version $Revision:$, submitted by $Author:$
 */
public class RandomTradingStrategy implements ITradingStrategy, IGameConstants
{
  static int count;

  private static String idGen()
  {
    return "brunk-" + (count++);
  }

  @SuppressWarnings("unchecked")
  private static JSONObject createOffer(boolean buy, String product, double qty, double price)
  {
    JSONObject o = new JSONObject();
    o.put(SIDE, buy ? BUY : SELL);
    o.put(ORDER_ID, idGen());
    o.put(PRODUCT_ID, product);
    o.put(QUANTITY, new Double(qty));
    o.put(PRICE, new Double(price));
    return o;
  }

  public boolean shouldOffer()
  {
    return true;
  }

  @SuppressWarnings("unchecked")
  public JSONArray calculateSellOffers(int round, JSONArray demand)
  {
    JSONArray sellOffers = new JSONArray();
    for (int i = 0; i < demand.size(); ++i)
    {
      JSONObject bid = (JSONObject)demand.get(i);

      if (shouldOffer())
      {
        String product = (String)bid.get(PRODUCT_ID);
        double qty = ((Number)bid.get(QUANTITY)).doubleValue();
        double price = ((Number)bid.get(PRICE)).doubleValue();

        // offer some random price below the bid
        sellOffers.add(createOffer(false, product, qty / ((int)10 * Math.random()), Math.abs(price - (10 * Math.random()))));
      }
    }
    return sellOffers;
  }

  @SuppressWarnings("unchecked")
  public JSONArray calculateBuyOffers(int round, JSONArray supply)
  {
    JSONArray buyOffers = new JSONArray();
    for (int i = 0; i < supply.size(); ++i)
    {
      JSONObject bid = (JSONObject)supply.get(i);

      if (shouldOffer())
      {
        String product = (String)bid.get(PRODUCT_ID);
        double qty = ((Number)bid.get(QUANTITY)).doubleValue();
        double price = ((Number)bid.get(PRICE)).doubleValue();

        // offer some random price above the ask
        buyOffers.add(createOffer(true, product, qty/ ((int)10 * Math.random()), price + (10 * Math.random())));
      }
    }
    return buyOffers;
  }

  public void updateWinningSellOffers(int round, JSONArray winningSellOffers)
  {
    // ignore
  }

  public void updateWinningBuyOffers(int round, JSONArray winningBuyOffers)
  {
    // ignore
  }

  public void updateMarketTransactions(int round, JSONArray marketTransactions)
  {
    // ignore
  }

}
