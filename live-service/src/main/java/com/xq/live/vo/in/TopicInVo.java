package com.xq.live.vo.in;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-08 13:59
 * @copyright:hbxq
 **/
public class TopicInVo extends BaseInVo {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String zanUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getZanUserId() {
        return zanUserId;
    }

    public void setZanUserId(String zanUserId) {
        this.zanUserId = zanUserId;
    }
}
