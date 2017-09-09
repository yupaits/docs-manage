const baseUrl = 'https://localhost:9000';

function ajax(options) {
    const accessToken = $.cookie('accessToken');
    const pathname = options.url;
    options.type = options.type || 'post';
    options.url = baseUrl + pathname;
    options.headers = {'authority': 'bearer ' + accessToken};
    options.contentType = options.contentType || 'application/json';
    options.data = options.contentType === 'application/json' ? JSON.stringify(options.data) : options.data;
    options.async = options.async || true;
    options.dataType = options.dataType || 'json';
    options.success = options.success || function (result) {
            showMsg(result.msg);
        };
    options.error = options.error || function (error) {
            showMsg('请求出错，请稍后再试');
        };
    options.beforeSend = function () {
        layer.msg('加载中', {icon: 16, shade: 0.01});
    };
    $.ajax(options);
    if (!pathname.startsWith('/auth') && accessToken !== undefined) {
        refreshAuthToken();
    }
}

function setLoginCookie(result) {
    $.cookie('accessToken', result.accessToken, {expires: new Date(result.expiredAt), path: '/'});
    $.cookie('user', JSON.stringify(result.user), {expires: new Date(result.expiredAt), path: '/'})
}

function deleteLoginCookie() {
    $.removeCookie('user', {path: '/'});
    $.removeCookie('accessToken', {path: '/'});
}

function refreshAuthToken() {
    const accessToken = $.cookie('accessToken');
    $.ajax({
        type: 'get',
        url: baseUrl + '/auth/refresh',
        headers: {'authority': 'bearer ' + accessToken},
        success: function (result) {
            if (result.code === 200) {
                setLoginCookie(result.data);
            } else {
                showMsg('更新授权信息失败');
            }
        },
        error: function () {
            showMsg('更新授权信息出错');
        }
    });
}

function showMsg(msg) {
    layer.closeAll();
    setTimeout(function () {
        layer.msg(msg);
    }, 300);
}