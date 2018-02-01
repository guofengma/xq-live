package com.xq.live.service.impl;/**
 * @author zhangpeng32
 * @create 2018-01-24 15:39
 */

import com.xq.live.dao.CategoryMapper;
import com.xq.live.model.Category;
import com.xq.live.service.CategoryServive;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.Oneway;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zhangpeng32
 * @create 2018-01-24 15:39
 **/
public class CategoryServiceImpl implements CategoryServive{
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void updateFullPath(){
        List<Category> list = categoryMapper.findAll();
        List<Long> pIdList = new ArrayList<Long>();
        for(Category cate : list){
            Long tempId = cate.getCategoryParentId();
            pIdList.add(tempId);
            while (tempId != 0){
                Category c = categoryMapper.selectByPrimaryKey(tempId);
                Long ppId = cate.getCategoryParentId();
                pIdList.add(ppId);
                tempId = ppId;
            }
        }

    }
}
