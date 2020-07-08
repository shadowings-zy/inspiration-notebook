package com.shadowingszy.notebook.entity;

/**
 * 笔记分支实体，主要用于笔记版本控制和内容展示
 */
public class Branch {
    private int branchId = 0;           // 分支ID
    private int userId = 0;             // 用户ID
    private String notebookName = "";   // 笔记本名
    private String noteName = "";       // 笔记名
    private String branchName = "";     // 分支名称
    private String content = "";        // 笔记内容
    private String status = "";         // 笔记状态

    public Branch(int branchId, int userId, String notebookName, String noteName, String branchName, String content, String status) {
        this.branchId = branchId;
        this.userId = userId;
        this.notebookName = notebookName;
        this.noteName = noteName;
        this.branchName = branchName;
        this.content = content;
        this.status = status;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branchId=" + branchId +
                ", userId=" + userId +
                ", notebookName='" + notebookName + '\'' +
                ", noteName='" + noteName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
