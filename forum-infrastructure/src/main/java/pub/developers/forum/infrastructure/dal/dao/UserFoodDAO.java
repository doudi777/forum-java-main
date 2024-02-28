package pub.developers.forum.infrastructure.dal.dao;

import org.apache.ibatis.annotations.Param;
import pub.developers.forum.infrastructure.dal.dataobject.UserFoodDO;

import java.util.List;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface UserFoodDAO {

    void insert(UserFoodDO userFoodDO);

    List<UserFoodDO> query(@Param("userId") Long userId);

    void deleteByPostsId(@Param("postsId") Long postsId);
}
