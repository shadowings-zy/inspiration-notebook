package com.shadowingszy.notebook.controller;

import com.shadowingszy.notebook.service.NotebookService;
import com.shadowingszy.tools.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"*", "null"})
@RequestMapping("/notebook")
public class NotebookController {
    private NotebookService notebookService;

    @Autowired
    public NotebookController(NotebookService notebookService) {
        this.notebookService = notebookService;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createNotebook(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("operator") String operator
    ) {
        ResponseMessage response = notebookService.newNotebook(userId, notebookName, operator);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String updateNotebook(
            @RequestParam("userId") int userId,
            @RequestParam("oldNotebookName") String oldNotebookName,
            @RequestParam("newNotebookName") String newNotebookName,
            @RequestParam("operator") String operator
    ) {
        ResponseMessage response = notebookService.updateNotebookName(userId, oldNotebookName, newNotebookName, operator);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String deleteNotebook(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName
    ) {
        ResponseMessage response = notebookService.deleteNotebook(userId, notebookName);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "getAllByUserId", method = RequestMethod.POST)
    public String getAllNoteByUserId(
            @RequestParam("userId") int userId
    ) {
        ResponseMessage response = notebookService.readAllNoteByUserId(userId);
        return ResponseMessage.toJSONString(response);
    }
}
