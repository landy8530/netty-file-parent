/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadServerPipelineFactory.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.support;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

public class FileServerPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		//它负责把字节解码成Http请求。
		pipeline.addLast("decoder", new HttpRequestDecoder());
		//当Server处理完消息后，需要向Client发送响应。那么需要把响应编码成字节，再发送出去。故添加HttpResponseEncoder处理器。
		pipeline.addLast("encoder", new HttpResponseEncoder());
		//它负责把多个HttpMessage组装成一个完整的Http请求或者响应。到底是组装成请求还是响应，则取决于它所处理的内容是请求的内容，还是响应的内容。
		//这其实可以通过Inbound和Outbound来判断，对于Server端而言，在Inbound 端接收请求，在Outbound端返回响应。
		//如果Server向Client返回的数据指定的传输编码是 chunked。则，Server不需要知道发送给Client的数据总长度是多少，它是通过分块发送的，参考分块传输编码
		//pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("deflater", new HttpContentCompressor());
		//该通道处理器主要是为了处理大文件传输的情形。大文件传输时，需要复杂的状态管理，而ChunkedWriteHandler实现这个功能。
		pipeline.addLast("http-chunked", new ChunkedWriteHandler());
		//自定义的通道处理器，其目的是实现文件服务器的业务逻辑。
		pipeline.addLast("handler", new FileServerHandler());
		return pipeline;
	}
}