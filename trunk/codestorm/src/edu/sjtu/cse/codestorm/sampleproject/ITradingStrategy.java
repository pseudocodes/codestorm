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

/**
 * TODO: type comment.
 *
 * @author brunk on Oct 16, 2008 @ 5:49:56 PM
 * @version $Revision:$, submitted by $Author:$
 */
public interface ITradingStrategy
{
  /**
   * Generate sell offers
   * @param round
   * @param demand
   * @return
   */
  public JSONArray calculateSellOffers(int round, JSONArray demand);

  /**
   * Generate buy offers.
   * @param round
   * @param supply
   * @return
   */
  public JSONArray calculateBuyOffers(int round, JSONArray supply);

  /**
   * Tell the strategy which of our sell offers won.
   * @param round
   * @param winningSellOffers
   */
  public void updateWinningSellOffers(int round, JSONArray winningSellOffers); 

  /**
   * Tell the strategy which of our buy offers won.
   * @param round
   * @param winningBuyOffers
   */
  public void updateWinningBuyOffers(int round, JSONArray winningBuyOffers);

  /**
   * Tell the strategy about the most recent transactions in the market.
   * @param marketTransactions
   */
  public void updateMarketTransactions(int round, JSONArray marketTransactions); 
}