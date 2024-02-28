package pub.developers.forum.infrastructure.transfer;

import org.springframework.util.ObjectUtils;
import pub.developers.forum.common.enums.AuditStateEn;
import pub.developers.forum.common.support.SafesUtil;
import pub.developers.forum.domain.entity.Article;
import pub.developers.forum.domain.entity.Faq;
import pub.developers.forum.domain.entity.Tag;
import pub.developers.forum.infrastructure.dal.dataobject.PostsDO;
import pub.developers.forum.infrastructure.dal.dataobject.TagDO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiongben
 * @create 23/11/23
 * @desc
 **/
public class TagTransfer {

    public static List<Faq> toFaqs(List<PostsDO> postsDOS) {
        List<Faq> faqs = new ArrayList<>();


        SafesUtil.ofList(postsDOS).forEach(postsDO -> {
            Faq faq = new Faq();
            faq.setId(postsDO.getId());
            faq.setTitle(postsDO.getTitle());
            faq.setCreateAt(postsDO.getCreateAt());

            faqs.add(faq);
        });

        return faqs;
    }
    public static List<Article> toArticles(List<PostsDO> postsDOS) {
        List<Article> articles = new ArrayList<>();

        SafesUtil.ofList(postsDOS).forEach(postsDO -> {
            Article article = new Article();
            article.setId(postsDO.getId());
            article.setTitle(postsDO.getTitle());
            article.setCreateAt(postsDO.getCreateAt());

            articles.add(article);
        });

        return articles;
    }
    public static TagDO toTagDO(Tag tag) {
        TagDO tagDO = TagDO.builder()
                .auditState(ObjectUtils.isEmpty(tag.getAuditState()) ? null : tag.getAuditState().getValue())
                .creatorId(tag.getCreatorId())
                .groupName(tag.getGroupName())
                .description(tag.getDescription())
                .name(tag.getName())
                .refCount(tag.getRefCount())
                .build();

        tagDO.initBase();

        return tagDO;
    }

    public static List<Tag> toTags(List<TagDO> tagDOS) {
        List<Tag> tags = new ArrayList<>();

        SafesUtil.ofList(tagDOS).forEach(tagDO -> tags.add(toTag(tagDO)));

        return tags;
    }

    public static Tag toTag(TagDO tagDO) {
        if (ObjectUtils.isEmpty(tagDO)) {
            return null;
        }

        Tag tag = Tag.builder()
                .auditState(AuditStateEn.getEntity(tagDO.getAuditState()))
                .groupName(tagDO.getGroupName())
                .creatorId(tagDO.getCreatorId())
                .description(tagDO.getDescription())
                .name(tagDO.getName())
                .refCount(tagDO.getRefCount())
                .build();
        tag.setId(tagDO.getId());
        tag.setCreatorId(tagDO.getCreatorId());
        tag.setCreateAt(tagDO.getCreateAt());
        tag.setUpdateAt(tagDO.getUpdateAt());

        return tag;
    }

}
