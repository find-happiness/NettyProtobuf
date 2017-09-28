package com.gameserver.net;

/**
 * 请求和返回的头文件
 *
 * @author zhaohui
 */
public class HearBeatHeader extends Header {

  //数据类型
  private int dataType;

  @Override public HearBeatHeader clone() {

    return (HearBeatHeader) super.clone();
  }

  public HearBeatHeader(short length, int dataType) {
    this.cmdHeader = new CmdHeader(length);
    this.dataType = dataType;
  }

  public int getDataType() {
    return dataType;
  }

  public void setDataType(int dataType) {
    this.dataType = dataType;
  }

  public HearBeatHeader() {

  }
}
