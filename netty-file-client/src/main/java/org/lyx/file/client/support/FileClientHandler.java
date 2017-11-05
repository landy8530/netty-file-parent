/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: UploadClientHandle.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-12			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;
import org.lyx.file.Result;
import org.lyx.file.client.utils.common.JSONUtil;

public class FileClientHandler extends SimpleChannelUpstreamHandler {
	private static final Log logger = LogFactory
			.getLog(FileClientHandler.class);
	private boolean readingChunks;
	private final StringBuilder responseContent = new StringBuilder();
	private Result result;

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!this.readingChunks) {
			HttpResponse response = (HttpResponse) e.getMessage();
			logger.info("STATUS: " + response.getStatus());
			if ((response.getStatus().getCode() == 200)
					&& (response.isChunked())) {
				this.readingChunks = true;
			} else {
				ChannelBuffer content = response.getContent();
				if (content.readable())
					this.responseContent.append(content
							.toString(CharsetUtil.UTF_8));
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				this.readingChunks = false;
				this.responseContent.append(chunk.getContent().toString(
						CharsetUtil.UTF_8));

				String json = this.responseContent.toString();
				this.result = ((Result) JSONUtil.parseObject(json,Result.class));
			} else {
				this.responseContent.append(chunk.getContent().toString(
						CharsetUtil.UTF_8));
			}
		}
	}

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	public Result getResult() {
		return this.result;
	}
}