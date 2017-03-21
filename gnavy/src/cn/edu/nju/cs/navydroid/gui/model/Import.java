package cn.edu.nju.cs.navydroid.gui.model;

import java.util.HashMap;
import java.util.Map;

public class Import {

	public static final Map<String, String> ways = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("APK file", "Add test subject from an APK file");
			put("Android project", "Add test subject from a directory");
		}
	};

}
