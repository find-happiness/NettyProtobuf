package happiness.com.nettyprotobuf;

import android.util.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by YDCQ-T1 on 2017/9/20.
 */

public class ConnectInboundHander extends ByteToMessageDecoder {
  private static final String TAG = "ConnectInboundHander";
  private Client client;

  public ConnectInboundHander(Client client) {
    this.client = client;
  }

  @Override protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
      throws Exception {
    if (in != null) {
      out.add(in);
    }
  }


}
