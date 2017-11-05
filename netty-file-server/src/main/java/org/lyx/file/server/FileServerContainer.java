/**
 * 版权所有：福建邮科电信业务部厦门研发中心 
 *====================================================
 * 文件名称: UploadServerContainer.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2013-4-15			liuyuanxian(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package org.lyx.file.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lyx.file.Account;
import org.lyx.file.Constants;

public class FileServerContainer {
	private static Log log = LogFactory.getLog(FileServerContainer.class);

	private List<Account> accounts = new ArrayList<Account>();
	private String fileBaseDirectory;
	private int port;
	private static FileServerContainer instance;
	private static Map<String, Account> AccountMap = new HashMap<String, Account>();

	public static FileServerContainer factoryMethod() {
		log.info("初始化静态资源传输平台");
		instance = new FileServerContainer();
		return instance;
	}

	public static FileServerContainer getInstance() {
		return instance;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
		for (Account item : this.accounts) {
			if ((StringUtils.isBlank(item.getUserName()))
					|| (StringUtils.isBlank(item.getPwd()))
					|| (StringUtils.isBlank(item.getRootPath()))) {
				log.error("账户配置出现错误，请检查，" + item);
				new Exception().printStackTrace();
			}
			if (!AccountMap.containsKey(item.getUserName())) {
				AccountMap.put(item.getUserName(), item);
			} else
				log.error("账户出现重复配置：" + item);
		}

		log.info("加入默认账户：" + Constants.DEFAULT_ACCOUNT);
		AccountMap.put(Constants.DEFAULT_ACCOUNT.getUserName(),
				Constants.DEFAULT_ACCOUNT);
	}

	public Account getAccount(String userName) {
		Account account = (Account) AccountMap.get(userName);
		if (account == null) {
			log.error("不存在账户UserName=" + userName + ",返回默认账户");
			account = Constants.DEFAULT_ACCOUNT;
		}
		return account;
	}

	public String getFileBaseDirectory() {
		return fileBaseDirectory;
	}

	public void setFileBaseDirectory(String fileBaseDirectory) {
		this.fileBaseDirectory = fileBaseDirectory;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
