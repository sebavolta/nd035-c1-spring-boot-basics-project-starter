package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FilesMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getSingleFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userId}")
    File getUserFile(String filename, Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata)" +
            "VALUES(#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    @Update("UPDATE FILES set filename = #{filename}, contenttype = #{contentType}, filesize = #{fileSize}, " +
            "userId = #{userId}, filedata = #{fileData} where fileId = #{fileId}")
    Integer updateFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(File file);
}
