/**
 * 
 */
package org.randomchat.server.http.api;

import java.util.List;

import org.randomchat.common.ImAio;
import org.randomchat.common.ImPacket;
import org.randomchat.common.ImStatus;
import org.randomchat.common.http.HttpConfig;
import org.randomchat.common.http.HttpRequest;
import org.randomchat.common.http.HttpResponse;
import org.randomchat.common.packets.CloseReqBody;
import org.randomchat.common.packets.Command;
import org.randomchat.common.packets.RespBody;
import org.randomchat.common.packets.User;
import org.randomchat.server.command.handler.ChatReqHandler;
import org.randomchat.server.command.handler.CloseReqHandler;
import org.randomchat.server.http.annotation.RequestPath;
import org.randomchat.server.util.HttpResps;
import org.tio.core.ChannelContext;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: Leo Yang 创建时间: 2017年8月8日 上午9:08:48
 */
@RequestPath(value = "/api")
public class HttpApiController {
	
	@RequestPath(value = "/message/send")
	public HttpResponse chat(HttpRequest request, HttpConfig httpConfig, ChannelContext channelContext)throws Exception {
		HttpResponse response = new HttpResponse(request,httpConfig);
		ImPacket chatPacket = new ChatReqHandler().handler(request, channelContext);
		if(chatPacket != null){
			response = (HttpResponse)chatPacket;
		}
		return response;
	}
	/**
	 * 判断用户是否在线接口;
	 * @param request
	 * @param httpConfig
	 * @param channelContext
	 * @return
	 * @throws Exception
	 */
	@RequestPath(value = "/user/online")
	public HttpResponse online(HttpRequest request, HttpConfig httpConfig, ChannelContext channelContext)throws Exception {
		Object[] params = request.getParams().get("userid");
		if(params == null || params.length == 0){
			return HttpResps.json(request, new RespBody(ImStatus.C10020));
		}
		String userid = params[0].toString();
		List<User> users = ImAio.getUser(userid);
		if(users.size() > 0){
			return HttpResps.json(request, new RespBody(ImStatus.C10019));
		}else{
			return HttpResps.json(request, new RespBody(ImStatus.C10001));
		}
	}
	/**
	 * 判断用户是否在线接口;
	 * @param request
	 * @param httpConfig
	 * @param channelContext
	 * @return
	 * @throws Exception
	 */
	@RequestPath(value = "user/close")
	public HttpResponse close(HttpRequest request, HttpConfig httpConfig, ChannelContext channelContext)throws Exception {
		Object[] params = request.getParams().get("userid");
		if(params == null || params.length == 0){
			return HttpResps.json(request, new RespBody(ImStatus.C10020));
		}
		String userid = params[0].toString();
		ImPacket closePacket = new ImPacket(Command.COMMAND_CLOSE_REQ,new CloseReqBody(userid).toByte());
		new CloseReqHandler().handler(closePacket, channelContext);
		return HttpResps.json(request, new RespBody(ImStatus.C10021));
	}
}
