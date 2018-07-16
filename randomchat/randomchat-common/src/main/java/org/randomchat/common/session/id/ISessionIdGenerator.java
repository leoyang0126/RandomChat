package org.randomchat.common.session.id;

import org.randomchat.common.http.HttpConfig;

/**
 * @author Leo Yang
 * 2017年8月15日 上午10:49:58
 */
public interface ISessionIdGenerator {

	/**
	 *
	 * @return
	 * @author Leo Yang
	 */
	String sessionId(HttpConfig httpConfig);

}
