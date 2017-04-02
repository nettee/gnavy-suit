package cn.edu.nju.cs.navydroid.gui.model;

import java.util.ArrayList;
import java.util.List;

public class TestSubjectManager {
	
	private static TestSubjectManager instance = null;
	
	private List<TestSubject> testSubjects = new ArrayList<>();
	
	public static TestSubjectManager getInstance() {
		if (instance == null) {
			instance = new TestSubjectManager();
		}
		return instance;
	}
	
	private TestSubjectManager() {
		
	}
	
	public void addTestSubject(TestSubject testSubject) {
		testSubjects.add(testSubject);
	}
	
	public Iterable<TestSubject> getTestSubjects() {
		return testSubjects;
	}

}
