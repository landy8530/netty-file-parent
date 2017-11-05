/**
 * 版权所有：蚂蚁与咖啡的故事 
 *====================================================
 * 文件名称: ThumbnailConvert.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-5-2			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.utils.common;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class ThumbnailConvert {
	private String CMYK_COMMAND = "mogrify -colorspace RGB -quality 80 file1";// 转换cmyk格式

	public void setCMYK_COMMAND(String file1) {
		exeCommand(CMYK_COMMAND.replace("file1", file1));
	}

	public boolean exeCommand(String cmd) {
		InputStreamReader ir = null;
		LineNumberReader input = null;
		try {
			// linux下java执行指令：Runtime.getRuntime().exec (String str);
			Process process = Runtime.getRuntime().exec(cmd);
			ir = new InputStreamReader(process.getInputStream());
			input = new LineNumberReader(ir);
			while ((input.readLine()) != null) {
			}
			ir.close();
			input.close();
		} catch (java.io.IOException e) {
			System.err.println("IOException " + e.getMessage());
			return false;
		}
		return true;
	}

}
