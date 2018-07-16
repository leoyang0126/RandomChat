/**
 * 
 */
package org.randomchat.server.command.handler.processor.handshake;

import org.randomchat.common.ImPacket;
import org.randomchat.common.Protocol;
import org.tio.core.ChannelContext;
import org.randomchat.common.packets.Command;
import org.randomchat.common.packets.HandshakeBody;
import org.randomchat.common.packets.RespBody;
import org.randomchat.common.tcp.TcpSessionContext;
import org.randomchat.common.utils.ImKit;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: Leo Yang 创建时间: 2017年9月11日 下午8:11:34
 */
public class TcpHandshakeProcessor implements HandshakeProcessorIntf {

	@Override
	public ImPacket handshake(ImPacket packet, ChannelContext channelContext) throws Exception {
		RespBody handshakeBody = new RespBody(Command.COMMAND_HANDSHAKE_RESP,new HandshakeBody(Protocol.HANDSHAKE_BYTE));
		ImPacket handshakePacket = ImKit.ConvertRespPacket(handshakeBody,channelContext);
		return handshakePacket;
	}

	
	@Override
	public boolean isProtocol(ChannelContext channelContext){
		Object sessionContext = channelContext.getAttribute();
		if(sessionContext == null){
			return false;
		}else if(sessionContext instanceof TcpSessionContext){
			return true;
		}
		return false;
	}


	@Override
	public String name() {
		
		return Protocol.TCP;
	}
	
}
