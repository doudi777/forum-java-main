package pub.developers.forum.infrastructure.dal.dao;

import pub.developers.forum.infrastructure.dal.dataobject.OptLogDO;

import java.util.List;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface OptLogDAO {

    void insert(OptLogDO optLogDO);

    List<OptLogDO> query(OptLogDO optLogDO);
}
