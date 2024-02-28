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
public enum PostsCategoryEn {
    ARTICLE("ARTICLE", "文章"),
    FAQ("FAQ", "问答"),
    ;
    private String value;
    private String desc;
}
