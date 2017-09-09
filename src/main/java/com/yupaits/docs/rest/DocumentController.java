package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Result;
import com.yupaits.docs.common.response.ResultCode;
import com.yupaits.docs.mapper.DocumentHistoryMapper;
import com.yupaits.docs.mapper.DocumentMapper;
import com.yupaits.docs.model.Document;
import com.yupaits.docs.model.DocumentHistory;
import com.yupaits.docs.util.validate.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

/**
 * 文档REST接口
 * Created by yupaits on 2017/8/5.
 */
@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentHistoryMapper documentHistoryMapper;

    @GetMapping("/documents/{documentId}")
    public Result getDocumentById(@PathVariable Integer documentId) {
        if (ValidateUtils.idInvalid(documentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        return Result.ok(documentMapper.selectByPrimaryKey(documentId));
    }

    @GetMapping("/directories/{directoryId}/documents")
    public Result getDirectoryDocuments(@PathVariable Integer directoryId) {
        if (ValidateUtils.idInvalid(directoryId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Document document = new Document();
        document.setDirectoryId(directoryId);
        return Result.ok(documentMapper.selectBySelective(document));
    }

    @PostMapping("/documents")
    public Result createDocument(@RequestBody Document document) {
        if (document == null || ValidateUtils.idInvalid(document.getDirectoryId()) || StringUtils.isBlank(document.getContent())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        documentMapper.insertSelective(document);
        return Result.ok();
    }

    @DeleteMapping("/documents/{documentId}")
    public Result deleteDocument(@PathVariable Integer documentId) {
        if (ValidateUtils.idInvalid(documentId)) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        documentMapper.deleteByPrimaryKey(documentId);
        return Result.ok();
    }

    @PutMapping("/documents/{documentId}")
    public Result updateDocument(@RequestBody Document document) {
        if (document == null || ValidateUtils.idInvalid(document.getId()) || ValidateUtils.idInvalid(document.getDirectoryId())
                || StringUtils.isBlank(document.getContent())) {
            return Result.fail(ResultCode.PARAMS_ERROR);
        }
        Document documentInDb = documentMapper.selectByPrimaryKey(document.getId());
        DocumentHistory documentHistory = new DocumentHistory();
        if (StringUtils.isNotBlank(documentInDb.getContent())) {
            documentHistory.setDocumentId(documentInDb.getId());
            documentHistory.setContent(documentInDb.getContent());
            documentHistory.setSavedTime(new Date(System.currentTimeMillis()));
            documentHistoryMapper.insertSelective(documentHistory);
        }
        documentMapper.updateByPrimaryKeySelective(document);
        return Result.ok();
    }
}
