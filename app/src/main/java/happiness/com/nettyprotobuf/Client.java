package happiness.com.nettyprotobuf;

import android.util.Log;
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

public class Client {
  private static final String TAG = "Client";
  private final static int READER_IDLE_TIME_SECONDS = 20;//读操作空闲20秒
  private final static int WRITER_IDLE_TIME_SECONDS = 20;//写操作空闲20秒
  private final static int ALL_IDLE_TIME_SECONDS = 40;//读写全部空闲40秒

  private static final int MAX_FRAME_LENGTH = 1024 * 1024;
  private static final int LENGTH_FIELD_LENGTH = 2;
  private static final int LENGTH_FIELD_OFFSET = 0;
  private static final int LENGTH_ADJUSTMENT = -2;
  private static final int INITIAL_BYTES_TO_STRIP = 0;

  private static final String HOST = "192.168.1.58";
  private static final int PORT = 30000;

  static Client client = new Client();

  Channel channel;

  Bootstrap bootstrap;

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
    }
  }

  public void send() {

    new Thread(new Runnable() {
      @Override public void run() {
        if (channel != null && channel.isActive()) {
          //66793

          CustomerProtocol.FrameGetUserRoomReq.Builder builder =
              CustomerProtocol.FrameGetUserRoomReq.newBuilder();
          builder.setUserID(66796);
          builder.setAppSocketID(0);
          //builder.setAppSocketID(0);

          DataPacket dataPacket = new DataPacket();
          byte[] content = builder.build().toByteArray();
          dataPacket.setLength((char) (content.length + 20));
          dataPacket.setMinorID((byte) 0);
          dataPacket.setMajorID((byte) 0);
          dataPacket.setBroadcastType(CustomerProtocol.Indexmessage.C2S_UNMASS_DATA_VALUE);
          dataPacket.setBroadcastLenght(content.length + 8);
          dataPacket.setType(CustomerProtocol.Indexmessage.C2S_GETUSERROOMREQ_VALUE);
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
              pipeline.addLast(new ProtobufDecoder());
              pipeline.addLast(new ProtobufEncoder());
            }
          });

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
}
