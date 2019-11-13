package com.hwang.util.sftpserver;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SFtpFileListener implements SFtpEventListener.FileListener {
	private Log log = LogFactory.getLog(SFtpServer.class);
	
	public SFtpFileListener() {
		
	}
	@Override
	public void afterFileClose(File file, String type,SFtpConfig sftpconfig) {
		try {

			log.info(file.getName() + " " + type + " completed.");
		} catch (Exception e) {
			log.info(String.format("An error occurred while attempting to do things with the file: \"%s\"",
					file.getName()), e);
		}
	}

	@Override
	public void afterFileUpload(String srcFile, String dstFile,SFtpConfig sftpconfig) {
		try {

			log.info(dstFile + " upload completed.");
		} catch (Exception e) {
			log.info(String.format("An error occurred while attempting to do things with the file: \"%s\"", srcFile),
					e);
		}
	}

}
