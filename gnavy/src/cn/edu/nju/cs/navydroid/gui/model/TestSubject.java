package cn.edu.nju.cs.navydroid.gui.model;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
	
	public void analyze(LogListener logListener, ReportListener reportListener) {
		Properties.toFile(this, Exec.FILE_PROPERTIES);
		OnStdoutLineListener stdoutListener = (line) -> {
			logListener.onLogLine(line);
		};
		OnStderrLineListener stderrListener = (line) -> {
			logListener.onLogLine(line);
		};
		OnExitValueListener exitValueListener = (exitValue) -> {
			if (exitValue != 0) {
				return;
			}
			// read report
			try (Scanner scanner = new Scanner(Exec.FILE_PROBLEMS)) {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					reportListener.onReportLine(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			reportListener.onReportLine("");
			try (Scanner scanner = new Scanner(Exec.FILE_TRACE)) {
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					reportListener.onReportLine(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		};
		Exec.exec(stdoutListener, stderrListener, exitValueListener);
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
	
	@Override
	public String toString() {
		return String.format("TestSubject{name=%s, type=%d, location=%s}", name, sourceType, sourcePath);
	}


}
