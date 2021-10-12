package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {
    WebDriver driver;
    @FindBy(id="logoutBtn")
    private WebElement logoutBtn;

    @FindBy(id ="nav-notes-tab")
    private WebElement navNoteTab;

    @FindBy(id = "openNoteModal")
    private WebElement openNoteModal;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "saveNoteBtn")
    private WebElement saveNoteBtn;

    @FindBy(id ="nav-credential-tab")
    private WebElement navCredentialsTab;

    @FindBy(id = "openCredentialModal")
    private WebElement openCredentialModal;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "credentialSubmitBtn")
    private WebElement credentialSubmitBtn;

    public HomePage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.logoutBtn.click();
    }

    public void createNote(String noteTitle, String noteDescription) {
        this.navNoteTab.click();
        this.openNoteModal.click();
        this.noteTitle.sendKeys(noteTitle);
        this.noteDescription.sendKeys(noteDescription);
        this.saveNoteBtn.click();
    }

    public String getNoteText() {
        this.navNoteTab.click();
        WebElement titleNote = this.driver.findElement(By.className("title-note"));
        WebElement descriptionNote = this.driver.findElement(By.className("description-note"));
        return titleNote.getText() + " - " + descriptionNote.getText();
    }

    public void editNote(String editedTitle, String editedDescription) {
        this.navNoteTab.click();
        WebElement editNoteBtn = this.driver.findElement(By.className("edit-note-btn"));
        editNoteBtn.click();
        this.noteTitle.sendKeys(this.noteTitle.getText() + " " + editedTitle);
        this.noteDescription.sendKeys(this.noteDescription.getText() + " " + editedDescription);
        this.saveNoteBtn.click();
    }

    public void deleteNote() {
        this.navNoteTab.click();
        WebElement deleteNoteBtn = this.driver.findElement(By.className("delete-note-btn"));
        deleteNoteBtn.click();
    }

    public void createCredential(String url, String username, String password) {
        this.navCredentialsTab.click();
        this.openCredentialModal.click();
        this.credentialUrl.sendKeys(url);
        this.credentialUsername.sendKeys(username);
        this.credentialPassword.sendKeys(password);
        this.credentialSubmitBtn.click();
    }

    public List<WebElement> getCredentialsUrl() {
        this.navCredentialsTab.click();
        return this.driver.findElements(By.className("url-credential"));
    }

    public List<WebElement> getCredentialsUsername() {
        this.navCredentialsTab.click();
        return this.driver.findElements(By.className("username-credential"));

    }

    public List<WebElement> getCredentialsPassword() {
        this.navCredentialsTab.click();
        return this.driver.findElements(By.className("password-credential"));
    }

    public String getCredentialModalPassword(int index) {
        this.navCredentialsTab.click();

        List<WebElement> editCredentialsBtn = this.driver.findElements(By.className("edit-credential-btn"));
        editCredentialsBtn.get(index).click();

        return this.driver.findElement(By.id("credential-password")).getAttribute("value");
    }

    public void closeCredModal() {
        this.driver.findElement(By.id("closeCredentialModal")).click();
    }

    public void editCredential(String editedUrl, String editedUsername, String editedPassword, int index) {
        this.navCredentialsTab.click();

        List<WebElement> editCredentialsBtn = this.driver.findElements(By.className("edit-credential-btn"));
        editCredentialsBtn.get(index).click();

        this.credentialUrl.sendKeys(this.credentialUrl.getText() + " " + editedUrl);
        this.credentialUsername.sendKeys(this.credentialUsername.getText() + " " + editedUsername);
        this.credentialPassword.sendKeys(editedPassword);
        this.credentialSubmitBtn.click();
    }

    public void deleteCredential() {
        this.navCredentialsTab.click();

        WebElement deleteCredentialsBtn = this.driver.findElement(By.className("delete-credential-btn"));
        deleteCredentialsBtn.click();
    }

}
