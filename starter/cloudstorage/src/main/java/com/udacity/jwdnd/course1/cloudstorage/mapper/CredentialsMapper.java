package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("Select * from Credentials Where userid = #{userId}")
    List<Credential> getCredentials(Integer userId);

    @Select("Select * from Credentials Where credentialid = #{credentialId}")
    Credential getSingleCredential(Integer credentialId);

    @Select("Select * From Credentials Where credentialid = #{credentialId} And userid = #{userId}")
    Credential getUserCredential(Integer credentialId, Integer userId);

    @Insert("Insert Into Credentials (url, username, password, credkey, userid) Values (#{url}, #{username}, #{password}, #{credkey}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(Credential credential);

    @Update("Update Credentials Set url = #{url}, username = #{username}, password = #{password} Where credentialid = #{credentialId}")
    int updateCredential(Credential credential);

    @Delete("Delete From Credentials Where credentialid = #{credentialId}")
    int deleteCredential(Integer credentialId);


}
