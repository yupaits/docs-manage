const baseUrl = 'http://localhost:11030/ui';
const logoutUrl = baseUrl + '/logout';
const redirectUri = 'http://localhost:11030/ui/index#';
const authUri = 'http://localhost:11010/auth/oauth/authorize?client_id=docs-manage&response_type=code&redirect_uri=' + redirectUri;
const tokenCookie = 'accessToken';
const tokenPrefix = 'Bearer ';
const tokenExpiredInMinutes = '120min';
const userInfoUri = 'http://localhost:11010/auth/user';

export default {
  baseUrl, logoutUrl, authUri, tokenCookie, tokenPrefix, tokenExpiredInMinutes, userInfoUri
}