package happiness.com.nettyprotobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 发送数据到服务器,value（群播长度）= 数据类型长度 + 数据长度长度 + 数据长度
 * value（数据长度）= 数据长度
 * 包头中len = 包头长度 + 群播类型长度 + 群播长度 + 数据类型长度 + 数据长度长度 + 数据长度
 * ----------------------------------------------------------------------------------------------
 * |             |                 |                 |                 |                 |     |
 * | 包头(4 byte)| 群播类型(4 byte)| 群播长度(4 byte)| 数据类型(4 byte)| 数据长度(4 byte)| 数据|
 * |             |                 |                 |                 |                 |     |
 * ----------------------------------------------------------------------------------------------
 *
 *
 *
 */
public class ProtobufEncoder extends MessageToByteEncoder<Packet> {
  @Override protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet,
      ByteBuf byteBuf) throws Exception {

    writeHead(packet, byteBuf);

    if (packet instanceof BeatPacket) {
      byteBuf.writeIntLE(packet.getType());
      byteBuf.writeBytes(packet.getContent());
    }else if (packet instanceof DataPacket){
      DataPacket dataPacket = (DataPacket) packet;
      byteBuf.writeIntLE(dataPacket.getBroadcastType());
      byteBuf.writeIntLE(dataPacket.getBroadcastLenght());
      byteBuf.writeIntLE(dataPacket.getType());
      byteBuf.writeIntLE(dataPacket.getDataLenght());
      byteBuf.writeBytes(dataPacket.getContent());
    }
  }

  /**
   * 写包头
   */
  private void writeHead(Packet packet, ByteBuf byteBuf) {
    byteBuf.writeShortLE(packet.getLength());
    byteBuf.writeByte(packet.getMajorID());
    byteBuf.writeByte(packet.getMinorID());

  }
}
