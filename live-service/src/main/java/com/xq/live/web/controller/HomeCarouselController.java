package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.HomeCarousel;
import com.xq.live.service.HomeCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lipeng on 2018/3/6.
 */
@RestController
@RequestMapping(value = "hcl")
public class HomeCarouselController {
     @Autowired
     private HomeCarouselService homeCarouselService;

    /**
     *
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public BaseResp<List<HomeCarousel>> list(){
        List<HomeCarousel> res = homeCarouselService.list();

        return new BaseResp<List<HomeCarousel>>(ResultStatus.SUCCESS,res);
    }
}
