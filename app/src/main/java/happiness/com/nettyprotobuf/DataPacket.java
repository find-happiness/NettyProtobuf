package happiness.com.nettyprotobuf;

/**
 * Created by YDCQ-T1 on 2017/9/19.
 */

public class DataPacket extends Packet {

  /**
   * 数据长度
   */
  private int dataLenght;

  private int broadcastType;

  private int broadcastLenght;

  public int getBroadcastLenght() {
    return broadcastLenght;
  }

  public void setBroadcastLenght(int broadcastLenght) {
    this.broadcastLenght = broadcastLenght;
  }

  public int getDataLenght() {
    return dataLenght;
  }

  public void setDataLenght(int dataLenght) {
    this.dataLenght = dataLenght;
  }

  public int getBroadcastType() {
    return broadcastType;
  }

  public void setBroadcastType(int broadcastType) {
    this.broadcastType = broadcastType;
  }

  @Override public String toString() {
    return super.toString() + "  DataPacket{" +
        "dataLenght=" + dataLenght +
        ", broadcastType=" + broadcastType +
        '}';
  }
}
