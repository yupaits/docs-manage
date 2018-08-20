package com.yupaits.docs.auth.controller;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author yupaits
 * @date 2018/8/20
 */
@Controller
@FrameworkEndpoint
@SessionAttributes("authorizationRequest")
public class PageController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码有误!");
        }
        return "login";
    }

    @GetMapping("/oauth/confirm_access")
    public String confirmAccessPage(Map<String, Object> model, HttpServletRequest request) throws Exception {
        //noinspection unchecked
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes"));
        Set<String> scopeList = scopes.keySet();
        model.put("scopeList", scopeList);
        return "confirmAccess";
    }

    @GetMapping("/oauth/error")
    public String handleError(Map<String, Object> model, HttpServletRequest request) {
        Object error = request.getAttribute("error");
        String errorInfo;
        if (error instanceof OAuth2Exception) {
            OAuth2Exception oauthError = (OAuth2Exception) error;
            errorInfo = HtmlUtils.htmlEscape(oauthError.getSummary());
        } else {
            errorInfo = ((Exception) error).toString();
        }
        model.put("errorInfo", errorInfo);
        return "oauthError";
    }
}
