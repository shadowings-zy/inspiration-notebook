package com.shadowingszy.notebook.service;

import com.shadowingszy.notebook.entity.Branch;
import com.shadowingszy.notebook.mapper.BranchMapper;
import com.shadowingszy.tools.ResponseMessage;
import com.shadowingszy.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BranchService {
    private BranchMapper branchMapper;

    @Autowired
    public BranchService(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    public ResponseMessage createBranch(int userId, String notebookName, String noteName, String branchName, String content, String operator) {
        String status = Tools.generateOperationInformation(operator, "新建笔记分支");
        int branchCount = branchMapper.selectBranchCount(userId, notebookName, noteName, branchName);
        int noteCount = branchMapper.selectNoteCount(userId, notebookName, noteName);
        int notebookCount = branchMapper.selectNotebookCount(userId, notebookName);
        if (branchCount == 0 && noteCount > 0 && notebookCount > 0) {
            Branch branch = new Branch(0, userId, notebookName, noteName, branchName, content, status);
            int insertStatus = branchMapper.insertBranch(branch);
            if (insertStatus > 0) {
                ArrayList<Branch> branchArrayList = branchMapper.selectBranchByNoteName(userId, notebookName, noteName);
                return new ResponseMessage("200", "新建分支成功", branchArrayList);
            } else {
                return new ResponseMessage("1030", "新建分支失败", "新建分支失败");
            }
        } else if (branchCount > 0 && noteCount > 0 && notebookCount > 0) {
            return new ResponseMessage("1031", "分支名重复", "分支名重复");
        } else {
            return new ResponseMessage("1037", "新建分支失败", "新建分支失败");
        }
    }

    public ResponseMessage updateBranchContent(int userId, int branchId, String notebookName, String noteName, String content, String operator) {
        ArrayList<Branch> oldBranch = branchMapper.selectBranchByBranchId(userId, branchId);
        if (oldBranch.size() == 1 && oldBranch.get(0).getNotebookName().equals(notebookName) && oldBranch.get(0).getNoteName().equals(noteName)) {
            String status = Tools.generateOperationInformation(operator, "修改笔记分支");
            int updateStatus = branchMapper.updateBranchContent(userId, branchId, content, status);
            if (updateStatus > 0) {
                ArrayList<Branch> branchArrayList = branchMapper.selectBranchByNoteName(userId, notebookName, noteName);
                return new ResponseMessage("200", "更新分支成功", branchArrayList);
            } else {
                return new ResponseMessage("1032", "更新分支失败", "更新分支失败");
            }
        } else {
            return new ResponseMessage("1037", "更新分支失败", "更新分支失败");
        }
    }

    public ResponseMessage readBranchByNoteName(int userId, String notebookName, String noteName) {
        ArrayList<Branch> branchArrayList = branchMapper.selectBranchByNoteName(userId, notebookName, noteName);
        return new ResponseMessage("200", "查询笔记成功", branchArrayList);
    }

    public ResponseMessage readBranchByBranchId(int userId, int branchId) {
        ArrayList<Branch> branchArrayList = branchMapper.selectBranchByBranchId(userId, branchId);
        if (branchArrayList.size() == 1) {
            return new ResponseMessage("200", "获取分支成功", branchArrayList.get(0));
        } else {
            return new ResponseMessage("1034", "获取分支失败", "获取分支失败");
        }
    }

    public ResponseMessage readBranchByKeywords(int userId, String keywords) {
        ArrayList<Branch> branchArrayList = branchMapper.selectBranchByKeywords(userId, keywords);
        return new ResponseMessage("200", "获取分支成功", Tools.clearContent(branchArrayList));
    }
}
