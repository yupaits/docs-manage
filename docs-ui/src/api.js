import axios from "axios";
import consts from "./consts";

const api = axios.create({
  baseURL: consts.baseUrl
});

api.interceptors.request.use(config => {
  config.headers.Authorization = 'Bearer ' + window.$cookies.get(constant.accessToken);
  return config;
}, error => {
  return Promise.reject(error);
});

api.interceptors.response.use(res => {
  return res.data;
}, error => {
  return Promise.reject(error);
});

export default api
