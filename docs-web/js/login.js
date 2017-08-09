new Vue({
    el: '#loginForm',
    data: {
        username: '',
        password: ''
    },
    methods: {
        submitLogin: function () {
            ajax({
                type: 'post',
                url: '/auth/login?username=' + this.username + '&password=' + this.password,
                success: function (result) {
                    setLoginCookie(result);
                    window.location.href = '/';
                }
            })
        }
    }
});