package com.example.andy.iotexp.clientSocketBh1750;

import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class clientSocketTools {

	/**
	 * get localhost IP address
	 * 
	 * @return IP address
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreferenceIpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * byte arrary to int
	 * 
	 * @param bb
	 *            byte array
	 * @param index
	 *            start position
	 * @return
	 */
	public static int getInt(byte[] data, int index) {
		return (int) ((((data[index + 1] & 0xff) << 8) | ((data[index + 0] & 0xff) << 0)));
	}

	/**
	 * java�ֽ���ת�ַ���
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b, int len) { // һ���ֽڵ�����

		// ת��16�����ַ���

		String hs = "";
		String tmp = "";
		for (int n = 0; n < len; n++) {
			// ����ת��ʮ�����Ʊ�ʾ

			tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (tmp.length() == 1) {
				hs = hs + "0" + tmp;
			} else {
				hs = hs + tmp;
			}
		}
		tmp = null;
		return hs.toUpperCase();

	}

	/**
	 * �ֽ�ת��Ϊ����
	 * 
	 * @param b
	 *            �ֽڣ�����4���ֽڣ�
	 * @param index
	 *            ��ʼλ��
	 * @return
	 */
	public static float byte2float(byte[] b, int index) {
		int temp;
		temp = b[index + 0];
		temp &= 0xff;
		temp |= ((long) b[index + 1] << 8);
		temp &= 0xffff;
		temp |= ((long) b[index + 2] << 16);
		temp &= 0xffffff;
		temp |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(temp);
	}
}
