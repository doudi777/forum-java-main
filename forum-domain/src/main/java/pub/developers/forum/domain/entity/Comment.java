package pub.developers.forum.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc 帖子评论
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

    /**
     * 评论人ID
     */
    private Long userId;

    /**
     * 二次评论人ID
     */
    private Long replyId;

    /**
     * 二次评论人ID
     */
    private Long replyReplyId;

    /**
     * 帖子ID
     */
    private Long postsId;

    /**
     * 内容
     */
    private String content;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getReplyReplyId() {
        return replyReplyId;
    }

    public void setReplyReplyId(Long replyReplyId) {
        this.replyReplyId = replyReplyId;
    }

    public Long getPostsId() {
        return postsId;
    }

    public void setPostsId(Long postsId) {
        this.postsId = postsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
