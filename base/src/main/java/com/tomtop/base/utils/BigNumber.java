package com.tomtop.base.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigNumber {
	//默认除法运算精度,即保留小数点多少位 
	private static final int DEFAULT_DIV_SCALE = 10;
	//这个类不能实例化 
	private BigNumber() {
	}
	/** 
	* 提供精确的加法运算。 
	* @param v1 被加数 
	* @param v2 加数 
	* @return 两个参数的和 
	*/
	public static double add(double v1, double v2) {
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));
	   return (b1.add(b2)).doubleValue();
	}
	/** 
	* 提供精确的减法运算。 
	* @param v1 被减数 
	* @param v2 减数 
	* @return 两个参数的差 
	*/
	public static double sub(double v1, double v2) {
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));
	   return (b1.subtract(b2)).doubleValue();
	}
	/** 
	* 提供精确的乘法运算。 
	* @param v1 被乘数 
	* @param v2 乘数 
	* @return 两个参数的积 
	*/
	public static double mul(double v1, double v2) {
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));
	   return (b1.multiply(b2)).doubleValue();
	}
	/** 
	* 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 
	* 小数点以后多少位，以后的数字四舍五入。 
	* @param v1 被除数 
	* @param v2 除数 
	* @return 两个参数的商 
	*/
	public static double div(double v1, double v2) {
	   return div(v1, v2, DEFAULT_DIV_SCALE);
	}
	/** 
	* 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 
	* 定精度，以后的数字四舍五入。 
	* @param v1 被除数 
	* @param v2 除数 
	* @param scale 表示需要精确到小数点以后几位。 
	* @return 两个参数的商 
	*/
	public static double div(double v1, double v2, int scale) {
	   if (scale < 0) {
	    System.err.println("除法精度必须大于0!");
	    return 0;
	   }
	   BigDecimal b1 = new BigDecimal(Double.toString(v1));
	   BigDecimal b2 = new BigDecimal(Double.toString(v2));
	   return (b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)).doubleValue();
	}
	/**
	* 计算Factorial阶乘！
	* @param n   任意大于等于0的int
	* @return     n!的值
	*/
	public static BigInteger getFactorial(int n) {
	   if (n < 0) {
	    System.err.println("n必须大于等于0！");
	    return new BigInteger("-1");
	   } else if (n == 0) {
	    return new BigInteger("0");
	   }
	   //将数组换成字符串后构造BigInteger
	   BigInteger result = new BigInteger("1");
	   for (; n > 0; n--) {
	    //将数字n转换成字符串后，再构造一个BigInteger对象，与现有结果做乘法
	    result = result.multiply(new BigInteger(new Integer(n).toString()));
	   }
	   return result;
	}
	public static void main(String[] args) {
	   //   如果我们编译运行下面这个程序会看到什么？
	   System.out.println(0.05 + 0.01);
	   System.out.println(1.0 - 0.42);
	   System.out.println(4.015 * 100);
	   System.out.println(123.3 / 100);
	   //   0.060000000000000005
	   //   0.5800000000000001
	   //   401.49999999999994
	   //   1.2329999999999999
	   //计算阶乘，可以将n设得更大
	   int n = 30;
	   System.out.println("计算n的阶乘" + n + "! = " + BigNumber.getFactorial(n));
	   //用double构造BigDecimal
	   BigDecimal bd1 = new BigDecimal(0.1);
	   System.out.println("(bd1 = new BigDecimal(0.1)) = " + bd1.toString());
	   //用String构造BigDecimal
	   BigDecimal bd2 = new BigDecimal("0.1");
	   System.out.println("(bd2 = new BigDecimal(\"0.1\")) = "
	     + bd2.toString());
	   BigDecimal bd3 = new BigDecimal("0.10");
	   //equals方法比较两个BigDecimal对象是否相等，相等返回true，不等返回false
	   System.out.println("bd2.equals(bd3) = " + bd2.equals(bd3));//false
	   //compareTo方法比较两个BigDecimal对象的大小，相等返回0，小于返回-1，大于返回1。
	   System.out.println("bd2.compareTo(bd3) = " + bd2.compareTo(bd3));//0
	   //进行精确计算
	   System.out.println("0.05 + 0.01 = " + BigNumber.add(0.05, 0.01));
	   System.out.println("1.0 - 0.42 = " + BigNumber.sub(1.0, 0.42));
	   System.out.println("4.015 * 100 =" + BigNumber.mul(4.015, 100));
	   System.out.println("123.3 / 100 = " + BigNumber.div(123.3, 100));
	   }
	}