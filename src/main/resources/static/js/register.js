// 设置axios基础URL
axios.defaults.baseURL = 'http://localhost:8091';


// 表单元素
const registerForm = document.getElementById('registerForm');
const usernameInput = document.getElementById('username');
const passwordInput = document.getElementById('password');
const confirmPasswordInput = document.getElementById('confirmPassword');
const registerBtn = document.getElementById('registerBtn');

// 错误消息元素
const usernameError = document.getElementById('usernameError');
const passwordError = document.getElementById('passwordError');
const confirmPasswordError = document.getElementById('confirmPasswordError');
const passwordStrength = document.getElementById('passwordStrength');

// 密码强度检查
function checkPasswordStrength(password) {
    if (password.length < 6) {
        passwordStrength.textContent = '密码太短';
        passwordStrength.className = 'password-strength weak';
        return;
    }

    let strength = 0;
    if (password.length >= 8) strength++;
    if (/[a-z]/.test(password)) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;

    if (strength <= 2) {
        passwordStrength.textContent = '密码强度：弱';
        passwordStrength.className = 'password-strength weak';
    } else if (strength <= 3) {
        passwordStrength.textContent = '密码强度：中等';
        passwordStrength.className = 'password-strength medium';
    } else {
        passwordStrength.textContent = '密码强度：强';
        passwordStrength.className = 'password-strength strong';
    }
}

// 显示错误消息
function showError(element, message) {
    element.textContent = message;
    element.style.display = 'block';
    element.parentElement.querySelector('input').classList.add('error');
}

// 隐藏错误消息
function hideError(element) {
    element.style.display = 'none';
    element.parentElement.querySelector('input').classList.remove('error');
}

// 验证用户名
function validateUsername() {
    const username = usernameInput.value.trim();
    if (!username) {
        showError(usernameError, '用户名不能为空');
        return false;
    }
    if (username.length < 3) {
        showError(usernameError, '用户名至少3个字符');
        return false;
    }
    if (username.length > 50) {
        showError(usernameError, '用户名最多50个字符');
        return false;
    }
    hideError(usernameError);
    return true;
}

// 验证密码
function validatePassword() {
    const password = passwordInput.value;
    if (!password) {
        showError(passwordError, '密码不能为空');
        return false;
    }
    if (password.length < 6) {
        showError(passwordError, '密码至少6个字符');
        return false;
    }
    hideError(passwordError);
    return true;
}

// 验证确认密码
function validateConfirmPassword() {
    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;
    if (!confirmPassword) {
        showError(confirmPasswordError, '请确认密码');
        return false;
    }
    if (password !== confirmPassword) {
        showError(confirmPasswordError, '两次输入的密码不一致');
        return false;
    }
    hideError(confirmPasswordError);
    return true;
}

// 事件监听器
usernameInput.addEventListener('blur', validateUsername);
passwordInput.addEventListener('input', function() {
    checkPasswordStrength(this.value);
    if (this.value) validatePassword();
});
passwordInput.addEventListener('blur', validatePassword);
confirmPasswordInput.addEventListener('input', validateConfirmPassword);
confirmPasswordInput.addEventListener('blur', validateConfirmPassword);

// 注册表单提交
registerForm.addEventListener('submit', async function(e) {
    e.preventDefault();

    // 验证所有字段
    const isUsernameValid = validateUsername();
    const isPasswordValid = validatePassword();
    const isConfirmPasswordValid = validateConfirmPassword();

    if (!isUsernameValid || !isPasswordValid || !isConfirmPasswordValid) {
        return;
    }

    const username = usernameInput.value.trim();
    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;

    // 禁用提交按钮
    registerBtn.disabled = true;
    registerBtn.textContent = '注册中...';

    try {
        // 调用后端注册API
        const response = await axios.post('/user/register', {
            username: username,
            password: password,
            confirmPassword: confirmPassword,
            roleId: 1 // 默认为普通用户
        });

        if (response.data && response.data.code === 200) {
            alert('🎉 注册成功！即将跳转到登录页面...');

            // 跳转到登录页面，并添加来源参数
            setTimeout(() => {
                window.location.href = 'login.html?from=register';
            }, 1000);
        } else {
            alert('❌ 注册失败：' + (response.data?.message || '未知错误'));
        }

    } catch (error) {
        console.error('注册错误:', error);
        let errorMessage = '注册失败';

        if (error.response) {
            errorMessage = error.response.data?.message || `服务器错误 (${error.response.status})`;
        } else if (error.request) {
            errorMessage = '网络错误，无法连接到服务器';
        } else {
            errorMessage = error.message;
        }

        alert('❌ ' + errorMessage);
    } finally {
        // 恢复提交按钮
        registerBtn.disabled = false;
        registerBtn.textContent = '注册';
    }
});