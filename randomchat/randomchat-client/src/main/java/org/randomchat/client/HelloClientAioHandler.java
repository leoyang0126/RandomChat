package org.randomchat.client;

import java.nio.ByteBuffer;

import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.AioHandler;
import org.tio.core.intf.Packet;
import org.randomchat.common.packets.Command;
import org.randomchat.common.tcp.TcpPacket;
import org.randomchat.common.tcp.TcpServerDecoder;
import org.randomchat.common.tcp.TcpServerEncoder;
import org.randomchat.common.Const;
import org.randomchat.common.Protocol;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * @author Leo Yang 创建时间: 2018年6月25日 下午1:10:28
 */
public class HelloClientAioHandler  implements AioHandler,ClientAioHandler
{
	/** 
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception
	{
		TcpPacket helloPacket = (TcpPacket)packet;
		byte[] body = helloPacket.getBody();
		if (body != null)
		{
			String str = new String(body, Const.CHARSET);
			System.out.println("收到消息：" + str);
		}

		return;
	}
	/**
	 * 编码：把业务消息包编码为可以发送的ByteBuffer
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
	@Override
	public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext)
	{
		TcpPacket tcpPacket = (TcpPacket)packet;
		return TcpServerEncoder.encode(tcpPacket, groupContext, channelContext);
	}
	
	@Override
	public TcpPacket decode(ByteBuffer buffer,int limit, int position, int readableLength,ChannelContext channelContext) throws AioDecodeException {
		TcpPacket tcpPacket = TcpServerDecoder.decode(buffer, channelContext);
		return tcpPacket;
	}
	
	private static TcpPacket heartbeatPacket = new TcpPacket(Command.COMMAND_HEARTBEAT_REQ,new byte[]{Protocol.HEARTBEAT_BYTE});

	/** 
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 */
	@Override
	public TcpPacket heartbeatPacket()
	{
		return heartbeatPacket;
	}
}
