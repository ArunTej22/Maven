package DriverFactory;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utilities.ExcelFileUtil;

public class AppTest {
	WebDriver driver;
	String inputpath ="F:\\AutomationTesting\\MavenAutomation\\TestInput\\LoginData.xlsx";
	String outputpath="F:\\AutomationTesting\\MavenAutomation\\TestOutput\\ApptestResults.xlsx";
	ExtentReports reports;
	ExtentTest test;
	@BeforeTest
	public void setup()
	{
		reports= new ExtentReports("./Reports/DataDriven.html");
		System.setProperty("webdriver.chrome.driver","f://chromedriver.exe" );
		driver=new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
	}
	@Test
	public void verifyLogin() throws Throwable 
	{
		ExcelFileUtil xl=new ExcelFileUtil(inputpath);
		int rc = xl.rowCount("Login");
		Reporter.log("No of rows are::"+rc,true);
		for (int i = 1; i <=rc; i++)
		{
			test=reports.startTest("validte login");
			driver.get("http://orangehrm.qedgetech.com/");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String username= xl.getCellData("Login", i, 0);
			String password =xl.getCellData("Login", i, 1);
			driver.findElement(By.cssSelector("#txtUsername")).sendKeys(username);
			driver.findElement(By.cssSelector("#txtPassword")).sendKeys(password);
			driver.findElement(By.cssSelector("#btnLogin")).click();
			String expected="dashboard";
			String actual =driver.getCurrentUrl();
			if(actual.contains(expected))
			{				
				xl.setCellData("Login", i, 2, "Login success", outputpath);			
				xl.setCellData("Login", i, 3, "Pass", outputpath);
				test.log(LogStatus.PASS, "Login success:::");
				Reporter.log("Login success::",true);
			}
			else
			{				
				xl.setCellData("Login", i, 2, "login fail", outputpath);
				xl.setCellData("Login", i, 3, "Fail", outputpath);
				test.log(LogStatus.FAIL,"login fail");
				Reporter.log("Login Fail::",true);
				
			}
			reports.endTest(test);
			reports.flush();
			
		}
		
	}
	@AfterTest
	public void tearDown()
	{
		driver.close();
	}

}
