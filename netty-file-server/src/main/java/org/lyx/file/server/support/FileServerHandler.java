/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: UploadServerHandler.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.support;

import java.net.URI;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.util.CharsetUtil;
import org.lyx.file.Constants;
import org.lyx.file.server.FileServerContainer;
import org.lyx.file.server.handler.factory.FileServerHandlerFactory;
import org.lyx.file.server.parse.RequestParam;
import org.lyx.file.server.parse.RequestParamParser;
import org.lyx.file.server.utils.enumobj.EnumFileAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *<pre><b><font color="blue">FileServerHandler</font></b></pre>
 *
 *<pre><b>文件处理核心句柄类</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   FileServerHandler obj = new FileServerHandler();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class FileServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileServerHandler.class);
	//http请求
	private HttpRequest request;
	//是否需要断点续传作业
	private boolean readingChunks;
	//接收到的文件内容
	private final StringBuffer responseContent = new StringBuffer();
	//解析收到的文件
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //16384L
	//post请求的解码类,它负责把字节解码成Http请求。
	private HttpPostRequestDecoder decoder;
	//请求参数
	private RequestParam requestParams = new RequestParam();

	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		if (this.decoder != null)
			this.decoder.cleanFiles();
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		if (!this.readingChunks) {
			if (this.decoder != null) {
				this.decoder.cleanFiles();
				this.decoder = null;
			}
			HttpRequest request = this.request = (HttpRequest) e.getMessage();
			URI uri = new URI(request.getUri());
			//如果以form开头
			if (!uri.getPath().startsWith("/form")) {
				writeMenu(e);
				return;
			}
			try {
				//初始化decoder
				this.decoder = new HttpPostRequestDecoder(factory, request);
			} catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
				e1.printStackTrace();
				e1.printStackTrace();
				this.responseContent.append(e1.getMessage());
				writeResponse(e.getChannel());
				Channels.close(e.getChannel());
				return;
			} catch (HttpPostRequestDecoder.IncompatibleDataDecoderException e1) {
				e1.printStackTrace();
				this.responseContent.append(e1.getMessage());
				this.responseContent.append("\r\n\r\nEND OF GET CONTENT\r\n");
				writeResponse(e.getChannel());
				return;
			}

			if (request.isChunked()) { //说明还没有请求完成，继续
				this.readingChunks = true;
				LOGGER.info("文件分块操作....");
			} else {
				LOGGER.info("文件大小小于1KB，文件接收完成，直接进行相应的文件处理操作....");
				//请求完成，则接收请求参数，进行初始化请求参数
				RequestParamParser.parseParams(this.decoder, this.requestParams);
				//根据请求参数进行相应的文件操作
				LOGGER.info("文件处理开始....requestParams参数解析：{}",requestParams);
				String result = FileServerHandlerFactory.process(this.requestParams);
				LOGGER.info("文件处理结束....FileServerHandlerFactory处理结果：{}",result);
				this.responseContent.append(result);
				//给客户端响应信息
				writeResponse(e.getChannel());

				e.getFuture().addListener(ChannelFutureListener.CLOSE);
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			try {
				//chunk.getContent().capacity();
				LOGGER.info("文件分块操作....文件大小：{} bytes",chunk.getContent().capacity());
				this.decoder.offer(chunk);
			} catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
				e1.printStackTrace();
				this.responseContent.append(e1.getMessage());
				writeResponse(e.getChannel());
				Channels.close(e.getChannel());
				return;
			}

			if (chunk.isLast()) {
				//文件末尾
				this.readingChunks = false;
				LOGGER.info("到达文件内容的末尾，进行相应的文件处理操作....start");
				RequestParamParser.parseParams(this.decoder, this.requestParams);
				
				LOGGER.info("文件处理开始....requestParams参数解析：{}",requestParams);
				String result = FileServerHandlerFactory.process(this.requestParams);
				LOGGER.info("文件处理结束....FileServerHandlerFactory处理结果：{}",result);
				
				this.responseContent.append(result);
				//给客户端响应信息
				writeResponse(e.getChannel());

				e.getFuture().addListener(ChannelFutureListener.CLOSE);
				LOGGER.info("到达文件内容的末尾，进行相应的文件处理操作....end");
			}
		}
	}

	private void writeResponse(Channel channel) {
		ChannelBuffer buf = ChannelBuffers.copiedBuffer(
				this.responseContent.toString(), CharsetUtil.UTF_8);
		this.responseContent.setLength(0);

		boolean close = ("close".equalsIgnoreCase(this.request
				.getHeader("Connection")))
				|| ((this.request.getProtocolVersion()
						.equals(HttpVersion.HTTP_1_0)) && (!"keep-alive"
						.equalsIgnoreCase(this.request.getHeader("Connection"))));

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		response.setContent(buf);
		response.setHeader("Content-Type", "text/plain; charset=UTF-8");
		if (!close) {
			response.setHeader("Content-Length",
					String.valueOf(buf.readableBytes()));
		}
		ChannelFuture future = channel.write(response);
		if (close)
			future.addListener(ChannelFutureListener.CLOSE);
	}

	private void writeMenu(MessageEvent e) {
		this.responseContent.setLength(0);

		this.responseContent.append("<html>");
		this.responseContent.append("<head>");
		this.responseContent.append("<title>Netty Test Form</title>\r\n");
		this.responseContent.append("</head>\r\n");
		this.responseContent
				.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");
		this.responseContent.append("<table border=\"0\">");
		this.responseContent.append("<tr>");
		this.responseContent.append("<td>");
		this.responseContent.append("<h1>Netty Test Form</h1>");
		this.responseContent.append("Choose one FORM");
		this.responseContent.append("</td>");
		this.responseContent.append("</tr>");
		this.responseContent.append("</table>\r\n");

		this.responseContent
				.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		this.responseContent
				.append("<FORM ACTION=\"/formget\" METHOD=\"POST\">");
		this.responseContent
				.append("<input type=hidden name=getform value=\"POST\">");

		this.responseContent.append("<table border=\"0\">");
		this.responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");

		this.responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");

		this.responseContent
				.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");

		this.responseContent.append("</td></tr>");
		this.responseContent
				.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");

		this.responseContent
				.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");

		this.responseContent.append("</table></FORM>\r\n");
		this.responseContent
				.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		this.responseContent
				.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		this.responseContent
				.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
		this.responseContent
				.append("<input type=hidden name=getform value=\"POST\">");
		this.responseContent.append("<table border=\"0\">");
		this.responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		this.responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		this.responseContent
				.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");

		this.responseContent
				.append("<tr><td>Fill with file (only file name will be transmitted): <br> <input type=file name=\"myfile\">");

		this.responseContent.append("</td></tr>");
		this.responseContent
				.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		this.responseContent
				.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");

		this.responseContent.append("</table></FORM>\r\n");
		this.responseContent
				.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		this.responseContent
				.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		this.responseContent
				.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
		this.responseContent
				.append("<input type=hidden name=getform value=\"POST\">");

		this.responseContent.append("<input type=hidden name=\"" + Constants.ACTION_KEY + "\" value=\""
				+ EnumFileAction.UPLOAD_FILE.getValue() + "\">");
		this.responseContent.append("<table border=\"0\">");
		this.responseContent
				.append("<tr><td>账户: <br> <input type=text name=\"" + Constants.USER_NAME_KEY + "\" value=\""
						+ Constants.DEFAULT_ACCOUNT.getUserName()
						+ "\" size=10></td></tr>");
		this.responseContent
		.append("<tr><td>密码: <br> <input type=text name=\"" + Constants.PWD_KEY + "\" value=\""
				+ Constants.DEFAULT_ACCOUNT.getPwd()
				+ "\" size=10></td></tr>");
		this.responseContent
				.append("<tr><td>产生缩略图: <br> <select type=file name=\"" + Constants.THUMB_MARK_KEY + "\">");
		this.responseContent.append("<option value=\"" + Constants.THUMB_MARK_YES + "\">是</option>");
		this.responseContent.append("<option value=\"" + Constants.THUMB_MARK_NO + "\">否</option>");
		this.responseContent.append("</select></td></tr>");
		this.responseContent
				.append("<tr><td>Fill with file: <br> <input type=file name=\"" + Constants.FILE_NAME_KEY + "\">");
		this.responseContent.append("</td></tr>");
		this.responseContent
				.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		this.responseContent
				.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");

		this.responseContent.append("</table></FORM>\r\n");
		this.responseContent
				.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		this.responseContent.append("</body>");
		this.responseContent.append("</html>");

		ChannelBuffer buf = ChannelBuffers.copiedBuffer(
				this.responseContent.toString(), CharsetUtil.UTF_8);

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		response.setContent(buf);
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		response.setHeader("Content-Length",
				String.valueOf(buf.readableBytes()));

		e.getChannel().write(response);
	}

	static {
		org.jboss.netty.handler.codec.http.multipart.DiskFileUpload.deleteOnExitTemporaryFile = false;
		org.jboss.netty.handler.codec.http.multipart.DiskFileUpload.baseDirectory = FileServerContainer
				.getInstance().getFileBaseDirectory();
		org.jboss.netty.handler.codec.http.multipart.DiskAttribute.deleteOnExitTemporaryFile = false;
		org.jboss.netty.handler.codec.http.multipart.DiskAttribute.baseDirectory = FileServerContainer
				.getInstance().getFileBaseDirectory();
	}
}