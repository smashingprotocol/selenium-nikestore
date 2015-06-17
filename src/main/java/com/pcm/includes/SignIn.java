package com.pcm.includes;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.pcm.form.SetInputField;
import com.pcm.request.ClickElement;
import com.pcm.verify.verifyXPath;

public class SignIn {

	public static Properties properties;
	static boolean login = true;
	
	public static void login(WebDriver driver, String email, String password) throws Exception{
		// TODO Auto-generated method stub
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try {
			properties.load(reader);
			
			//Check first if user is logged in, click sign out.
			if (login = verifyXPath.isfound(driver,properties.getProperty("HEADER_LINK_SIGNOUT_XPATH"))){
				System.out.println("[PCM] Logging out user...");
				ClickElement.byXPath(driver, properties.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			}
			
			//ClickElement.byXPath(driver, properties.getProperty("HEADER_LINK_SIGNIN_XPATH"));
			ClickElement.byXPath(driver, properties.getProperty("FOOTER_LINK_MYACT_LOGIN_XPATH"));
			SetInputField.byXPath(driver,properties.getProperty("LOGIN_INPUT_EMAIL_XPATH"),email);
			SetInputField.byXPath(driver,properties.getProperty("LOGIN_INPUT_PSWD_XPATH"),password);
			try {
				ClickElement.byXPath(driver, properties.getProperty("LOGIN_BTN_SIGNIN_XPATH"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void logout(WebDriver driver) throws Exception{
		// TODO Auto-generated method stub
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try {
			properties.load(reader);
			
			//Check first if user is logged in, click sign out.
			if (verifyXPath.isfound(driver,properties.getProperty("HEADER_LINK_SIGNOUT_XPATH"))){
				ClickElement.byXPath(driver, properties.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
