package com.yupaits.docs.repository;

import com.yupaits.docs.entity.DocumentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DocumentHistoryRepository
 * Created by yupaits on 2017/9/13.
 */
public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Integer> {
    List<DocumentHistory> findByDocumentId(Integer documentId);
}
