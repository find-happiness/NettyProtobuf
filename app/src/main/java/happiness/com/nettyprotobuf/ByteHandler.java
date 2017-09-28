package happiness.com.nettyprotobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class ByteHandler extends MessageToMessageDecoder<ByteBuf> {
  public ByteHandler() {
  }

  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    byte[] array = new byte[msg.readableBytes()];
    msg.getBytes(0, array);
    out.add(array);
  }
}