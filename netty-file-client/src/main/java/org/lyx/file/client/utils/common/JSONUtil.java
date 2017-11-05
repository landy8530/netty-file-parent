/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: JSONUtil.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-12			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.utils.common;

import com.alibaba.fastjson.JSON;

public class JSONUtil {
	public static String toJSONString(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
}
