package com.yupaits.file.config.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 基于Jwt的SubjectFactory
 * Created by ts495 on 2017/9/9.
 */
public class StatelessSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        //不创建session
        context.setSessionCreationEnabled(false);
        super.createSubject(context).getSession(false);
        return super.createSubject(context);
    }
}
