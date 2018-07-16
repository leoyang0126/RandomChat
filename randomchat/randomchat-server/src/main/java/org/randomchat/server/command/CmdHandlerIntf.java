package org.randomchat.server.command;

import org.randomchat.common.ImPacket;
import org.randomchat.common.packets.Command;
import org.tio.core.ChannelContext;
/**
 * 
 * 版本: [1.0]
 * 功能说明: 
 * 作者: Leo Yang 创建时间: 2017年9月8日 下午4:29:38
 */
public interface CmdHandlerIntf
{
	/**
	 * 
		 * 功能描述：[命令主键]
		 * 创建者：Leo Yang 创建时间: 2017年7月17日 下午2:31:51
		 * @return
		 *
	 */
	public Command command();
	/**
	 * 
	 * @param packet
	 * @param channelContext
	 * @return
	 *
	 * @author: Leo Yang
	 * 2016年11月18日 下午1:08:45
	 *
	 */
	public ImPacket handler(ImPacket packet, ChannelContext channelContext)  throws Exception;
	
}
