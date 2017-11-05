/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadClientHandle.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.support;


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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 *<pre><b><font color="blue">FileClientHandler</font></b></pre>
 *
 *<pre><b>客户端文件处理核心句柄</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   FileClientHandler obj = new FileClientHandler();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class FileClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileClientHandler.class);
	
	private boolean readingChunks;
	private final StringBuilder responseContent = new StringBuilder();
	private Result result;

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!this.readingChunks) {
			HttpResponse response = (HttpResponse) e.getMessage();
			LOGGER.info("STATUS: " + response.getStatus());
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