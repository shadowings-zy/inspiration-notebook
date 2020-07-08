/**
 * axios的设置
 */

import axios from 'axios'

const axiosInstance = axios.create({
  headers: {'Content-Type': 'application/x-www-form-urlencoded'},
  withCredentials: true,
  baseURL: process.env.NODE_ENV === 'production' ? './' : '/api',
})


export default axiosInstance