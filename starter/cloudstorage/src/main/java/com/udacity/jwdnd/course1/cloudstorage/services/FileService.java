package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    FilesMapper filesMapper;

    public FileService(FilesMapper filesMapper) {
        this.filesMapper = filesMapper;
    }

    public List<File> getFiles(Integer userId) {
        return this.filesMapper.getFiles(userId);
    }

    public File getFile(Integer fileId) {
        return this.filesMapper.getSingleFile(fileId);
    }

    public boolean isFIleNameAvailable(String filename, Integer userId) {
        File file = filesMapper.getUserFile(filename, userId);

        return file == null || file.getFilename() == null;
    }

    public void uploadFile( MultipartFile file, Integer userId) throws IOException {
        File myFile = new File();
        myFile.setFilename(file.getOriginalFilename());
        myFile.setFileData(file.getBytes());
        myFile.setFileSize(file.getSize());
        myFile.setContentType(file.getContentType());
        myFile.setUserId(userId);

        this.filesMapper.insertFile(myFile);
    }

    public void deleleFile(File file) {
        this.filesMapper.deleteFile(file);
    }
}
