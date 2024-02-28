package pub.developers.forum.domain.repository;

import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.entity.Faq;
import pub.developers.forum.domain.entity.value.PostsPageQueryValue;

import java.util.List;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public interface ArticleRepository {

    void save(Article article);

    Article get(Long id);

    void update(Article article);

    PageResult<Article> page(Integer pageNo, Integer pageSize, PostsPageQueryValue pageQueryValue);
    List<Article> recommends(int size);

}
