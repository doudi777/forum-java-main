package pub.developers.forum.domain.service;

import pub.developers.forum.domain.entity.Message;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface MessageService {

    void send(Message message);

}
