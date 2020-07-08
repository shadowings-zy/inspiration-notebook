package com.shadowingszy.notebook.controller;

import com.shadowingszy.notebook.service.BranchService;
import com.shadowingszy.tools.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"*", "null"})
@RequestMapping("/branch")
public class BranchController {
    private BranchService branchService;

    @Autowired
    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String createBranch(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("noteName") String noteName,
            @RequestParam("branchName") String branchName,
            @RequestParam("content") String content,
            @RequestParam("operator") String operator
    ) {
        ResponseMessage response = branchService.createBranch(userId, notebookName, noteName, branchName, content, operator);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "updateContent", method = RequestMethod.POST)
    public String updateBranchContent(
            @RequestParam("userId") int userId,
            @RequestParam("branchId") int branchId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("noteName") String noteName,
            @RequestParam("content") String content,
            @RequestParam("operator") String operator
    ) {
        ResponseMessage response = branchService.updateBranchContent(userId, branchId, notebookName, noteName, content, operator);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "getByNoteName", method = RequestMethod.POST)
    public String getByBranchId(
            @RequestParam("userId") int userId,
            @RequestParam("notebookName") String notebookName,
            @RequestParam("noteName") String noteName
    ) {
        ResponseMessage response = branchService.readBranchByNoteName(userId, notebookName, noteName);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "getByBranchId", method = RequestMethod.POST)
    public String getByBranchId(
            @RequestParam("userId") int userId,
            @RequestParam("branchId") int branchId
    ) {
        ResponseMessage response = branchService.readBranchByBranchId(userId, branchId);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "getByKeywords", method = RequestMethod.POST)
    public String getByBranchId(
            @RequestParam("userId") int userId,
            @RequestParam("keywords") String keywords
    ) {
        ResponseMessage response = branchService.readBranchByKeywords(userId, keywords);
        return ResponseMessage.toJSONString(response);
    }
}
