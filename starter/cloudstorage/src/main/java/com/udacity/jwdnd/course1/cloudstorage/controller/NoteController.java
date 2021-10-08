package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class NoteController {

    NoteService noteService;
    UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/add-note")
    public String addtNote(@ModelAttribute Note note, Authentication authentication, Model model) {
        User user = this.userService.getUser(authentication.getName());
        note.setUserId(user.getUserid());

        if(note.getNoteId() != null) {
            try {
                this.noteService.updateNote(note);
                model.addAttribute("success", true);
            }   catch (Exception e) {
                model.addAttribute("error", "Error updating your note! " + e.getMessage());
            }
        } else {
            try {
                this.noteService.addNote(note);
                model.addAttribute("success", true);
            } catch(Exception e) {
                model.addAttribute("error", "Error adding your note! " + e.getMessage());
            }
        }

        return "/result";
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, Authentication authentication, Model model) {
        Note note = this.noteService.getSingleNote(noteId);

        try {
          this.noteService.deleteNote(note);
          model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting your note! " + e.getMessage());
        }
        return "/result";
    }

}
