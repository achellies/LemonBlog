
package com.limemobile.app.blog.weibo.tencent.entity.user;

import com.limemobile.app.blog.constant.Constant;
import com.limemobile.app.blog.weibo.tencent.entity.TObject;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tag;
import com.limemobile.app.blog.weibo.tencent.entity.common.Tweet;
import com.loopj.android.http.JSONArrayPoxy;
import com.loopj.android.http.JSONObjectProxy;

import org.json.JSONException;

import java.lang.reflect.Field;
import java.util.ArrayList;

// 帐户相关/获取自己的详细资料 https://open.t.qq.com/api/user/info
// 帐户相关/获取其他人资料 https://open.t.qq.com/api/user/other_info
public class Info extends TObject {
    /**
     * 
     */
    private static final long serialVersionUID = 2611612263722000267L;
    public boolean verify; // true-已注册，false－未注册
    public int birth_day; // 出生天
    public int birth_month; // 出生月
    public int birth_year; // 出生年
    public int city_code; // 城市id
    public ArrayList<Comp> comp; // 工作信息
    public int country_code; // 国家id
    public ArrayList<Edu> edu; // 教育信息
    public long fansnum; // 听众数
    public long favnum; // 收藏数
    public String head; // 头像url
    public int homecity_code; // 家乡所在城市id
    public int homecountry_code; // 家乡所在国家id
    public String homepage; // 个人主页
    public int homeprovince_code; // 家乡所在省id
    public int hometown_code; // 家乡所在城镇id
    public long idolnum; // 收听的人数
    public int industry_code; // 行业id
    public String introduction; // 个人介绍
    public int isent; // 是否企业机构
    public int ismyblack; // 是否在当前用户的黑名单中，0-不是，1-是
    public int ismyfans; // 是否是当前用户的听众，0-不是，1-是
    public int ismyidol; // 是否是当前用户的偶像，0-不是，1-是
    public int isrealname; // 是否实名认证，0-未实名认证，1-已实名认证
    public int isvip; // 是否认证用户，0-不是，1-是
    public String location; // 所在地
    public long mutual_fans_num; // 互听好友数
    public String name; // 用户帐户名
    public String nick; // 用户昵称
    public String openid; // 用户唯一id，与name相对应
    public int province_code; // 地区id
    public String regtime; // 注册时间
    public int send_private_flag; // 是否允许所有人给当前用户发私信，0-仅有偶像，1-名人+听众，2-所有人
    public int sex; // 用户性别，1-男，2-女，0-未填写
    public ArrayList<Tag> tag; // 标签
    public ArrayList<Tweet> tweetinfo; // 最近的一条原创微博信息
    public long tweetnum; // 发表的微博数
    public String verifyinfo; // 认证信息
    public long exp; // 经验值
    public long level; // 微博等级

    public Info() {
        verify = true;
    }

    @Override
    public void parse(Field field, String typeString, JSONObjectProxy data, String fieldName)
            throws IllegalArgumentException, IllegalAccessException {
        if (typeString.equals(Constant.CLASS_TYPE_ARRAY_LIST)) {
            JSONArrayPoxy jsonArray = data.getJSONArrayOrNull(fieldName);
            if (jsonArray != null) {
                ArrayList<TObject> array = new ArrayList<TObject>();
                for (int index = 0; index < jsonArray.length(); ++index) {
                    try {
                        JSONObjectProxy jsonObject = jsonArray.getJSONObject(index);
                        if (jsonObject != null) {
                            if (fieldName.equals("comp")) {
                                Comp comp = new Comp();
                                comp.parse(jsonObject);
                                array.add(comp);
                            } else if (fieldName.equals("edu")) {
                                Edu edu = new Edu();
                                edu.parse(jsonObject);
                                array.add(edu);
                            } else if (fieldName.equals("tag")) {
                                Tag tag = new Tag();
                                tag.parse(jsonObject);
                                array.add(tag);
                            } else if (fieldName.equals("tweetinfo")) {
                                Tweet tweetinfo = new Tweet();
                                tweetinfo.parse(jsonObject);
                                array.add(tweetinfo);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
                field.set(this, array);
            }
        }
    }
}
