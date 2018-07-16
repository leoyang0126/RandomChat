package org.randomchat.server.util;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.randomchat.common.http.GroupContextKey;
import org.randomchat.common.http.HttpConfig;
import org.randomchat.common.http.HttpRequest;
/**
 * @author Leo Yang
 * 2017年8月18日 下午5:47:00
 */
public class HttpServerUtils {
	/**
	 *
	 * @param request
	 * @return
	 * @author Leo Yang
	 */
	public static HttpConfig getHttpConfig(HttpRequest request) {
		ChannelContext channelContext = request.getChannelContext();
		GroupContext groupContext = channelContext.getGroupContext();
		HttpConfig httpConfig = (HttpConfig) groupContext.getAttribute(GroupContextKey.HTTP_SERVER_CONFIG);
		return httpConfig;
	}

	/**
	 * @param args
	 * @author Leo Yang
	 */
	public static void main(String[] args) {

	}

	/**
	 *
	 * @author Leo Yang
	 */
	public HttpServerUtils() {
	}
}
