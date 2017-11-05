/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: UploadServerPipelineFactory.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-15			liuyuanxian(创建:创建文件)
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

public class FileServerPipelineFactory implements ChannelPipelineFactory {
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());

		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", new FileServerHandler());
		return pipeline;
	}
}