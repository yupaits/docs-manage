$(function () {
    new Vue({
        el: '#main',
        data: {
            hasLogin: false,
            username: '',
        },
        created: function (){
            const username = $.cookie('username');
            const hasLogin = (username && username !== '');
            this.username = username;
            this.hasLogin = hasLogin;
            if (hasLogin) {
                ajax({
                    type: 'get',
                    url: '/api/projects/owner/1'
                });
            }
        },
        methods: {
            logout: function () {
                deleteLoginCookie();
                window.location.href = '/';
            }
        }
    });
});
