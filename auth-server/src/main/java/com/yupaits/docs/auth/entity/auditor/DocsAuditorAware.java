package com.yupaits.docs.auth.entity.auditor;

import com.yupaits.docs.common.constants.DocsConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author yupaits
 * @date 2018/8/11
 */
@Component
public class DocsAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : DocsConsts.DEV_USERNAME;
        return StringUtils.isBlank(username) ? DocsConsts.DEV_USERNAME : username;
    }
}
