package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    NotesMapper notesMapper;

    public NoteService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public List<Note> getNotes(Integer userId) {
       return this.notesMapper.getNotes(userId);
    }

    public void addNote(Note note) {
        this.notesMapper.insertNote(note);
    }

    public Note getSingleNote(Integer noteId) {
        return this.notesMapper.getSingleNote(noteId);
    }

    public void updateNote(Note note) {
        this.notesMapper.updateNote(note);
    }

    public void deleteNote(Note note) {
        this.notesMapper.deleteNote(note);
    }

    public void deleteAllNotes() {
        this.notesMapper.deleteAllNotes();
    }

}
