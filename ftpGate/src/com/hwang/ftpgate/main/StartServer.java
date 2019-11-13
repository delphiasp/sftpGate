package com.hwang.ftpgate.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hwang.common.util.PropTool;
import com.hwang.util.sftpserver.SFtpServer;

public class StartServer {
	private static final String CONFIG_FILE = System.getProperty("user.dir") + File.separator + "config"
			+ File.separator + "config.properties";

	private Map<String, String> map_config = new HashMap<String, String>();

	static Log logger = LogFactory.getLog(SFtpServer.class);

	public String getRootPath() {
		String filePath = this.getClass().getClassLoader().getResource(CONFIG_FILE).getPath();
		return filePath;

	}

	public static void initLog() {
		// log4j
		System.setProperty("WORK_DIR", System.getProperty("user.dir"));
		System.out.println(
				System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.properties");
	}

	private void initConfig() {
		System.out.println(CONFIG_FILE);
		// SFTP服务器连接参数
		map_config.put("SFTP_ADDR", PropTool.getProperties(CONFIG_FILE, "SFTP_ADDR"));
		map_config.put("SFTP_PORT", PropTool.getProperties(CONFIG_FILE, "SFTP_PORT"));
		map_config.put("SFTP_USER", PropTool.getProperties(CONFIG_FILE, "SFTP_USER"));
		map_config.put("SFTP_KEYS", PropTool.getProperties(CONFIG_FILE, "SFTP_KEYS"));
		map_config.put("CONFIG_FILE", CONFIG_FILE);

		System.setProperty("WORK_DIR", System.getProperty("user.dir"));
		System.out.println(
				System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.properties");

	}

	public Map<String, String> getMap_config() {
		return map_config;
	}

	public static void main(String args[]) throws Exception {

		StartServer main = new StartServer();
		main.initConfig();

		SFtpServer sftp = new SFtpServer(main.getMap_config());
		try {
			sftp.startServer();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		while (true) {
			Thread.sleep(100000);
		}
	}

}
