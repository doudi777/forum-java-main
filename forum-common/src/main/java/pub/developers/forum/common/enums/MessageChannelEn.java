package pub.developers.forum.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@AllArgsConstructor
@Getter
public enum MessageChannelEn {
    STATION_LETTER("STATION_LETTER", "站内信"),
    MAIL("MAIL", "邮件")
    ;

    private String value;
    private String desc;

    public static MessageChannelEn getEntity(String value) {
        for (MessageChannelEn contentType : values()) {
            if (contentType.getValue().equals(value)) {
                return contentType;
            }
        }
        return null;
    }
}
