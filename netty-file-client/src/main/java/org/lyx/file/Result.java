package org.lyx.file;

public class Result {
	private boolean code;
	private String msg;
	private String action;
	private String filePath;

	public boolean isCode() {
		return this.code;
	}

	public void setCode(boolean code) {
		this.code = code;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}