package pub.developers.forum.api.request.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pub.developers.forum.api.request.user.UserBaseLoginRequest;

import java.io.Serializable;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GithubOauthLoginRequest extends UserBaseLoginRequest implements Serializable {

    private String code;
}
