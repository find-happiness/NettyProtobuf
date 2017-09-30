package happiness.com.nettyprotobuf;

import com.google.protobuf.GeneratedMessage;

/**
 * Created by YDCQ-T1 on 2017/9/30.
 */

public class Message {

  int broadcastType;
  int msgType;
  GeneratedMessage message;

  public Message(int broadcastType, int msgType, GeneratedMessage message) {
    this.broadcastType = broadcastType;
    this.msgType = msgType;
    this.message = message;
  }

  public int getBroadcastType() {
    return broadcastType;
  }

  public void setBroadcastType(int broadcastType) {
    this.broadcastType = broadcastType;
  }

  public int getMsgType() {
    return msgType;
  }

  public void setMsgType(int msgType) {
    this.msgType = msgType;
  }

  public GeneratedMessage getMessage() {
    return message;
  }

  public void setMessage(GeneratedMessage message) {
    this.message = message;
  }
}
