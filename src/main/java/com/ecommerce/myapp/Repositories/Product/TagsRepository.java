package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.DTO.Product.TagsDto;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, Integer> {
    @Query("SELECT new com.ecommerce.myapp.DTO.Product.TagsDto(t.id, t.tagName) FROM Tags t")
    List<TagsDto> findAllTags();

    @Query("SELECT new com.ecommerce.myapp.DTO.Product.TagsDto(c.id, c.tagName) FROM Tags c JOIN c.products p WHERE p.id = :productId")
    List<TagsDto> findByProductId(Integer productId);
}