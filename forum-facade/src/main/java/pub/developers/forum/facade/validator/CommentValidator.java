package pub.developers.forum.facade.validator;

import pub.developers.forum.api.request.comment.CommentCreateRequest;
import pub.developers.forum.common.support.CheckUtil;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public class CommentValidator {

    public static void create(CommentCreateRequest request) {
        CheckUtil.checkParamToast(request, "request");
        CheckUtil.checkParamToast(request.getPostsId(), "postsId");
        CheckUtil.checkParamToast(request.getContent(), "content");
    }
}
