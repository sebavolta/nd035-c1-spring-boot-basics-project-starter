package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Select("Select * From Notes Where userid = #{userId}")
    List<Note> getNotes(Integer userId);

    @Select("Select * From Notes Where noteid = #{noteId}")
    Note getSingleNote(Integer noteId);

    @Insert("Insert Into Notes (noteTitle, noteDescription, userid) values (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("Update Notes Set noteTitle = #{noteTitle}, noteDescription = #{noteDescription} Where noteId = #{noteId}")
    int updateNote(Note note);

    @Delete("Delete From Notes Where noteId = #{noteId}")
    int deleteNote(Note note);
}
