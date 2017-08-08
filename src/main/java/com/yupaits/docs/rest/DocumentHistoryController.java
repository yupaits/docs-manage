package com.yupaits.docs.rest;

import com.yupaits.docs.common.response.Response;
import com.yupaits.docs.common.response.ResponseBuilder;
import com.yupaits.docs.mapper.DocumentHistoryMapper;
import com.yupaits.docs.model.DocumentHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yupaits on 2017/8/5.
 */
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api")
public class DocumentHistoryController {

    @Autowired
    private DocumentHistoryMapper documentHistoryMapper;

    @GetMapping("/documents/{documentId}/documentHistories")
    public Response<List<DocumentHistory>> getDocumentHistories(@PathVariable Integer documentId) {
        DocumentHistory documentHistory = new DocumentHistory();
        documentHistory.setDocumentId(documentId);
        return ResponseBuilder.ok(documentHistoryMapper.selectBySelective(documentHistory));
    }
}
