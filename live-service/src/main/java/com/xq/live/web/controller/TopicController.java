package com.xq.live.web.controller;

import com.xq.live.common.BaseResp;
import com.xq.live.common.Pager;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.Topic;
import com.xq.live.service.TopicService;
import com.xq.live.vo.in.TopicInVo;
import com.xq.live.vo.out.TopicForZanOut;
import com.xq.live.vo.out.TopicOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 主题controller
 *
 * @author zhangpeng32
 * @create 2018-01-17 18:56
 **/
@RestController
@RequestMapping(value = "/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    /**
     * 根据ID查询主题信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public BaseResp<Topic> getTopicById(@PathVariable(value = "id") Long id) {
        Topic topic = topicService.selectOne(id);
        return new BaseResp<Topic>(ResultStatus.SUCCESS, topic);
    }

    @RequestMapping(value = "/getTopicByZan", method = RequestMethod.GET)
    public BaseResp<TopicForZanOut> getTopicById(TopicInVo inVo) {
        if(inVo.getZanUserId()==null){
            return  new BaseResp<TopicForZanOut>(0,"zanUserId必填",null);
        }

        TopicForZanOut topic = topicService.selectByZan(inVo);

        return new BaseResp<TopicForZanOut>(ResultStatus.SUCCESS, topic);
    }

    /**
     * 新增一条商家记录
     *
     * @param topic
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResp<Long> add(@Valid Topic topic, BindingResult result) {
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            return new BaseResp<Long>(ResultStatus.FAIL.getErrorCode(), list.get(0).getDefaultMessage(), null);
        }
        Long id = topicService.add(topic);
        return new BaseResp<Long>(ResultStatus.SUCCESS, id);
    }

    /**
     * 删除一条商家记录
     * @param id
     * @return
     */
/*    @RequestMapping(value = "/delete/{id}",  method = RequestMethod.DELETE)
    public int  delete(@PathVariable(value="id") Long id){
        return 0;
    }*/

    /**
     * 更新
     *
     * @param topic
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public BaseResp<Integer> update(Topic topic) {
        int r = topicService.update(topic);
        return new BaseResp<Integer>(ResultStatus.SUCCESS, r);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResp<Pager<TopicOut>> list(TopicInVo inVo) {
        Pager<TopicOut> result = topicService.list(inVo);
        return new BaseResp<Pager<TopicOut>>(ResultStatus.SUCCESS, result);
    }

    /**
     * 查询热门主题
     *
     * @param inVo
     * @return
     */
    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public BaseResp<List<TopicOut>> top(TopicInVo inVo) {
        List<TopicOut> result = topicService.top(inVo);
        return new BaseResp<List<TopicOut>>(ResultStatus.SUCCESS, result);
    }
}
