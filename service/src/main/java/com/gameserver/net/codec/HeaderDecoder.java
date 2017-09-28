package com.gameserver.net.codec;

import com.gameserver.net.Header;
import com.gameserver.net.HearBeatHeader;
import com.gameserver.net.Message;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * 对包的头文件进行解码
 *
 * @author zhaohui
 */
public class HeaderDecoder extends FrameDecoder {

  /** 头文件长度 **/
  public static final int HEAD_LENGHT = 16;
  /** 包头标志 **/
  public static final byte PACKAGE_TAG = 0x01;

  public static final int HEADER_TYPE = 10000;

  @Override
  protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
      throws Exception {
    if (buffer.readableBytes() < HEAD_LENGHT) {
      return null;
    }
    buffer.markReaderIndex();
    //byte tag = buffer.readByte();
    //if (tag != PACKAGE_TAG) {
    //	throw new CorruptedFrameException("非法协议包");
    //}
    System.out.println("包长度为:" + buffer.readableBytes());
    ChannelBuffer head = buffer.readBytes(4);
    int type = buffer.readInt();
    Header header;
    ChannelBuffer data;
    System.out.println("类型: " + type);
    if (type == HEADER_TYPE) {

      System.out.println("获取到心跳包------》");

      header = new HearBeatHeader();
      if (buffer.readableBytes() < 8) {
        buffer.resetReaderIndex();
        return null;
      }
      data = buffer.readBytes(buffer.readableBytes());
    } else {
      return null;
      ////数据类型
      //int dataType = buffer.readInt();
      ////数据长度
      //int dataLenght = buffer.readInt();
      //
      //if (buffer.readableBytes() < dataLenght) {
      //  buffer.resetReaderIndex();
      //  return null;
      //}
      //
      //data = buffer.readBytes(buffer.readableBytes());
      //header = new DataHeader();
      //header.setLength();
    }

    Message message = new Message(header, data);

    return message;
  }
}
