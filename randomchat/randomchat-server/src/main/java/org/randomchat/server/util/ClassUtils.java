package org.randomchat.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.randomchat.common.http.UploadFile;

import cn.hutool.core.util.ClassUtil;
/**
 * @author Leo Yang 
 * 2017年7月26日 下午6:46:11
 */
public class ClassUtils {
	private static Logger log = LoggerFactory.getLogger(ClassUtils.class);

	/**
	 * 
	 * @author: Leo Yang
	 */
	public ClassUtils() {
	}
	
	public static boolean isSimpleTypeOrArray(Class<?> clazz){
		return ClassUtil.isSimpleTypeOrArray(clazz) || clazz.isAssignableFrom(UploadFile.class);
	}
	
	

	/**
	 * @param args
	 * @author: Leo Yang
	 */
	public static void main(String[] args) {
		log.info("");
	}
}
