package org.randomchat.common.session.id.impl;

import org.randomchat.common.http.HttpConfig;
import org.randomchat.common.session.id.ISessionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.util.RandomUtil;
/**
 * @author Leo Yang
 * 2017年8月15日 上午10:53:39
 */
public class UUIDSessionIdGenerator implements ISessionIdGenerator {
	private static Logger log = LoggerFactory.getLogger(UUIDSessionIdGenerator.class);

	public final static UUIDSessionIdGenerator instance = new UUIDSessionIdGenerator();

	/**
	 * @param args
	 * @author Leo Yang
	 */
	public static void main(String[] args) {
		UUIDSessionIdGenerator uuidSessionIdGenerator = new UUIDSessionIdGenerator();
		String xx = uuidSessionIdGenerator.sessionId(null);
		log.info(xx);

	}

	/**
	 *
	 * @author Leo Yang
	 */
	private UUIDSessionIdGenerator() {
	}

	/**
	 * @return
	 * @author Leo Yang
	 */
	@Override
	public String sessionId(HttpConfig httpConfig) {
		return RandomUtil.randomUUID().replace("-", "");
	}
}
