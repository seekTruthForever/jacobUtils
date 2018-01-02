package com.whv.jacobOper.constant;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.whv.jacobOper.utils.PropOptUtil;


/**
 * 公共常量类
 * @author huawei
 *
 */
public class Constant {
	/**
	 * url列表配置文件地址ַ
	 */
	private static final String URL_LIST_PROP_PATH = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getServletContext().getRealPath( PropOptUtil.getProperties("global.properties", "jacob.web.urlListConf"));
	/**
	 * 获取url
	 * @param urlFlag
	 * @return
	 */
	public static String getURL(String urlFlag){
		String url = PropOptUtil.getPropertiesAP(URL_LIST_PROP_PATH, urlFlag);
		if(url==null || "".equals(url)){
			url = PropOptUtil.getPropertiesAP(URL_LIST_PROP_PATH, "__default");
		}
		return url;
	}
}
