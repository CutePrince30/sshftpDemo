package org.jeremy.app.sshsftp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) throws Exception {
		//sshSftp("10.161.34.227", "ftpuser", "resuptf", 2200);
		ssh2();
	}

	public static void ssh2() throws Exception {
		String charset = "UTF-8" ;
	    String user = "root" ;
	    String passwd = "gong!yffw@wang" ;
	    String host = "202.99.45.154" ;
	    int port = 238;

		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setPassword(passwd);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		
		session.connect(3000);
		
		for (int i = 0; i < 3; i++) {
			System.out.println(i + ": \n");
			
			getMaxLineNum(session);

			/*ChannelExec channel = (ChannelExec) session.openChannel("exec");
			
			channel.setCommand("cd /opt/app/training4g/logs\n cat app.log| head -n 20 | tail -n +10");
			channel.setInputStream(null);
			channel.setErrStream(System.err);

			channel.connect();
			InputStream in = channel.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in,
					Charset.forName(charset)));
			String buf = null;
			while ((buf = reader.readLine()) != null) {
				System.out.println(buf);
			}
			reader.close();
			channel.disconnect();*/
			
			Thread.sleep(10000);
		}
		
		session.disconnect();
		
	}

	private static void getMaxLineNum(Session session) throws Exception {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand("cd /opt/unisk\n wc -l app.log");
		channel.setInputStream(null);
		channel.setErrStream(System.err);

		channel.connect();
		InputStream in = channel.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in,
				Charset.forName("UTF-8")));
		String buf = null;
		while ((buf = reader.readLine()) != null) {
			System.out.println(buf);
		}
		reader.close();
		channel.disconnect();
	}

	/**
	 * 利用JSch包实现SFTP下载、上传文件
	 * 
	 * @param ip
	 *            主机IP
	 * @param user
	 *            主机登陆用户名
	 * @param psw
	 *            主机登陆密码
	 * @param port
	 *            主机ssh2登陆端口，如果取默认值，传-1
	 */
	public static void sshSftp(String ip, String user, String psw, int port)
			throws Exception {
		Session session = null;
		Channel channel = null;

		JSch jsch = new JSch();

		if (port <= 0) {
			// 连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		}
		else {
			// 采用指定的端口连接服务器
			session = jsch.getSession(user, ip, port);
		}

		// 如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}

		// 设置登陆主机的密码
		session.setPassword(psw);// 设置密码
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		// 设置登陆超时时间
		session.connect(30000);

		try {
			// 创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;

			// 进入服务器指定的文件夹
			sftp.cd("/data/ftpuser/d_files");

			// 列出服务器指定的文件列表
			Vector v = sftp.ls("*.*");
			for (int i = 0; i < v.size(); i++) {
				System.out.println(v.get(i));
			}

			/*
			 * // 以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了 OutputStream outstream =
			 * sftp.put("1.txt"); InputStream instream = new FileInputStream(new
			 * File("c:/print.txt"));
			 * 
			 * byte b[] = new byte[1024]; int n; while ((n = instream.read(b))
			 * != -1) { outstream.write(b, 0, n); }
			 * 
			 * outstream.flush(); outstream.close(); instream.close();
			 */
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (session != null) {
				session.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
		}
	}

}