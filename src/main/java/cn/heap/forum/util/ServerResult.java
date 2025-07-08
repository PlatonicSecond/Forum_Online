package cn.heap.forum.util;

import lombok.Data;

@Data
public class ServerResult<T> {
    private Integer code;
    private String message;
    private T data;  // 修正字段名

    public static <T> ServerResult<T> success(T data){
        ServerResult<T> result = new ServerResult<>();
        result.setCode(200);
        result.setMessage("OK");
        result.setData(data);  // 修正方法名
        return result;
    }

    public static <T> ServerResult<T> success(){
        return success(null);
    }

    public static <T> ServerResult<T> error(Integer code, String message){
        ServerResult<T> result = new ServerResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}

