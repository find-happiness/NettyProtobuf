package happiness.com.nettyprotobuf;

import java.util.Arrays;

/**
 * Created by YDCQ-T1 on 2017/9/19.
 */

public class Packet {
  /**
   *数据包
   --------------------------------------------------
   * |         |                  |                   |
   * |   包头  |   数据类型       |    数据           |
   * |         |                  |                   |
   * ---------------------------------------------------

   * 包头总长度是4个byte
   *
   * -------------------------------------------------------------------------
   * |                   |                        |                           |
   * |   长度（ushort）  |   MajorID(byte)        |    MinorID(byte)          |
   * |                   |                        |                           |
   * --------------------------------------------------------------------------
   *
   */

  /**
   * 整个数据包长度
   */
  protected char length;

  /**
   * majorID
   */
  protected byte majorID;

  /**
   * minorID
   */
  protected byte minorID;

  /**
   * 数据类型
   */
  protected int type;

  /**
   * 消息的内容
   */
  protected byte[] content;

  public char getLength() {
    return length;
  }

  public void setLength(char length) {
    this.length = length;
  }

  public byte getMajorID() {
    return majorID;
  }

  public void setMajorID(byte majorID) {
    this.majorID = majorID;
  }

  public byte getMinorID() {
    return minorID;
  }

  public void setMinorID(byte minorID) {
    this.minorID = minorID;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  @Override public String toString() {
    return "Packet{" +
        "length=" + length +
        ", majorID=" + majorID +
        ", minorID=" + minorID +
        ", type=" + type +
        ", content=" + Arrays.toString(content) +
        '}';
  }
}
