import axios from 'axios';
import constant from './constant';

const apiBaseUrl = 'http://localhost:9000/api';
const authBaseUrl = 'http://localhost:9100/auth';

const refreshAuthTokenUrl = authBaseUrl + '/refresh';

const Api = axios.create({
  baseURL: apiBaseUrl
});

Api.interceptors.request.use(function (config) {
  config.headers.authority = 'bearer ' + window.$cookies.get(constant.accessToken);
  return config;
}, function (error) {
  return Promise.reject(error);
});

Api.interceptors.response.use(function (response) {
  refreshAuthToken();
  return response.data;
}, function (error) {
  return Promise.reject(error);
});

const Auth = axios.create({
  baseURL: authBaseUrl
});

Auth.interceptors.request.use(function (config) {
  config.headers.authority = 'bearer ' + window.$cookies.get(constant.accessToken);
  return config;
}, function (error) {
  return Promise.reject(error);
});

Auth.interceptors.response.use(function (response) {
  return response.data;
}, function (error) {
  return Promise.reject(error);
});

function refreshAuthToken() {
  Auth.get(refreshAuthTokenUrl).then(function (result) {
    if (result.code === 200) {
      window.$cookies.set(constant.user, JSON.stringify(result.data.user), result.data.expiredIn);
      window.$cookies.set(constant.accessToken, result.data.accessToken, result.data.expiredIn);
    } else {
      console.warn('refresh auth token fail.');
    }
  }).catch(function (error) {
    console.error('refresh auth token error', error);
  })
}

export default {
  Auth, Api
};
