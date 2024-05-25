package com.pdfTool.utils;

public class TimeUtil {
    public static long pre = 0;

    public static void start() {
        pre = System.currentTimeMillis();
    }

    public static void end() {
        long cost = System.currentTimeMillis() - pre;
        System.out.println("cost" + cost + "ms");
    }
}
