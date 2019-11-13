package com.hwang.util.sftpserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.DirectoryHandle;
import org.apache.sshd.server.subsystem.sftp.FileHandle;
import org.apache.sshd.server.subsystem.sftp.Handle;
import org.apache.sshd.server.subsystem.sftp.SftpEventListener;

public class SFtpEventListener implements SftpEventListener {

	Log logger = LogFactory.getLog(SftpEventListener.class);
	SFtpServer sftpserver;
	private List<String> uploadList = new ArrayList<String>();
	private List<String> downloadList = new ArrayList<String>();

	private List<SFtpConfig> user_list;

	public SFtpEventListener(SFtpServer sftpserver) {
		this.sftpserver = sftpserver;
		user_list = sftpserver.getuser_list();
		user_list.addAll(sftpserver.getkeys_list());
	}

	public interface FileListener {
		void afterFileClose(File file, String type, SFtpConfig sftpconfig);

		void afterFileUpload(String srcFile, String dstFile, SFtpConfig sftpconfig);
	}

	private List<FileListener> fileListeners = new ArrayList<FileListener>();

	public void addFileListener(FileListener listener) {
		fileListeners.add(listener);
	}

	public void removeFileListener(FileListener listener) {
		fileListeners.remove(listener);
	}

	@Override
	public void initialized(ServerSession serverSession, int version) {
		logger.info(serverSession.getClientAddress() + ":" + serverSession.getUsername() + "--->Connected");

	}

	@Override
	public void destroying(ServerSession serverSession) {
		logger.info(serverSession.getClientAddress() + ":" + serverSession.getUsername() + "--->DisConnected");
	}

	@Override
	public void opening(ServerSession session, String remoteHandle, Handle localHandle) throws IOException {

	}

	@Override
	public void open(ServerSession serverSession, String remoteHandle, Handle localHandle) {
		File openedFile = localHandle.getFile().toFile();
		if (openedFile.exists() && openedFile.isFile()) {
		}
	}

	@Override
	public void read(ServerSession session, String remoteHandle, DirectoryHandle localHandle,
			Map<String, Path> entries) {

	}

	@Override
	public void reading(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data,
			int dataOffset, int dataLen) throws IOException {
		if (!checkPriv(session.getUsername(), "R")) {
			logger.info(session.getClientAddress() + ":" + session.getUsername() + "<---download---"
					+ localHandle.getFile().toAbsolutePath() + " refused.");
			throw new IOException("No permission");
		}
	}

	@Override
	public void read(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data,
			int dataOffset, int dataLen, int readLen, Throwable thrown) throws IOException {

		if (readLen >= 0 && (offset * 100 / localHandle.getFileChannel().size() % 5 == 0
				&& offset*100 / localHandle.getFileChannel().size() <= 100)) {
			logger.info(session.getClientAddress() + ":" + session.getUsername() + "<---download---"
					+ localHandle.getFile().toAbsolutePath() + ":" + offset + "/" + localHandle.getFileChannel().size()
					+ "(" + offset * 100 / localHandle.getFileChannel().size() + "%)");
		}
		if (!downloadList.contains(localHandle.getFile().toAbsolutePath().toString()))
			downloadList.add(localHandle.getFile().toAbsolutePath().toString());
	}

	@Override
	public void writing(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data,
			int dataOffset, int dataLen) throws IOException {

	}

	@Override
	public void written(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, byte[] data,
			int dataOffset, int dataLen, Throwable thrown) throws IOException {
		if (localHandle.getFileChannel().size() / 1024 / 1024 % 2 == 0)
			logger.info(session.getClientAddress() + ":" + session.getUsername() + "---upload--->"
					+ localHandle.getFile().getFileName() + ":" + localHandle.getFileChannel().size());
		if (!uploadList.contains(localHandle.getFile().toAbsolutePath().toString()))
			uploadList.add(localHandle.getFile().toAbsolutePath().toString());
	}

	@Override
	public void blocking(ServerSession serverSession, String remoteHandle, FileHandle localHandle, long offset,
			long length, int mask) {
	}

	@Override
	public void blocked(ServerSession serverSession, String remoteHandle, FileHandle localHandle, long offset,
			long length, int mask, Throwable thrown) {
	}

	@Override
	public void unblocking(ServerSession serverSession, String remoteHandle, FileHandle localHandle, long offset,
			long length) {
	}

