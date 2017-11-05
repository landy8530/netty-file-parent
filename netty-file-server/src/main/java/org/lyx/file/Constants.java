package org.lyx.file;

public abstract class Constants {
	public static final String BACKSLASH = "/";
	public static final String USER_NAME_KEY = "userName";
	public static final String PWD_KEY = "pwd";
	public static final String ACTION_KEY = "action";
	public static final String FILE_PATH_KEY = "filePath";
	public static final String FILE_NAME_KEY = "fileName";
	public static final String THUMB_MARK_KEY = "thumbMark";
	public static final String THUMB_MARK_YES = "Y";
	public static final String THUMB_MARK_NO = "N";
	public static final String THUMB_SUFFIX = "_thumb";

	public static char[] LETTER_AND_NUMBER_CHAR = { 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };
	public static final int FOLDER_MAX_LEVEL = 10;
	
	public static final int FOLDER_MIN_LEVEL = 1;
	
	public static final Account DEFAULT_ACCOUNT = new Account(
			"default_account", "lyx", System.getProperty("user.dir"), FOLDER_MIN_LEVEL);
	
}