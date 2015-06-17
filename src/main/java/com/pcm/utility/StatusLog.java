package com.pcm.utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;

public class StatusLog {

	public static void printlnPassedResult(WebDriver driver,String string) throws IOException {
		
		String now = new SimpleDateFormat("mmddhhmmss").format(new Date());
		TakeScreenShot.CurrentPage(driver,string.replaceAll("[^\\w]", "") + now + "PASSED");
		System.out.println(string + " :PASSED");
		
	}

	public static void printlnFailedResult(WebDriver driver, String string) throws IOException {
		
		String now = new SimpleDateFormat("mmddhhmmss").format(new Date());
		TakeScreenShot.CurrentPage(driver,string.replaceAll("[^\\w]", "") + now + "FAILED");
		System.out.println(string + " :FAILED");
		
	}

}
