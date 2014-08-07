
package com.limemobile.app.blog.weibo.tencent.entity.user;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;

// 工作信息
public class Comp extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 6275922879176719598L;
    public long begin_year; // 开始年
    public String company_name; // 公司名称
    public String department_name; // 部门名称
    public long end_year; // 结束年
    public long id; // 公司id
}
