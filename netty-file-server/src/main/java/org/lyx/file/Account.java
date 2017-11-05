/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: Account.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
/**
 * 
 *<pre><b><font color="blue">Account</font></b></pre>
 *
 *<pre><b>文件服务器鉴权账户类</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   Account obj = new Account();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class Account {
	private String userName;
	private String pwd;
	private String rootPath;
	private int level;
	private int thumbHeight = 100;

	private int thumbWidth = 100;

	public Account() {
	}

	public Account(String userName, String pwd, String rootPath, int level) {
		this.userName = userName;
		this.pwd = pwd;
		this.rootPath = rootPath;

		if (!this.rootPath.endsWith(File.separator)) {
			this.rootPath += File.separator;
		}
		this.level = level;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRootPath() {
		return this.rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
		if (!this.rootPath.endsWith(File.separator))
			this.rootPath += File.separator;
	}

	public String toString() {
		return "UserName【" + this.userName + "】Pwd【" + this.pwd + "】rootPath【"
				+ this.rootPath + "】level【" + this.level + "】";
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean auth(String pwd) {
		return StringUtils.equals(this.pwd, pwd);
	}

	public int getThumbHeight() {
		return this.thumbHeight;
	}

	public void setThumbHeight(int thumbHeight) {
		this.thumbHeight = thumbHeight;
	}

	public int getThumbWidth() {
		return this.thumbWidth;
	}

	public void setThumbWidth(int thumbWidth) {
		this.thumbWidth = thumbWidth;
	}
}