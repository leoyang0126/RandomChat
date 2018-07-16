/**
 * 
 */
package org.randomchat.server.http;

import java.nio.ByteBuffer;

import org.randomchat.common.Const;
import org.randomchat.common.ImConfig;
import org.randomchat.common.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.randomchat.common.http.GroupContextKey;
import org.randomchat.common.http.HttpConfig;
import org.randomchat.common.http.HttpConst;
import org.randomchat.common.http.HttpRequest;
import org.randomchat.common.http.HttpRequestDecoder;
import org.randomchat.common.http.HttpResponse;
import org.randomchat.common.http.HttpResponseEncoder;
import org.randomchat.common.http.handler.IHttpRequestHandler;
import org.randomchat.common.http.session.HttpSession;
import org.randomchat.common.session.id.impl.UUIDSessionIdGenerator;
import org.randomchat.server.ImServerStarter;
import org.randomchat.server.handler.AbServerHandler;
import org.randomchat.server.http.mvc.Routes;
import org.tio.utils.SystemTimer;
import org.tio.utils.cache.guava.GuavaCache;
/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年8月3日 下午3:07:54
 */
public class HttpServerHandler extends AbServerHandler{
	
	private Logger log = LoggerFactory.getLogger(HttpServerHandler.class);

	private HttpConfig httpConfig;
	
	private IHttpRequestHandler httpRequestHandler;
	
	public HttpServerHandler() {}
	
	public HttpServerHandler(HttpConfig httpServerConfig){
		this.httpConfig = httpServerConfig;
	}
	@Override
	public void init(ImConfig imConfig)throws Exception{
		long start = SystemTimer.currentTimeMillis();
		this.httpConfig = imConfig.getHttpConfig();
		if (httpConfig.getSessionStore() == null) {
			GuavaCache guavaCache = GuavaCache.register(httpConfig.getSessionCacheName(), null, httpConfig.getSessionTimeout());
			httpConfig.setSessionStore(guavaCache);
		}
		if (httpConfig.getPageRoot() == null) {
			httpConfig.setPageRoot("page");
		}
		if (httpConfig.getSessionIdGenerator() == null) {
			httpConfig.setSessionIdGenerator(UUIDSessionIdGenerator.instance);
		}
		if(httpConfig.getScanPackages() == null){
			String[] scanPackages = new String[] { ImServerStarter.class.getPackage().getName() };//t-im mvc需要扫描的根目录包
			httpConfig.setScanPackages(scanPackages);
		}else{
			String[] scanPackages = new String[httpConfig.getScanPackages().length+1];
			scanPackages[0] = ImServerStarter.class.getPackage().getName();
			System.arraycopy(httpConfig.getScanPackages(), 0, scanPackages, 1, httpConfig.getScanPackages().length);
			httpConfig.setScanPackages(scanPackages);
		}
		Routes routes = new Routes(httpConfig.getScanPackages());
		httpRequestHandler = new DefaultHttpRequestHandler(httpConfig, routes);
		httpConfig.setHttpRequestHandler(httpRequestHandler);
		ImConfig.groupContext.setAttribute(GroupContextKey.HTTP_SERVER_CONFIG, httpConfig);
		long end = SystemTimer.currentTimeMillis();
		long iv = end - start;
		log.info("j-im Http Server初始化完毕,耗时:{}ms", iv);
	}
	
	@Override
	public boolean isProtocol(ByteBuffer buffer,ChannelContext channelContext)throws Throwable{
		if(buffer != null){
			HttpRequest request = HttpRequestDecoder.decode(buffer, channelContext,false);
			if(request.getHeaders().get(HttpConst.RequestHeaderKey.Sec_WebSocket_Key) == null)
			{
				channelContext.setAttribute(new HttpSession().setServerHandler(this));
				return true;
			}
		}
		return false;
	}

	@Override
	public ByteBuffer encode(Packet packet, GroupContext groupContext,ChannelContext channelContext) {
		HttpResponse httpResponsePacket = (HttpResponse) packet;
		ByteBuffer byteBuffer = HttpResponseEncoder.encode(httpResponsePacket, groupContext, channelContext,false);
		return byteBuffer;
	}

	@Override
	public void handler(Packet packet, ChannelContext channelContext)throws Exception {
		HttpRequest httpRequestPacket = (HttpRequest) packet;
		HttpResponse httpResponsePacket = httpRequestHandler.handler(httpRequestPacket, httpRequestPacket.getRequestLine());
		Aio.send(channelContext, httpResponsePacket);
	}

	@Override
	public Packet decode(ByteBuffer buffer, ChannelContext channelContext)throws AioDecodeException {
		HttpRequest request = HttpRequestDecoder.decode(buffer, channelContext,true);
		channelContext.setAttribute(Const.HTTP_REQUEST,request);
		return request;
	}
	
	public IHttpRequestHandler getHttpRequestHandler() {
		return httpRequestHandler;
	}

	public void setHttpRequestHandler(IHttpRequestHandler httpRequestHandler) {
		this.httpRequestHandler = httpRequestHandler;
	}
	
	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	public void setHttpConfig(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

	@Override
	public String name() {
		
		return Protocol.HTTP;
	}
	
}
