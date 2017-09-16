const accessToken = window.$cookies.get('accessToken');
const apiBaseUrl = 'http://localhost:9000/api';
const authBaseUrl = 'http://localhost:9100/auth';
const refreshAuthTokenUrl = authBaseUrl + '/refresh';

var defaultAlert = {type: 'secondary', secs: 6, countDown: 0};
var defaultSearch = {projectSearch: '', fileSearch: ''};
var defaultItem = {id: null, name: '', sortCode: null};

var Api = axios.create({
    baseURL: apiBaseUrl,
    headers: {'authority': 'bearer ' + accessToken}
});

var Auth = axios.create({
    baseURL: authBaseUrl,
    headers: {'authority': 'bearer ' + accessToken}
});

// request拦截器
Api.interceptors.request.use(function (config) {
    return config;
}, function (error) {
    console.error(error);
    return Promise.reject(error);
});

// response拦截器
Api.interceptors.response.use(function (response) {
    refreshAuthToken();
    // 返回调用接口的Result
    return response.data;
}, function (error) {
    return Promise.reject(error);
});

Auth.interceptors.response.use(function (response) {
    return response.data;
}, function (error) {
    return Promise.reject(error);
});

// 设置登录相关Cookie
function setLoginCookie(result) {
    window.$cookies.set('accessToken', result.accessToken, result.expiredIn);
    window.$cookies.set('user', JSON.stringify(result.user), result.expiredIn);
}

// 删除登录相关Cookie
function deleteLoginCookie() {
    window.$cookies.remove('user');
    window.$cookies.remove('accessToken');
}

// 刷新token
function refreshAuthToken() {
    Auth.get(refreshAuthTokenUrl).then(function (result) {
            if (result.code === 200) {
                setLoginCookie(result.data);
            } else {
                console.warn('更新授权信息失败');
            }
        }).catch(function (error) {
            console.error('更新授权信息出错');
        });
}

var typeOptions = [{text: '目录', value: 0}, {text: '文档', value: 1}];

