package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Getter
@AllArgsConstructor
public enum SearchTypeEn {
    POSTS("POSTS", "帖子"),
    ;

    private String value;
    private String desc;
}
