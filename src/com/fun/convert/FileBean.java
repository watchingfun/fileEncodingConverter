package com.fun.convert;

import java.io.File;
import java.util.*;

public class FileBean {
    static File[] getSelectFiles() {
        File[] file =  FileBean.selectFiles.toArray(new File[selectFiles.size()]);
        return file;
    }

    static ArrayList<File> getSelectArray() {
        return selectFiles;
    }

    public static void setSelectFiles(File[] selectFiles) {
        if (selectFiles == null) {
            FileBean.selectFiles = new ArrayList<>();
        } else if (FileBean.selectFiles == null) {
            FileBean.selectFiles = new ArrayList<>(Arrays.asList(selectFiles));
        } else {
            ArrayList<File> selectFiles_t = new ArrayList<>(Arrays.asList(selectFiles));
            Set<File> set = new TreeSet<>();
            set.addAll(FileBean.selectFiles);
            set.addAll(selectFiles_t);
            FileBean.selectFiles = new ArrayList<>(set);
        }


    }

    public static String getNowEncoding() {
        return nowEncoding;
    }

    public static void setNowEncoding(String nowEncoding) {
        FileBean.nowEncoding = nowEncoding;
    }

    public static String getToEncoding() {
        return ToEncoding;
    }

    public static void setToEncoding(String ToEncoding) {
        FileBean.ToEncoding = ToEncoding;
    }

    static ArrayList<File> selectFiles = null;
    static String nowEncoding = null;
    static String ToEncoding = null;
}
