package com.gameserver;

import com.gameserver.datalayer.protocol.Protocol;
import com.gameserver.net.codec.HeaderDecoder;
import com.gameserver.net.codec.HeaderEncoder;
import com.gameserver.net.codec.decoder.ProtobufDecoder;
import com.gameserver.net.codec.encoder.ProtobufEncoder;
import com.google.protobuf.ExtensionRegistry;
import java.net.InetSocketAddress;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 服务器启动程序
 * 
 * @author Administrator
 * 
 */
public class ServerMain {

	private static final Logger logger = Logger.getLogger(ServerMain.class);

	public static void main(String[] args) {

		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory());
		final ServerHandler handler = new ServerHandler();
		final ExtensionRegistry registry = ExtensionRegistry.newInstance();
		Protocol.registerAllExtensions(registry);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("decoder", new HeaderDecoder());
				pipeline.addLast("pDecoder", new ProtobufDecoder(
						Protocol.Request.getDefaultInstance(), registry));

				pipeline.addLast("encoder", new HeaderEncoder());
				pipeline.addLast("pEncoder", new ProtobufEncoder());
				pipeline.addLast("handler", handler);
				return pipeline;
			}
		});
		int port = 8081;
		bootstrap.bind(new InetSocketAddress(port));

		logger.info("============Server Startup OK============");
	}
}
