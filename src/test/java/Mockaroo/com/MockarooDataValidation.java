package Mockaroo.com;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import static org.testng.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {
	WebDriver d;
	String pathToFile;
	List<String> resultFromFile;
	List<String> resultCities;
	List<String> resultCountries;

	public List fileReader(String pathToFile) {
		List<String> tempRead = new ArrayList<>();
		try {
			BufferedReader read = new BufferedReader(new FileReader(pathToFile));
			String line = read.readLine();
			while (line != null) {
				tempRead.add(line);
				line = read.readLine();
			}
			read.close();
		} catch (Exception e) {
			System.out.println(e.getMessage() + "Not correct path to file!!! method fileReader");
		}
		return tempRead;
	}

	public List addCities() {
		List<String> cities = new ArrayList<>();
		for (String str : resultFromFile) {
			cities.add(str.substring(0, str.indexOf(",")));
		}
		return cities;
	}

	public List addCountries() {
		List<String> countries = new ArrayList<>();
		for (String str : resultFromFile) {
			countries.add(str.substring(str.indexOf(",") + 1));
		}
		return countries;
	}

	@BeforeClass
	public void setup() {
		WebDriverManager.chromedriver().setup();
		d = new ChromeDriver();
		d.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		d.manage().window().fullscreen();
		String url = "https://mockaroo.com/";
		d.get(url);
	}

	@BeforeMethod
	public void beforeMethod() {
		pathToFile = "/Users/jurec/Downloads/MOCK_DATA.csv";
		resultFromFile = fileReader(pathToFile);
		resultCities = addCities();// test 20
		resultCountries = addCountries();// test 21
	}

	@Test(priority = 0)
	public void titleTest() {
		// ===Test 3===============================Checking Title======================

		String actualTitle = d.getTitle();
		String expectedTitle = "Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel";
		assertEquals(actualTitle, expectedTitle);
	}

	@Test(priority = 1)
	public void isDisplayedMockaroo() {
		// ====Test 4==============================is Displayed Mockaroo================
		// part 1
		WebElement mockaroo = d.findElement(By.xpath("//div[@class='brand']"));
		assertTrue(mockaroo.isDisplayed());

		String actualMockarooText = mockaroo.getText();
		String expectedMockarooText = "mockaroo";
		assertEquals(actualMockarooText, expectedMockarooText);
		// part 2
		WebElement secondPart = d.findElement(By.xpath("//div[@class='tagline']"));
		assertTrue(secondPart.isDisplayed());

		String actualSecondPartText = secondPart.getText();
		String expectedSecondPartText = "realistic data generator";
		assertEquals(actualSecondPartText, expectedSecondPartText);
	}

	@Test(priority = 2)
	public void clickOnX() throws InterruptedException {
		// ====Test 5==============================Click on X===========================

		List<WebElement> a = d.findElements(By.xpath("//a[@class='close remove-field remove_nested_fields']"));

		for (int i = 0; i < a.size(); i++) {

			a.get(i).click();

		}

	}

	@Test(priority = 3)
	public void fieldDisplayed() {
		// ====Test 6==============================Field displayed======================
		WebElement fieldName = d.findElement(By.xpath("//div[@class='column column-header column-name']"));
		WebElement type = d.findElement(By.xpath("//div[@class='column column-header column-type']"));
		WebElement options = d.findElement(By.xpath("//div[@class='column column-header column-options']"));

		assertTrue(fieldName.isDisplayed());
		assertTrue(type.isDisplayed());
		assertTrue(options.isDisplayed());
	}

	@Test(priority = 4)
	public void addAnotherFieldIsTrue() {
		// ====Test 7==============================Add another Field Is True============
		WebElement text = d.findElement(By.xpath("//a[contains(text(),'Add another field')]"));
		assertTrue(text.isEnabled());

	}

	@Test(priority = 5)
	public void checkRow() {
		// ====Test 8==============================Check row===========================
		WebElement checkRow = d.findElement(By.xpath("//input[@value='1000']"));
		assertTrue(checkRow.isDisplayed());
	}

	@Test(priority = 6)
	public void checkCSV() {
		// ====Test 9==============================Check CSV===========================
		Select s = new Select(d.findElement(By.xpath("//select[@id='schema_file_format']")));

		String actualCsv = s.getFirstSelectedOption().getText();
		String expectedCsv = "CSV";
		assertEquals(actualCsv, expectedCsv);
	}

	@Test(priority = 7)
	public void UnixIsTrue() {
		// ====Test 10==============================Unix Is True=======================
		Select u = new Select(d.findElement(By.xpath("//select[@id='schema_line_ending']")));
		String actualUnix = u.getFirstSelectedOption().getText();
		String expectedUnix = "Unix (LF)";
		assertEquals(actualUnix, expectedUnix);

	}

	@Test(priority = 8)
	public void checkAndUncheck() {
		// ====Test 11==============================check Header ,unchecked Bom =======
		// header
		WebElement header = d.findElement(By.xpath("//input[@id='schema_include_header']"));
		assertTrue(header.isSelected());
		// bom
		WebElement bom = d.findElement(By.xpath("//input[@id='schema_bom']"));
		assertFalse(bom.isSelected());

	}

	@Test(priority = 9)
	public void clickAddAnotherField() throws InterruptedException {
		// ====Test 12================click AddAnotherField and enter name city=======
		// click on button
		d.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		// enter text "City"
		String text = "City";
		List<WebElement> a = d.findElements(By.xpath("//input[@placeholder='enter name...']"));
		for (WebElement w : a) {
			if (w.isDisplayed()) {
				w.sendKeys(text);
			}

		}
	}

	@Test(priority = 10)
	public void dialogBoxIsDisplayed() throws InterruptedException {
		// ====Test 13====================Choose a Type dialog box is displayed=======
		// click on choose type
		List<WebElement> element = d.findElements(By.xpath("//input[@class='btn btn-default']"));
		for (WebElement w : element) {
			if (w.isDisplayed()) {
				w.click();
				;
			}

		}
		// check is choose type is displayed after click
		Thread.sleep(1000);
		WebElement chooseType = d.findElement(By.xpath("//h3[@class='modal-title'][contains(text(),'Choose a Type')]"));
		assertTrue(chooseType.isDisplayed());
	}

	@Test(priority = 11)
	public void searchCity() throws InterruptedException {
		// ====Test 14=========================Search for“city”======================
		// search city and click on result city
		Thread.sleep(1000);
		d.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("city" + Keys.TAB + Keys.ENTER);
	}

	@Test(priority = 12)
	public void searchCountry() throws InterruptedException {
		// ====Test 15=========================repeat 12-14 “country”=================
		// click on button
		Thread.sleep(1000);
		d.findElement(By.xpath("//a[@class='btn btn-default add-column-btn add_nested_fields']")).click();
		// enter text "Country"
		Thread.sleep(1000);
		String text = "Country";
		List<WebElement> a = d.findElements(By.xpath("//input[@placeholder='enter name...']"));
		for (int i = a.size() - 1; i >= 0; i--) {
			if (a.get(i).isDisplayed()) {
				a.get(i).clear();
				a.get(i).sendKeys(text);
				break;
			}
		}
		// click on choose type
		Thread.sleep(1000);
		List<WebElement> element = d.findElements(By.xpath("//input[@class='btn btn-default']"));
		for (int i = element.size() - 1; i >= 0; i--) {
			if (element.get(i).isDisplayed()) {
				element.get(i).click();
				break;
			}
		}
		// check is choose type is displayed after click
		Thread.sleep(1000);
		WebElement chooseType = d.findElement(By.xpath("//h3[@class='modal-title'][contains(text(),'Choose a Type')]"));
		assertTrue(chooseType.isDisplayed());

		// search country and click on result country
		Thread.sleep(1000);
		d.findElement(By.xpath("//input[@id='type_search_field']")).sendKeys("country" + Keys.TAB + Keys.ENTER);
	}

	@Test(priority = 13)
	public void clickDownloadData() throws InterruptedException {
		// ====Test 16=========================click on Download Data=================
		Thread.sleep(1000);
		d.findElement(By.xpath("//button[@id='download']")).click();
	}

	@Test(priority = 14)
	public void openDownLoadedFile() {
		// ====Test 17=========================open downloaded file=================
		// used method fileReader
	}

	@Test(priority = 15)
	public void checkInsideFile() {
		// ====Test 18=========================check fieldrow city,country===========

		String expectedResult = "City,Country";

		assertEquals(resultFromFile.get(0), expectedResult);
	}

	@Test(priority = 16)
	public void check1000Records() {
		// ====Test 19=========================check 1000 records====================
		int count = 0;
		for (int i = 1; i < resultFromFile.size(); i++) {
			count++;
		}
		assertEquals(count, 1000);
	}

	@Test(priority = 17)
	public void addAllCitiesCountries() {
		// ====Test 20=Test 21========================add all cities countries======
		// created two method addCities and addCountries
	}

	@Test(priority = 18)
	public void sortCities() {
		// ====Test 22========================sort all cities========================
		// Sort all cities and find the city with the longest name and shortest name
		SortedSet<String> sSet1 = new TreeSet<>();
		sSet1.addAll(resultCities);
		String longestNameCity = "";
		String shorterNameCity = "";

		longestNameCity = sSet1.first();
		shorterNameCity = sSet1.first();
		for (String str : sSet1) {
			if (str.length() > longestNameCity.length()) {
				longestNameCity = str;
			}
			if (str.length() < shorterNameCity.length()) {
				shorterNameCity = str;
			}
		}
		System.out.println("longest name city - " + longestNameCity);
		System.out.println("shorter name city - " + shorterNameCity);
	}

	@Test(priority = 19)
	public void sortCountries() {
		// ====Test 23===================the same countries========================
		// In Countries ArrayList, find how many times each Country is mentioned. and
		// print out
		int count=0;
		SortedSet<String> sSet=new TreeSet<>();
		sSet.addAll(resultCountries);
		for (String str : sSet) {
			System.out.println(str+" - "+Collections.frequency(resultCountries,str));
		}
		}
		
	@Test(priority = 20)
	public void uniqueCities() {
		// ====Test 24 and 25===================cities count unique========================
		//From file add all Cities to citiesSet HashSet
		Set<String> sSets=new HashSet<>(resultCities);
		//Count how many unique cities are in Cities ArrayList and assert that
		//it is matching with the count of citiesSet HashSet.
		
		int arrUniqueCount = 0;
		for (int i = 0; i < resultCities.size(); i++) {
			if (i == resultCities.lastIndexOf(resultCities.get(i)))
				arrUniqueCount++;
		}
		assertEquals(arrUniqueCount, sSets.size());
		
//		SortedSet<String> sSet=new TreeSet<>(resultCities);
//		
//		assertEquals(sSets, sSet);
	}
	@Test(priority = 21)
	public void uniqueCountries() {
		// ====Test 24 and 25===================countries count unique========================
		//From file add all Cities to citiesSet HashSet
		Set<String> sSets=new HashSet<>(resultCountries);
		//Count how many unique cities are in Cities ArrayList and assert that
		//it is matching with the count of citiesSet HashSet.
		
		int arrUniqueCountries = 0;
		for (int i = 0; i < resultCountries.size(); i++) {
			if (i == resultCountries.lastIndexOf(resultCountries.get(i)))
				arrUniqueCountries++;
		}
		assertEquals(arrUniqueCountries, sSets.size());
		// SortedSet<String> sSet=new TreeSet<>(resultCountries);
		//
		// assertEquals(sSets, sSet);
	}

	@AfterClass
	public void finish() throws InterruptedException {
		Thread.sleep(1000);
		d.quit();
	}

}
