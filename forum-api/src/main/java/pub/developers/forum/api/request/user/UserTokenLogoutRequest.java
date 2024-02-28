package pub.developers.forum.api.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenLogoutRequest extends UserBaseLoginRequest {

    private String token;

}