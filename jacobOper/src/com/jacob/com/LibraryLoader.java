package com.jacob.com;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class LibraryLoader {
	public static final String JACOB_DLL_PATH = "jacob.dll.path";
	public static final String JACOB_DLL_NAME = "jacob.dll.name";
	public static final String JACOB_DLL_NAME_X86 = "jacob.dll.name.x86";
	public static final String JACOB_DLL_NAME_X64 = "jacob.dll.name.x64";
	public static final String DLL_NAME_MODIFIER_32_BIT = "x86";
	public static final String DLL_NAME_MODIFIER_64_BIT = "x64";
	public static final String PROJ_DLL_PATH = "WEB-INF/ext/dll/";

	public static void loadJacobLibrary() {
		ResourceBundle resources = null;
		Set keys = new HashSet();
		try {
			resources = ResourceBundle.getBundle(LibraryLoader.class.getName(),
					Locale.getDefault(), LibraryLoader.class.getClassLoader());

			Enumeration i = resources.getKeys();
			while (i.hasMoreElements()) {
				String key = (String) i.nextElement();
				keys.add(key);
			}

		} catch (MissingResourceException e) {
		}
		String path, name, dir;
		//String allPath=Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
		//System.out.println("------"+allPath);
		String contentPath=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getServletContext().getRealPath(PROJ_DLL_PATH);
		path = contentPath + File.separator; //先获取当前目录下的dll文件夹路径
		System.out.println(path);
		name = getPreferredDLLName();
		dir = path + name + ".dll"; //加后缀适应load()方法；

		JacobObject.debug("Loadinglibrary " + dir
				+ " using System.loadLibrary ");
		if (new File(dir).exists()) {
			System.load(dir); //使用load()可载入任意绝对路径下的库文件；
			System.out.println("载入dll文件成功！");

		} else {
			path = System.getProperty(JACOB_DLL_PATH);
			if ((path == null) && (resources != null)
					&& (keys.contains(JACOB_DLL_PATH))) {
				path = (String) resources.getObject(JACOB_DLL_PATH);
			}

			if (path != null) {
				JacobObject.debug("Loading library" + path
						+ " using System.loadLibrary ");
				System.loadLibrary(path);
			} else {
				name = null;

				if (System.getProperty(JACOB_DLL_NAME) != null)
					name = System.getProperty(JACOB_DLL_NAME);
				else if ((System.getProperty(JACOB_DLL_NAME_X86) != null)
						&& (shouldLoad32Bit())) {
					name = System.getProperty(JACOB_DLL_NAME_X86);
				} else if ((System.getProperty(JACOB_DLL_NAME_X64) != null)
						&& (!shouldLoad32Bit())) {
					name = System.getProperty(JACOB_DLL_NAME_X64);
				} else if ((resources != null)
						&& (keys.contains(JACOB_DLL_NAME)))
					name = resources.getString(JACOB_DLL_NAME);
				else if ((resources != null)
						&& (keys.contains(JACOB_DLL_NAME_X86))
						&& (shouldLoad32Bit())) {
					name = resources.getString(JACOB_DLL_NAME_X86);
				} else if ((resources != null)
						&& (keys.contains(JACOB_DLL_NAME_X64))
						&& (!shouldLoad32Bit())) {
					name = resources.getString(JACOB_DLL_NAME_X64);
				} else {
					name = getPreferredDLLName();
				}

				JacobObject.debug("Loading library" + name
						+ " using System.loadLibrary ");

				System.loadLibrary(name);
			}

		}
	}

	public static String getPreferredDLLName() {
		if (shouldLoad32Bit()) {
			return "jacob-" + JacobReleaseInfo.getBuildVersion() + "-" + "x86";
		}

		return "jacob-" + JacobReleaseInfo.getBuildVersion() + "-" + "x64";
	}

	protected static boolean shouldLoad32Bit() {
		String bits = System.getProperty("sun.arch.data.model", "?");
		if (bits.equals("32"))
			return true;
		if (bits.equals("64")) {
			return false;
		}

		String arch = System.getProperty("java.vm.name", "?");
		if (arch.toLowerCase().indexOf("64-bit") >= 0) {
			return false;
		}
		return true;
	}
}