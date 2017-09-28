package com.gameserver.net;

/**
 * 包头
 * Created by YDCQ-T1 on 2017/5/26.
 */
public class CmdHeader {
  /** 数据包长 **/
  public short len;
  public byte MajorID;
  public byte MinorID;

  public CmdHeader(short len) {
    this.len = len;
  }

  public short getLen() {
    return len;
  }

  public void setLen(short len) {
    this.len = len;
  }

  public byte getMajorID() {
    return MajorID;
  }

  public void setMajorID(byte majorID) {
    MajorID = majorID;
  }

  public byte getMinorID() {
    return MinorID;
  }

  public void setMinorID(byte minorID) {
    MinorID = minorID;
  }

  @Override public String toString() {
    return "CmdHeader{" +
        "len=" + len +
        ", MajorID=" + MajorID +
        ", MinorID=" + MinorID +
        '}';
  }
}
