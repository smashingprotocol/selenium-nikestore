package com.pcm.utility;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TakeScreenShot {

	static Properties sys = System.getProperties(); 
	
	public static void CurrentPage(WebDriver driver, String fileName) throws IOException {
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(sys.getProperty("screenShotDir") + "\\" + fileName + ".png"));
		
		System.out.println("[SCREENSHOT] Storing screenshot at " + sys.getProperty("screenShotDir") + "\\" + fileName + ".png");
		
	} //end CurrentPage

	
	
}
