/**
 * 
 */
package org.randomchat.server.command.handler;

import java.util.List;

import org.randomchat.common.ImAio;
import org.randomchat.common.ImPacket;
import org.randomchat.common.ImStatus;
import org.tio.core.ChannelContext;
import org.randomchat.common.packets.Command;
import org.randomchat.common.packets.RespBody;
import org.randomchat.common.packets.User;
import org.randomchat.common.packets.UserReqBody;
import org.randomchat.common.utils.ImKit;
import org.randomchat.common.utils.JsonKit;
import org.randomchat.server.command.AbCmdHandler;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: Leo Yang 创建时间: 2017年9月18日 下午4:08:47
 */
public class UserReqHandler extends AbCmdHandler{

	@Override
	public Command command() {
		return Command.COMMAND_GET_USER_REQ;
	}

	@Override
	public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
		UserReqBody userReqBody = JsonKit.toBean(packet.getBody(),UserReqBody.class);
		List<User> clients = null;
		RespBody resPacket = null;
		if(userReqBody.getType() == null || 0 == userReqBody.getType()){
			clients = ImAio.getUser(userReqBody.getUserid());
			resPacket = new RespBody(Command.COMMAND_GET_USER_RESP,ImStatus.C10003);
		}else if(1 == userReqBody.getType()){
			clients = ImAio.getAllOnlineUser();
			resPacket = new RespBody(Command.COMMAND_GET_USER_RESP,ImStatus.C10005);
		}else if(2 == userReqBody.getType()){
			clients = ImAio.getAllUser();
			resPacket = new RespBody(Command.COMMAND_GET_USER_RESP,ImStatus.C10006);
		}
		if(clients == null)
			return ImKit.ConvertRespPacket(new RespBody(Command.COMMAND_GET_USER_RESP,ImStatus.C10004), channelContext);
		resPacket.setData(clients);
		return ImKit.ConvertRespPacket(resPacket, channelContext);
	}

}
