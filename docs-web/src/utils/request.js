import axios from 'axios';
import constant from './constant';

const accessToken = window.$cookies.get(constant.accessToken);
const apiBaseUrl = 'http://localhost:9000/api';
const authBaseUrl = 'http://localhost:9100/auth';
const authHeader = {'authority': 'bearer ' + accessToken};

const refreshAuthTokenUrl = authBaseUrl + '/refresh';

const Api = axios.create({
  baseURL: apiBaseUrl,
  headers: authHeader
});

Api.interceptors.response.use(function (response) {
  refreshAuthToken();
  return response.data;
}, function (error) {
  return Promise.reject(error);
});

const Auth = axios.create({
  baseURL: authBaseUrl,
  headers: authHeader
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
