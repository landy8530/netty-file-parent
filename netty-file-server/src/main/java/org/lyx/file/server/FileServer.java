/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: FileServer.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.lyx.file.server.support.FileServerPipelineFactory;
/**
 * 
 *<pre><b><font color="blue">FileServer</font></b></pre>
 *
 *<pre><b>文件服务器netty启动类</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   FileServer obj = new FileServer();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class FileServer {
	private void run() {
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(new FileServerPipelineFactory());
		bootstrap.bind(new InetSocketAddress(FileServerContainer.getInstance()
				.getPort()));
	}

	public void init() {
		run();
	}
}