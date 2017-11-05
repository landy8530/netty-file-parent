/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: JSONUtil.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.utils.common;

import com.alibaba.fastjson.JSON;
/**
 * 
 *<pre><b><font color="blue">JSONUtil</font></b></pre>
 *
 *<pre><b>JSON工具类</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   JSONUtil obj = new JSONUtil();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class JSONUtil {
	/**
	 * 把对象转化为json字符串
	 * @param obj
	 * @return
	 * @author:landyChris
	 */
	public static String toJSONString(Object obj) {
		return JSON.toJSONString(obj);
	}
	/**
	 * 把json字符串转化为相应的实体对象
	 * @param json
	 * @param clazz
	 * @return
	 * @author:landyChris
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
}
