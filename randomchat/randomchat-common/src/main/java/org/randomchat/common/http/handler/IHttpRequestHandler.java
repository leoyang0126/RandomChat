package org.randomchat.common.http.handler;

import org.randomchat.common.http.HttpRequest;
import org.randomchat.common.http.HttpResponse;
import org.randomchat.common.http.RequestLine;

/**
 *
 * @author Leo Yang
 *
 */
public interface IHttpRequestHandler {
	/**
	 *
	 * @param packet
	 * @param requestLine
	 * @return
	 * @throws Exception
	 * @author Leo Yang
	 */
	public HttpResponse handler(HttpRequest packet, RequestLine requestLine) throws Exception;

	/**
	 *
	 * @param request
	 * @param requestLine
	 * @param channelContext
	 * @return
	 * @author Leo Yang
	 */
	public HttpResponse resp404(HttpRequest request, RequestLine requestLine);

	/**
	 *
	 * @param request
	 * @param requestLine
	 * @param throwable
	 * @return
	 * @author Leo Yang
	 */
	public HttpResponse resp500(HttpRequest request, RequestLine requestLine, java.lang.Throwable throwable);
	
	/**
	 * 清空静态资源缓存，如果没有缓存，可以不处理
	 * @param request
	 * @author: Leo Yang
	 */
	public void clearStaticResCache(HttpRequest request);
}
