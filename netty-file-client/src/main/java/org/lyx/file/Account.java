package org.lyx.file;

import org.apache.commons.lang3.StringUtils;

public class Account {
	private String userName;
	private String pwd;
	private String rootPath;
	private int level;
	private int thumbHeight = 20;

	private int thumbWidth = 20;

	public Account() {
	}

	public Account(String userName, String pwd, String rootPath, int level) {
		this.userName = userName;
		this.pwd = pwd;
		this.rootPath = rootPath;

		if (!this.rootPath.endsWith("/")) {
			this.rootPath += "/";
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
		if (!this.rootPath.endsWith("/"))
			this.rootPath += "/";
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