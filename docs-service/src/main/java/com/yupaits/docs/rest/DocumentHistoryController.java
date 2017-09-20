package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.config.jwt.JwtHelper;
import com.yupaits.docs.entity.DocumentHistory;
import com.yupaits.docs.repository.DocumentHistoryRepository;
import com.yupaits.docs.util.http.HttpUtil;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文档历史REST接口
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/api/documentHistories")
public class DocumentHistoryController {

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/documents/{documentId}")
    public Result getDocumentHistories(@PathVariable Integer documentId) {
        if (ValidateUtils.idInvalid(documentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        List<DocumentHistory> documentHistoryList = documentHistoryRepository.findByDocumentIdOrderBySavedTimeDesc(documentId);
        if (CollectionUtils.isNotEmpty(documentHistoryList)
                && documentHistoryList.get(0).getOwnerId().compareTo(jwtHelper.getUserId(HttpUtil.getRequest())) != 0) {
            return Result.fail(ResultCode.FORBIDDEN);
        }
        return Result.ok(documentHistoryList);
    }
}
