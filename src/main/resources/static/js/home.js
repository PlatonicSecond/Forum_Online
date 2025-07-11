// 设置axios基础URL
axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.withCredentials = true;

// 从localStorage获取token并设置到axios header
const token = localStorage.getItem('token');
if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

new Vue({
    el: '#app',
    data: {
        username: '用户',
        currentUserInfo: {
            userId: null,
            username: '',
            avatarPath: '',
            roleId: null,
            registerTime: null
        },
        profileForm: {
            username: '',
            password: '',
            newPassword: '',
            confirmNewPassword: '',
            avatarPath: ''
        },
        profileMessage: null,
        isUpdating: false,
        avatarPreview: '', // 新增
        avatarFile: null   // 新增
    },
    mounted() {
        // 从localStorage获取用户名
        const storedUsername = localStorage.getItem('username');
        if (storedUsername) {
            this.username = storedUsername;
        }

        // 检查是否有token，没有则跳转到登录页面
        if (!token) {
            alert('请先登录！');
            window.location.href = 'test-login.html';
            return;
        }

        // 如果有token，加载用户详细信息
        this.loadUserInfo();
    },
    methods: {
        async submitForm() {
            // 验证表单
            if (!this.postForm.title) {
                this.$message.error('请输入标题');
                return;
            }
            if (!this.postForm.content) {
                this.$message.error('请输入内容');
                return;
            }

            try {
                // 创建FormData对象
                const formData = new FormData();
                formData.append('title', this.postForm.title);
                formData.append('content', this.postForm.content);

                // 如果有上传的文件，添加到formData中
                if (this.selectedFile) {
                    formData.append('file', this.selectedFile);
                }

                // 发送请求
                const response = await axios.post('/post/add', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                if (response.data.code === 200) {
                    this.$message.success('发布成功');
                    // 重置表单
                    this.postForm = {
                        title: '',
                        content: '',
                        plateId: 1
                    };
                    this.selectedFile = null;
                    this.fileUrl = '';

                    // 2秒后关闭对话框
                    setTimeout(() => {
                        this.dialogVisible = false;
                    }, 2000);
                } else {
                    this.$message.error('发布失败: ' + response.data.message);
                }
            } catch (error) {
                console.error('发布失败:', error);
                this.$message.error('发布失败: ' + error.message);
            }
        },

        // 处理文件选择
        handleFileChange(event) {
            const file = event.target.files[0];
            if (file) {
                this.selectedFile = file;
                // 预览图片
                const reader = new FileReader();
                reader.onload = (e) => {
                    this.fileUrl = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        },
        async loadUserInfo() {
            try {
                const response = await axios.get('/user/info');
                if (response.data && response.data.code === 200) {
                    this.currentUserInfo = response.data.data;
                    this.username = this.currentUserInfo.username;
                    console.log('用户信息加载成功:', this.currentUserInfo);
                } else {
                    console.error('获取用户信息失败:', response.data.msg);
                    alert('获取用户信息失败，请重新登录');
                    this.logout();
                }
            } catch (error) {
                console.error('加载用户信息失败:', error);
                alert('网络错误，请重新登录');
                this.logout();
            }
        },
        openProfile() {
            // 重置表单
            this.profileForm = {
                username: '',
                password: '',
                newPassword: '',
                confirmNewPassword: '',
                avatarPath: this.currentUserInfo.avatarPath || ''
            };
            this.profileMessage = null;

            // 显示模态框
            document.getElementById('profileModal').style.display = 'flex';
        },
        closeProfile() {
            document.getElementById('profileModal').style.display = 'none';
        },
        handleAvatarUpload(event) {
            const file = event.target.files[0];
            if (!file) return;
            this.avatarFile = file;
            // 预览
            this.avatarPreview = URL.createObjectURL(file);
        },
        async updateProfile() {
            this.isUpdating = true;
            this.profileMessage = null;

            try {
                // 验证表单
                if (this.profileForm.newPassword && this.profileForm.newPassword !== this.profileForm.confirmNewPassword) {
                    this.profileMessage = {
                        type: 'error',
                        text: '两次输入的新密码不一致'
                    };
                    this.isUpdating = false;
                    return;
                }

                if (this.profileForm.newPassword && this.profileForm.newPassword.length < 6) {
                    this.profileMessage = {
                        type: 'error',
                        text: '新密码长度至少6个字符'
                    };
                    this.isUpdating = false;
                    return;
                }

                if (this.profileForm.newPassword && !this.profileForm.password) {
                    this.profileMessage = {
                        type: 'error',
                        text: '修改密码时必须输入当前密码'
                    };
                    this.isUpdating = false;
                    return;
                }

                // 构建 FormData
                const formData = new FormData();
                if (this.profileForm.username) formData.append('username', this.profileForm.username);
                if (this.profileForm.password) formData.append('password', this.profileForm.password);
                if (this.profileForm.newPassword) formData.append('newPassword', this.profileForm.newPassword);
                if (this.avatarFile) formData.append('avatar', this.avatarFile);

                // 发送 multipart/form-data 请求
                const response = await axios.put('/user/update', formData, {
                    headers: { 'Content-Type': 'multipart/form-data' }
                });

                if (response.data && response.data.code === 200) {
                    alert("更新用户信息成功！")
                    this.profileMessage = { type: 'success', text: '用户信息更新成功！' };
                    if (this.profileForm.username) {
                        localStorage.setItem('username', this.profileForm.username);
                        this.username = this.profileForm.username;
                    }
                    await this.loadUserInfo();
                    setTimeout(() => { this.closeProfile(); }, 2000);
                } else {
                    alert(response.data.message)
                    this.profileMessage = { type: 'error', text: response.data.message || '更新失败' };
                }
            } catch (error) {
                this.profileMessage = { type: 'error', text: error.message || '更新失败' };
            } finally {
                this.isUpdating = false;
            }
        },
        formatDate(dateString) {
            if (!dateString) return '未知';
            const date = new Date(dateString);
            return date.toLocaleString('zh-CN');
        },
        logout() {
            // 清除本地存储的用户信息
            localStorage.removeItem('username');
            localStorage.removeItem('userRole');
            localStorage.removeItem('token');

            // 清除axios header
            delete axios.defaults.headers.common['Authorization'];

            // 跳转到登录页面
            window.location.href = 'login.html';
        },
        exploreForums() {
            // 返回论坛首页
            window.location.href = 'index.html';
        },
        goToDiscussions() {
            // 参与讨论，返回论坛首页
            window.location.href = 'index.html';
        },
        createPost() {
            // 打开发布帖子页面
            window.location.href = 'addpost.html';
        },
        postComment() {
            // 打开帖子详情页，这里使用一个示例帖子ID=1
            window.location.href = 'post.html?id=1';
        }
    }
});

// 点击模态框外部关闭
window.onclick = function(event) {
    const modal = document.getElementById('profileModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
}