package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    CredentialsMapper credentialsMapper;
    EncryptionService encryptionService;

    public CredentialService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(Integer userId) {
        List<Credential> credentials = this.credentialsMapper.getCredentials(userId).stream()
                .map(credential -> {
                    System.out.println("---------------------------- credent: " + credential);
                    String decrypted = this.encryptionService.decryptValue(credential.getPassword(), credential.getCredkey());
                    credential.setDecryptedPassword(decrypted);
                    return credential;
                })
                .collect(Collectors.toList());

        return credentials;
    }

    public void addCredendial(Credential credential) {
        String encodedKey = this.encryptionService.makeSecureKey();

        Credential encodedCredential = this.encodeCredential(credential, encodedKey);

        this.credentialsMapper.insertCredential(encodedCredential);
    }

    public Credential getSingleCredential(Integer credentialId) {
        return this.credentialsMapper.getSingleCredential(credentialId);
    }

    public void updateCredential(Credential credential) {
        Credential cred = this.credentialsMapper.getSingleCredential(credential.getCredentialId());

        Credential encodedCredential = this.encodeCredential(credential, cred.getCredkey());

        this.credentialsMapper.updateCredential(encodedCredential);
    }

    public void deleteCredential(Integer credentialId) {
        this.credentialsMapper.deleteCredential(credentialId);
    }

    private Credential encodeCredential(Credential credential, String encodedKey) {
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setCredkey(encodedKey);
        credential.setPassword(encryptedPassword);

        return credential;
    }

}
