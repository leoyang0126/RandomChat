package org.randomchat.server.command.handler;

import org.randomchat.common.Const;
import org.randomchat.common.ImPacket;
import org.randomchat.common.ImStatus;
import org.tio.core.ChannelContext;
import org.randomchat.common.packets.AuthReqBody;
import org.randomchat.common.packets.Command;
import org.randomchat.common.packets.RespBody;
import org.randomchat.common.utils.ImKit;
import org.randomchat.common.utils.JsonKit;
import org.randomchat.server.command.AbCmdHandler;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * 作者: Leo Yang 创建时间: 2017年9月13日 下午1:39:35
 */
public class AuthReqHandler extends AbCmdHandler
{
	@Override
	public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception
	{
		if (packet.getBody() == null) {
			RespBody respBody = new RespBody(Command.COMMAND_AUTH_RESP,ImStatus.C10010);
			return ImKit.ConvertRespPacket(respBody, channelContext);
		}
		AuthReqBody authReqBody = JsonKit.toBean(packet.getBody(), AuthReqBody.class);
		String token = authReqBody.getToken() == null ? "" : authReqBody.getToken();
		String data = token +  Const.authkey;
		authReqBody.setToken(data);
		authReqBody.setCmd(null);
		RespBody respBody = new RespBody(Command.COMMAND_AUTH_RESP,ImStatus.C10009).setData(authReqBody);
		return ImKit.ConvertRespPacket(respBody, channelContext);
	}

	@Override
	public Command command() {
		return Command.COMMAND_AUTH_REQ;
	}
}
