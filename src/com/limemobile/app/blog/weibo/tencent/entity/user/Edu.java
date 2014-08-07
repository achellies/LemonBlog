
package com.limemobile.app.blog.weibo.tencent.entity.user;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;

// 教育信息
public class Edu extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 4922389532322649887L;
    public long departmentid; // 院系id
    public long id; // 教育信息记录id
    public long level; // 学历级别
    public long schoolid; // 学校id
    public long year; // 入学年
}
