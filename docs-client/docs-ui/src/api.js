import {notification} from 'ant-design-vue'
import axios from "axios"
import consts from "./consts"

const api = axios.create({
  baseURL: consts.baseUrl
});

api.interceptors.request.use(config => {
  config.headers.Authorization = consts.tokenPrefix + window.$cookies.get(consts.tokenCookie);
  return config;
}, error => {
  return Promise.reject(error);
});

api.interceptors.response.use(res => {
  if (res.status === 200) {
    if (res.data.code === 200) {
      //返回自定义成功Result，示例：{code:200, msg: '成功', data: []}
      return Promise.resolve(res.data);
    } else if (res.data.code === undefined) {
      //返回默认response
      return Promise.resolve(res);
    } else {
      //返回自定义失败Result
      notification.error({
        message: '错误',
        description: res.data.msg,
        duration: 3
      });
      return Promise.reject(res);
    }
  } else {
    return Promise.reject(res);
  }
}, error => {
  if (error.response) {
    //带状态码的错误信息
    notification.error({
      message: '错误',
      description: error.response.status + ' - ' + error.response.statusText + ': ' + error.response.data.message,
      duration: 3
    });
    return Promise.reject(error.response);
  } else {
    //无状态码的错误信息
    notification.error({
      message: '错误',
      description: error.toString(),
      duration: 3
    });
    return Promise.reject(error.response);
  }
});

export default api
