package com.socialheat.util;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ProjectInfo {
	
	private String name;
	private String startTime;
	private String endTime;
	
	public ProjectInfo(String projectName) {
		init(projectName);
	}
	
	private void init(String projectName){
		String url = ProjectInfo.class.getResource("/project.xml").getFile();  
		SAXReader reader = new SAXReader();
        Document document = null;
		try {
			document = reader.read(new File(url));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        Element root = document.getRootElement();
		for (Object object : root.elements()) {
			Element element = (Element) object;
			if(element.elementText("name").equals(projectName)) {
				name = element.elementText("name");
		   		startTime = element.elementText("startTime");
		   		endTime = element.elementText("endTime");
		   		break;
			}
		}
   		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public static void main(String[] args) {
		ProjectInfo p = new ProjectInfo("dfxz");
		System.out.println(p.getName());
		System.out.println(p.getStartTime());
		System.out.println(p.getEndTime());
	}
}
