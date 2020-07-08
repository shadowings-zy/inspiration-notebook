package com.shadowingszy.notebook.entity;

import java.util.ArrayList;

/**
 * 笔记实体
 */
public class Note {
    private String noteName = "";                 // 笔记名称
    private ArrayList<Branch> branchArrayList;    // 笔记分支列表

    public Note(String noteName, ArrayList<Branch> branchArrayList) {
        this.noteName = noteName;
        this.branchArrayList = branchArrayList;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public ArrayList<Branch> getBranchArrayList() {
        return branchArrayList;
    }

    public void setBranchArrayList(ArrayList<Branch> branchArrayList) {
        this.branchArrayList = branchArrayList;
    }

    @Override
    public String toString() {
        return "Note{" +
                ", noteName='" + noteName + '\'' +
                ", branchArrayList=" + branchArrayList +
                '}';
    }
}
