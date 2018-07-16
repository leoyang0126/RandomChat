/**
 * 
 */
package org.randomchat.server.command.handler.processor.login;

import org.tio.core.ChannelContext;
import org.randomchat.common.packets.LoginReqBody;
import org.randomchat.common.packets.User;
import org.randomchat.server.command.handler.processor.ProcessorIntf;
/**
 * @author Leo Yang
 *
 */
public interface LoginProcessorIntf extends ProcessorIntf{
	
	public User getUser(LoginReqBody loginReqBody ,ChannelContext channelContext);
}
