const baseUrl = 'https://localhost:9000';

function ajax(options) {
    const accessToken = $.cookie('accessToken');
    const pathname = options.url;
    options.type = options.type || 'post';
    options.url = baseUrl + pathname;
    options.headers = {'auth': 'bearer ' + accessToken};
    options.contentType = options.contentType || 'application/json';
    options.data = options.contentType === 'application/json' ? JSON.stringify(options.data) : options.data;
    options.async = options.async || true;
    options.dataType = options.dataType || 'json';
    options.success = options.success || function (result) {
            setTimeout(function () {
                layer.msg(result.msg);
            }, 300);
        };
    options.error = options.error || function (error) {
            layer.closeAll('loading');
            error = JSON.parse(error.responseText);
            setTimeout(function () {
                layer.msg(error.error);
            }, 300);
        };
    options.beforeSend = function () {
        layer.msg('加载中', {icon: 16, shade: 0.01});
    };
    $.ajax(options);
    const now = new Date();
    if (!pathname.startsWith('/auth/login') && accessToken.expires - now < 120000) {
        refreshAuthToken();
    }
}

function setLoginCookie(result) {
    $.cookie('accessToken', result.accessToken, {expires: new Date(result.expiredAt), path: '/'});
    $.cookie('username', result.username, {expires: new Date(result.expiredAt), path: '/'});
}

function deleteLoginCookie() {
    $.removeCookie('username', {path: '/'});
    $.removeCookie('accessToken', {path: '/'});
}

function refreshAuthToken() {
    const accessToken = $.cookie('accessToken');
    $.ajax({
        type: 'get',
        url: baseUrl + '/auth/refresh',
        headers: {'auth': 'bearer ' + accessToken},
        success: function (result) {
            if (result.code === 200) {
                setLoginCookie(result.data);
            } else {
                setTimeout(function () {
                    layer.msg('更新授权信息失败');
                }, 300);
            }
        },
        error: function () {
            setTimeout(function () {
                layer.msg('更新授权信息出错');
            }, 300);
        }
    });
}
