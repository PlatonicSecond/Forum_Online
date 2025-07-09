// 设置axios基础URL
axios.defaults.baseURL = 'http://localhost:8091';

// 当前用户token
let currentToken = localStorage.getItem('token');

// 如果有token，设置到axios header
if (currentToken) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
    document.getElementById('tokenDisplay').style.display = 'block';
    document.getElementById('tokenContent').textContent = currentToken;
}

// 登录表单处理
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await axios.post('/user/login', {
            username: username,
            password: password
        });

        console.log('服务器响应:', response);
        console.log('响应数据:', response.data);

        if (response.data && response.data.code === 200) {
            const loginData = response.data.data;
            console.log('登录数据:', loginData);

            if (loginData && loginData.token) {
                currentToken = loginData.token;

                // 保存token到localStorage
                localStorage.setItem('token', currentToken);

                // 设置axios默认header
                axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;

                // 显示成功信息
                document.getElementById('loginResult').innerHTML = `
                            <div class="success-box">
                                <strong>🎉 登录成功！</strong><br>
                                用户名: ${loginData.username || '未知'}<br>
                                角色: ${loginData.roleName || '未知'}<br>
                                角色ID: ${loginData.roleId || '未知'}
                            </div>
                        `;

                // 显示token
                document.getElementById('tokenDisplay').style.display = 'block';
                document.getElementById('tokenContent').textContent = currentToken;
            } else {
                document.getElementById('loginResult').innerHTML = `
                            <div class="error-box">
                                <strong>❌ 登录数据错误</strong><br>
                                服务器返回的数据格式不正确<br>
                                返回数据: ${JSON.stringify(response.data)}
                            </div>
                        `;
            }

        } else {
            document.getElementById('loginResult').innerHTML = `
                        <div class="error-box">
                            <strong>❌ 登录失败</strong><br>
                            ${response.data?.message || '未知错误'}<br>
                            完整响应: ${JSON.stringify(response.data)}
                        </div>
                    `;
        }

    } catch (error) {
        console.error('登录错误详情:', error);

        let errorMessage = '未知错误';
        if (error.response) {
            // 服务器返回了错误响应
            errorMessage = `状态码: ${error.response.status}<br>`;
            errorMessage += `错误信息: ${error.response.data?.message || error.response.statusText}<br>`;
            errorMessage += `完整响应: ${JSON.stringify(error.response.data)}`;
        } else if (error.request) {
            // 请求已发出，但没有收到响应
            errorMessage = '网络错误: 无法连接到服务器<br>';
            errorMessage += `请求详情: ${error.request}`;
        } else {
            // 其他错误
            errorMessage = `请求设置错误: ${error.message}`;
        }

        document.getElementById('loginResult').innerHTML = `
                    <div class="error-box">
                        <strong>❌ 登录错误</strong><br>
                        ${errorMessage}
                    </div>
                `;
    }
});

// 退出登录
function logout() {
    currentToken = null;
    localStorage.removeItem('token');
    delete axios.defaults.headers.common['Authorization'];

    document.getElementById('loginResult').innerHTML = `
                <div class="info-box">
                    <strong>👋 已退出登录</strong>
                </div>
            `;

    document.getElementById('tokenDisplay').style.display = 'none';
    document.getElementById('apiResult').innerHTML = '';
}

// 验证JWT和ThreadLocal
async function testJwtAndThreadLocal() {
    if (!currentToken) {
        document.getElementById('apiResult').innerHTML = `
                    <div class="error-box">
                        <strong>❌ 请先登录</strong>
                    </div>
                `;
        return;
    }

    try {
        const response = await axios.get('/user/verify');

        document.getElementById('apiResult').innerHTML = `
                    <div class="success-box">
                        <strong>✅ JWT和ThreadLocal验证成功</strong><br>
                        <pre style="white-space: pre-wrap; margin-top: 10px;">${response.data.data}</pre>
                    </div>
                `;

    } catch (error) {
        document.getElementById('apiResult').innerHTML = `
                    <div class="error-box">
                        <strong>❌ 验证失败</strong><br>
                        ${error.response?.data?.message || error.message}
                    </div>
                `;
    }
}

// 测试创建帖子（需要登录）
async function testCreatePost() {
    if (!currentToken) {
        document.getElementById('apiResult').innerHTML = `
                    <div class="error-box">
                        <strong>❌ 请先登录</strong>
                    </div>
                `;
        return;
    }

    try {
        const response = await axios.post('/post/create', {
            content: '这是一个测试帖子内容',
            plateId: 1
        });

        document.getElementById('apiResult').innerHTML = `
                    <div class="success-box">
                        <strong>✅ 创建帖子测试成功</strong><br>
                        ${response.data.message}<br>
                        <small>这证明JWT拦截器和ThreadLocal用户上下文正常工作</small>
                    </div>
                `;

    } catch (error) {
        document.getElementById('apiResult').innerHTML = `
                    <div class="error-box">
                        <strong>❌ 创建帖子失败</strong><br>
                        ${error.response?.data?.message || error.message}
                    </div>
                `;
    }
}

// 测试获取帖子（需要登录）
async function testGetPosts() {
    if (!currentToken) {
        document.getElementById('apiResult').innerHTML = `
                    <div class="error-box">
                        <strong>❌ 请先登录</strong>
                    </div>
                `;
        return;
    }

    try {
        const response = await axios.get('/post/select?id=1');

        document.getElementById('apiResult').innerHTML = `
                    <div class="success-box">
                        <strong>✅ 获取帖子测试成功</strong><br>
                        获取到 ${response.data.data.length} 条帖子数据<br>
                        <small>这证明JWT拦截器验证通过</small>
                    </div>
                `;

    } catch (error) {
        document.getElementById('apiResult').innerHTML = `
                    <div class="error-box">
                        <strong>❌ 获取帖子失败</strong><br>
                        ${error.response?.data?.message || error.message}
                    </div>
                `;
    }
}

// 测试无令牌请求
async function testWithoutToken() {
    try {
        // 临时移除token
        const tempToken = axios.defaults.headers.common['Authorization'];
        delete axios.defaults.headers.common['Authorization'];

        const response = await axios.get('/post/select?id=1');

        // 恢复token
        if (tempToken) {
            axios.defaults.headers.common['Authorization'] = tempToken;
        }

        document.getElementById('apiResult').innerHTML = `
                    <div class="error-box">
                        <strong>⚠️ 意外情况</strong><br>
                        无令牌请求竟然成功了，拦截器可能没有正常工作
                    </div>
                `;

    } catch (error) {
        // 恢复token
        if (currentToken) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
        }

        if (error.response?.status === 401) {
            document.getElementById('apiResult').innerHTML = `
                        <div class="success-box">
                            <strong>✅ 拦截器测试成功</strong><br>
                            无令牌请求被正确拦截（401错误）<br>
                            <small>这证明JWT拦截器正常工作</small>
                        </div>
                    `;
        } else {
            document.getElementById('apiResult').innerHTML = `
                        <div class="error-box">
                            <strong>❌ 测试失败</strong><br>
                            ${error.response?.data?.message || error.message}
                        </div>
                    `;
        }
    }
}