/**
* 版权所有：福建邮科电信业务部厦门研发中心 
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
		FileClient.uploadFile(new File("D:\\tmp\\FUp_378131942802165004_刘渊先简历20170728.pdf"), "FUp_378131942802165004_刘渊先简历20170728.pdf.pdf",false);
//		FileClient.replaceFile(new File("D:\\FUp_378131942802165004_刘渊先简历20170728.pdf"), "/yt/n/130527150721_6868.jpg");
	}
	
}
