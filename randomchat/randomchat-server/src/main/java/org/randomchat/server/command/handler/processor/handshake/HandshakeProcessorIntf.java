/**
 * 
 */
package org.randomchat.server.command.handler.processor.handshake;

import org.randomchat.common.ImPacket;
import org.tio.core.ChannelContext;
import org.randomchat.server.command.handler.processor.ProcessorIntf;
/**
 * @author Leo Yang
 *
 */
public interface HandshakeProcessorIntf extends ProcessorIntf{
	
	public ImPacket handshake(ImPacket packet,ChannelContext channelContext)  throws Exception;
}
