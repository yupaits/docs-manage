new Vue({
    el: '#registerForm',
    data: {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    },
    methods: {
        submitRegister: function () {
            var registerForm = {
                username: this.username,
                email: this.email,
                password: this.password,
                confirmPassword: this.confirmPassword
            };
            ajax({
                type: 'post',
                url: '/auth/register',
                data: registerForm,
                success: function (result) {
                    if (result.code !== 200) {
                        showMsg(result.msg);
                    } else {
                        window.location.href = '/login.html';
                    }
                }
            });
        }
    }
});