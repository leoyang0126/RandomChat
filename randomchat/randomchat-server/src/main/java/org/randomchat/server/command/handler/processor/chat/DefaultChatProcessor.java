package org.randomchat.server.command.handler.processor.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.randomchat.common.packets.ChatBody;
/**
 * @author Leo Yang
 * @date 2018年4月3日 下午1:12:30
 */
public class DefaultChatProcessor extends AbstractChatProcessor{
	
	Logger log = LoggerFactory.getLogger(DefaultChatProcessor.class);
	
	@Override
	public void doHandler(ChatBody chatBody, ChannelContext channelContext){
		
	}
}
