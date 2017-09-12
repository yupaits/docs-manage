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
            Api.post('/auth/login', loginForm).then(function (result) {
                if (result.code !== 200) {
                    showMsg(result.msg);
                } else {
                    setLoginCookie(result.data);
                    window.location.href = '/';
                }
            }).catch(function (error) {

            });
        }
    }
});