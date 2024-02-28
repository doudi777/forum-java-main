package pub.developers.forum.facade.validator;

import pub.developers.forum.api.request.message.MessagePageRequest;
import pub.developers.forum.common.enums.MessageTypeEn;
import pub.developers.forum.common.support.CheckUtil;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public class MessageValidator {

    public static void page(MessagePageRequest request) {
        CheckUtil.checkParamToast(request, "request");
        CheckUtil.checkParamToast(request.getTypeDesc(), "typeDesc");
        CheckUtil.checkParamToast(MessageTypeEn.getEntityByDesc(request.getTypeDesc()), "不存在的消息类型");
    }
}
