package com.gameserver.net;

/**
 * 请求和返回的头文件
 *
 * @author zhaohui
 */
public class DataHeader extends Header {

  //群播类型
  private int broadcastType;

  //群播长度
  private int broadcastLenght;

  //数据长度
  private int dataLenght;
  //数据类型
  private int dataType;

  @Override public DataHeader clone() {
    return (DataHeader) super.clone();
  }

  public DataHeader(short length, int broadcastType, int broadcastLenght, int dataLenght,
      int dataType) {
    this.cmdHeader = new CmdHeader(length);
    this.broadcastType = broadcastType;
    this.broadcastLenght = broadcastLenght;
    this.dataLenght = dataLenght;
    this.dataType = dataType;
  }

  public int getLength() {
    return cmdHeader.getLen();
  }

  public void setLength(short length) {
    this.cmdHeader.setLen(length);
  }

  public int getBroadcastType() {
    return broadcastType;
  }

  public void setBroadcastType(int broadcastType) {
    this.broadcastType = broadcastType;
  }

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

  public int getDataType() {
    return dataType;
  }

  public void setDataType(int dataType) {
    this.dataType = dataType;
  }

  @Override public String toString() {
    return "Header{" +
        "broadcastType=" + broadcastType +
        ", broadcastLenght=" + broadcastLenght +
        ", dataLenght=" + dataLenght +
        ", dataType=" + dataType +
        ", cmdHeader=" + cmdHeader +
        '}';
  }

  public DataHeader() {

  }
}
