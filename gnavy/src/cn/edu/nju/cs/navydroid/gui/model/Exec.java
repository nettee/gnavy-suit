package cn.edu.nju.cs.navydroid.gui.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Exec {
	
	public static final File FILE_BIN = new File("/home/william/projects/GreenDroid2/bin/navy");
	public static final File FILE_PROPERTIES = new File("/tmp/navy_ts.properties");
	public static final File FILE_TRACE = new File("/tmp/trace.xml");
	public static final File FILE_PROBLEMS = new File("/tmp/problems.xml");
	
	public static void exec(OnStdoutLineListener stdoutListener,
			OnStderrLineListener stderrLineListener,
			OnExitValueListener exitValueListener) {
		Runtime rt = Runtime.getRuntime();
		
		String[] command = {
				FILE_BIN.getAbsolutePath(),
				FILE_PROPERTIES.getAbsolutePath(),
		};
		
		try {
			Process process = rt.exec(command);
			
			{
				InputStream stdout = process.getInputStream();
				BufferedReader br1 = new BufferedReader(new InputStreamReader(stdout));
				String stdoutLine = null;
				while ((stdoutLine = br1.readLine()) != null) {
					stdoutListener.onOutputLine(stdoutLine);
				}
			}

			{
				InputStream stderr = process.getErrorStream();
				BufferedReader br2 = new BufferedReader(new InputStreamReader(stderr));
				String stderrLine = null;
				while ((stderrLine = br2.readLine()) != null) {
					stderrLineListener.onStderrLine(stderrLine);
				}
			}
			
			int exitValue = process.waitFor();
			exitValueListener.onExitValue(exitValue);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
