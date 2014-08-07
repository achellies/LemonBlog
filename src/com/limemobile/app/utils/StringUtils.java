package com.limemobile.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.limemobile.app.blog.R;

import android.content.Context;

/** 
 * 字符串操作工具包
 */
public class StringUtils 
{
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 将字符串转位日期类型
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
		    dateFormater.setTimeZone(TimeZone.getDefault());
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
		    try {
		        Date date = new Date(Long.parseLong(sdate) * 1000);
		        return date;
		    } catch (Exception e1) {
		        e1.printStackTrace();
		    }
			return null;
		}
	}
	
	/**
	 * 以友好的方式显示时间
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(Context context, String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if (hour == 0) {
				int minute = (int)((cal.getTimeInMillis() - time.getTime()) / 1000);
				if (minute == 0)
					ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 1000, 1) + context.getString(R.string.friendly_time_second_before);
				else
					ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + context.getString(R.string.friendly_time_minute_before);
			}
			else 
				ftime = hour + context.getString(R.string.friendly_time_hour_before);
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if (days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if (hour == 0) {
				int minute = (int)((cal.getTimeInMillis() - time.getTime()) / 1000);
				if (minute == 0)
					ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 1000, 1) + context.getString(R.string.friendly_time_second_before);
				else
					ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1) + context.getString(R.string.friendly_time_minute_before);
			}
			else 
				ftime = hour + context.getString(R.string.friendly_time_hour_before);
		}
		else if (days == 1){
			ftime = context.getString(R.string.friendly_time_yesterday);
		}
		else if (days == 2){
			ftime = context.getString(R.string.friendly_time_the_day_before_yesterday);
		}
		else if (days > 2 && days <= 10){ 
			ftime = days + context.getString(R.string.friendly_time_day_before);			
		}
		else if (days > 10){			
			ftime = dateFormater2.format(time);
		}
		return ftime;
	}
	
	public static String friendly_long(Context context, long number) {
		String flong = Long.toString(number);
		
		final int THOUSAND = 1000;
		final int MILLION = 1000000;
		final int BILLION = 1000000000;
		
		long billion = (long) (number / BILLION);
		if (billion > 0) {
			flong = String.format(context.getString(R.string.friendly_time_extend_billion), (int)(number / 100000000));
		} else {
			int million = (int) (number / MILLION);
			if (million > 0) {
				flong = String.format(context.getString(R.string.friendly_long_extend_million), (int)(number /10000));
			} else {
				int thousand = (int) (number / THOUSAND);
				if (thousand > 0) {
					flong = Integer.toString(thousand) + "," + Long.toString(number - thousand * THOUSAND);
				}
			}
		}
		
		return flong;
	}
	
	/**
	 * 判断给定字符串时间是否为今日
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate){
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if(time != null){
			String nowDate = dateFormater2.format(today);
			String timeDate = dateFormater2.format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * 判断给定字符串是否空白串。
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input ) 
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if(email == null || email.trim().length()==0) 
			return false;
	    return emailer.matcher(email).matches();
	}
	/**
	 * 字符串转整数
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try{
			return Long.parseLong(obj);
		}catch(Exception e){}
		return 0;
	}
	/**
	 * 字符串转布尔值
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try{
			return Boolean.parseBoolean(b);
		}catch(Exception e){}
		return false;
	}
}