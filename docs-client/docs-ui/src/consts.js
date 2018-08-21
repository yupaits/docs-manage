const baseUrl = 'http://localhost:11030/ui';
const logoutUrl = 'http://localhost:11010/auth/oauth/token?accessToken=';
const redirectUri = 'http://localhost:11030/ui/index#';
const authUri = 'http://localhost:11010/auth/oauth/authorize?client_id=docs-manage&response_type=code&redirect_uri=' + redirectUri;
const tokenCookie = 'accessToken';
const tokenPrefix = 'Bearer ';
const tokenExpiredInMinutes = '120min';

export default {
  baseUrl, logoutUrl, redirectUri, authUri, tokenCookie, tokenPrefix, tokenExpiredInMinutes
}