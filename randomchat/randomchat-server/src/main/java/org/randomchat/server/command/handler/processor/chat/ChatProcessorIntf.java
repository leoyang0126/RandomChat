package org.randomchat.server.command.handler.processor.chat;

import org.randomchat.common.ImPacket;
import org.tio.core.ChannelContext;
import org.randomchat.server.command.handler.processor.ProcessorIntf;
/**
 * @author Leo Yang
 * @date 2018年4月2日 下午3:21:01
 */
public interface ChatProcessorIntf extends ProcessorIntf{
	public void handler(ImPacket chatPacket,ChannelContext channelContext)  throws Exception;
}
