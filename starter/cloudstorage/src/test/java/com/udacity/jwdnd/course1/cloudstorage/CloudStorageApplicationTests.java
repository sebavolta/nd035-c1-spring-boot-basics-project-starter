package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
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

	String credentialUrl = "www.google.com";
	String credentialUsername = "john";
	String credentialPassword = "test1234";

	@Autowired
	CredentialService credentialService;

	@Autowired
	EncryptionService encryptionService;

	@Autowired
	NoteService noteService;

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

	private void createCredentialsHelper() {
		this.createAccountAndLoginHelper();

		HomePage homePage = new HomePage(driver);
		homePage.createCredential(this.credentialUrl, this.credentialUsername, this.credentialPassword);

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		homePage.createCredential(this.credentialUrl + "/abc", this.credentialUsername + "abc", this.credentialPassword + "abc");

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");
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

		this.noteService.deleteAllNotes();

		HomePage homePage = new HomePage(driver);
		homePage.createNote(this.noteTitle, this.noteDescription);

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertEquals(this.noteTitle + " - " + this.noteDescription, homePage.getNoteText());
	}

	@Test
	public void editNote() {
		this.createAccountAndLoginHelper();

		this.noteService.deleteAllNotes();

		driver.get("http://localhost:" + this.port + "/home");

		HomePage homePage = new HomePage(driver);
		homePage.createNote(this.noteTitle, this.noteDescription);

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		homePage.editNote("edited", "edited");

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertEquals(this.noteTitle + " edited - " + this.noteDescription + " edited", homePage.getNoteText());
	}

	@Test
	public void deleteNote() {
		this.createAccountAndLoginHelper();

		this.noteService.deleteAllNotes();

		HomePage homePage = new HomePage(driver);
		homePage.createNote(this.noteTitle, this.noteDescription);

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		System.out.println("amount " +  driver.findElements(By.className("delete-note-btn")));

		homePage.deleteNote();

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertThrows(NoSuchElementException.class, () -> homePage.getNoteText());
	}



	@Test
	public void createCredentialAndDisplay() {
		this.createCredentialsHelper();

		driver.get("http://localhost:" + this.port + "/home");

		HomePage homePage = new HomePage(driver);

		Credential cred1 = this.credentialService.getSingleCredential(1);
		Credential cred2 = this.credentialService.getSingleCredential(2);

		String encryptedPassword = this.encryptionService.encryptValue(this.credentialPassword, cred1.getCredkey());
		String encryptedPassword2 = this.encryptionService.encryptValue(this.credentialPassword + "abc", cred2.getCredkey());


		Assertions.assertEquals(this.credentialUrl, homePage.getCredentialsUrl().get(0).getText());
		Assertions.assertEquals(this.credentialUrl + "/abc", homePage.getCredentialsUrl().get(1).getText());

		Assertions.assertEquals(this.credentialUsername, homePage.getCredentialsUsername().get(0).getText());
		Assertions.assertEquals(this.credentialUsername + "abc", homePage.getCredentialsUsername().get(1).getText());

		Assertions.assertEquals(encryptedPassword, homePage.getCredentialsPassword().get(0).getText());
		Assertions.assertEquals(encryptedPassword2, homePage.getCredentialsPassword().get(1).getText());
	}

	@Test
	public void editCredentialAndDisplay() {
		this.createCredentialsHelper();

		driver.get("http://localhost:" + this.port + "/home");

		HomePage homePage = new HomePage(driver);

		Assertions.assertEquals(this.credentialPassword, homePage.getCredentialModalPassword(0));
		homePage.closeCredModal();

		try {
			this.wait(1000);
		} catch (Exception e) {
			System.out.println("wait error " + e);
		}

		Assertions.assertEquals(this.credentialPassword + "abc", homePage.getCredentialModalPassword(1));
		homePage.closeCredModal();


		homePage.editCredential("www.yahoo.com", "somename", "newpass", 0);

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");


		homePage.editCredential("www.abc.com", "othername", "otherpass", 1);

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Credential cred1 = this.credentialService.getSingleCredential(1);
		Credential cred2 = this.credentialService.getSingleCredential(2);


		String encryptedPassword = this.encryptionService.encryptValue(this.credentialPassword+"newpass", cred1.getCredkey());
		String encryptedPassword2 = this.encryptionService.encryptValue(this.credentialPassword + "abc" + "otherpass", cred2.getCredkey());

		Assertions.assertEquals("www.google.com www.yahoo.com", homePage.getCredentialsUrl().get(0).getText());
		Assertions.assertEquals("www.google.com/abc www.abc.com", homePage.getCredentialsUrl().get(1).getText());

		Assertions.assertEquals("john somename", homePage.getCredentialsUsername().get(0).getText());
		Assertions.assertEquals("johnabc othername", homePage.getCredentialsUsername().get(1).getText());

		Assertions.assertEquals(encryptedPassword, homePage.getCredentialsPassword().get(0).getText());
		Assertions.assertEquals(encryptedPassword2, homePage.getCredentialsPassword().get(1).getText());
	}

	@Test
	public void deleteCredential() {
		this.createAccountAndLoginHelper();

		this.credentialService.deleteAllCredentials();

		HomePage homePage = new HomePage(driver);
		homePage.createCredential(this.credentialUrl, this.credentialUsername, this.credentialPassword);

		driver.get("http://localhost:" + this.port + "/home");

		homePage.deleteCredential();

		this.resultPageHelper();

		driver.get("http://localhost:" + this.port + "/home");

		Assertions.assertTrue(homePage.getCredentialsUrl().isEmpty());
	}

}
