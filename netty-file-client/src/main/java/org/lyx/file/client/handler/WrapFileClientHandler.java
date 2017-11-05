/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: WrapFileClientHandler.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.handler;

import java.net.URI;

import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WrapFileClientHandler {
	protected static final Logger LOGGER = LoggerFactory.getLogger(WrapFileClientHandler.class);
	private String host;
	private URI uri;
	private String userName;
	private String pwd;
	private HttpRequest request;

	public WrapFileClientHandler(String host, URI uri, String userName,
			String pwd) {
		this.host = host;
		this.uri = uri;
		this.userName = userName;
		this.pwd = pwd;
		this.request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
				HttpMethod.POST, uri.toASCIIString());
		setHeaderDatas();
	}

	private void setHeaderDatas() {
		this.request.setHeader("Host", this.host);
		this.request.setHeader("Connection", "close");
		this.request.setHeader("Accept-Encoding", "gzip,deflate");
		this.request.setHeader("Accept-Charset",
				"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		this.request.setHeader("Accept-Language", "fr");
		this.request.setHeader("Referer", this.uri.toString());
		this.request.setHeader("User-Agent", "Netty Simple Http Client side");
		this.request
				.setHeader("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	}

	public abstract HttpPostRequestEncoder wrapRequestData(
			HttpDataFactory paramHttpDataFactory);

	public HttpRequest getRequest() {
		return this.request;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}