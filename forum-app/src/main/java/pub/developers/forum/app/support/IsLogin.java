package pub.developers.forum.app.support;

import pub.developers.forum.common.enums.UserRoleEn;

import java.lang.annotation.*;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IsLogin {

    /**
     * 登录角色
     * @return
     */
    UserRoleEn role() default UserRoleEn.USER;

}