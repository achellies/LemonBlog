
package com.limemobile.app.blog.weibo.tencent.entity.friends;

import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.loopj.android.http.JSONObjectProxy;

import java.util.ArrayList;

// 检测是否我的听众或收听的人
// 在使用时我们只是用flag=2的情况
/*
 *  flag 0-检测听众，1-检测收听的人 2-两种关系都检测
 {
 ret:0,
 msg:"ok",
 errcode:0,
 data:
 {
 name1:true,
 name2:false,
 name3:true,
 ......
 }
 ｝

 当flag=2时

 {
 ret:0,
 msg:"ok",
 errcode:0,
 data:
 {
 name1:｛isidol:true,isfans,false｝,
 name2:｛isidol:true,isfans,false｝,
 name3:｛isidol:true,isfans,false｝,
 ......
 }
 }
 */
public class Check extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1582826884495045687L;
    public ArrayList<Info> info;

    public static class Info {
        public String name; // name或fopenid:检测结果，false-没有此关系，true-有此关系
        public boolean isidol; // false-不是听众，true-是听众
        public boolean isfans; // false-不是偶像，true-是偶像
    }

    public void parse(JSONObjectProxy data) {
    }
}
