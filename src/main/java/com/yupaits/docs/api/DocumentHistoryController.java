package com.yupaits.docs.api;

import com.yupaits.docs.common.Response;
import com.yupaits.docs.common.ResponseBuilder;
import com.yupaits.docs.model.DocumentHistory;
import com.yupaits.docs.mapper.DocumentHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yupaits on 2017/8/5.
 */
@RestController
public class DocumentHistoryController {

    @Autowired
    private DocumentHistoryMapper documentHistoryMapper;

    @GetMapping("/documents/{documentId}/documentHistories")
    public Response<List<DocumentHistory>> getDocumentHistories(@PathVariable Integer documentId) {
        DocumentHistory documentHistory = new DocumentHistory();
        documentHistory.setDocumentId(documentId);
        return ResponseBuilder.success(documentHistoryMapper.selectBySelective(documentHistory));
    }
}
