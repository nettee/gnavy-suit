package cn.edu.nju.cs.navydroid.gui.model;

import java.util.HashMap;
import java.util.Map;

public class TestSubjectManager {
	
	private static TestSubjectManager instance = null;
	
	private Map<String, TestSubject> testSubjectMap = new HashMap<>();
	
	public static TestSubjectManager getInstance() {
		if (instance == null) {
			instance = new TestSubjectManager();
		}
		return instance;
	}
	
	private TestSubjectManager() {
		
	}
	
	public void addTestSubject(TestSubject testSubject) {
		testSubjectMap.put(testSubject.getName(), testSubject);
	}
	
	public TestSubject getTestSubject(String name) {
		return testSubjectMap.get(name);
	}
	
	public Iterable<TestSubject> getTestSubjects() {
		return testSubjectMap.values();
	}

}
