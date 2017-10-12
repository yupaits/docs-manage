package com.yupaits.docs.rest;

import com.yupaits.docs.config.JwtHelper;
import com.yupaits.docs.entity.Template;
import com.yupaits.docs.repository.TemplateRepository;
import com.yupaits.docs.response.Result;
import com.yupaits.docs.response.ResultCode;
import com.yupaits.docs.util.bean.BeanUtil;
import com.yupaits.docs.util.http.HttpUtil;
import com.yupaits.docs.util.validate.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * 文档模板REST接口
 * Created by ts495 on 2017/9/21.
 */
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/owner/{ownerId}")
    public Result templates(@PathVariable Integer ownerId,
                            @PageableDefault Pageable pageable,
                            @RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "category", required = false) String category,
                            @RequestParam(name = "tag", required = false) String tag) {
        if (ValidateUtils.idInvalid(ownerId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (!ownerId.equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        if (validateQuery(keyword, category, tag)) {
            Page<Template> templatePage = templateRepository.findAll(new Specification<Template>() {
                @Override
                public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new LinkedList<>();
                    Path<Boolean> isDeletedPath = root.get("isDeleted");
                    predicates.add(criteriaBuilder.equal(isDeletedPath, false));
                    Path<Integer> ownerIdPath = root.get("ownerId");
                    predicates.add(criteriaBuilder.equal(ownerIdPath, ownerId));
                    if (StringUtils.isNotBlank(keyword)) {
                        Path<String> namePath = root.get("name");
                        Path<String> descriptionPath = root.get("description");
                        predicates.add(criteriaBuilder.or(criteriaBuilder.like(namePath, "%" + keyword + "%"),
                                criteriaBuilder.like(descriptionPath, "%" + keyword + "%")));
                    }
                    if (StringUtils.isNotBlank(category)) {
                        Path<String> categoryPath = root.get("category");
                        predicates.add(criteriaBuilder.equal(categoryPath, category));
                    }
                    if (StringUtils.isNotBlank(tag)) {
                        Path<String> tagsPath = root.get("tags");
                        predicates.add(criteriaBuilder.like(tagsPath, "%" + tag + "%"));
                    }
                    Path<Integer> sortCodePath = root.get("sortCode");
                    return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).orderBy(new OrderImpl(sortCodePath)).getRestriction();
                }
            }, pageable);
            return Result.ok(templatePage);
        }
        return Result.ok(templateRepository.findByOwnerIdAndIsDeletedIsFalseOrderBySortCodeAsc(ownerId, pageable));
    }

    @GetMapping("/owner/{ownerId}/category/{category}")
    public Result getTemplatesByCategory(@PathVariable Integer ownerId, @PathVariable String category) {
        if (ValidateUtils.idInvalid(ownerId) || StringUtils.isBlank(category)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (!ownerId.equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        return Result.ok(templateRepository.findByOwnerIdAndCategoryAndIsDeletedIsFalseOrderBySortCodeAsc(ownerId, category));
    }

    /**
     * 验证查询条件是否有效
     * @param keyword 关键字
     * @param category 类别
     * @param tag 标签
     * @return 验证结果
     */
    private boolean validateQuery(String keyword, String category, String tag) {
        return (StringUtils.isNotBlank(keyword)
                || StringUtils.isNotBlank(category)
                || StringUtils.isNotBlank(tag));
    }

    @GetMapping("/{templateId}")
    public Result getTemplate(@PathVariable Integer templateId) {
        if (ValidateUtils.idInvalid(templateId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Template template = templateRepository.findOne(templateId);
        if (template != null && !template.getOwnerId().equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        return Result.ok(template);
    }

    @PostMapping("")
    public Result createTemplate(@RequestBody Template template) {
        if (template == null || ValidateUtils.idInvalid(template.getOwnerId()) || StringUtils.isBlank(template.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (!template.getOwnerId().equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        template.setIsDeleted(false);
        template.setVisitCount(0);
        template.setLikings(0);
        template.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        templateRepository.save(template);
        return Result.ok();
    }

    @DeleteMapping("/{templateId}")
    public Result deleteTemplate(@PathVariable Integer templateId) {
        if (ValidateUtils.idInvalid(templateId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Template templateInDb = templateRepository.findOne(templateId);
        if (templateInDb == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        if (!templateInDb.getOwnerId().equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        templateInDb.setIsDeleted(true);
        templateInDb.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        templateRepository.save(templateInDb);
        return Result.ok();
    }

    @PutMapping("/{templateId}")
    public Result updateTemplate(@RequestBody Template template) {
        if (template == null || ValidateUtils.idInvalid(template.getId()) || StringUtils.isBlank(template.getName())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Template templateInDb = templateRepository.findOne(template.getId());
        if (templateInDb == null) {
            return Result.fail(ResultCode.DATA_NOT_FOUND);
        }
        if (!templateInDb.getOwnerId().equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        BeanUtil.copyProperties(template, templateInDb);
        templateInDb.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        templateRepository.save(templateInDb);
        return Result.ok();
    }

    @GetMapping("/categories/owner/{ownerId}")
    public Result templateCategories(@PathVariable Integer ownerId) {
        if (ValidateUtils.idInvalid(ownerId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (!ownerId.equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        return Result.ok(templateRepository.findTemplateCategoryList(ownerId));
    }

    @GetMapping("/tags/owner/{ownerId}")
    public Result templatesTags(@PathVariable Integer ownerId) {
        if (ValidateUtils.idInvalid(ownerId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        if (!ownerId.equals(jwtHelper.getUserId(HttpUtil.getRequest()))) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        List<String> tagsList = templateRepository.findTagsList(ownerId);
        if (CollectionUtils.isNotEmpty(tagsList)) {
            Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            };
            Map<String, Integer> tagMap = new HashMap<>();
            for (String tags : tagsList) {
                String[] tagArray = tags.split(",");
                if (ArrayUtils.isNotEmpty(tagArray)) {
                    for (String tag : tagArray) {
                        if (tagMap.containsKey(tag)) {
                            tagMap.put(tag, tagMap.get(tag) + 1);
                        } else {
                            tagMap.put(tag, 1);
                        }
                    }
                }
            }
            ArrayList<Map.Entry<String, Integer>> tagRateList = new ArrayList<>(tagMap.entrySet());
            tagRateList.sort(comparator);
            List<TagRate> sortedTagRateList = new LinkedList<>();
            TagRate tagRate;
            for (Map.Entry<String, Integer> entry : tagRateList) {
                tagRate = new TagRate(entry.getKey(), entry.getValue());
                sortedTagRateList.add(tagRate);
            }
            return Result.ok(sortedTagRateList);
        }
        return Result.ok(new ArrayList<>());
    }

    @Data
    @AllArgsConstructor
    private class TagRate {
        private String tag;
        private Integer rate;
    }
}
