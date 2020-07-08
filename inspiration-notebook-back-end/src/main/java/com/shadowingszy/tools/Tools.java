package com.shadowingszy.tools;

import com.shadowingszy.notebook.entity.Branch;
import com.shadowingszy.notebook.entity.Note;
import com.shadowingszy.notebook.entity.Notebook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Tools {
    static String zipPath = "/www/server/node-express/public/inspiration-notebook/markdown/";
    /**
     * 为了尽可能地少传输数据，我们在查询笔记列表时，会把所有的content都先消除掉，再返回给前端
     *
     * @param branchArrayList branch列表
     * @return 去除掉content的列表
     */
    public static ArrayList<Branch> clearContent(ArrayList<Branch> branchArrayList) {
        for (Branch branch : branchArrayList) {
            branch.setContent("");
        }
        return branchArrayList;
    }

    /**
     * 生成最近的操作记录
     *
     * @param operator 操作人
     * @param status   操作
     * @return 操作记录
     */
    public static String generateOperationInformation(String operator, String status) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return "最近操作【操作人：" + operator + "，操作时间：" + dateFormat.format(new Date()) + "，操作：" + status + "】";
    }

    /**
     * 生成markdown文件
     *
     * @param documentName 文件名
     * @param content      文件内容
     */
    public static void generateFile(String documentName, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(zipPath + documentName, true), "UTF-8"));
            writer.write(content);
            writer.close();
            zip(zipPath + documentName, zipPath + documentName + ".zip", documentName + ".md");  // 压缩一下
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩文件
     *
     * @param input  输入路径
     * @param output 输出路径
     * @param name   压缩的单个文件名
     * @throws Exception 异常
     */
    public static void zip(String input, String output, String name) throws Exception {
        //要生成的压缩文件
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(output));
        String[] paths = input.split("\\|");
        File[] files = new File[paths.length];
        byte[] buffer = new byte[1024];
        for (int i = 0; i < paths.length; i++) {
            files[i] = new File(paths[i]);
        }
        for (File file : files) {
            FileInputStream fis = new FileInputStream(file);
            if (files.length == 1 && name != null) {
                out.putNextEntry(new ZipEntry(name));
            } else {
                out.putNextEntry(new ZipEntry(file.getName()));
            }
            int len;
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        }
        out.close();
    }

    /**
     * 判断某个note的branchArrayList中有没有当前branch。
     *
     * @param branchArrayList branch列表
     * @param currentBranchId 当前branchId
     * @return 若有，则返回index，若没有，则返回-1
     */
    public static int indexOfBranchArrayList(ArrayList<Branch> branchArrayList, int currentBranchId) {
        for (int a = 0; a < branchArrayList.size(); a++) {
            if (branchArrayList.get(a).getBranchId() == currentBranchId) {
                return a;
            }
        }
        return -1;
    }

    /**
     * 判断某个notebook的noteArrayList中有没有当前note，若有，则返回index，若没有，则返回-1。
     *
     * @param noteArrayList   note列表
     * @param currentNoteName 当前noteName
     * @return 若有，则返回index，若没有，则返回-1
     */
    public static int indexOfNoteArrayList(ArrayList<Note> noteArrayList, String currentNoteName) {
        for (int a = 0; a < noteArrayList.size(); a++) {
            if (noteArrayList.get(a).getNoteName().equals(currentNoteName)) {
                return a;
            }
        }
        return -1;
    }

    /**
     * 判断notebookArrayList中有没有当前notebook，若有，则返回index，若没有，则返回-1。
     *
     * @param notebookArrayList   notebook列表
     * @param currentNotebookName 当前notebookName
     * @return 若有，则返回index，若没有，则返回-1
     */
    public static int indexOfNotebookArrayList(ArrayList<Notebook> notebookArrayList, String currentNotebookName) {
        for (int a = 0; a < notebookArrayList.size(); a++) {
            if (notebookArrayList.get(a).getNotebookName().equals(currentNotebookName)) {
                return a;
            }
        }
        return -1;
    }
}
