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
import java.util.Date;

@Service
public class NoteService {
    BranchMapper branchMapper;

    @Autowired
    public NoteService(BranchMapper branchMapper) {
        this.branchMapper = branchMapper;
    }

    public ResponseMessage createNote(int userId, String notebookName, String noteName, String operator) {
        String newNoteBranchName = "主分支";
        String status = Tools.generateOperationInformation(operator, "新建笔记");
        int noteCount = branchMapper.selectNoteCount(userId, notebookName, noteName);
        int notebookCount = branchMapper.selectNotebookCount(userId, notebookName);
        if (noteCount == 0 && notebookCount > 0) {
            Branch branch = new Branch(0, userId, notebookName, noteName, newNoteBranchName, "", status);
            int insertStatus = branchMapper.insertBranch(branch);
            if (insertStatus > 0) {
                return readNoteByNotebookName(userId, notebookName);
            } else {
                return new ResponseMessage("1030", "新建笔记分支失败", "新建笔记分支失败");
            }
        } else if (noteCount > 0 && notebookCount > 0) {
            return new ResponseMessage("1021", "笔记名重复", "笔记名重复");
        } else {
            return new ResponseMessage("1027", "新建笔记失败", "新建笔记失败");
        }
    }

    public ResponseMessage updateNoteName(int userId, String notebookName, String oldNoteName, String newNoteName, String operator) {
        int noteCount = branchMapper.selectNoteCount(userId, notebookName, newNoteName);
        if (noteCount == 0) {
            String status = Tools.generateOperationInformation(operator, "修改笔记名");
            int updateStatus = branchMapper.updateNoteName(userId, notebookName, oldNoteName, newNoteName, status);
            if (updateStatus > 0) {
                return readNoteByNotebookName(userId, notebookName);
            } else {
                return new ResponseMessage("1022", "修改笔记名出错", "修改笔记名出错");
            }
        } else {
            return new ResponseMessage("1021", "笔记名重复", "笔记名重复");
        }
    }

    public ResponseMessage deleteNote(int userId, String notebookName, String noteName) {
        int deleteStatus = branchMapper.deleteNote(userId, notebookName, noteName);
        if (deleteStatus > 0) {
            return readNoteByNotebookName(userId, notebookName);
        } else {
            return new ResponseMessage("1022", "删除笔记出错", "删除笔记出错");
        }
    }

    public ResponseMessage readNoteByNotebookName(int userId, String notebookName) {
        ArrayList<Branch> branchArrayList = branchMapper.selectBranchByNotebookName(userId, notebookName);
        ArrayList<Branch> clearBranchArrayList = Tools.clearContent(branchArrayList);
        ArrayList<Note> noteArrayList = Notebook.getNoteArrayListFromBranchArrayList(notebookName, clearBranchArrayList);
        Notebook notebook = new Notebook(notebookName, noteArrayList);
        return new ResponseMessage("200", "操作成功", notebook);
    }

    public ResponseMessage generateNoteDocument(int userId, String notebookName, String order) {
        ArrayList<Branch> branchArrayList = branchMapper.selectBranchByNotebookName(userId, notebookName);
        StringBuilder markdownContent = new StringBuilder("[toc]\n");
        String[] orderList = order.split(",");
        for (String orderItem : orderList) {
            for (Branch branch : branchArrayList) {
                if (orderItem.equals(branch.getBranchId() + "") && branch.getBranchName().equals("主分支")) {
                    markdownContent.append(branch.getContent()).append("\n");
                }
            }
        }

        String documentName = branchArrayList.get(0).getNotebookName() + "-" + new Date().getTime();
        Tools.generateFile(documentName, markdownContent.toString());
        return new ResponseMessage("200", "生成笔记成功", documentName + ".zip");
    }
}