// tree元素组件
Vue.component('tree-item', {
    template: '#tree-template',
    props: {
        model: Object,
        id: String
    },
    data: function () {
        return {
            hover: false,
            open: false,
            alert: JSON.parse(JSON.stringify(defaultAlert)),
            typeOptions: typeOptions,
            type: null,
            item: JSON.parse(JSON.stringify(defaultItem)),
            directory: JSON.parse(JSON.stringify(defaultItem)),
            deleteId: null
        }
    },
    computed: {
        isFolder() {
            return this.model.subDirectories && this.model.subDirectories.length >= 0;
        },
        addSortCodeState() {
            return this.item.sortCode > 0 ? null : 'invalid';
        },
        editSortCodeState() {
            return this.directory.sortCode > 0 ? null : 'invalid';
        }
    },
    methods: {
        fetchDirectory: function (directoryId) {
            var instance = this;
            Api.get('/directories/' + directoryId).then(function (result) {
                if (result.code !== 200) {
                    instance.showAlert('warning', result.msg);
                } else {
                    instance.directory = result.data;
                }
            }).catch(function (error) {
                instance.showAlert('danger', '获取目录详情出错');
            });
        },
        countDownChanged: function (dismissCountDown) {
            this.alert.countDown = dismissCountDown;
        },
        showAlert: function (type, msg, secs) {
            this.alert.type = type;
            this.alert.msg = msg;
            this.alert.countDown = secs ? secs : this.alert.secs;
        },
        toggle: function () {
            if (this.isFolder) {
                this.open = !this.open;
            }
        },
        showDoc: function (documentId) {
            docs.selectDocument(documentId);
        },
        showAdd: function () {
            this.$refs.tree_add_modal.show();
        },
        showEdit: function () {
            this.$refs.directory_edit_modal.show();
        },
        showDelete: function () {
            this.$refs.directory_delete_modal.show();
        },
        hideAdd: function () {
            this.$refs.tree_add_modal.hide();
        },
        hideDelete: function () {
            this.$refs.directory_delete_modal.hide();
        },
        hideEdit: function () {
            this.$refs.directory_edit_modal.hide();
        },
        addChild: function () {
            this.alert = JSON.parse(JSON.stringify(defaultAlert));
            this.type = null;
            this.item = JSON.parse(JSON.stringify(defaultItem));
            this.showAdd();
        },
        editDirectory: function (directoryId) {
            this.alert = JSON.parse(JSON.stringify(defaultAlert));
            this.fetchDirectory(directoryId);
            this.showEdit();
        },
        deleteDirectory: function (directoryId) {
            this.alert = JSON.parse(JSON.stringify(defaultAlert));
            this.deleteId = directoryId;
            this.showDelete();
        },
        submitAdd: function (event) {
            event.cancel();
            var instance = this;
            if (this.type === 0) {
                var directory = {
                    ownerId: docs.user.id,
                    projectId: docs.selectedProject.id,
                    parentId: this.model.id,
                    name: this.item.name,
                    sortCode: this.item.sortCode
                };
                Api.post('/directories', directory).then(function (result) {
                    if (result.code !== 200) {
                        instance.showAlert('warning', result.msg);
                    } else {
                        docs.fetchProjectDirectoryTree();
                        instance.hideAdd();
                    }
                }).catch(function (error) {
                    instance.showAlert('danger', '新建目录出错');
                });
            } else if (this.type === 1) {
                var document = {
                    ownerId: docs.user.id,
                    directoryId: this.model.id,
                    name: this.item.name,
                    content: '',
                    sortCode: this.item.sortCode
                };
                Api.post('/documents', document).then(function (result) {
                    if (result.code !== 200) {
                        instance.showAlert('warning', result.msg);
                    } else {
                        docs.fetchProjectDirectoryTree();
                        instance.hideAdd();
                    }
                }).catch(function (eroor) {
                    instance.showAlert('danger', '新建文档出错');
                })
            } else {
                instance.showAlert('warning', '请选择一种类型');
            }
        },
        cancelAdd: function () {
            this.hideAdd();
        },
        submitEdit: function (event) {
            event.cancel();
            var instance = this;
            Api.put('/directories/' + this.directory.id, this.directory).then(function (result) {
                if (result.code !== 200) {
                    instance.showAlert('warning', result.msg);
                } else {
                    docs.fetchProjectDirectoryTree();
                    instance.hideEdit();
                }
            }).catch(function (error) {
                instance.showAlert('danger', '编辑目录出错');
            });
        },
        cancelEdit: function () {
            this.hideEdit();
        },
        submitDelete: function (event) {
            event.cancel();
            var instance = this;
            Api.delete('/directories/' + this.deleteId).then(function (result) {
                if (result.code !== 200) {
                    instance.showAlert('warning', result.msg);
                } else {
                    docs.fetchProjectDirectoryTree();
                    instance.hideDelete();
                }
            }).catch(function (error) {
                instance.showAlert('danger', '删除目录出错');
            });
        },
        cancelDelete: function () {
            this.hideDelete();
        }
    }
});

//markdown编辑组件
Vue.component('markdown-editor', {
    template: '#editor-template',
    props: {
        value: String,
        previewClass: String,
        autofocus: {
            type: Boolean,
            default() {
                return true;
            }
        },
        autoinit: {
            type: Boolean,
            default() {
                return true;
            },
        },
        highlight: {
            type: Boolean,
            default() {
                return false;
            },
        },
        configs: {
            type: Object,
            default() {
                return {};
            },
        },
    },
    mounted() {
        if (this.autoinit) this.initialize();

    },
    activated() {
        const editor = this.simplemde;
        if (!editor) return;
        const isActive = editor.isSideBySideActive() || editor.isPreviewActive();
        if (isActive) editor.toggleFullScreen();
    },
    methods: {
        initialize() {
            const configs = {
                element: this.$el.firstElementChild,
                initialValue: this.value,
                autofocus: true,
                renderingConfig: {}
            };
            Object.assign(configs, this.configs);
            // 判断是否开启代码高亮
            if (this.highlight) {
                configs.renderingConfig.codeSyntaxHighlighting = true;
            }
            // 实例化编辑器
            this.simplemde = new SimpleMDE(configs);
            // 添加自定义 previewClass
            const className = this.previewClass || '';
            this.addPreviewClass(className);
            // 绑定事件
            this.bindingEvents();
        },
        bindingEvents() {
            this.simplemde.codemirror.on('change', () => {
                this.$emit('input', this.simplemde.value());
            });
        },
        addPreviewClass(className) {
            const wrapper = this.simplemde.codemirror.getWrapperElement();
            const preview = document.createElement('div');
            wrapper.nextSibling.className += ` ${className}`;
            preview.className = `editor-preview ${className}`;
            wrapper.appendChild(preview);
        }
    },
    destroyed() {
        this.simplemde = null;
    },
    watch: {
        value(val) {
            if (val === this.simplemde.value()) return;
            this.simplemde.value(val);
        }
    }
});
