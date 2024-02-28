package pub.developers.forum.api.service;

import pub.developers.forum.api.model.ResultModel;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface ApprovalApiService {

    ResultModel<Long> create(Long postsId);

    ResultModel<Long> delete(Long postsId);

    ResultModel<Boolean> hasApproval(Long postsId);

}
