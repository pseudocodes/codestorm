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

package edu.sjtu.cse.codestorm.sampleproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * TODO: type comment.
 *
 * @author brunk on Sep 19, 2008 @ 2:06:00 PM
 * @version $Revision:$, submitted by $Author:$
 */
public class SocketGameIO implements IGameIO
{
  private Socket s;
  private PrintWriter out;
  private BufferedReader in;
  
  public SocketGameIO(String host, int port) throws UnknownHostException, IOException
  {
    s = new Socket(host, port);
    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
  }
  
  /**
   * @see com.ms.cc.IGameIO#read()
   */
  public String read() throws IOException
  {
    return in.readLine();
  }

  /**
   * @see com.ms.cc.IGameIO#send(java.lang.String)
   */
  public void send(String msg) throws IOException
  {
    out.println(msg);
    out.flush();
  }

}
