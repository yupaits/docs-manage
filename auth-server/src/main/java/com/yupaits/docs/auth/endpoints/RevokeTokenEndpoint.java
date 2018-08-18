package com.yupaits.docs.auth.endpoints;

import com.yupaits.docs.common.result.Result;
import com.yupaits.docs.common.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yupaits
 * @date 2018/8/18
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    private final ConsumerTokenServices consumerTokenServices;

    @Autowired
    public RevokeTokenEndpoint(@Qualifier("consumerTokenServices") ConsumerTokenServices consumerTokenServices) {
        this.consumerTokenServices = consumerTokenServices;
    }

    @DeleteMapping("/oauth/token")
    @ResponseBody
    public Result revokeToken(String accessToken) {
        return consumerTokenServices.revokeToken(accessToken) ? Result.ok() : Result.fail(ResultCode.REVOKE_TOKEN_FAIL);
    }
}
