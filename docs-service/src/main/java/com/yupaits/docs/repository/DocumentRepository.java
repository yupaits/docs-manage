package com.yupaits.docs.repository;

import com.yupaits.docs.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DocumentRepository
 * Created by yupaits on 2017/9/13.
 */
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    @Override
    Document findOne(Integer documentId);

    List<Document> findByDirectoryIdOrderBySortCodeAsc(Integer directoryId);

    @Override
    <S extends Document> S save(S s);

    @Override
    void delete(Integer documentId);

    int countByDirectoryId(Integer directoryId);
}
