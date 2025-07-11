// 设置 Axios 基础配置
axios.defaults.baseURL = 'http://localhost:8080'; // 根据你的后端地址修改
axios.defaults.withCredentials = true;

// 从 localStorage 获取 token 并设置到请求头
const token = localStorage.getItem('token');
if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// Vue 实例
new Vue({
    el: '#app',
    data: {
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
        avatarPreview: '',
        avatarFile: null
    },
    mounted() {
        // 页面加载时获取用户信息
        this.loadUserInfo();
    },
    methods: {
        // 加载用户信息
        async loadUserInfo() {
            try {
                const response = await axios.get('/user/info');
                if (response.data && response.data.code === 200) {
                    this.currentUserInfo = response.data.data;
                } else {
                    alert('获取用户信息失败，请重新登录');
                    this.logout();
                }
            } catch (error) {
                console.error('加载用户信息失败:', error);
                alert('网络错误，请重新登录');
                this.logout();
            }
        },

        // 处理头像上传
        handleAvatarUpload(event) {
            const file = event.target.files[0];
            if (!file) return;
            this.avatarFile = file;
            this.avatarPreview = URL.createObjectURL(file);
        },

        // 更新用户信息
        async updateProfile() {
            this.isUpdating = true;
            this.profileMessage = null;

            try {
                // 表单验证
                if (this.profileForm.newPassword && this.profileForm.newPassword !== this.profileForm.confirmNewPassword) {
                    this.profileMessage = { type: 'error', text: '两次输入的新密码不一致' };
                    this.isUpdating = false;
                    return;
                }

                if (this.profileForm.newPassword && this.profileForm.newPassword.length < 6) {
                    this.profileMessage = { type: 'error', text: '新密码长度至少6个字符' };
                    this.isUpdating = false;
                    return;
                }

                if (this.profileForm.newPassword && !this.profileForm.password) {
                    this.profileMessage = { type: 'error', text: '修改密码时必须输入当前密码' };
                    this.isUpdating = false;
                    return;
                }

                // 构建 FormData 对象
                const formData = new FormData();
                if (this.profileForm.username) {
                    formData.append('username', this.profileForm.username);
                }
                if (this.profileForm.password) {
                    formData.append('password', this.profileForm.password);
                }
                if (this.profileForm.newPassword) {
                    formData.append('newPassword', this.profileForm.newPassword);
                }
                if (this.avatarFile) {
                    formData.append('avatar', this.avatarFile);
                }

                // 发送请求到后端
                const response = await axios.put('/user/update', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                // 处理响应
                if (response.data && response.data.code === 200) {
                    this.profileMessage = { type: 'success', text: '用户信息更新成功！' };

                    // 如果修改了用户名，更新本地存储
                    if (this.profileForm.username) {
                        localStorage.setItem('username', this.profileForm.username);
                        this.currentUserInfo.username = this.profileForm.username;
                    }

                    // 重新加载用户信息
                    await this.loadUserInfo();

                    // 2 秒后关闭模态框
                    setTimeout(() => {
                        this.closeProfile();
                    }, 2000);
                } else {
                    this.profileMessage = { type: 'error', text: response.data.message || '更新失败' };
                }
            } catch (error) {
                console.error('更新用户信息失败:', error);
                this.profileMessage = { type: 'error', text: error.response?.data?.message || '更新失败' };
            } finally {
                this.isUpdating = false;
            }
        },

        // 关闭模态框
        closeProfile() {
            document.getElementById('profileModal').style.display = 'none';
        },

        // 格式化日期
        formatDate(dateString) {
            if (!dateString) return '未知';
            const date = new Date(dateString);
            return date.toLocaleString('zh-CN');
        },

        // 退出登录
        logout() {
            localStorage.removeItem('username');
            localStorage.removeItem('token');
            delete axios.defaults.headers.common['Authorization'];
            window.location.href = 'login.html';
        }
    }
});

// 点击模态框外部关闭
window.onclick = function(event) {
    const modal = document.getElementById('profileModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};