package pub.developers.forum.api.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Data
public class UserBaseLoginRequest implements Serializable {

    private String ua;

    private String ip;

}
