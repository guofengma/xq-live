package com.xq.live.dao;

import com.xq.live.model.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    int updateFullPath(Long id);

    List<Category> findAll();
}