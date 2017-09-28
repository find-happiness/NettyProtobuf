package com.gameserver.handler;

import com.gameserver.datalayer.protocol.Protocol;
import com.gameserver.net.Header;
import com.gameserver.net.Message;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

public class IdleClientHandler extends IdleStateAwareChannelHandler {
  private static final String TAG = "IdleClientHandler";

  private int heartbeatCount = 0;
  private final static String CLIENTID = "123456789";

  @Override public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent event)
      throws Exception {
    super.channelIdle(ctx, event);
    String type = "";
    if (event.getState() == IdleState.READER_IDLE) {
      type = "read idle";
    } else if (event.getState() == IdleState.WRITER_IDLE) {
      type = "write idle";
    } else if (event.getState() == IdleState.ALL_IDLE) {
      type = "all idle";
    }
    System.out.println("time out type :" + type);
    sendPingMsg(ctx);
  }

  /**
   * 发送ping消息
   */
  protected void sendPingMsg(ChannelHandlerContext context) {
    //TODO send ping msg
  }

  @Override public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
      throws Exception {
    super.channelDisconnected(ctx, e);

    System.out.println("channelDisconnected-------------------->");
  }
}
