/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadClientPipelineFactory.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client.support;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;
import org.lyx.file.Result;

/**
 * 
 *<pre><b><font color="blue">FileClientPipelineFactory</font></b></pre>
 *
 *<pre><b>客户端处理</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   FileClientPipelineFactory obj = new FileClientPipelineFactory();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class FileClientPipelineFactory implements ChannelPipelineFactory {
	private FileClientHandler clientHandler;

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		this.clientHandler = new FileClientHandler();
		pipeline.addLast("codec", new HttpClientCodec());
		pipeline.addLast("inflater", new HttpContentDecompressor());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		pipeline.addLast("handler", this.clientHandler); //具体文件处理句柄
		return pipeline;
	}

	/**
	 * 处理结果
	 * @return
	 * @author:landyChris
	 */
	public Result getResult() {
		if (this.clientHandler != null) {
			return this.clientHandler.getResult();
		}
		return null;
	}
}