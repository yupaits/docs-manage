var login = new Vue({
    el: '#loginForm',
    data: {
        alert: defaultAlert,
        username: '',
        password: ''
    },
    computed: {
        passwordState() {
            return this.password.length >= 3 ? null : 'invalid';
        }
    },
    methods: {
        countDownChanged: function (dismissCountDown) {
            this.alert.countDown = dismissCountDown;
        },
        showAlert: function (type, msg, secs) {
            this.alert.type = type;
            this.alert.msg = msg;
            this.alert.countDown = secs ? secs : this.alert.secs;
        },
        submitLogin: function () {
            var loginForm = {
                username: this.username,
                password: this.password
            };
            Api.post('/auth/login', loginForm).then(function (result) {
                if (result.code !== 200) {
                    alert(JSON.stringify(result));
                } else {
                    alert(JSON.stringify(result));
                    setLoginCookie(result.data);
                    window.location.href = '/';
                }
            }).catch(function (error) {
                login.showAlert('danger', '登录出错');
            });
        }
    }
});