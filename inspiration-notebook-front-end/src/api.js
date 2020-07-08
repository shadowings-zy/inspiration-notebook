/**
 * 全部api的请求
 */
import axiosInstance from './plugins/axios'
import qs from 'qs'

// 用户相关接口
export const login = data => {
  return axiosInstance.post('/user/login/', qs.stringify({
    username: data.username,
    password: data.password,
    userType: data.userType
  }))
}

export const register = data => {
  return axiosInstance.post('/user/register/', qs.stringify({
    username: data.username,
    password: data.password,
    userType: data.userType
  }))
}

export const updateUser = data => {
  return axiosInstance.post('/user/update/', qs.stringify({
    userId: data.userId,
    username: data.username,
    password: data.password
  }))
}

// 笔记本相关接口
export const addNotebook = data => {
  return axiosInstance.post('/notebook/create/', qs.stringify({
    notebookName: data.notebookName,
    userId: data.userId,
    operator: data.operator
  }))
}

export const updateNotebookName = data => {
  return axiosInstance.post('/notebook/update/', qs.stringify({
    userId: data.userId,
    oldNotebookName: data.oldNotebookName,
    newNotebookName: data.newNotebookName,
    operator: data.operator
  }))
}

export const deleteNotebook = data => {
  return axiosInstance.post('/notebook/delete/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName
  }))
}

export const getNotebookAndNote = data => {
  return axiosInstance.post('/notebook/getAllByUserId/', qs.stringify({
    userId: data.userId
  }))
}

// 笔记相关接口
export const addNote = data => {
  return axiosInstance.post('/note/create/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName,
    noteName: data.noteName,
    operator: data.operator
  }))
}

export const updateNote = data => {
  return axiosInstance.post('/note/update/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName,
    oldNoteName: data.oldNoteName,
    newNoteName: data.newNoteName,
    operator: data.operator
  }))
}

export const deleteNote = data => {
  return axiosInstance.post('/note/delete/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName,
    noteName: data.noteName
  }))
}

export const getNoteByNotebook = data => {
  return axiosInstance.post('/note/getByNotebookName/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName
  }))
}

export const getDocument = data => {
  return axiosInstance.post('/note/getDocument/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName,
    order: data.order
  }))
}

// 分支相关接口
export const addBranch = data => {
  return axiosInstance.post('/branch/create/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName,
    noteName: data.noteName,
    branchName: data.branchName,
    content: data.content,
    operator: data.operator
  }))
}

export const updateBranchContent = data => {
  return axiosInstance.post('/branch/updateContent/', qs.stringify({
    userId: data.userId,
    branchId: data.branchId,
    notebookName: data.notebookName,
    noteName: data.noteName,
    branchName: data.branchName,
    content: data.content,
    operator: data.operator
  }))
}

export const getNoteDetail = data => {
  return axiosInstance.post('/branch/getByNoteName/', qs.stringify({
    userId: data.userId,
    notebookName: data.notebookName,
    noteName: data.noteName
  }))
}

export const getBranch = data => {
  return axiosInstance.post('/branch/getByBranchId/', qs.stringify({
    userId: data.userId,
    branchId: data.branchId
  }))
}

export const searchKeywords = data => {
  return axiosInstance.post('/branch/getByKeywords/', qs.stringify({
    userId: data.userId,
    keywords: data.keywords
  }))
}


