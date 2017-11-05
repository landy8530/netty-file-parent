/**
* 版权所有：蚂蚁与咖啡的故事
*====================================================
* 文件名称: Test.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    2013-4-11			liuyuanxian(创建:创建文件)
*====================================================
* 类描述：(说明未实现或其它不应生成javadoc的内容)
* 
*/
package org.lyx.upload.client;

import java.io.File;

import org.lyx.file.client.FileClient;


public class Test {

	/**
	 * @param args
	 * @author liuyuanxian
	 */
	public static void main(String[] args) throws Exception {
//		FileClient.uploadFile(new File("D:\\tmp\\Test.class"), "a.class",false);
//		FileClient.uploadFile(new File("D:\\tmp\\FUp_378131942802165004.pdf"), "FUp_378131942802165004.pdf",false);
		FileClient.replaceFile(new File("D:\\tmp\\FUp_378131942802165004.pdf"), "yt\\k\\171105144056_7470.pdf");
		FileClient.deleteFile("yt\\k\\171105144056_7470.pdf");
	}
	
}
