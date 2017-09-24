package com.yupaits.docs.repository;

import com.yupaits.docs.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TemplateRepository
 * Created by ts495 on 2017/9/21.
 */
public interface TemplateRepository extends JpaRepository<Template, Integer> {

    @Override
    Template findOne(Integer templateId);

    @Override
    <S extends Template> S save(S s);
}
