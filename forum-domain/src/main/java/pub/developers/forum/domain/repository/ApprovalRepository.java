package pub.developers.forum.domain.repository;

import pub.developers.forum.domain.entity.Approval;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface ApprovalRepository {

    void save(Approval approval);

    void delete(Long approvalId);

    Approval get(Long postsId, Long userId);

}
