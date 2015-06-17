package com.pcm.tests;


import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;

public class HomepageAlive {


	public boolean testStatus;
	
	@Test
	public void openHomepage() throws Exception{

		try 
		{
			
			Properties pr = Config.properties(); //create a method for the pcm.properies
			
			Config.setup(pr.getProperty("TEST_HOST"));
			
			//Verify the xpath of the Search box is found.
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("SEARCH_INPUT_XPATH"));
			
			Assert.assertTrue(testStatus);
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Homepage");
			
			
		}
		
			catch (Exception e)
			{
				System.out.println("[PCM] Verify Homepage...FAILED");
				Assert.fail(e.getMessage());
				
			}
		
	}
	
	@AfterClass
	public static void quit() throws IOException{
		TakeScreenShot.CurrentPage(Config.driver, "Last Page Test Result");
		Config.driver.close();
		Config.driver.quit();
	}
	
	
}
