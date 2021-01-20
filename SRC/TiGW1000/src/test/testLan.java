package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import tijos.framework.platform.lan.TiLAN;
import tijos.framework.platform.network.NetworkException;
import tijos.framework.util.BigBitConverter;
import tijos.framework.util.Formatter;

public class testLan {

	public static void main(String[] args) throws NetworkException {

		TiLAN.getInstance().startup(20);
		String deviceId = "15018740867435057486946";
		String password = "6NX7nYNmTWNypWDEwmwvR6r908AprbKUdZzatG8xOEY";
		String version = "1.0";

		int pos = 0;
		byte[] buff = new byte[512];

		buff[pos++] = 0x01;
		BigBitConverter.FillBytes((short) deviceId.length(), buff, pos);
		pos += 2;

		deviceId.getBytes(0, deviceId.length(), buff, pos);
		pos += deviceId.length();

		BigBitConverter.FillBytes((short) password.length(), buff, pos);
		pos += 2;

		password.getBytes(0, password.length(), buff, pos);
		pos += password.length();

		BigBitConverter.FillBytes((short) version.length(), buff, pos);
		pos += 2;

		version.getBytes(0, version.length(), buff, pos);
		pos += version.length();

		System.out.println("login " + Formatter.toHexString(buff, 0, pos, ""));

		// TCP服务器IP及PORT
		String host = "tcp.ctwing.cn";
		int port = 8996;
		Socket client = null;
		try {
			// Connect to the server with TCP
			client = new Socket(host, port);
			System.out.println(
					"Connect : " + client.getRemoteSocketAddress() + " local " + client.getLocalSocketAddress());

			OutputStream output = client.getOutputStream();

			// Send data to the TCP server
			// output.write(("Hello, this is client " + port).getBytes());
			output.write(buff, 0, pos);
			// output.flush();

			// Get remote data from the server
			InputStream input = client.getInputStream();

			byte[] buffer = new byte[1024];
			while (true) {
				int len = -1;
				len = input.read(buffer);

				if (len > 0) {
					System.out.println("message form server:" + Formatter.toHexString(buffer, 0, len, ""));

					// echo to the server
					// output.write(buffer, 0, len);
					// output.flush();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

}
