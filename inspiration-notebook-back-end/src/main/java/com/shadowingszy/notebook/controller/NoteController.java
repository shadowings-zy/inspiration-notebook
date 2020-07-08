package com.shadowingszy.notebook.controller;

import com.shadowingszy.notebook.service.NoteService;
import com.shadowingszy.tools.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"*", "null"})
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createNote(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("noteName") String noteName,
            @RequestParam("operator") String operator
    ) {
        ResponseMessage response = noteService.createNote(userId, notebookName, noteName, operator);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String updateNoteName(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("oldNoteName") String oldNoteName,
            @RequestParam("newNoteName") String newNoteName,
            @RequestParam("operator") String operator
    ) {
        ResponseMessage response = noteService.updateNoteName(userId, notebookName, oldNoteName, newNoteName, operator);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String deleteNote(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("noteName") String noteName
    ) {
        ResponseMessage response = noteService.deleteNote(userId, notebookName, noteName);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "getByNotebookName", method = RequestMethod.POST)
    public String getNoteByUserIdAndNotebookId(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName
    ) {
        ResponseMessage response = noteService.readNoteByNotebookName(userId, notebookName);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "getDocument", method = RequestMethod.POST)
    public String getNoteByUserIdAndKeywords(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("order") String order
    ) {
        ResponseMessage response = noteService.generateNoteDocument(userId, notebookName, order);
        return ResponseMessage.toJSONString(response);
    }
}
