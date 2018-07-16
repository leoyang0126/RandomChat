package org.randomchat.common.ws;

import org.tio.core.ChannelContext;
import org.randomchat.common.ws.WsRequestPacket;
import org.randomchat.common.ImPacket;
/**
 * 
 * @author Leo Yang 
 * 2017年7月30日 上午9:34:59
 */
public interface IWsMsgHandler
{
	/**
	 * 
	 * @param packet
	 * @param channelContext
	 * @return
	 *
	 * @author: Leo Yang
	 * 2016年11月18日 下午1:08:45
	 *
	 */
	public ImPacket handler(ImPacket packet, ChannelContext channelContext)  throws Exception;
	/**
	 * @param websocketPacket
	 * @param text
	 * @param channelContext
	 * @return 可以是WsResponsePacket、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
	 * @throws Exception
	 * @author: Leo Yang
	 */
	Object onText(WsRequestPacket wsPacket, String text, ChannelContext channelContext) throws Exception;
	
	/**
	 * 
	 * @param websocketPacket
	 * @param bytes
	 * @param channelContext
	 * @return 可以是WsResponsePacket、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
	 * @throws Exception
	 * @author: Leo Yang
	 */
	Object onClose(WsRequestPacket websocketPacket, byte[] bytes, ChannelContext channelContext) throws Exception;

	/**
	 * 
	 * @param websocketPacket
	 * @param bytes
	 * @param channelContext
	 * @return 可以是WsResponsePacket、byte[]、ByteBuffer、String或null，如果是null，框架不会回消息
	 * @throws Exception
	 * @author: Leo Yang
	 */
	Object onBytes(WsRequestPacket websocketPacket, byte[] bytes, ChannelContext channelContext) throws Exception;
}
