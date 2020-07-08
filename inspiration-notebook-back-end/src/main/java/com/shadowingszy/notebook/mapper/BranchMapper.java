package com.shadowingszy.notebook.mapper;

import com.shadowingszy.notebook.entity.Branch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface BranchMapper {
    /**
     * 向数据库中插入一个新分支
     *
     * @param branch 分支对象
     * @return 插入结果
     */
    int insertBranch(Branch branch);

    /**
     * 修改笔记本名
     *
     * @param userId          用户ID
     * @param oldNotebookName 笔记本原始名称
     * @param newNotebookName 笔记本新名称
     * @param status          分支状态
     * @return 修改结果
     */
    int updateNotebookName(int userId, String oldNotebookName, String newNotebookName, String status);

    /**
     * 修改笔记名
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名称
     * @param oldNoteName  笔记原始名称
     * @param newNoteName  笔记新名称
     * @param status       分支状态
     * @return 修改结果
     */
    int updateNoteName(int userId, String notebookName, String oldNoteName, String newNoteName, String status);

    /**
     * 修改某一个分支的内容
     *
     * @param branchId 分支ID
     * @param userId   用户ID
     * @param content  分支内容
     * @param status   分支状态
     * @return 修改结果
     */
    int updateBranchContent(int userId, int branchId, String content, String status);

    /**
     * 删除某个笔记本
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名称
     * @return 删除结果
     */
    int deleteNotebook(int userId, String notebookName);

    /**
     * 删除某个笔记
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名称
     * @param noteName     笔记名称
     * @return 删除结果
     */
    int deleteNote(int userId, String notebookName, String noteName);

    /**
     * 查询名为notebookName的笔记数量
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名称
     * @return 笔记数量
     */
    int selectNotebookCount(int userId, String notebookName);

    /**
     * 查询名为noteName的笔记数量
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名称
     * @param noteName     笔记名称
     * @return 笔记数量
     */
    int selectNoteCount(int userId, String notebookName, String noteName);

    /**
     * 查询名为branchName的分支数量
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名称
     * @param noteName     笔记名称
     * @param branchName   分支名称
     * @return 分支数量
     */
    int selectBranchCount(int userId, String notebookName, String noteName, String branchName);

    /**
     * 查询当前用户的所有分支
     *
     * @param userId 用户ID
     * @return 笔记分支
     */
    ArrayList<Branch> selectBranchByUserId(int userId);

    /**
     * 查询当前笔记本的所有分支
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名
     * @return 笔记分支
     */
    ArrayList<Branch> selectBranchByNotebookName(int userId, String notebookName);

    /**
     * 查询当前笔记的所有分支
     *
     * @param userId       用户ID
     * @param notebookName 笔记本名
     * @param noteName     笔记名
     * @return 笔记分支
     */
    ArrayList<Branch> selectBranchByNoteName(int userId, String notebookName, String noteName);

    /**
     * 查询当前笔记的所有分支
     *
     * @param userId   用户ID
     * @param branchId 分支ID
     * @return 笔记分支
     */
    ArrayList<Branch> selectBranchByBranchId(int userId, int branchId);

    /**
     * 根据关键词查询分支
     *
     * @param userId   用户ID
     * @param keywords 关键词
     * @return 笔记分支
     */
    ArrayList<Branch> selectBranchByKeywords(int userId, String keywords);
}
