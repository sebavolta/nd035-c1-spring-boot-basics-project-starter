package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {
    CredentialService credentialService;
    UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/add-credential")
    public String addCredential(@ModelAttribute Credential credential, Authentication authentication, Model model) {
        User user = this.userService.getUser(authentication.getName());
        credential.setUserId(user.getUserid());

        if(credential.getCredentialId() != null) {
            try {
                this.credentialService.updateCredential(credential);
                model.addAttribute("success", true);
            } catch (Exception e) {
                model.addAttribute("error", "Error updating the credential " + e);
            }
        } else {
            try {
                this.credentialService.addCredendial(credential);
                model.addAttribute("success", true);
            } catch (Exception e) {
                model.addAttribute("error", "Error adding the credential " + e);
            }
        }

        return "/result";
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, Model model) {

        try {
            this.credentialService.deleteCredential(credentialId);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting the credential " + e);
        }

        return "/result";
    }

}
