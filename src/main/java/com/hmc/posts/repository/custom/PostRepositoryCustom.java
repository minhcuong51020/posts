package com.hmc.posts.repository.custom;

import com.hmc.common.persistence.support.SqlUtils;
import com.hmc.common.util.StrUtils;
import com.hmc.posts.dto.request.PostSearchRequest;
import com.hmc.posts.entity.PostEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PostEntity> search(PostSearchRequest request, String currentUserId) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT R FROM PostEntity R ");
        sql.append(createWhereQuery(request, currentUserId, values));
        sql.append(createOrderQuery(request.getSortBy()));
        Query query = entityManager.createQuery(sql.toString(), PostEntity.class);
        values.forEach(query::setParameter);
        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        return query.getResultList();
    }

    public Long count(PostSearchRequest request, String currentUserId) {
        Map<String, Object> values = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT COUNT(R) FROM PostEntity R ");
        sql.append(createWhereQuery(request, currentUserId, values));
        Query query = entityManager.createQuery(sql.toString(), Long.class);
        values.forEach(query::setParameter);
        return (Long) query.getSingleResult();
    }

    private String createOrderQuery(String sortBy) {
        StringBuilder sql = new StringBuilder(" ");
        if(StringUtils.hasLength(sortBy)){
            sql.append("ORDER BY R.").append(sortBy.replace("."," "));
        }else {
            sql.append("ORDER BY R.createdAt desc ");
        }
        return  sql.toString();
    }

    private String createWhereQuery(PostSearchRequest request, String currentUserId, Map<String, Object> values) {
        StringBuilder sql = new StringBuilder(" ");
        sql.append("WHERE 1 = 1 ");
        sql.append("AND R.deleted = false ");
        if(!StrUtils.isBlank(request.getKeyword())) {
            sql.append("AND (R.title like :title ");
            values.put("title", SqlUtils.encodeKeyword(request.getKeyword()));
            sql.append("OR R.content like :content) ");
            values.put("content", SqlUtils.encodeKeyword(request.getKeyword()));
        }
        if(!StrUtils.isBlank(currentUserId)) {
            sql.append("AND R.ownerId = :ownerId ");
            values.put("ownerId", currentUserId);
        }
        return sql.toString();
    }

}
