package pub.developers.forum.api.service;

import pub.developers.forum.api.model.ResultModel;
import pub.developers.forum.api.request.github.GithubOauthLoginRequest;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface GithubApiService {

    ResultModel<String> oauthLogin(GithubOauthLoginRequest request);

}
