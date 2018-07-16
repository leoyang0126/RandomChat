package org.randomchat.server.command.handler;

import org.randomchat.common.ImPacket;
import org.randomchat.common.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.randomchat.common.packets.Command;
import org.randomchat.common.packets.HeartbeatBody;
import org.randomchat.common.packets.RespBody;
import org.randomchat.common.utils.ImKit;
import org.randomchat.server.command.AbCmdHandler;

public class HeartbeatReqHandler extends AbCmdHandler
{
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(HeartbeatReqHandler.class);

	@Override
	public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception
	{
		RespBody heartbeatBody = new RespBody(Command.COMMAND_HEARTBEAT_REQ).setData(new HeartbeatBody(Protocol.HEARTBEAT_BYTE));
		ImPacket heartbeatPacket = ImKit.ConvertRespPacket(heartbeatBody,channelContext);
		return heartbeatPacket;
	}

	@Override
	public Command command() {
		return Command.COMMAND_HEARTBEAT_REQ;
	}
}
