package pub.developers.forum.domain.repository;

import pub.developers.forum.common.enums.FollowedTypeEn;

import java.util.List;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface UserFollowRepository {

    List<Long> getAllFollowerIds(Long follower, FollowedTypeEn type);
}
