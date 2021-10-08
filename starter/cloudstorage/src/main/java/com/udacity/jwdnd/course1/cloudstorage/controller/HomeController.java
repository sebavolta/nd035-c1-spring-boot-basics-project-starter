package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    FileService fileService;
    UserService userService;
    NoteService noteService;
    CredentialService credentialService;

    public HomeController(FileService fileService, UserService userService, NoteService noteService, CredentialService credentialService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String homePage(Authentication authentication, Model model) {
        User user = this.userService.getUser(authentication.getName());
        Integer userId = user.getUserid();

        /*Get Files*/
        List<File> files = this.fileService.getFiles(userId);

        if (!files.isEmpty()) {
            model.addAttribute("files", files.toArray());
        }

        /*Get Notes*/
        List<Note> notes = this.noteService.getNotes(userId);

        if(!notes.isEmpty()) {
            model.addAttribute("notes", notes);
        }

        /*Get Credentials*/
        List<Credential> credentials = this.credentialService.getCredentials(userId);

        if(!credentials.isEmpty()) {
            model.addAttribute("credentials", credentials);
        }

        return "home";
    }

}