	@Override
	public void unblocked(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, long length,
			Throwable thrown) throws IOException {
	}
 
	@Override
	public void closed(ServerSession serverSession, String remoteHandle, Handle localHandle, Throwable thrown) {
		File closedFile = localHandle.getFile().toFile();
		if (closedFile.exists() && closedFile.isFile()) {
			logger.info(byteToHex(serverSession.getSessionId()) + ":" + serverSession.getClientAddress() + ":"
					+ serverSession.getUsername()
					+ String.format(" Closed file:  %s", localHandle.getFile().toAbsolutePath()));
			SFtpConfig sftpconfig = getSFtpConfig(serverSession.getUsername());
			for (FileListener fileReadyListener : fileListeners) {
				if (uploadList.contains(localHandle.getFile().toAbsolutePath().toString())) {
					fileReadyListener.afterFileClose(closedFile, "Upload", sftpconfig);
					uploadList.remove(localHandle.getFile().toAbsolutePath().toString());
				}
				if (downloadList.contains(localHandle.getFile().toAbsolutePath().toString())) {
					fileReadyListener.afterFileClose(closedFile, "Download", sftpconfig);
					downloadList.remove(localHandle.getFile().toAbsolutePath().toString());
				}
			}
		}
	}

	@Override
	public void creating(ServerSession serverSession, Path path, Map<String, ?> attrs)
			throws UnsupportedOperationException {
		logger.warn(String.format("Blocked user %s attempt to create a directory \"%s\"", serverSession.getUsername(),
				path.toString()));
		throw new UnsupportedOperationException("Creating sub-directories is not permitted.");
	}

	@Override
	public void created(ServerSession serverSession, Path path, Map<String, ?> attrs, Throwable thrown) {
		String username = serverSession.getUsername();
		logger.info(String.format("User %s created: \"%s\"", username, path.toString()));
	}

	@Override
	public void moving(ServerSession session, Path srcPath, Path dstPath, Collection<CopyOption> opts)
			throws IOException {
		// ignored
	}

	@Override
	public void moved(ServerSession serverSession, Path source, Path destination, Collection<CopyOption> collection,
			Throwable throwable) {
		logger.info(byteToHex(serverSession.getSessionId()) + ":" + serverSession.getClientAddress() + ":"
				+ serverSession.getUsername()
				+ String.format(" moved: %s to %s ", source.toString(), destination.toString()));
		SFtpConfig sftpconfig = getSFtpConfig(serverSession.getUsername());
		for (FileListener fileReadyListener : fileListeners) {

			fileReadyListener.afterFileUpload(destination.toString(), destination.toString(), sftpconfig);

		}
	}



	@Override
	public void linking(ServerSession serverSession, Path source, Path target, boolean symLink)
			throws UnsupportedOperationException {
		logger.warn(String.format("Blocked user %s attempt to create a link to \"%s\" at \"%s\"",
				serverSession.getUsername(), target.toString(), source.toString()));
		throw new UnsupportedOperationException("Creating links is not permitted");
	}

	@Override
	public void linked(ServerSession serverSession, Path source, Path target, boolean symLink, Throwable thrown) {

	}

	@Override
	public void modifyingAttributes(ServerSession serverSession, Path path, Map<String, ?> attrs) {

	}

	@Override
	public void modifiedAttributes(ServerSession serverSession, Path path, Map<String, ?> attrs, Throwable thrown) {
	}

	public static String byteToHex(byte[] b) {
		String retStr = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			retStr += hex.toUpperCase();
		}
		return retStr;
	}

	private boolean checkPriv(String userName, String type) {
		Iterator<SFtpConfig> iter = user_list.iterator();
		while (iter.hasNext()) {
			SFtpConfig sftpconfig = iter.next();
			if (userName.contentEquals(sftpconfig.getSftp_user()) && sftpconfig.getSftp_priv().indexOf(type) != -1) {
				return true;
			}
		}
		return false;
	}

	private SFtpConfig getSFtpConfig(String userName) {
		Iterator<SFtpConfig> iter = user_list.iterator();
		while (iter.hasNext()) {
			SFtpConfig sftpconfig = iter.next();
			if (userName.contentEquals(sftpconfig.getSftp_user())) {
				return sftpconfig;
			}
		}

		return null;
	}
}