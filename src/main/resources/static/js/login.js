// 设置axios基础URL
axios.defaults.baseURL = 'http://localhost:8091';

// 检查是否从注册页面跳转过来
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('from') === 'register') {
        document.getElementById('registrationSuccess').style.display = 'block';
    }
});

// 登录表单处理
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (!username || !password) {
        alert('❌ 请填写完整的登录信息');
        return;
    }

    try {
        // 调用真正的后端登录API
        const response = await axios.post('/user/login', {
            username: username,
            password: password
        });

        if (response.data && response.data.code === 200) {
            const loginData = response.data.data;

            if (loginData && loginData.token) {
                // 保存JWT令牌到localStorage
                localStorage.setItem('token', loginData.token);
                localStorage.setItem('username', loginData.username);
                localStorage.setItem('userRole', loginData.roleId);

                // 设置axios默认header
                axios.defaults.headers.common['Authorization'] = `Bearer ${loginData.token}`;

                // 登录成功
                alert('🎉 登录成功！即将跳转到主页...');

                // 跳转到主页
                setTimeout(() => {
                    window.location.href = `home.html?username=${encodeURIComponent(loginData.username)}`;
                }, 1000);
            } else {
                alert('❌ 登录失败：服务器返回数据格式错误');
            }
        } else {
            alert('❌ 登录失败：' + (response.data?.message || '未知错误'));
        }

    } catch (error) {
        console.error('登录错误:', error);
        let errorMessage = '登录失败';

        if (error.response) {
            errorMessage = error.response.data?.message || `服务器错误 (${error.response.status})`;
        } else if (error.request) {
            errorMessage = '网络错误，无法连接到服务器';
        } else {
            errorMessage = error.message;
        }

        alert('❌ ' + errorMessage);
    }
});