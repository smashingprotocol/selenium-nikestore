package com.pcm.verify;

import org.openqa.selenium.WebDriver;

public class VerifyText {

	public static Boolean isFound(WebDriver driver,String keyword) {
		
		if (driver.getPageSource().toLowerCase().contains(keyword.toLowerCase())){
			return true;
		} else{
			return false;
		}
		
		
		
		
		
		
	}

}
