package pub.developers.forum.portal.request;

import lombok.Data;

/**
 * @author xiongben
 * @create 23/12/23
 * @desc
 **/
@Data
public class IndexRequest extends BasePageRequest {

    private String type;

    private String toast;

    private String token;
}
