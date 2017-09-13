var defaultProject = {ownerId: null, name: '', description: '', sortCode: null};

var docs = new Vue({
    el: '#main',
    data: {
        alert: JSON.parse(JSON.stringify(defaultAlert)),
        search: defaultSearch,
        hasLogin: false,
        user: null,
        showPart: 'projectList',
        projects: [],
        project: defaultProject,
        selectedProject: null,
        directoryTree: [],
        selectedDocument: null,
    },
    created: function () {
        const user = window.$cookies.get('user');
        const hasLogin = (user !== '' && user !== undefined && user !== null);
        const pathname = window.location.hash;
        this.user = hasLogin ? JSON.parse(user) : {id: 0, username: '', email: ''};
        this.hasLogin = hasLogin;
        if (hasLogin) {
            this.getProjectList();
        }
    },
    computed: {
        sortCodeState() {
            return this.project.sortCode > 0 ? null : 'invalid';
        }
    },
    methods: {
        logout: function () {
            deleteLoginCookie();
            window.location.href = '/login.html';
        },
        getProjectList: function () {
            Api.get('/projects/owner/' + this.user.id).then(function (result) {
                if (result.code === 200) {
                    if (result.data.length === 0) {
                        docs.showAlert('info', '项目清单为空');
                    } else {
                        docs.projects = result.data;
                    }
                } else {
                    docs.showAlert('warning', '获取项目清单失败');
                }
            }).catch(function (error) {
                docs.showAlert('danger', '获取项目清单出错');
            });
        },
        jump: function (target) {
            this.showPart = target;
        },
        countDownChanged: function (dismissCountDown) {
            this.alert.countDown = dismissCountDown;
        },
        showAlert: function (type, msg, secs) {
            this.alert.type = type;
            this.alert.msg = msg;
            this.alert.countDown = secs ? secs : this.alert.secs;
        },
        showProject: function () {
            this.jump('showProject');
            this.project = defaultAlert;
        },
        submitProject: function () {
            this.project.ownerId = this.user.id;
            Api.post('/projects', this.project).then(function (result) {
                if (result.code !== 200) {
                    docs.showAlert('warning', result.msg);
                } else {
                    docs.project = defaultProject;
                    docs.showAlert('success', '创建项目成功');
                    this.cancelProject();
                }
            }).catch(function (error) {
                docs.showAlert('danger', '创建项目出错');
            });
        },
        cancelProject: function () {
            this.jump('projectList');
        },
        selectProject: function (project) {
            this.selectedProject = project;
            this.jump('documents');
        },
        addChild: function () {
            addModal.show();
        }
    }
});

var typeOptions = [{text: '目录', value: 0}, {text: '文档', value: 1}];

var addModal = new Vue({
    el: '#add-modal',
    data: {
        alert: JSON.parse(JSON.stringify(defaultAlert)),
        name: '',
        sortCode: null
    },
    computed: {
        sortCodeState() {
            return this.sortCode > 0 ? null : 'invalid';
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
        show: function (directory) {
            this.$refs.add_modal.show();
        },
        hide: function () {
            this.$refs.add_modal.hide();
        },
        submitAdd: function (event) {
            event.cancel();
            var directory = {
                ownerId: docs.user.id,
                projectId: docs.selectedProject.id,
                parentId: 0,
                name: this.name,
                sortCode: this.sortCode
            };
            Api.post('/directories', directory).then(function (result) {
                if (result.code !== 200) {
                    addModal.showAlert('warning', result.msg);
                } else {
                    docs.directoryTree.push(directory);
                    addModal.hide();
                }
            }).catch(function (error) {
                addModal.showAlert('error', '新建目录出错');
            });

        },
        cancelAdd: function () {
            this.hide();
        }
    }
});
