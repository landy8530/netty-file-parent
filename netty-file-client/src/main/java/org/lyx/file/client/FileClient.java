/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: UploadClient.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-12			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.client;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import org.lyx.file.Constants;
import org.lyx.file.Result;
import org.lyx.file.client.handler.CreateThumbPictureClientHandler;
import org.lyx.file.client.handler.DeleteFileClientHandler;
import org.lyx.file.client.handler.ReplaceFileClientHandler;
import org.lyx.file.client.handler.UploadFileClientHandler;
import org.lyx.file.client.handler.WrapFileClientHandler;
import org.lyx.file.client.support.FileClientPipelineFactory;

public class FileClient {
	private static final Log logger = LogFactory.getLog(FileClient.class);

	private static URI getUri(String host, int port) {
		String postUrl = "http://" + host + ":" + port + "/formpost";
		URI uri;
		try {
			uri = new URI(postUrl);
		} catch (URISyntaxException e) {
			logger.error("Error: " + e.getMessage());
			return null;
		}
		return uri;
	}

	private static ClientBootstrap createClientBootstrap(
			FileClientPipelineFactory clientPipelineFactory) {
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		bootstrap.setPipelineFactory(clientPipelineFactory);

		return bootstrap;
	}

	private static HttpDataFactory getHttpDataFactory() {
		HttpDataFactory factory = new DefaultHttpDataFactory(16384L);
		org.jboss.netty.handler.codec.http.multipart.DiskFileUpload.deleteOnExitTemporaryFile = false;
		org.jboss.netty.handler.codec.http.multipart.DiskFileUpload.baseDirectory = System
				.getProperty("user.dir");
		org.jboss.netty.handler.codec.http.multipart.DiskAttribute.deleteOnExitTemporaryFile = false;
		org.jboss.netty.handler.codec.http.multipart.DiskAttribute.baseDirectory = System
				.getProperty("user.dir");
		return factory;
	}

	private static void uploadFile(ClientBootstrap bootstrap, String host,
			int port, File file, String fileName, String thumbMark,
			String userName, String pwd) {
		URI uri = getUri(host, port);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}
		WrapFileClientHandler handler = new UploadFileClientHandler(host, uri,
				file, fileName, thumbMark, userName, pwd);
		HttpRequest request = handler.getRequest();
		HttpDataFactory factory = getHttpDataFactory();
		HttpPostRequestEncoder bodyRequestEncoder = handler
				.wrapRequestData(factory);
		channel.write(request);
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.getCloseFuture().awaitUninterruptibly();

		bootstrap.releaseExternalResources();
		factory.cleanAllHttpDatas();
	}

	public static String uploadFile(File file, String fileName,
			boolean thumbMark) {
		FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory();
		ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
		String strThumbMark = Constants.THUMB_MARK_NO;
		if (thumbMark) {
			strThumbMark = Constants.THUMB_MARK_YES;
		}
		uploadFile(bootstrap, FileClientContainer.getHost(),
				FileClientContainer.getPort(), file, fileName, strThumbMark,
				FileClientContainer.getUserName(),
				FileClientContainer.getPassword());
		Result result = clientPipelineFactory.getResult();
		if ((result != null) && (result.isCode())) {
			return result.getFilePath();
		}
		return null;
	}

	private static void deleteFile(ClientBootstrap bootstrap, String host,
			int port, String filePath, String userName, String pwd) {
		URI uri = getUri(host, port);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		WrapFileClientHandler handler = new DeleteFileClientHandler(host, uri,
				filePath, userName, pwd);
		HttpRequest request = handler.getRequest();
		HttpDataFactory factory = getHttpDataFactory();
		HttpPostRequestEncoder bodyRequestEncoder = handler
				.wrapRequestData(factory);
		channel.write(request);
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
		factory.cleanAllHttpDatas();
	}

	public static boolean deleteFile(String filePath, String userName,
			String pwd) {
		FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory();
		ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
		deleteFile(bootstrap, FileClientContainer.getHost(),
				FileClientContainer.getPort(), filePath, userName, pwd);
		Result result = clientPipelineFactory.getResult();
		if ((result != null) && (result.isCode())) {
			return result.isCode();
		}
		return false;
	}

	public static boolean deleteFile(String filePath) {
		return deleteFile(filePath, FileClientContainer.getUserName(),
				FileClientContainer.getPassword());
	}

	private static void replaceFile(ClientBootstrap bootstrap, String host,
			int port, File file, String filePath, String userName, String pwd) {
		URI uri = getUri(host, port);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		WrapFileClientHandler handler = new ReplaceFileClientHandler(host, uri,
				filePath, file, userName, pwd);
		HttpRequest request = handler.getRequest();
		HttpDataFactory factory = getHttpDataFactory();
		HttpPostRequestEncoder bodyRequestEncoder = handler
				.wrapRequestData(factory);
		channel.write(request);
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
		factory.cleanAllHttpDatas();
	}

	public static boolean replaceFile(File file, String filePath) {
		FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory();
		ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
		replaceFile(bootstrap, FileClientContainer.getHost(),
				FileClientContainer.getPort(), file, filePath,
				FileClientContainer.getUserName(),
				FileClientContainer.getPassword());
		Result result = clientPipelineFactory.getResult();
		if ((result != null) && (result.isCode())) {
			return result.isCode();
		}

		return false;
	}

	private static void createThumbPicture(ClientBootstrap bootstrap,
			String host, int port, String filePath, String userName, String pwd) {
		URI uri = getUri(host, port);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		WrapFileClientHandler handler = new CreateThumbPictureClientHandler(
				host, uri, userName, pwd, filePath);
		HttpRequest request = handler.getRequest();
		HttpDataFactory factory = getHttpDataFactory();
		HttpPostRequestEncoder bodyRequestEncoder = handler
				.wrapRequestData(factory);
		channel.write(request);
		if (bodyRequestEncoder.isChunked()) {
			channel.write(bodyRequestEncoder).awaitUninterruptibly();
		}
		bodyRequestEncoder.cleanFiles();
		channel.getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
		factory.cleanAllHttpDatas();
	}

	public static boolean createThumbPicture(String filePath, String userName,
			String pwd) {
		FileClientPipelineFactory clientPipelineFactory = new FileClientPipelineFactory();
		ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
		createThumbPicture(bootstrap, FileClientContainer.getHost(),
				FileClientContainer.getPort(), filePath, userName, pwd);
		Result result = clientPipelineFactory.getResult();
		if ((result != null) && (result.isCode())) {
			return result.isCode();
		}
		return false;
	}

	public static boolean createThumbPicture(String filePath) {
		return createThumbPicture(filePath,
				FileClientContainer.getUserName(),
				FileClientContainer.getPassword());
	}
}