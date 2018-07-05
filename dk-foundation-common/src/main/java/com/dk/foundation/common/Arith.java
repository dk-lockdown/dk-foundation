package com.dk.foundation.common;

import java.math.BigDecimal;

/**
 * Created by duguk on 2018/1/5.
 */
public class Arith {

    private static final int DEF_DIV_SCALE = 10;


    public static float add(float v1, float v2) {
        BigDecimal b1 = new BigDecimal("" + v1);
        BigDecimal b2 = new BigDecimal("" + v2);
        return b1.add(b2).floatValue();
    }

    public static float sub(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float mul(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).floatValue();
    }

    public static float div(float v1, float v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    public static float div(float v1, float v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

 
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal("" + v1);
        BigDecimal b2 = new BigDecimal("" + v2);
        return b1.add(b2).doubleValue();
    }

    
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

  
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

   
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

   
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static float round(float v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
    
   
    public static double ceil(double v) {
        int intv = (int) v;
        double douv = 0;

        if (v - intv < 0.00001) {
            douv = v - 0.1;
        } else {
            douv = v;
        }
        return Math.ceil(douv);
    }

    public static double decimalPrice(float v) {
        return new BigDecimal(Float.toString(v)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float decimal(float v, int num) {
        return new BigDecimal(Float.toString(v)).setScale(num, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static double decimalPrice(double v) {
        return new BigDecimal(Double.toString(v)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double decimal(double v, int num) {
        return new BigDecimal(Double.toString(v)).setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
 
}
