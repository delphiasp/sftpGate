package com.hwang.util.sftpserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.root.RootedFileSystemProvider;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.auth.pubkey.UserAuthPublicKeyFactory;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

import com.hwang.util.sftpserver.readonly.ReadOnlyRootedFileSystemProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SFtpServer {

	private Log log = LogFactory.getLog(SFtpServer.class);
	private Map<String, String> config;
	private List<SFtpConfig> user_list = new ArrayList<SFtpConfig>();
	private List<SFtpConfig> keys_list = new ArrayList<SFtpConfig>();

	public SFtpServer(Map<String, String> map_config) {
		this.config = map_config;
		String sftp_user = config.get("SFTP_USER");
		String[] user_array = sftp_user.split(";");
		// 用户|密码|主目录|权限|转发目标协议|转发目标地址|转发目标端口|转发目标用户|转发目标密码|转发目标路径|转发目标名称
		for (int i = 0; i < user_array.length; i++) {
			if (user_array[i].length() > 1) {
				String[] item_detail = user_array[i].split("\\|",-1);
				SFtpConfig sftpconfig = new SFtpConfig();

				sftpconfig.setSftp_user(item_detail[0]);
				sftpconfig.setSftp_pass(item_detail[1]);
				sftpconfig.setSftp_home(item_detail[2]);
				sftpconfig.setSftp_priv(item_detail[3]);
				sftpconfig.setProx_type(item_detail[4]);
				sftpconfig.setProx_addr(item_detail[5]);
				sftpconfig.setProx_port(item_detail[6]);
				sftpconfig.setProx_user(item_detail[7]);
				sftpconfig.setProx_pass(item_detail[8]);
				sftpconfig.setProx_path(item_detail[9]);
				sftpconfig.setProx_file(item_detail[10]);
				user_list.add(sftpconfig);
			}
		}

		String sftp_keys = config.get("SFTP_KEYS");
		String[] keys_array = sftp_keys.split(";");
		// 用户|密码|主目录|权限|转发目标协议|转发目标地址|转发目标端口|转发目标用户|转发目标密码|转发目标路径|转发目标名称
		for (int i = 0; i < keys_array.length; i++) {
			if (keys_array[i].length() > 1) {
				String[] item_detail = keys_array[i].split("\\|",-1);
				SFtpConfig sftpconfig = new SFtpConfig();
				sftpconfig.setSftp_user(item_detail[0]);
				sftpconfig.setSftp_home(item_detail[2]);
				sftpconfig.setSftp_priv(item_detail[3]);
				sftpconfig.setProx_type(item_detail[4]);
				sftpconfig.setProx_addr(item_detail[5]);
				sftpconfig.setProx_port(item_detail[6]);
				sftpconfig.setProx_user(item_detail[7]);
				sftpconfig.setProx_pass(item_detail[8]);
				sftpconfig.setProx_path(item_detail[9]);
				sftpconfig.setProx_file(item_detail[10]);
				keys_list.add(sftpconfig);
			}
		}

	}

	public void startServer() throws IOException {

		start();
	}

	private void start() throws IOException {

		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setHost(config.get("SFTP_ADDR"));
		sshd.setPort(Integer.parseInt(config.get("SFTP_PORT")));

		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("ftpGate.ser")));

		SftpSubsystemFactory sftpSubsystemFactory = new SftpSubsystemFactory();
		SFtpEventListener sftpEventListener = new SFtpEventListener(this);
		sftpEventListener.addFileListener(new SFtpFileListener());
		sftpSubsystemFactory.addSftpEventListener(sftpEventListener);
		sshd.setSubsystemFactories(Collections.singletonList(sftpSubsystemFactory));

		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<>();
		userAuthFactories.add(new UserAuthPasswordFactory());
		userAuthFactories.add(new UserAuthPublicKeyFactory());
		sshd.setUserAuthFactories(userAuthFactories);

		// 密码认证方式
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
			@Override
			public boolean authenticate(String username, String password, ServerSession session) {
				// set home directory
				Iterator<SFtpConfig> iter = user_list.iterator();
				while (iter.hasNext()) {
					SFtpConfig sftpconfig = iter.next();
					if ((username.equals(sftpconfig.getSftp_user())) && (password.equals(sftpconfig.getSftp_pass()))) {
						log.info("User Auth successfully:[" + username + "]--Home[" + sftpconfig.getSftp_home()
								+ "]--Privilege:[" + sftpconfig.getSftp_priv() + "]");

						boolean writeable = false;
						if (sftpconfig.getSftp_priv().contains("W"))
							writeable = true;

						try {
							SecureFileSystemFactory vfs = new SecureFileSystemFactory(writeable,
									sftpconfig.getSftp_home());

							sshd.setFileSystemFactory(vfs);
						} catch (Exception e) {
							log.info("UserName:" + username + " " + e.getMessage());
						}
						return true;
					}
				}

				return false;
			}
		});

		log.info("Public key file:" + new File("config/authorized_keys").getAbsolutePath());
		Path pubFilekey = Paths.get("config/authorized_keys");
		// 证书认证方式
		sshd.setPublickeyAuthenticator(new AuthorizedKeysAuthenticator(pubFilekey) {
			@Override
			public boolean authenticate(String username, PublicKey key, ServerSession session) {
				if (!isValidUsername(username, session)) {
					if (log.isDebugEnabled()) {
						log.debug("authenticate(" + username + ")[" + session + "][" + key.getAlgorithm()
								+ "] invalid user name - file = " + getPath());
					}
					return false;
				}

				try {
					PublickeyAuthenticator delegate = Objects
							.requireNonNull(resolvePublickeyAuthenticator(username, session), "No delegate");
					boolean accepted = delegate.authenticate(username, key, session);
					if (log.isDebugEnabled()) {
						log.debug("authenticate(" + username + ")[" + session + "][" + key.getAlgorithm()
								+ "] accepted " + accepted + " from " + getPath());
					}

					// set home directory
					Iterator<SFtpConfig> iter = keys_list.iterator();
					while (iter.hasNext()) {
						SFtpConfig sftpconfig = iter.next();
						if ((username.equals(sftpconfig.getSftp_user()))) {
						

							log.info("Keys Auth Info:[" + username + "]--Home[" + sftpconfig.getSftp_home()
									+ "]--Privilege:[" + sftpconfig.getSftp_priv() + "]");
							
							boolean writeable = false;
							if (sftpconfig.getSftp_priv().contains("W"))
								writeable = true;
							try {
								SecureFileSystemFactory vfs = new SecureFileSystemFactory(writeable,
										sftpconfig.getSftp_home());

								sshd.setFileSystemFactory(vfs);
							} catch (Exception e) {
								log.info("UserName:" + username + " " + e.getMessage());
							}
							break;
						}
					}

					if (accepted) {

						log.info("Keys Auth successfully:[" + username + "]");
					}else {

						log.info("Keys Auth failed:[" + username + "]");
					}
					return accepted;
				} catch (Throwable e) {
					if (log.isDebugEnabled()) {
						log.debug("authenticate(" + username + ")[" + session + "][" + getPath() + "]" + " failed ("
								+ e.getClass().getSimpleName() + ")" + " to resolve delegate: " + e.getMessage());
					}

					if (log.isTraceEnabled()) {
						log.trace("authenticate(" + username + ")[" + session + "][" + getPath() + "] failure details",
								e);
					}
					return false;
				}

			}

		});

		sshd.start();
		log.info("SFTP server started");

	}

	public Map<String, String> getConfig() {
		return config;
	}

	public List<SFtpConfig> getuser_list() {
		return user_list;
	}

	public List<SFtpConfig> getkeys_list() {
		return keys_list;
	}

	static class SecureFileSystemFactory extends VirtualFileSystemFactory {
		private boolean writeable;
		private String home;

		public SecureFileSystemFactory(final boolean writeable, String home) {
			this.writeable = writeable;
			this.home = home;

		}

		@Override
		public FileSystem createFileSystem(Session session) throws IOException {

			String username = session.getUsername();
			super.setUserHomeDir(username, new File(home).toPath());
			Path dir = computeRootDir(session);
			if (dir == null) {
				throw new InvalidPathException(username, "Cannot resolve home directory");
			}
			final RootedFileSystemProvider rfsp = writeable ? new RootedFileSystemProvider()
					: new ReadOnlyRootedFileSystemProvider();
			return rfsp.newFileSystem(Paths.get(home), Collections.<String, Object>emptyMap());

		}

	}

}