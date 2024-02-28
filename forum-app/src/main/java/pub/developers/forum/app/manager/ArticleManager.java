package pub.developers.forum.app.manager;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import pub.developers.forum.api.model.PageRequestModel;
import pub.developers.forum.api.model.PageResponseModel;
import pub.developers.forum.api.request.AdminBooleanRequest;
import pub.developers.forum.api.request.article.*;
import pub.developers.forum.api.response.article.ArticleInfoResponse;
import pub.developers.forum.api.response.article.ArticleQueryTypesResponse;
import pub.developers.forum.api.response.article.ArticleRecommendsResponse;
import pub.developers.forum.api.response.article.ArticleUserPageResponse;
import pub.developers.forum.api.response.faq.FaqHotsResponse;
import pub.developers.forum.app.support.IsLogin;
import pub.developers.forum.app.support.LoginUserContext;
import pub.developers.forum.app.support.PageUtil;
import pub.developers.forum.app.support.Pair;
import pub.developers.forum.app.transfer.ArticleTransfer;
import pub.developers.forum.app.transfer.FaqTransfer;
import pub.developers.forum.common.enums.*;
import pub.developers.forum.common.model.PageResult;
import pub.developers.forum.common.support.CheckUtil;
import pub.developers.forum.common.support.EventBus;
import pub.developers.forum.domain.entity.*;
import pub.developers.forum.domain.entity.value.PostsPageQueryValue;
import pub.developers.forum.domain.repository.*;
import pub.developers.forum.domain.service.CacheService;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
@Slf4j
@Component
public class ArticleManager extends AbstractPostsManager {

    @Resource
    private ArticleTypeRepository articleTypeRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserFollowRepository userFollowRepository;

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private PostsRepository postsRepository;

    @Resource
    private ArticleRepository articleRepository;

    private static final Long INIT_SORT = 1000L;

    @IsLogin
    @Transactional
    public Long saveArticle(ArticleSaveArticleRequest request) {
        // 创建
        if (ObjectUtils.isEmpty(request.getId())) {
            // 校验类型
            ArticleType articleType = checkArticleType(request.getTypeId());

            // 校验标签
            Set<Tag> selectTags = checkTags(request.getTagIds());

            Article article = ArticleTransfer.toArticle(request, articleType, selectTags, false, INIT_SORT);
            articleRepository.save(article);

            // 触发文章创建事件
            EventBus.emit(EventBus.Topic.ARTICLE_CREATE, article);

            return article.getId();
        }

        // 更新
        // 校验文章
        Article article = articleRepository.get(request.getId());
        CheckUtil.isEmpty(article, ErrorCodeEn.ARTICLE_NOT_EXIST);
        CheckUtil.isFalse(LoginUserContext.getUser().getId().equals(article.getAuthor().getId()), ErrorCodeEn.ARTICLE_NOT_EXIST);

        // 校验类型
        ArticleType articleType = checkArticleType(request.getTypeId());

        // 校验标签
        Set<Tag> selectTags = checkTags(request.getTagIds());

        // 删除旧标签关联关系
        tagRepository.deletePostsMapping(request.getId());

        Article oldArticle = article.copy();
        Article newArticle = ArticleTransfer.toArticle(request, articleType, selectTags, true, INIT_SORT);

        articleRepository.update(newArticle);

        // 触发文章更新事件
        EventBus.emit(EventBus.Topic.ARTICLE_UPDATE, Pair.build(oldArticle, newArticle));

        return request.getId();
    }

