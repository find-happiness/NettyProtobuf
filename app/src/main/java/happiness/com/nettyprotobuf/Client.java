package happiness.com.nettyprotobuf;

import android.util.Log;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yudong.fitnew.data.model.protobuf.CustomerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.GenericFutureListener;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Client {
  private static final String TAG = "Client";
  public final AtomicInteger writeLimit     = new AtomicInteger(8192);
  public final AtomicLong reconnectDelay = new AtomicLong(5000);
  public final AtomicInteger connectTimeout = new AtomicInteger(5000);
  public final AtomicInteger writeTimeout   = new AtomicInteger(5000);
  public final AtomicInteger writeBufferHigh = new AtomicInteger(1024 * 64);
  public final AtomicInteger writeBufferLow  = new AtomicInteger(1024 * 8);

  private static final int MAX_FRAME_LENGTH = 1024 * 1024;
  private static final int LENGTH_FIELD_LENGTH = 2;
  private static final int LENGTH_FIELD_OFFSET = 0;
  private static final int LENGTH_ADJUSTMENT = -2;
  private static final int INITIAL_BYTES_TO_STRIP = 0;

  private static final String HOST = "192.168.1.58";
  private static final int PORT = 30000;

  static Client client = new Client();

  Channel channel;

  private Client() {

  }

  public static Client getInstance() {
    return client;
  }

  public void connect() {
    new Thread(new Runnable() {
      @Override public void run() {
        createBootstrap(new Bootstrap(), new NioEventLoopGroup());
      }
    }).start();
  }

  public void disconnect() {

    if (channel != null && channel.isActive()) {
      channel.eventLoop().shutdownGracefully();
      channel.close();
    }
  }

  public void send(int broadcastType,int msgType,GeneratedMessage message) {

    new Thread(new Runnable() {
      @Override public void run() {
        if (channel != null && channel.isActive()) {
          //66793

          DataPacket dataPacket = new DataPacket();
          byte[] content = message.toByteArray();
          dataPacket.setLength((char) (content.length + 20));
          dataPacket.setMinorID((byte) 0);
          dataPacket.setMajorID((byte) 0);
          dataPacket.setBroadcastType(broadcastType);
          dataPacket.setBroadcastLenght(content.length + 8);
          dataPacket.setType(msgType);
          dataPacket.setDataLenght(content.length);
          dataPacket.setContent(content);
          channel.writeAndFlush(dataPacket);
        } else {
          createBootstrap(new Bootstrap(), new NioEventLoopGroup());
        }
      }
    }).start();
  }

  public Bootstrap createBootstrap(Bootstrap cbApp, EventLoopGroup eventLoop) {
    if (cbApp != null) {
      cbApp.option(ChannelOption.SO_KEEPALIVE, true);
      cbApp.remoteAddress(HOST, PORT);
      cbApp.channel(NioSocketChannel.class)
          .group(eventLoop)
          .handler(new ChannelInitializer<SocketChannel>() {

            @Override protected void initChannel(SocketChannel socketChannel) throws Exception {
              ChannelPipeline pipeline = socketChannel.pipeline();
              pipeline.addLast(
                  new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, MAX_FRAME_LENGTH,
                      LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT,
                      INITIAL_BYTES_TO_STRIP, true));
              ProtobufDecoder pd = new ProtobufDecoder();
              addCallbackToHandle(pd);
              pipeline.addLast(pd);
              pipeline.addLast(new ProtobufEncoder());
            }
          });

      cbApp.option(ChannelOption.TCP_NODELAY, true);
      cbApp.option(ChannelOption.SO_KEEPALIVE, true);
      cbApp.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout.get());

      try {
        ChannelFuture channelFuture = cbApp.connect(HOST, PORT);
        channel = channelFuture.channel();
        channelFuture.addListener(new GenericFutureListener<ChannelFuture>() {
          @Override public void operationComplete(ChannelFuture future) throws Exception {
            if (!future.isSuccess()) {
              Log.d(TAG, "operationComplete: --------->Reconnect");
              final EventLoop loop = future.channel().eventLoop();
              loop.schedule(new Runnable() {
                @Override public void run() {
                  client.createBootstrap(new Bootstrap(), loop);
                }
              }, 1L, TimeUnit.SECONDS);
            }
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
      } finally {

      }
    }
    return cbApp;
  }

  private void addCallbackToHandle(ProtobufDecoder pd){

      pd.addCallback(CustomerProtocol.Indexmessage.S2C_GETUSERROOMREQ_ANSWER_VALUE,
          (data)->decodeGetUserRoomAnswer(data));

  }

  private void decodeGetUserRoomAnswer(byte[] data) {
    Log.d(TAG, "获取到GetUserRoomAnswer --------》");
    //查询用户是否在房间中返回
    try {
      CustomerProtocol.FrameGetUserRoomResp resp =
          CustomerProtocol.FrameGetUserRoomResp.parseFrom(data);

      Log.d(TAG, "decodeGetUserRoomAnswer: " + resp.toString());
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }
}
