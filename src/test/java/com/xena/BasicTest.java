package com.xena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class BasicTest {
	
	private WebDriver driver; 

	LocalDate today = LocalDate.now();
	
	@Before
    public void setUp() {
		
		System.setProperty("webdriver.chrome.driver", "/Users/xenadaisy/Development/Drivers/chromedriver");
		String baseURL = "http://computer-database.gatling.io/computers"; 
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(baseURL); 	
    }
	
	@After 
	public void tearDown() {
		driver.quit(); 
	}   

	
	private void addNewComputer(String newComputerName) throws InterruptedException {
		
		WebElement addComputerButton = driver.findElement(By.id("add"));
		addComputerButton.click();   
		
		Thread.sleep(3000);
		
		WebElement computerName = driver.findElement(By.id("name"));
		computerName.click(); 
		computerName.clear();
		computerName.sendKeys(newComputerName); 
		
	//	Thread.sleep(5000);
		
		
		WebElement dateIntroduced = driver.findElement(By.id("introduced"));
		dateIntroduced.click(); 
		dateIntroduced.clear();
		dateIntroduced.sendKeys(today.minusYears(10).toString());  
		
		
		WebElement dateDiscontinued = driver.findElement(By.id("discontinued"));
		dateDiscontinued.click(); 
		dateDiscontinued.clear();
		dateDiscontinued.sendKeys(today.toString());
	
		Select drpCompany = new Select(driver.findElement(By.id("company"))); 
		drpCompany.selectByVisibleText("Apple Inc.");     
		
		
		WebElement createComputer = driver.findElement(By.xpath("//*[@id=\"main\"]/form/div/input"));
		createComputer.click();
		
		Thread.sleep(3000); 
			
	}  
	
	private String readAlertMessage() {
		
		return driver.findElement(By.xpath("//*[@id=\"main\"]/div[1]")).getText(); 
		
	}

     
	private boolean findByNameInDatabase(String computerName) throws InterruptedException {
		
		WebElement searchBox = driver.findElement(By.id("searchbox")); 
		searchBox.click();
		searchBox.clear();
		
		Thread.sleep(3000);
		
		searchBox.sendKeys(computerName);
		searchBox.submit();
		
		String expectedResult = computerName; 
		
		String foundComputerName = ""; 
		
		try {
			foundComputerName = driver.findElement(By.xpath("//*[@id=\"main\"]/table/tbody/tr[1]/td[1]")).getText();
			
		} catch (Exception e) {
			
			foundComputerName = ""; // ignore 
		}
	
		 return computerName.equals(foundComputerName); 
	
	}  
 
	
	@Test 
	public void testComputerAddedWithMessage() throws InterruptedException {
		
		String computerName = "McBookXena"; 
		String expectedAlert = "Done ! Computer " + computerName + " has been created"; 
		addNewComputer(computerName); 
		String actualAlert = readAlertMessage(); 
		assertEquals("The computer has not been added!", expectedAlert, actualAlert); 
		
	}


	@Test 
	public void testIfNewComputerInDatabase() throws InterruptedException {
		
		String computerName = "McBookXena2";
		addNewComputer(computerName); 
		
		assertTrue(findByNameInDatabase(computerName)); 
		
	}
	
	
}