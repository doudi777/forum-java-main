package pub.developers.forum.domain.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface GithubService {

    JSONObject getUserInfo(String code);

}
