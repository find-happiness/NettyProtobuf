package com.gameserver.handler;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * 连接空闲Handler
 */
public class IdleServerHandler extends IdleStateAwareChannelHandler {
  @Override public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent event)
      throws Exception {

    String type = "";
    if (event.getState() == IdleState.READER_IDLE) {
      type = "read idle";
    } else if (event.getState() == IdleState.WRITER_IDLE) {
      type = "write idle";
    } else if (event.getState() == IdleState.ALL_IDLE) {
      type = "all idle";
    }
    System.out.println(
        ctx.getPipeline().getChannel().getRemoteAddress() + "  time out type:" + type);
  }
}
