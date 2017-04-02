package cn.edu.nju.cs.navydroid.gui.model;

import java.util.HashMap;
import java.util.Map;

public class TestSubject {

	public static final int SOURCE_APK = 0;
	public static final int SOURCE_DIRECTORY = 1;

	private String name;
	private int sourceType;
	private String sourcePath;
	private final Map<Integer, String> properties = new HashMap<>();
	
	public void addProperty(Integer key, String value) {
		properties.put(key, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public Map<Integer, String> getProperties() {
		return properties;
	}

}
