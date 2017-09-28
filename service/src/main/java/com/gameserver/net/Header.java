package com.gameserver.net;

/**
 * 请求和返回的头文件
 *
 * @author zhaohui
 */
public class Header implements Cloneable {

  protected CmdHeader cmdHeader;

  @Override public Header clone() {
    try {
      return (Header) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Header(short length) {
    this.cmdHeader = new CmdHeader(length);
  }

  public CmdHeader getCmdHeader() {
    return cmdHeader;
  }

  public void setCmdHeader(CmdHeader cmdHeader) {
    this.cmdHeader = cmdHeader;
  }

  public int getLength() {
    return cmdHeader.getLen();
  }

  public void setLength(short length) {
    this.cmdHeader.setLen(length);
  }

  @Override public String toString() {
    return "Header{" +
        "cmdHeader=" + cmdHeader +
        '}';
  }

  public Header() {

  }
}
