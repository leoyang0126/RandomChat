package org.randomchat.server.command.handler;

import org.randomchat.common.Const;
import org.randomchat.common.ImAio;
import org.randomchat.common.ImPacket;
import org.tio.core.ChannelContext;
import org.randomchat.common.packets.ChatBody;
import org.randomchat.common.packets.ChatType;
import org.randomchat.common.packets.Command;
import org.randomchat.common.packets.RespBody;
import org.randomchat.common.utils.ChatKit;
import org.randomchat.server.command.AbCmdHandler;
import org.randomchat.server.command.handler.processor.chat.MsgQueueRunnable;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: Leo Yang 创建时间: 2017年9月22日 下午2:58:59
 */
public class ChatReqHandler extends AbCmdHandler {
	
	@Override
	public ImPacket handler(ImPacket packet, ChannelContext channelContext) throws Exception {
		if (packet.getBody() == null) {
			throw new Exception("body is null");
		}
		ChatBody chatBody = ChatKit.toChatBody(packet.getBody(), channelContext);
		if(chatBody == null || chatBody.getChatType() == null){//聊天数据格式不正确
			ImPacket respChatPacket = ChatKit.dataInCorrectRespPacket(channelContext);
			return respChatPacket;
		}
		if(ChatType.forNumber(chatBody.getChatType()) != null){//异步调用业务处理消息接口
			MsgQueueRunnable msgQueueRunnable = (MsgQueueRunnable)channelContext.getAttribute(Const.CHAT_QUEUE);
			msgQueueRunnable.addMsg(packet);
			msgQueueRunnable.getExecutor().execute(msgQueueRunnable);
		}
		ImPacket chatPacket = new ImPacket(Command.COMMAND_CHAT_REQ,new RespBody(Command.COMMAND_CHAT_REQ,chatBody).toByte());
		chatPacket.setSynSeq(packet.getSynSeq());//设置同步序列号;
		if(ChatType.CHAT_TYPE_PRIVATE.getNumber() == chatBody.getChatType()){//私聊
			String toId = chatBody.getTo();
			if(ChatKit.isOnline(toId)){
				ImAio.sendToUser(toId, chatPacket);
				return ChatKit.sendSuccessRespPacket(channelContext);//发送成功响应包
			}else{
				return ChatKit.offlineRespPacket(channelContext);//用户不在线响应包
			}
		}else if(ChatType.CHAT_TYPE_PUBLIC.getNumber() == chatBody.getChatType()){//群聊
			String group_id = chatBody.getGroup_id();
			ImAio.sendToGroup(group_id, chatPacket);
			return ChatKit.sendSuccessRespPacket(channelContext);//发送成功响应包
		}
		return null;
	}
	
	@Override
	public Command command() {
		return Command.COMMAND_CHAT_REQ;
	}
}
