package cn.edu.nju.cs.navydroid.gui.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Property {

	public static final int SOURCEPATH = 0;
	public static final int CLASSPATH = 1;
	public static final int ENTRY_ACTIVITY = 2;
	public static final int R_ID_CLASS = 3;
	public static final int R_LAYOUT_CLASS = 4;
	public static final int PACKAGE_NAME = 5;
	public static final int LAYOUT_DIRECTORY = 6;
	public static final int STRINGS_XML_FILE = 7;
	public static final int MANIFEST_XML_FILE = 8;
	public static final int OUTPUT_FILE = 9;
	
	private static final Map<Integer, Predicate<File>> filters = new HashMap<Integer, Predicate<File>>() {
		private static final long serialVersionUID = 1L;
		{
			put(SOURCEPATH, (f) -> {
				return f.isDirectory() 
						&& f.getName().equals("src");
			});
			put(CLASSPATH, (f) -> {
				return f.isDirectory() 
						&& f.getName().equals("classes") 
						&& f.getParentFile().getName().equals("bin");
			});
			put(LAYOUT_DIRECTORY, (f) -> {
				return f.isDirectory()
						&& f.getName().equals("layout");
			});
			put(STRINGS_XML_FILE, (f) -> {
				return f.isFile()
						&& f.getName().equals("strings.xml")
						&& f.getParentFile().getName().equals("values");
			});
			put(MANIFEST_XML_FILE, (f) -> {
				return f.isFile()
						&& f.getName().equals("AndroidManifest.xml");
			});
		}
	};
	
	public static Map<Integer, String> infer(String projectPath) {
		System.out.println("infer " + projectPath);
		Map<Integer, String> properties = inferFromFilename(projectPath);
		String manifestPath = properties.get(MANIFEST_XML_FILE);
		if (manifestPath != null) {
			Map<Integer, String> propertiesFromManifest = inferFromManifest(manifestPath);
			properties.putAll(propertiesFromManifest);
		}
		return properties;
	}

	private static Map<Integer, String> inferFromFilename(String projectPath) {
		File project = new File(projectPath);
		Map<Integer, String> properties = new HashMap<>();
		for (Entry<Integer, Predicate<File>> e : filters.entrySet()) {
			Integer propertyKey = e.getKey();
			Predicate<File> predicate = e.getValue();
			File result = findFile(project, predicate);
			properties.put(propertyKey, result.getAbsolutePath());
		}
		return properties;
	}
	
	private static File findFile(File path, Predicate<File> pred) {
		for (File child : path.listFiles()) {
			if (pred.test(child)) {
				return child;
			}
			if (child.isDirectory()) {
				File result = findFile(child, pred);
				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}
	
	private static class ManifestHandler extends DefaultHandler {
		
		private static final String MANIFEST_QNAME = "manifest";
		private static final String ACTIVITY_QNAME = "activity";
		private static final String INTENT_FILTER_QNAME = "intent-filter";
		private static final String ACTION_QNAME = "action";
		private static final String CATEGORY_QNAME = "category";
		
		private boolean inActivity;
		private String activityName;
		
		private boolean inIntentFilter;
		private boolean intentFilterIsActionMain;
		private boolean intentFilterIsCategoryLauncher;
		
		private String packageName;
		private String entryActivityName;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if (qName.equals(MANIFEST_QNAME)) {
				packageName = attributes.getValue("package");
			} else if (qName.equals(ACTIVITY_QNAME)) {
				inActivity = true;
				String android_name = attributes.getValue("android:name");
				setActivityName(android_name);
			} else if (inActivity && qName.equals(INTENT_FILTER_QNAME)) {
				inIntentFilter = true;
				intentFilterIsActionMain = false;
				intentFilterIsCategoryLauncher = false;
			} else if (inActivity && inIntentFilter && qName.equals(ACTION_QNAME)) {
				String android_name = attributes.getValue("android:name");
				if (android_name != null && android_name.equals("android.intent.action.MAIN")) {
					intentFilterIsActionMain = true;
				}
			} else if (inActivity && inIntentFilter && qName.equals(CATEGORY_QNAME)) {
				String android_name = attributes.getValue("android:name");
				if (android_name != null && android_name.equals("android.intent.category.LAUNCHER")) {
					intentFilterIsCategoryLauncher = true;
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals(ACTIVITY_QNAME)) {
				inActivity = false;
			} else if (qName.equals(INTENT_FILTER_QNAME)) {
				if (intentFilterIsActionMain && intentFilterIsCategoryLauncher) {
					entryActivityName = activityName;
				}
				inIntentFilter = false;
			}
		}
		
		private void setActivityName(String android_name) {
			if (android_name.contains(packageName)) {
				activityName = android_name;
			} else {
				if (!android_name.startsWith(".")) {
					android_name = "." + android_name;
				}
				activityName = packageName + android_name;
			}
		}

		public String getPackageName() {
			return packageName;
		}

		public String getEntryActivityName() {
			return entryActivityName;
		}
		
	}

	private static Map<Integer, String> inferFromManifest(String manifestPath) {
		File manifest = new File(manifestPath);
		ManifestHandler manifestHandler = new ManifestHandler();
		
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(manifest, manifestHandler);
		} catch (ParserConfigurationException e) {
			// do nothing
		} catch (SAXException e) {
			// do nothing
		} catch (IOException e) {
			// do nothing
		}
		
		Map<Integer, String> properties = new HashMap<>();
		String entryActivityName = manifestHandler.getEntryActivityName();
		String packageName = manifestHandler.getPackageName();
		properties.put(ENTRY_ACTIVITY, entryActivityName);
		properties.put(PACKAGE_NAME, packageName);
		properties.put(R_ID_CLASS, packageName + ".R$id");
		properties.put(R_LAYOUT_CLASS, packageName + ".R$layout");
		return properties;
	}
	
	public static void main(String[] args) {
		Map<Integer, String> properties = infer("/home/william/projects/GreenDroid2/testSubjects/AndTweet-bad");
		for (String p : properties.values()) {
			System.out.println(p);
		}
	}
}
