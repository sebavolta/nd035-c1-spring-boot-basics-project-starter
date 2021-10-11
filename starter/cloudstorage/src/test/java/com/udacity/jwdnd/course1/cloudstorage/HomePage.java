package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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



}
