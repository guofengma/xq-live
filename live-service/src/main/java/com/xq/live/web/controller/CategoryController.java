package com.xq.live.web.controller;/**
 * 类目
 *
 * @author zhangpeng32
 * @create 2018-01-24 15:38
 */

import com.xq.live.service.CategoryServive;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类目
 * @author zhangpeng32
 * @create 2018-01-24 15:38
 **/
@RestController
public class CategoryController {

    private CategoryServive categoryServive;
    @RequestMapping(value="/update")
    public String updateFullPath()  {
        categoryServive.updateFullPath();
        return "success";
    }

}
