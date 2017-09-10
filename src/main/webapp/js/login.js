new Vue({
    el: '#loginForm',
    data: {
        username: '',
        password: ''
    },
    methods: {
        submitLogin: function () {
            var loginForm = {
                username: this.username,
                password: this.password
            };
            ajax({
                type: 'post',
                url: '/auth/login',
                data: loginForm,
                success: function (result) {
                    if (result.code !== 200) {
                        showMsg(result.msg);
                    } else {
                        setLoginCookie(result.data);
                        window.location.href = '/';
                    }
                }
            });
        }
    }
});