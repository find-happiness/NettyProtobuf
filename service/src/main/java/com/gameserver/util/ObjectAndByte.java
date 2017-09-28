package com.gameserver.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by YDCQ-T1 on 2017/5/26.
 */

public class ObjectAndByte {

  /**
   * 对象转数组
   */
  public static byte[] toByteArray(Object obj) {
    byte[] bytes = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);
      oos.flush();
      bytes = bos.toByteArray();
      oos.close();
      bos.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return bytes;
  }

  /**
   * 数组转对象
   */
  public static Object toObject(byte[] bytes) {
    Object obj = null;
    try {
      ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
      ObjectInputStream ois = new ObjectInputStream(bis);
      obj = ois.readObject();
      ois.close();
      bis.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }
    return obj;
  }
}
