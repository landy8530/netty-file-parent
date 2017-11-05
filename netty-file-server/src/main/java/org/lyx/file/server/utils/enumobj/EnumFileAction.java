/**
 * 版权所有：蚂蚁与咖啡的故事
 *====================================================
 * 文件名称: EnumUploadAction.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2017-08-29			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server.utils.enumobj;
/**
 * 
 *<pre><b><font color="blue">EnumFileAction</font></b></pre>
 *
 *<pre><b>文件操作类型枚举</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   EnumFileAction obj = new EnumFileAction();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public enum EnumFileAction {
	NULL("", ""),
	/**
	 * 上传文件
	 */
	UPLOAD_FILE("上传文件", "uploadFile"),
	/**
	 * 删除文件
	 */
	DELETE_FILE("删除文件", "deleteFile"),
	/**
	 * 替换文件
	 */
	REPLACE_FILE("替换文件", "replaceFile"),
	/**
	 * 生成缩略图
	 */
	CREATE_THUMB_PICTURE("生成缩略图", "createThumbPicture");

	private String value;
	private String name;

	private EnumFileAction(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static EnumFileAction converByValue(String value) {
		if (UPLOAD_FILE.value.equals(value))
			return UPLOAD_FILE;
		if (DELETE_FILE.value.equals(value))
			return DELETE_FILE;
		if (REPLACE_FILE.value.equals(value))
			return REPLACE_FILE;
		if (CREATE_THUMB_PICTURE.value.equals(value)) {
			return CREATE_THUMB_PICTURE;
		}
		return NULL;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}