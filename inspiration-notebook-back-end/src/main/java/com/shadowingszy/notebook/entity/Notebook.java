package com.shadowingszy.notebook.entity;

import com.shadowingszy.tools.Tools;

import java.util.ArrayList;

/**
 * 笔记本实体
 */
public class Notebook {
    private String notebookName = "";       // 笔记本名
    private ArrayList<Note> NoteArrayList;  // 笔记本下的笔记列表

    public Notebook(String notebookName, ArrayList<Note> noteArrayList) {
        this.notebookName = notebookName;
        NoteArrayList = noteArrayList;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebookName(String notebookName) {
        this.notebookName = notebookName;
    }

    public ArrayList<Note> getNoteArrayList() {
        return NoteArrayList;
    }

    public void setNoteArrayList(ArrayList<Note> noteArrayList) {
        NoteArrayList = noteArrayList;
    }

    @Override
    public String toString() {
        return "Notebook{" +
                ", notebookName='" + notebookName + '\'' +
                ", NoteArrayList=" + NoteArrayList +
                '}';
    }

    /**
     * 从一个branch列表中选择当前notebook下的所有note，并放入noteArrayList中。
     *
     * @param notebookName    notebook的ID
     * @param branchArrayList note列表
     */
    public static ArrayList<Note> getNoteArrayListFromBranchArrayList(String notebookName, ArrayList<Branch> branchArrayList) {
        ArrayList<Note> outputNoteArrayList = new ArrayList<Note>();
        for (Branch branch : branchArrayList) {
            if (branch.getNotebookName().equals(notebookName)) {
                int currentNoteIndex = Tools.indexOfNoteArrayList(outputNoteArrayList, branch.getNoteName());
                if (currentNoteIndex == -1) {
                    Note newNote = new Note(branch.getNoteName(), new ArrayList<>());
                    newNote.getBranchArrayList().add(branch);
                    outputNoteArrayList.add(newNote);
                } else {
                    int currentBranchIndex = Tools.indexOfBranchArrayList(outputNoteArrayList.get(currentNoteIndex).getBranchArrayList(), branch.getBranchId());
                    if (currentBranchIndex == -1) {
                        outputNoteArrayList.get(currentNoteIndex).getBranchArrayList().add(branch);
                    }
                }
            }
        }
        return outputNoteArrayList;
    }
}
