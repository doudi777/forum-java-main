package pub.developers.forum.domain.entity;

import lombok.*;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc 文章点赞/问答关注
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Approval extends BaseEntity {

    private Long postsId;

    private Long userId;

}
