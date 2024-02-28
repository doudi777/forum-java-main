package pub.developers.forum.infrastructure.dal.dataobject;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> resp = new BaseResponse<>();
        resp.setData(data);
        resp.setCode(HttpStatus.OK.value());
        return resp;
    }

    public static <T> BaseResponse<T> error(int errorCode, String message) {
        BaseResponse<T> resp = new BaseResponse<>();
        resp.setCode(errorCode);
        resp.setMessage(message);
        return resp;
    }

}