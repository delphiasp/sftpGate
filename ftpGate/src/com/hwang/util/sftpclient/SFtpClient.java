package com.hwang.util.sftpclient;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.Security;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sshd.client.SshClient;

import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClientFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

public class SFtpClient {

	private SshClient sshclient;
	private ClientSession clientsession;
	private SftpClient sftpclient;

	private String serverIP = "";
	private int serverPort = 22;
	private String authType = "PASS";// PASS--密码认证 KEY--密钥对认证
	private int tcpTimeOut = 10 * 1000;
	private String userName = "";
	private String userPass = "";
	private String keyFile = "";
	private String keyPass = "";

	static Log logger = LogFactory.getLog(SFtpClient.class);

	private KeyPair readKeyPair(File pemFile, char[] password) throws Exception {
		PEMParser pemParser = new PEMParser(new FileReader(pemFile));
		Object object = pemParser.readObject();
		pemParser.close();
		PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password);
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

		KeyPair kp = null;
		if (object instanceof PEMEncryptedKeyPair) {
			kp = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
		} else {
			kp = converter.getKeyPair((PEMKeyPair) object);
		}

		return kp;
	}

	private KeyPair initKeyPair(String pemFilePath, String password) {

		Security.addProvider(new BouncyCastleProvider());
		KeyPair kp;
		try {
			kp = (KeyPair) readKeyPair(new File(pemFilePath), password.toCharArray());
			logger.info("Load keyPair file [" + pemFilePath + "] successfully.");
			return kp;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Load keyPair file [" + pemFilePath + "] failed.");
		}

		return null;
	}

	public SftpClient connect() {
		sshclient = SshClient.setUpDefaultClient();
		sshclient.start();

		try {
			logger.info("connecting to " + serverIP + ":" + serverPort + " for " + userName);
			clientsession = sshclient.connect(userName, serverIP, serverPort).verify(tcpTimeOut).getSession();
			logger.info("connect to server successfully.");

			if ("PASS".contentEquals(authType)) {
				clientsession.addPasswordIdentity(userPass);
			} else {
				KeyPair kp = initKeyPair(keyFile, keyPass);
				clientsession.addPublicKeyIdentity(kp);
			}

			clientsession.auth().verify(tcpTimeOut);

			if (!clientsession.isAuthenticated()) {
				logger.info("Auth failed.");
				return null;
			}

			sftpclient = SftpClientFactory.instance().createSftpClient(clientsession);
			logger.info("Auth successfully.");

			return sftpclient;

		} catch (IOException e) {

			e.printStackTrace();
			logger.info("connect to server failed.");
			return null;
		}

	}

	public void readFile(String remoteFile, String localFile) {
		Path localPath = Paths.get(localFile);

		try {
			InputStream in = sftpclient.read(remoteFile);

			Files.deleteIfExists(localPath);
			Files.copy(in, localPath);

			logger.info("read file [" + remoteFile + "] from server to [" + localFile + "] successfully.");

		} catch (IOException e) {
			logger.info("read file [" + remoteFile + "] from server to [" + localFile + "] failed.");
			e.printStackTrace();
		}

	}

	public void writeFile(String remoteFile, String localFile) {
		Path localPath = Paths.get(localFile);

		try {
			OutputStream out = sftpclient.write(remoteFile);
			Files.copy(localPath, out);
		} catch (Exception e) {
			logger.info("write file [" + remoteFile + "] from client [" + localFile + "] failed.");
			e.printStackTrace();
		}

	}

	public void listDir(String dirName) {
		try {

			sftpclient.readDir(dirName).forEach(dir -> {
				System.out.println(MessageFormat.format("name: {0}\t\t size: {2} \t\ttype: {1}", dir.getFilename(),
						dir.getAttributes().getType(), dir.getAttributes().getSize()));
			});
		} catch (IOException e) {
			logger.info("List directory failed.");
			e.printStackTrace();
		}

	}

	public void disconnect() {
		try {
			sftpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sshclient.stop();
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public int getTcpTimeOut() {
		return tcpTimeOut;
	}

	public void setTcpTimeOut(int tcpTimeOut) {
		this.tcpTimeOut = tcpTimeOut;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getKeyFile() {
		return keyFile;
	}

	public void setKeyFile(String keyFile) {
		this.keyFile = keyFile;
	}

	public String getKeyPass() {
		return keyPass;
	}

	public void setKeyPass(String keyPass) {
		this.keyPass = keyPass;
	}

	public static void main(String args[]) throws Exception {
		SFtpClient client = new SFtpClient();
		client.setServerIP("10.10.10.1");
		client.setServerPort(2222);
		client.setAuthType("KEY");
		client.setUserName("root");
		client.setKeyFile("id_rsa");
		client.setKeyPass("aaaaa");
		client.connect();
		client.listDir("/prog");
		
		client.readFile("/prog/pilot", "e:\\pilot");
		
		//client.writeFile("/pilot", "e:\\pilot");
		
		client.disconnect();

	 
		 
	}

}