    public List<ArticleQueryTypesResponse> queryAllTypes() {
        List<ArticleType> articleTypes = articleTypeRepository.queryByState(AuditStateEn.PASS);

        return ArticleTransfer.toArticleQueryTypesResponses(articleTypes);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public List<ArticleQueryTypesResponse> queryAdminTypes() {
        List<ArticleType> articleTypes = articleTypeRepository.queryByState(null);

        return ArticleTransfer.toArticleQueryTypesResponses(articleTypes);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public PageResponseModel<ArticleQueryTypesResponse> typePage(PageRequestModel<ArticleAdminTypePageRequest> pageRequestModel) {
        ArticleAdminTypePageRequest typePageRequest = pageRequestModel.getFilter();
        ArticleType articleType = ArticleType.builder()
                .name(typePageRequest.getName())
                .description(typePageRequest.getDescription())
                .build();
        if (!ObjectUtils.isEmpty(typePageRequest.getAuditState())) {
            articleType.setAuditState(AuditStateEn.getEntity(typePageRequest.getAuditState()));
        }
        if (!ObjectUtils.isEmpty(typePageRequest.getScope())) {
            articleType.setScope(ArticleTypeScopeEn.getEntity(typePageRequest.getScope()));
        }
        PageResult<ArticleType> pageResult = articleTypeRepository.page(PageUtil.buildPageRequest(pageRequestModel, articleType));

        return PageResponseModel.build(pageResult.getTotal(), pageResult.getSize(), ArticleTransfer.toArticleQueryTypesResponses(pageResult.getList()));
    }

    @IsLogin
    public List<ArticleQueryTypesResponse> queryEditArticleTypes() {
        List<ArticleTypeScopeEn> scopes = new ArrayList<>();
        scopes.add(ArticleTypeScopeEn.USER);

        User loginUser = LoginUserContext.getUser();
        if (!UserRoleEn.USER.equals(loginUser.getRole() )) {
            scopes.add(ArticleTypeScopeEn.OFFICIAL);
        }

        List<ArticleType> articleTypes = articleTypeRepository.queryByScopesAndState(scopes, AuditStateEn.PASS);

        return ArticleTransfer.toArticleQueryTypesResponses(articleTypes);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void addType(ArticleAddTypeRequest request) {
        CheckUtil.isNotEmpty(articleTypeRepository.query(ArticleType.builder()
                .name(request.getName())
                .build()), ErrorCodeEn.ARTICLE_TYPE_IS_EXIST);

        articleTypeRepository.save(ArticleTransfer.toArticleType(request));
    }


    public PageResponseModel<ArticleUserPageResponse> userPage(PageRequestModel<ArticleUserPageRequest> pageRequestModel) {
        PostsPageQueryValue pageQueryValue = PostsPageQueryValue.builder()
                .auditStates(Arrays.asList(AuditStateEn.PASS.getValue()))
                .build();

        ArticleUserPageRequest pageRequest = pageRequestModel.getFilter();
        if (!ObjectUtils.isEmpty(pageRequest.getTypeName())) {
            ArticleType articleType = articleTypeRepository.getByNameAndState(pageRequest.getTypeName(), AuditStateEn.PASS);
            CheckUtil.isEmpty(articleType, ErrorCodeEn.ARTICLE_TYPE_IS_EXIST);
            pageQueryValue.setTypeId(articleType.getId());
        }
        return pageQuery(pageRequestModel, pageQueryValue);
    }

    public PageResponseModel<ArticleUserPageResponse> authorPage(PageRequestModel<ArticleAuthorPageRequest> pageRequestModel) {
        ArticleAuthorPageRequest request = pageRequestModel.getFilter();

        List<String> auditStates = new ArrayList<>();
        auditStates.add(AuditStateEn.PASS.getValue());

        User user = LoginUserContext.getUser();
        if (!ObjectUtils.isEmpty(user) && user.getId().equals(request.getUserId())) {
            auditStates.add(AuditStateEn.WAIT.getValue());
            auditStates.add(AuditStateEn.REJECT.getValue());
        }

        PostsPageQueryValue pageQueryValue = PostsPageQueryValue.builder()
                .auditStates(auditStates)
                .authorId(request.getUserId())
                .build();

        return pageQuery(pageRequestModel, pageQueryValue);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public PageResponseModel<ArticleUserPageResponse> adminPage(PageRequestModel<ArticleAdminPageRequest> pageRequestModel) {
        ArticleAdminPageRequest request = pageRequestModel.getFilter();

        PostsPageQueryValue pageQueryValue = PostsPageQueryValue.builder()
                .category(PostsCategoryEn.ARTICLE.getValue())
                .authorId(request.getUserId())
                .typeId(request.getTypeId())
                .title(request.getTitle())
                .official(request.getOfficial())
                .marrow(request.getMarrow())
                .top(request.getTop())
                .build();
        if (!ObjectUtils.isEmpty(request.getAuditState()) && !ObjectUtils.isEmpty(AuditStateEn.getEntity(request.getAuditState()))) {
            List<String> auditStates = new ArrayList<>();
            auditStates.add(AuditStateEn.getEntity(request.getAuditState()).getValue());
            pageQueryValue.setAuditStates(auditStates);
        }

        return pageQuery(pageRequestModel, pageQueryValue);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void adminTop(AdminBooleanRequest booleanRequest) {
        BasePosts basePosts = postsRepository.get(booleanRequest.getId());
        CheckUtil.isEmpty(basePosts, ErrorCodeEn.ARTICLE_NOT_EXIST);

        basePosts.setSort(booleanRequest.getIs() ? 1L : INIT_SORT);
        basePosts.setTop(booleanRequest.getIs());
        postsRepository.update(basePosts);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void adminOfficial(AdminBooleanRequest booleanRequest) {
        BasePosts basePosts = postsRepository.get(booleanRequest.getId());
        CheckUtil.isEmpty(basePosts, ErrorCodeEn.ARTICLE_NOT_EXIST);

        basePosts.setOfficial(booleanRequest.getIs());
        postsRepository.update(basePosts);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void adminMarrow(AdminBooleanRequest booleanRequest) {
        BasePosts basePosts = postsRepository.get(booleanRequest.getId());
        CheckUtil.isEmpty(basePosts, ErrorCodeEn.ARTICLE_NOT_EXIST);

        basePosts.setMarrow(booleanRequest.getIs());
        postsRepository.update(basePosts);
    }

    @IsLogin(role = UserRoleEn.ADMIN)
    public void typeAuditState(AdminBooleanRequest booleanRequest) {
        ArticleType articleType = articleTypeRepository.get(booleanRequest.getId());
        CheckUtil.isEmpty(articleType, ErrorCodeEn.ARTICLE_TYPE_IS_EXIST);

        articleType.setAuditState(booleanRequest.getIs() ? AuditStateEn.PASS : AuditStateEn.REJECT);
        articleTypeRepository.update(articleType);
    }
//    推荐算法的实现
    public List<ArticleRecommendsResponse> recommends(int size,Long id) {


        //1.将user表里面所有用户查出来
        //2.遍历所有用户，将点赞表，收藏表里面该用户的记录中的博客id都找出来，放在一个Set
        //每遍历完一个用户就存Map里面
        List<User> userall = userRepository.getAllUser();
        //这个Map存的是用户ID所对应的一个交互过的博客id的Set集合，Set有自动去重功能
        Map<Long,Set<Long>> userToBlogs = new HashMap<>();
        //这个Map存的是每个用户ID对应的该用户交互过的物品总数
        Map<Long, Integer> num = new HashMap<>();
        System.out.println(userall);
        for(User user:userall)
        {
            System.out.println(user);
            //这个Set存放当前遍历到的用户所交互过的所有博客的id
            Set<Long> blogIds = new HashSet<>();
            //下面这个blogIdsFromComment是用户所有评论过的博客id
            Set<Long> blogIdsFromComment = new HashSet<>();
            List<Comment> comments=commentRepository.queryInUserId(user.getId());
            System.out.println(comments);
            if(comments==null)
                continue;
            for(Comment comment:comments){
                try {
                    if (comment.getPostsId()!=null) {
                        // 当id不为空时执行操作
                        blogIdsFromComment.add(comment.getPostsId());
                    } else {
                        // 当id为空或者未初始化时执行其他操作
                    }
                } catch (NullPointerException e) {
                    System.out.println("列表为空！");
                }
            }
            userToBlogs.put(user.getId(),blogIds);
            num.put(user.getId(),blogIds.size());

        }
        Map<Long, Set<Long>> ItemToUsers = new HashMap<>();
        for (Map.Entry<Long, Set<Long>> entry : userToBlogs.entrySet()) {
            Long userId = entry.getKey();
            Set<Long> blogs = entry.getValue();

            for (Long blogId : blogs) {
                //如果当前博客对应的用户集合中没有用户，就新建一个Set再把当前用户加进去，如果有的话就之间加进去
                Set<Long> users = ItemToUsers.getOrDefault(blogId, new HashSet<>());
                users.add(userId);
                ItemToUsers.put(blogId, users);
            }
        }
        Map<Long, Map<Long, Long>> CFMatrix = new HashMap<>();

        System.out.println("开始构建协同过滤矩阵....");

        // 遍历所有的物品，统计用户两两之间交互的物品数
        for (Map.Entry<Long, Set<Long>> entry : ItemToUsers.entrySet()) {
            Long item = entry.getKey();
            Set<Long> users = entry.getValue();

            // 首先统计每个用户交互的物品个数
            for (Long u : users) {//遍历所有该博客对应的交互过的用户

                // 统计每个用户与其它用户共同交互的物品个数
                if (!CFMatrix.containsKey(u)) {
                    CFMatrix.put(u, new HashMap<>());
                }

                for (Long v : users) {//再次遍历所有用户，对不是u的其他用户进行操作
                    if (!v.equals(u)) {
                        if (!CFMatrix.get(u).containsKey(v)) {
                            CFMatrix.get(u).put(v, 0L);
                        }
                        CFMatrix.get(u).put(v, CFMatrix.get(u).get(v) + 1);
                    }
                }
            }
        }
        Map<Long, Map<Long, Double>> sim =new HashMap<>();
        System.out.println("构建用户相似度矩阵....");

        for (Map.Entry<Long, Map<Long, Long>> entry : CFMatrix.entrySet()) {//遍历协同过滤矩阵，遍历每个键值对
            Long u = entry.getKey();
            Map<Long, Long> otherUser = entry.getValue();

            for (Map.Entry<Long, Long> userScore : otherUser.entrySet()) {
                Long v = userScore.getKey();
                Long score = userScore.getValue();
                if(!sim.containsKey(u))
                {
                    sim.put(u,new HashMap<>());
                }
                sim.get(u).put(v, CFMatrix.get(u).get(v) / Math.sqrt(num.get(u) * num.get(v)));
            }
        }


        return ArticleTransfer.ArticleRecommendsResponses(articleRepository.recommends(size));
    }

    public ArticleInfoResponse info(Long id) {
        Article article = articleRepository.get(id);
        CheckUtil.isEmpty(article, ErrorCodeEn.ARTICLE_NOT_EXIST);

        if (!AuditStateEn.PASS.equals(article.getAuditState())) {
            User user = LoginUserContext.getUser();
            CheckUtil.isEmpty(user, ErrorCodeEn.ARTICLE_IN_AUDIT_PROCESS);
            CheckUtil.isFalse(user.getId().equals(article.getAuthor().getId()), ErrorCodeEn.ARTICLE_IN_AUDIT_PROCESS);
        }

        // 触发文章查看详情事件
        EventBus.emit(EventBus.Topic.POSTS_INFO, article);

        return ArticleTransfer.toArticleInfoResponse(article);
    }

    private PageResponseModel<ArticleUserPageResponse> pageQuery(PageRequestModel pageRequestModel, PostsPageQueryValue pageQueryValue) {
        pageQueryValue.setCategory(PostsCategoryEn.ARTICLE.getValue());

        PageResult<Article> pageResult = articleRepository.page(pageRequestModel.getPageNo(), pageRequestModel.getPageSize(), pageQueryValue);

        return PageResponseModel.build(pageResult.getTotal(), pageResult.getSize(), ArticleTransfer.toArticleUserPageResponses(pageResult.getList()));
    }

    private ArticleType checkArticleType(Long typeId) {
        ArticleType articleType = articleTypeRepository.get(typeId);
        CheckUtil.isEmpty(articleType, ErrorCodeEn.ARTICLE_TYPE_IS_EXIST);
        CheckUtil.isFalse(AuditStateEn.PASS.equals(articleType.getAuditState()), ErrorCodeEn.ARTICLE_TYPE_IS_EXIST);

        return articleType;
    }

}
