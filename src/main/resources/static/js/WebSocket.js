// WebSocket连接配置
let stompClient = null;

function connectWebSocket() {
    // 创建SockJS连接
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    // 连接WebSocket服务器
    stompClient.connect({}, function(frame) {
        console.log('WebSocket连接成功: ' + frame);

        // 获取当前帖子ID（从页面URL或隐藏字段获取）
        const postId = document.getElementById('postId').value;

        // 订阅该帖子的评论主题
        stompClient.subscribe(`/topic/comments/${postId}`, function(message) {
            // 收到新评论消息，更新页面
            const comment = JSON.parse(message.body);
            appendComment(comment);
        });
    }, function(error) {
        console.error('WebSocket连接失败: ' + error);
        // 可以添加重连逻辑
    });
}

// 发送评论到服务器
function sendComment(comment) {
    if (stompClient) {
        stompClient.send("/app/addComment", {}, JSON.stringify(comment));
    }
}

// 页面加载完成后连接WebSocket
document.addEventListener('DOMContentLoaded', connectWebSocket);