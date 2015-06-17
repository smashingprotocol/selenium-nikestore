package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Search;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;

public class SearchKeyword {
	
	public String keyword;
	public String qty;

	
	@Test
	public void searchkeyword() throws Exception{
		
		try{
			
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			
			keyword = pr.getProperty("SEARCH_KEYWORD_DFLT");
			qty = pr.getProperty("SEARCH_INPUT_QTY_XPATH");
			
			Search.keyword(Config.driver,keyword);
			Assert.assertTrue(verifyXPath.isfound(Config.driver,qty));  //Verify the Qty field in search results appears
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Search for keyword: " + keyword);
			
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Search for keyword: " + keyword);
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
