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
/**
 * 
 *<pre><b><font color="blue">FileServerContainer</font></b></pre>
 *
 *<pre><b>文件服务器基本信息容器</b></pre>
 * <pre></pre>
 * <pre>
 * <b>--样例--</b>
 *   FileServerContainer obj = new FileServerContainer();
 *   obj.method();
 * </pre>
 * @author  <b>landyChris</b>
 */
public class FileServerContainer {
	private static Log log = LogFactory.getLog(FileServerContainer.class);
	/**
	 * 文件服务器上传鉴权账户列表
	 */
	private List<Account> accounts = new ArrayList<Account>();
	/**
	 * 文件上传的根目录
	 */
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
