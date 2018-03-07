package com.xq.live.vo.in;

/**
 * 入参基础类
 * @author zhangpeng32
 * @create 2018-02-07 15:02
 **/
public class BaseInVo {
    private Integer page = 1;   //当前页

    private Integer start;

    private Integer rows = 10;
    /**
     * 搜索关键字
     */
    private String searchKey;

    public Integer getStart() {
        start = rows * (page - 1);
        return start;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
