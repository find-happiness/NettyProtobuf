package happiness.com.nettyprotobuf;

import android.util.Log;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yudong.fitnew.data.model.protobuf.CustomerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 接收到服务器发送的消息，解析数据时先判断数据是什么类型，然后再解析：数据包格式为
 * -------------------------------------------------------------------
 * |         |                  |                   |               |
 * |   包头  |   数据类型       |    数据长度       |   数据        |
 * |         |                  |                   |               |
 * -------------------------------------------------------------------
 *
 * 心跳包数据为：
 * --------------------------------------------------
 * |         |                  |                   |
 * |   包头  |   数据类型       |    数据           |
 * |         |                  |                   |
 * ---------------------------------------------------
 */
public class ProtobufDecoder extends ByteToMessageDecoder {

  private static final String TAG = "ProtobufDecoder";

  /**
   * <pre>
   * 协议开始的标准head_data，占据4个字节.
   * 表示数据的类型，占据4个字节.
   * </pre>
   */
  public final int BASE_LENGTH = 4 + 4;

  @Override protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer,
      List<Object> list) throws Exception {
    // 可读长度必须大于基本长度
    if (buffer.readableBytes() >= BASE_LENGTH) {
      // 防止socket字节流攻击
      // 防止，客户端传来的数据过大
      // 因为，太大的数据，是不合理的
      if (buffer.readableBytes() > 2048) {
        buffer.skipBytes(buffer.readableBytes());
      }

      try {
        int packetLength = buffer.readUnsignedShortLE();
        byte majorID = buffer.readByte();
        byte minorID = buffer.readByte();
        int type = buffer.readIntLE();

        if (type == 10000) {
          //心跳包
          BeatPacket packet = new BeatPacket();
          packet.setLength((char) packetLength);
          packet.setMajorID(majorID);
          packet.setMinorID(minorID);
          packet.setType(type);
          byte[] dis = new byte[packetLength - buffer.readerIndex()];
          buffer.readBytes(dis);
          packet.setContent(dis);
          channelHandlerContext.channel().writeAndFlush(packet);

          Log.d(TAG, "decode: Beat packet----------->");
        } else {
          //数据包
          Log.d(TAG, "onReceive: -----》 数据包");
          //获取数据长度
          int dataLenght = buffer.readIntLE();
          if (buffer.readableBytes() < dataLenght) {
            Log.e(TAG, "onReceive: 数据剩余长度出错！");
            return;
          }
          //获取数据的byte数组
          byte[] data = new byte[dataLenght];
          buffer.readBytes(data);
          //decodeProtobuf(type, data);
          decodeGetUserRoomAnswer(data);
        }
      } catch (Exception e) {
        Log.e(TAG, "decode: ", e);
      }
    }
  }

  private void decodeGetUserRoomAnswer(byte[] data) {
    Log.d(TAG, "获取到GetUserRoomAnswer --------》");
    //查询用户是否在房间中返回
    try {
      CustomerProtocol.FrameGetUserRoomResp resp =
          CustomerProtocol.FrameGetUserRoomResp.parseFrom(data);

      Log.d(TAG, "decodeGetUserRoomAnswer: " + resp.toString());
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  @Override public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    final EventLoop eventLoop = ctx.channel().eventLoop();
    Log.d(TAG, "channelInactive: reconnect ----->");
    eventLoop.schedule(new Runnable() {
      @Override public void run() {
        Client.getInstance().createBootstrap(new Bootstrap(), eventLoop);
      }
    }, 1L, TimeUnit.SECONDS);
    super.channelInactive(ctx);
  }
}
