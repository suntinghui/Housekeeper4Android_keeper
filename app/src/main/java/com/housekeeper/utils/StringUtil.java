package com.housekeeper.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.util.Base64;

public class StringUtil {

	/**
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static int getInt(String str, int defaultValue) {
		if (str == null)
			return defaultValue;
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static double getDouble(String str, double defaultValue) {
		if (str == null)
			return defaultValue;
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static double Currency2Double(String str) {
		if (str == null)
			return 0.0;
		try {
			StringBuffer sb = new StringBuffer();
			String[] temp = StringUtil.split(str, ",");
			for (int i = 0; i < temp.length; i++) {
				sb.append(temp[i]);
			}
			return Double.parseDouble(sb.toString());
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static String[] split(String original, String separator) {
		Vector nodes = new Vector();

		// Parse nodes into vector
		int index = original.indexOf(separator);
		while (index >= 0) {
			nodes.addElement(original.substring(0, index));
			original = original.substring(index + separator.length());
			index = original.indexOf(separator);
		}
		// Get the last node
		nodes.addElement(original);

		// Create splitted string array
		String[] result = new String[nodes.size()];
		if (nodes.size() > 0) {
			for (int loop = 0; loop < nodes.size(); loop++)
				result[loop] = (String) nodes.elementAt(loop);
		}
		return result;
	}

	public static String GetDisplayTitle(String alias, String creditCode) {
		StringBuffer sb = new StringBuffer();
		if (alias != null && alias.trim() != "") {
			sb.append("[");
			sb.append(alias.trim());
			sb.append("] ");
		}
		if (creditCode != null && creditCode.trim() != "") {
			if (creditCode.length() > 10) {
				sb.append(creditCode.substring(0, 6));
				sb.append("...");
				sb.append(creditCode.substring(creditCode.length() - 4, 4));
			} else
				sb.append(creditCode);
		}
		return sb.toString();
	}

	public static String formatAccountNo(String accountNo) {
		try {
			StringBuffer s = new StringBuffer();
			for (int i = 0; i < accountNo.length() - 10; i++) {
				s.append("*");
			}

			StringBuffer sb = new StringBuffer(accountNo);
			sb.replace(6, accountNo.length() - 4, s.toString());
			return sb.toString();
		} catch (Exception e) {
			return accountNo;
		}
	}

	// 银行卡号，每四位一个空格
	public static String formatCardId(String cardId) {
		StringBuffer sb = new StringBuffer();

		if (cardId != null && cardId.length() > 0) {
			for (int i = 0; i < cardId.length(); i++) {
				sb.append(cardId.substring(i, i + 1));
				if (i >= 3 && (i - 3) % 4 == 0 && i != cardId.length() - 1) {
					sb.append(" ");
				}
			}
		}

		return sb.toString();
	}

	public static boolean getBool(String str, boolean b) {
		return null != str && str.equalsIgnoreCase("true") ? true : false;
	}

	public static InputStream getInputStream(String str) {
		InputStream inputStream = new ByteArrayInputStream(str.getBytes());
		return inputStream;
	}

	/**
	 * 返回字节数组
	 * 
	 * @param in输入的流
	 * @return
	 * @throws Exception
	 */
	public static byte[] InputStram2byteArray(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (in != null) {
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				out.close();
				in.close();
				return out.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String inputStreamToString(InputStream stream) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream), 8192);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.trim().length() > 0) {
					sb.append(line);
				}
			}
			return sb.toString();

		} catch (IOException e) {
			e.printStackTrace();

		}

		return "";
	}

	public static String Image2Base64(String imageName) {
		// 对文件的操作
		try {
			FileInputStream in = new FileInputStream(imageName);
			byte buffer[] = StringUtil.InputStram2byteArray(in);// 把图片文件流转成byte数组
			byte[] encode = Base64.encode(buffer, Base64.DEFAULT);// 使用base64编码
			return new String(encode);

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 12->12.00 or 11111.1 -> 1,111.10
	 * 
	 * @param str
	 * @return
	 */
	public static String formatAmount(double d) {
		try {
			// 0 一个数字 
			// # 一个数字，不包括 0 
			DecimalFormat df = new DecimalFormat("###,##0.00");
			return df.format(d);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 将金额转化银联要求的12位长字符串格式 12.08 -> 0000000001208 点付宝也可以直接使用
	 */
	public static String amount2String(String amount) {
		String temp = amount.replace(".", "").replace(",", "");
		return String.format("%012d", Long.parseLong(temp));
	}

	/**
	 * 将12位长的银联格式的字符串转为金额格式 ￥ 1,200.00
	 * 
	 * @param str
	 * @return
	 */
	public static String String2SymbolAmount(String str) {
		try {
			String tempStr = NumberFormat.getNumberInstance().format(Long.parseLong(str, 10)).replace(",", "");
			double temp = Long.parseLong(tempStr) / 100.00;
			return NumberFormat.getCurrencyInstance(Locale.CHINA).format(temp);
		} catch (Exception e) {
			e.printStackTrace();
			return str;
		}
	}

	public static double String2AmountFloat(String str) {
		try {
			String tempStr = NumberFormat.getNumberInstance().format(Long.parseLong(str, 10)).replace(",", "");
			return Long.parseLong(tempStr) / 100.00;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.00;
		}
	}

	public static String String2Amount(String str) {
		return String2SymbolAmount(str).replace("￥", "").replace(",", "").trim();
	}

	public static long String2AmountFloat4QPOS(String str) {
		try {
			String tempStr = NumberFormat.getNumberInstance().format(Long.parseLong(str, 10)).replace(",", "");
			return Long.parseLong(tempStr);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String MD5Crypto(String str) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "00000000000000000000000000000000";
	}

	public static String toHexString(byte[] b) { // String to byte
		char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * Convert byte[] to hex string. 这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * 
	 * @param data
	 *            需要进行hex的字节数组数据
	 * @return hex String (大写)
	 */
	public static String bytes2HexString(byte[] data) {
		String ret = "";
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(data[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String dateStringFormate(String yyyyMMddhhmmss) {
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = format1.parse(yyyyMMddhhmmss.replace(" ", ""));

			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format2.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return yyyyMMddhhmmss;
		}
	}

	public static String hexToASCII(String hex) {
		StringBuilder sb = new StringBuilder();
		// Convert Hex 0232343536AB into two characters stream.
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String output = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
		}
		return sb.toString();
	}

	public static String asciiToHex(String ascii) {
		StringBuilder hex = new StringBuilder();

		for (int i = 0; i < ascii.length(); i++) {
			hex.append(Integer.toHexString(ascii.charAt(i)));
		}
		return hex.toString();
	}

	public static HashMap<String, String> JSONObject2Map(JSONObject jsonObject) {
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {
			@SuppressWarnings("unchecked")
			Iterator<String> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				responseMap.put(key, jsonObject.getString(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

	public static boolean checkIdCard(String idCard) {
		if (idCard.length() != 15 && idCard.length() != 18) {
			return false;
		}
		return true;

	}

	public static String addCommaDouble(Double d) {

		DecimalFormat df = new DecimalFormat();
		String style = "0.0";// 定义要显示的数字的格式
		style = ",###.##";
		df.applyPattern(style);
		System.out.println("采用style: " + style + "格式化之后: " + df.format(d));
		return df.format(d);
	}

	/**
	 * 中文转Unicode码
	 * 
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	public static boolean matches(String patternStr, String text) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
