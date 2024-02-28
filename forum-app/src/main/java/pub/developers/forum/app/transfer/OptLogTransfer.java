package pub.developers.forum.app.transfer;

import org.springframework.util.ObjectUtils;
import pub.developers.forum.api.request.user.UserOptLogPageRequest;
import pub.developers.forum.common.enums.OptLogTypeEn;
import pub.developers.forum.domain.entity.OptLog;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public class OptLogTransfer {

    public static OptLog toOptLog(UserOptLogPageRequest request) {
        return OptLog.builder()
                .operatorId(request.getOperatorId())
                .type(ObjectUtils.isEmpty(request.getType()) ? null : OptLogTypeEn.getEntity(request.getType()))
                .build();
    }

}
