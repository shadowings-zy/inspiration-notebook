package com.shadowingszy.notebook.service;

import com.shadowingszy.notebook.entity.Branch;
import com.shadowingszy.notebook.entity.Note;
import com.shadowingszy.notebook.entity.Notebook;
import com.shadowingszy.notebook.mapper.BranchMapper;
import com.shadowingszy.tools.ResponseMessage;
import com.shadowingszy.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NotebookService {
    BranchMapper branchMapper;

    @Autowired
    public NotebookService(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    public ResponseMessage newNotebook(int userId, String notebookName, String operator) {
        String newNoteName = "快速笔记";
        String newNoteBranchName = "主分支";
        String status = Tools.generateOperationInformation(operator, "新建笔记本");
        int notebookCount = branchMapper.selectNotebookCount(userId, notebookName);
        if (notebookCount == 0) {
            Branch branch = new Branch(0, userId, notebookName, newNoteName, newNoteBranchName, "", status);
            int insertStatus = branchMapper.insertBranch(branch);
            if (insertStatus > 0) {
                return readAllNoteByUserId(userId);
            } else {
                return new ResponseMessage("1030", "新建笔记分支失败", "新建笔记分支失败");
            }
        } else {
            return new ResponseMessage("1011", "笔记本名重复", "笔记本名重复");
        }
    }

    public ResponseMessage updateNotebookName(int userId, String oldNotebookName, String newNotebookName, String operator) {
        int notebookCount = branchMapper.selectNotebookCount(userId, newNotebookName);
        if (notebookCount == 0) {
            String status = Tools.generateOperationInformation(operator, "修改笔记本名");
            int updateStatus = branchMapper.updateNotebookName(userId, oldNotebookName, newNotebookName, status);
            if (updateStatus > 0) {
                return readAllNoteByUserId(userId);
            } else {
                return new ResponseMessage("1012", "修改笔记本出错", "修改笔记本出错");
            }
        } else {
            return new ResponseMessage("1011", "笔记本名重复", "笔记本名重复");
        }
    }

    public ResponseMessage deleteNotebook(int userId, String notebookName) {
        int deleteStatus = branchMapper.deleteNotebook(userId, notebookName);
        if (deleteStatus > 0) {
            return readAllNoteByUserId(userId);
        } else {
            return new ResponseMessage("1013", "删除笔记本出错", "删除笔记本出错");
        }
    }

    public ResponseMessage readAllNoteByUserId(int userId) {
        ArrayList<Branch> branchArrayList = branchMapper.selectBranchByUserId(userId);
        ArrayList<Branch> clearBranchArrayList = Tools.clearContent(branchArrayList);
        ArrayList<Notebook> notebookArrayList = new ArrayList<Notebook>();
        for (Branch branch : clearBranchArrayList) {
            int notebookIndex = Tools.indexOfNotebookArrayList(notebookArrayList, branch.getNotebookName());
            if (notebookIndex == -1) {
                ArrayList<Note> noteArrayList = Notebook.getNoteArrayListFromBranchArrayList(branch.getNotebookName(), branchArrayList);
                Notebook notebook = new Notebook(branch.getNotebookName(), noteArrayList);
                notebookArrayList.add(notebook);
            }
        }
        return new ResponseMessage("200", "操作成功", notebookArrayList);
    }
}
