package cn.com.fanyu.utils;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 重命名文件
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String renameFileName(String path, String fileName) {
		// File file = new File(path + "/" + fileName);// 获取要报存的文件路径
		/*
		 * if (file.exists() && (StringUtil.getFileType(fileName).equals("jpg") ||
		 * StringUtil.getFileType(fileName).equals("jpeg") ||
		 * StringUtil.getFileType(fileName).equals("png") ||
		 * StringUtil.getFileType(fileName).equals("bmp")))//
		 * 如果文件已存在就重命名，不存在就直接返回 { return System.currentTimeMillis() + "." +
		 * StringUtil.getFileType(fileName); }
		 */
		return System.currentTimeMillis() + "." + StringUtil.getFileType(fileName);
		/*
		 * return System.currentTimeMillis() + "." +
		 * StringUtil.getFileType(fileName);
		 */
	}

	/**
	 * 判断字符串是否非空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNullOrEmpty(String s) {
		if (null == s || "".equals(s)) {
			return true;
		}
		return false;
	}

	/**
	 * 根据文件名获得文件类型
	 * 
	 * @param s
	 * @return
	 */
	public static String getFileType(String s) {
		return s.substring(s.lastIndexOf(".") + 1);
	}

	public static String getFileName(String s) {
		return s.substring(0, s.lastIndexOf("."));
	}

	public static boolean isNumber(String str) {
		boolean flag = false;
		// 判断正负数都可以
		// Pattern pattern = Pattern.compile("^[-]{0,1}[0-9]+$");
		Pattern pattern = Pattern.compile("^[1-9][0-9]*$");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			flag = true;
		}
		return flag;
	}

	public static String getPropValue(String key) {
		// action配置文件路径
		@SuppressWarnings("unused")
		String config_path = "WEB-INF/classes/config.properties";
		// 属性文件
		Properties prop = new Properties();
		// 把文件读入文件输入流，存入内存中
		InputStream fis;
		try {
			// fis = new FileInputStream(new File(path + config_path));
			fis = StringUtil.class.getClassLoader().getResourceAsStream("config.properties");
			// 加载文件流的属性
			prop.load(fis);
			return prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static boolean isNull(String str) {
		return (str == null || "".equals(str));
	}

	/**
	 * 校验两个字符串是否内容一致
	 */
	public static boolean equals(String source, String target) {
		if (source == null || target == null) {
			return false;
		}
		return source.trim().equals(target.trim());
	}

	/**
	 * 校验两个字符串是否内容一致
	 */
	public static boolean equals(Object source, Object target) {
		if (source == null || target == null) {
			return false;
		}
		return String.valueOf(source).equals(String.valueOf(target));
	}

	/**
	 * 校验两个字符串是否内容一致
	 */
	public static boolean equalsIgnoreCase(Object source, Object target) {
		if (source == null || target == null) {
			return false;
		}
		String src = String.valueOf(source).toUpperCase();
		String tag = String.valueOf(target).toUpperCase();
		return src.equals(tag);
	}

	public static String dictionarySort(String[] arr) {
		int i, j, k;
		String t = null;
		for (i = 0; i < arr.length - 1; i++) {
			k = i;
			for (j = i + 1; j < arr.length; j++) {
				Character c1 = Character.valueOf(arr[j].charAt(0));
				Character c2 = Character.valueOf(arr[k].charAt(0));
				if (c1.compareTo(c2) < 0)
					k = j;
			}
			if (i != k) {
				t = arr[i];
				arr[i] = arr[k];
				arr[k] = t;
			}
		}
		String sortStr = "";
		for (String s : arr)
			sortStr += s;
		return Encrypt(sortStr, "SHA-1");
	}

	public static String Encrypt(String strSrc, String encName) {
		// parameter strSrc is a string will be encrypted,
		// parameter encName is the algorithm name will be used.
		// encName dafault to "MD5"
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
		return strDes;
	}

	public static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}


	public static String tips(String message, String url, boolean flag) {
		try {
			return "redirect:/tips?url=" + URLEncoder.encode(url, "UTF-8") + "&msg="
					+ URLEncoder.encode(message, "UTF-8") + "&flag=" + flag;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String tips2(String message, String url, boolean flag) {
		try {
			return "/tips?url=" + URLEncoder.encode(url, "UTF-8") + "&msg=" + URLEncoder.encode(message, "UTF-8")
					+ "&flag=" + flag;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("static-access")
	public static String getSystemYear() {
		Calendar a = Calendar.getInstance();
		return a.YEAR + "";
	}

	public static String queryUrl(String pageContent) {
		String aReg = "<[aA].*?>.+?</[aA]>";
		// String urlReg = "<[a|A]\\s+href=([^>]*\\s*>)";
		Pattern p = Pattern.compile(aReg, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(pageContent);
		String tempURL = "";
		while (m.find()) {
			tempURL = m.group();
			tempURL = tempURL.substring(tempURL.indexOf("\"") + 1);
			if (!tempURL.contains("\""))
				continue;
			tempURL = tempURL.substring(0, tempURL.indexOf("\""));
			if (!tempURL.substring(0, 4).equals("http") && !tempURL.substring(0, 4).equals("HTTP")) {
				tempURL = "http" + "://" + 80 + tempURL;
			}
			// System.out.println(tempURL);
		}
		return tempURL;
	}

	public static void main(String[] args) throws MalformedURLException {
//		System.out.println(queryUrl("<a href=\"http://localhost/wx/main_project_list\"> >>壹关爱慈善指南 </a>"));
		//java Html2Image 实现html转图片功能

// html2image
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();

		String htmlstr = "<table width='654' cellpadding='0' cellspacing='0' bordercolor='#FFFFFF'><tr><td><img       src='http://img3.cache.netease.com/photo/0003/2012-05-10/8168FJIJ00AJ0003.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr><tr><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr><tr><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr><tr><td><img src='http://www.apkfather.com/yhqserver/images/mdl.jpg'/></td></tr></table>";

//		imageGenerator.loadHtml(htmlstr);
		imageGenerator.loadUrl("http://120.78.76.4:8080/robot/login");
		imageGenerator.getBufferedImage();

		imageGenerator.saveAsImage("d:/hello-world.png");

		imageGenerator.saveAsHtmlWithMap("hello-world.html", "hello-world.png");
	}


	/**
	 * 判断字符串是否为空
	 *
	 * @param strs
	 * @return
	 */
	public static boolean isNullOrEmpty(String... strs) {
		for (String str : strs) {
			if (null == str || "".equals(str)) {
				return true;
			}
		}
		return false;
	}

	//顺序表
	static String orderStr = "01234567890";
	static String orderStrDesc = "09876543210";

	//判断是否有顺序
	public static boolean isOrder(String str) {
		if (!str.matches("(\\d)+")) {
			return false;
		}
		return orderStr.contains(str);
	}
	//判断是否相同
	public static boolean isSame(String str) {
		String regex = str.substring(0, 1) + "{" + str.length() + "}";
		return str.matches(regex);
	}
}
