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
    //返回头里如果存在AccessToken，则说明返回了刷新之后的token
    if (res.headers.accesstoken) {
      window.$cookies.set(consts.tokenCookie, res.headers.accesstoken, consts.tokenExpiredInMinutes);
    }
    //自定义的错误信息
    if (res.data.code !== 200) {
      notification.error({
        message: '错误',
        description: res.data.msg,
        duration: 3
      });
      return Promise.reject(res);
    } else {
      return Promise.resolve(res.data);
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
