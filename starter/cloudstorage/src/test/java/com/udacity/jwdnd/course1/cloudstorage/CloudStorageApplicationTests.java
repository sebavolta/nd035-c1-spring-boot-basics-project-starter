package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	WebDriverWait wait;
	String noteTitle = "My title";
	String noteDescription = "Some description";

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		this.wait = new WebDriverWait(driver,2);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	private void createAccountAndLoginHelper() {
		driver.get("http://localhost:" + this.port + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("John", "Doe", "john", "testpass");

		driver.get("http://localhost:" + this.port + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("john", "testpass");

		driver.get("http://localhost:" + this.port + "/home");

		this.wait.until(driver -> driver.findElement(By.tagName("title")));
	}

	private void resultPageHelper() {
		this.wait.until(driver -> driver.findElement(By.tagName("title")));

		Assertions.assertEquals("Result", driver.getTitle());

		ResultPage resultPage = new ResultPage(driver);

		Assertions.assertTrue(resultPage.getSuccessMessage());
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void unauthorizedLogin() {
		driver.get("http://localhost:" + this.port + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login("someuser", "somepassword");

		this.wait.until(driver -> driver.findElement(By.id("error-msg")));

		WebElement error = driver.findElement(By.id("error-msg"));

		Assertions.assertNotNull(error);
	}

	@Test
	public void authorizedLogin() {
		this.createAccountAndLoginHelper();

		Assertions.assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		homePage.logout();

		this.wait.until(driver -> driver.findElement(By.tagName("title")));

		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");

		this.wait.until(driver -> driver.findElement(By.tagName("title")));

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void createNoteAndDisplay() {
		this.createAccountAndLoginHelper();

		HomePage homePage = new HomePage(driver);
		homePage.createNote(this.noteTitle, this.noteDescription);

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertEquals(this.noteTitle + " - " + this.noteDescription, homePage.getNoteText());
	}

	@Test
	public void editNote() {
		this.createAccountAndLoginHelper();
		this.createNoteAndDisplay();

		HomePage homePage = new HomePage(driver);
		homePage.editNote("edited", "edited");

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertEquals(this.noteTitle + " edited - " + this.noteDescription + " edited", homePage.getNoteText());
	}

	@Test
	public void deleteNote() {
		this.createAccountAndLoginHelper();
		this.createNoteAndDisplay();

		HomePage homePage = new HomePage(driver);
		homePage.deleteNote();

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertThrows(NoSuchElementException.class, () -> homePage.getNoteText());
	}

}
