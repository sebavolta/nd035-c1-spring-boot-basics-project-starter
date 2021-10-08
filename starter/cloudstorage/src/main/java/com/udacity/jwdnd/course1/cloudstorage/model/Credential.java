package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {
    private Integer credentialId;
    private String url;
    private String username;
    private String password;
    private Integer userId;
    private String credkey;
    private String decryptedPassword;


    public Credential(Integer credentialId, String url, String username, String password, String credkey, Integer userId) {
        this.credentialId = credentialId;
        this.url = url;
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.credkey = credkey;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "credentialId=" + credentialId +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                ", credkey='" + credkey + '\'' +
                ", decryptedPassword='" + decryptedPassword + '\'' +
                '}';
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCredkey() {
        return credkey;
    }

    public void setCredkey(String credkey) {
        this.credkey = credkey;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }
}
