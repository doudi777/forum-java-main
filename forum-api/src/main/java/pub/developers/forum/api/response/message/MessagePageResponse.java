package pub.developers.forum.api.response.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePageResponse implements Serializable {

    private Long id;

    private String read;

    private String sender;

    private String senderAvatar;

    private String senderName;

    private String typeDesc;

    private String title;

    private String infoId;

    private Date createAt;

}
