/**
 * 
 */
package org.randomchat.server.tcp;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;
import org.randomchat.common.ImAio;
import org.randomchat.common.ImConfig;
import org.randomchat.common.ImPacket;
import org.randomchat.common.ImStatus;
import org.randomchat.common.Protocol;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.randomchat.common.packets.Command;
import org.randomchat.common.packets.RespBody;
import org.randomchat.common.tcp.TcpPacket;
import org.randomchat.common.tcp.TcpServerDecoder;
import org.randomchat.common.tcp.TcpServerEncoder;
import org.randomchat.common.tcp.TcpSessionContext;
import org.randomchat.common.utils.ImUtils;
import org.randomchat.server.command.AbCmdHandler;
import org.randomchat.server.command.CommandManager;
import org.randomchat.server.handler.AbServerHandler;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年8月3日 下午7:44:48
 */
public class TcpServerHandler extends AbServerHandler{
	
	Logger logger = Logger.getLogger(TcpServerHandler.class);
	
	@Override
	public void init(ImConfig imConfig) {
	}

	@Override
	public boolean isProtocol(ByteBuffer buffer,ChannelContext channelContext){
		if(buffer != null){
			//获取第一个字节协议版本号;
			byte version = buffer.get();
			if(version == Protocol.VERSION){//TCP协议;
				channelContext.setAttribute(new TcpSessionContext().setServerHandler(this));
				ImUtils.setClient(channelContext);
				return true;
			}
		}
		return false;
	}

	@Override
	public ByteBuffer encode(Packet packet, GroupContext groupContext,ChannelContext channelContext) {
		TcpPacket tcpPacket = (TcpPacket)packet;
		return TcpServerEncoder.encode(tcpPacket, groupContext, channelContext);
	}

	@Override
	public void handler(Packet packet, ChannelContext channelContext)throws Exception {
		TcpPacket tcpPacket = (TcpPacket)packet;
		AbCmdHandler cmdHandler = CommandManager.getCommand(tcpPacket.getCommand());
		if(cmdHandler == null){
			ImPacket imPacket = new ImPacket(Command.COMMAND_UNKNOW, new RespBody(Command.COMMAND_UNKNOW,ImStatus.C10017).toByte());
			ImAio.send(channelContext, imPacket);
			return;
		}
		Object response = cmdHandler.handler(tcpPacket, channelContext);
		if(response != null && tcpPacket.getSynSeq() < 1){
			ImAio.send(channelContext,(ImPacket)response);
		}
	}

	@Override
	public TcpPacket decode(ByteBuffer buffer, ChannelContext channelContext)throws AioDecodeException {
		TcpPacket tcpPacket = TcpServerDecoder.decode(buffer, channelContext);
		return tcpPacket;
	}

	@Override
	public String name() {
		
		return Protocol.TCP;
	}
}
