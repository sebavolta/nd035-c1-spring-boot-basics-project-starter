package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {
    FileService fileService;
    UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication authentication, Model model) throws IOException {
        User user = this.userService.getUser(authentication.getName());
        String filename = multipartFile.getOriginalFilename();

        boolean fileNameAvailable = this.fileService.isFIleNameAvailable(filename, user.getUserid());

        if (filename.isEmpty()) {
            model.addAttribute("error", "Error, the filename is empty");

            return "/result";
        } else if(fileNameAvailable) {
            try {
                this.fileService.uploadFile(multipartFile, user.getUserid());
                model.addAttribute("success", true);

                return "/result";
            } catch (IOException e) {
                model.addAttribute("error", "Error uploading the file " + e);
            }
        }

        model.addAttribute("error", "Error, the filename already exists");

        return "/result";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity downloadFile(@PathVariable Integer fileId) {
        File file = fileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .body(file.getFileData());
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Model model) {
        File file = fileService.getFile(fileId);

        try {
            this.fileService.deleleFile(file);
            model.addAttribute("success", true);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "Error deleting your file!" + e.getMessage());
        }

        return "/result";
    }
}
