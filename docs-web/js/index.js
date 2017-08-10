$(function () {
    var data = new Vue({
        el: '#main',
        data: {
            hasLogin: false,
            user: null,
            showPart: 'projectList',
            projects: [],
            selectedProject: null,
        },
        created: function (){
            const user = $.cookie('user');
            const hasLogin = (user !== '' && user !== undefined);
            const pathname = window.location.hash;
            this.user = hasLogin ? JSON.parse(user) : {
                id: 0,
                username: '',
                email: ''
            };
            this.hasLogin = hasLogin;
            if (hasLogin) {
                ajax({
                    type: 'get',
                    url: '/api/projects/owner/' + this.user.id,
                    async: false,
                    success: function (result) {
                        if (result.code === 200) {
                            layer.closeAll('dialog');
                            if (result.data.length === 0) {
                                layer.msg('项目清单为空', {time: 1000});
                            } else {
                                data.projects = result.data;
                            }
                        } else {
                            layer.msg('获取项目清单失败');
                        }
                    }
                });
            }
        },
        methods: {
            logout: function () {
                deleteLoginCookie();
                window.location.href = '/';
            },
            jump: function (target) {
                this.showPart = target;
            },
            createProject: function () {

            },
            updateProject: function () {

            },
            cancelProject: function () {
                this.jump('projectList');
            }
        }
    });
});
