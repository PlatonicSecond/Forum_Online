// 设置axios基础URL
axios.defaults.baseURL = 'http://localhost:8080';

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
        isUpdating: false
    },
    mounted() {
        // 从URL参数获取用户名
        const urlParams = new URLSearchParams(window.location.search);
        const usernameParam = urlParams.get('username');
        if (usernameParam) {
            this.username = usernameParam;
        }

        // 或者从localStorage获取
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
        async loadUserInfo() {
            try {
                const response = await axios.get('/user/info');
                if (response.data && response.data.code === 200) {
                    this.currentUserInfo = response.data.data;
                    this.username = this.currentUserInfo.username;
                    console.log('用户信息加载成功:', this.currentUserInfo);
                }
            } catch (error) {
                console.error('加载用户信息失败:', error);
            }
        },
        openProfile() {
            // 检查是否有token
            if (!token) {
                alert('请先登录！');
                window.location.href = 'test-login.html';
                return;
            }

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
            document.getElementById('profileModal').style.display = 'block';
        },
        closeProfile() {
            document.getElementById('profileModal').style.display = 'none';
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

                // 发送更新请求
                const response = await axios.put('/user/update', this.profileForm);

                if (response.data && response.data.code === 200) {
                    this.profileMessage = {
                        type: 'success',
                        text: '用户信息更新成功！'
                    };

                    // 重新加载用户信息
                    await this.loadUserInfo();

                    // 2秒后关闭模态框
                    setTimeout(() => {
                        this.closeProfile();
                    }, 2000);

                } else {
                    this.profileMessage = {
                        type: 'error',
                        text: response.data?.message || '更新失败'
                    };
                }

            } catch (error) {
                console.error('更新用户信息失败:', error);
                this.profileMessage = {
                    type: 'error',
                    text: error.response?.data?.message || '更新失败，请检查输入信息'
                };
            }

            this.isUpdating = false;
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
            window.location.href = 'index.html';
        },
        goToDiscussions() {
            alert('💬 讨论区功能即将开发！');
        },
        createPost() {
            alert('📝 发布帖子功能即将开发！');
        },
        findFriends() {
            alert('👥 社交功能即将开发！');
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