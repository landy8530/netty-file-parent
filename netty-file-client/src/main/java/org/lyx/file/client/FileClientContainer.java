/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadClientContainer.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client;

import java.util.ResourceBundle;

public class FileClientContainer {
	private static ResourceBundle rb = null;
	
	static {
		rb = ResourceBundle.getBundle("file-config");
	}
	
	private static String userName = rb.getString("upload.userName");
	private static String password = rb.getString("upload.password");
	private static String host = rb.getString("upload.server.host");
	private static int port = Integer
			.parseInt(rb.getString("upload.server.port"));

	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static String getHost() {
		return host;
	}

	public static int getPort() {
		return port;
	}
}