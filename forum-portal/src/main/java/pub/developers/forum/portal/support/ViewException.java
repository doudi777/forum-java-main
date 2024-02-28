package pub.developers.forum.portal.support;

import lombok.Data;

/**
 * @author xiongben
 * @create 23/12/23
 * @desc
 **/
@Data
public class ViewException extends RuntimeException {

    private String message;

    public static ViewException build(String message) {
        ViewException viewException = new ViewException();
        viewException.setMessage(message);
        return viewException;
    }

}
