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

/**
 * TODO: type comment.
 *
 * @author brunk on Sep 18, 2008 @ 11:18:30 PM
 * @version $Revision:$, submitted by $Author:$
 */
public interface IGameIO
{
  public void send(String msg) throws IOException;
  public String read() throws IOException;
}
