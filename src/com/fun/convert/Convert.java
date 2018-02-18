package com.fun.convert;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;

public class Convert {
    public static boolean isStop=false;
    public static String getFileContentFromCharset(File file,
                                                   String fromEncoding, boolean preview) throws IOException, InterruptedException {
        if (file != null) {
            InputStream inputStream = new FileInputStream(file);
            RandomAccessFile reader = new RandomAccessFile(file, "r");
            //InputStreamReader reader = new InputStreamReader(inputStream,
            //        fromEncoding);
            byte[] bytes = null;
            long filelength = 0;
            if (preview) {
                filelength = file.length();
                if (filelength >= 104857600) {
                    Panel.textArea.setText("文件大小超过100MB");
                    return null;
                } else {
                    bytes = new byte[1024];
                }

            } else {
                bytes = new byte[102400];
            }

            int len = 0;
            int position = 0;
            double proportion=filelength/100.0;
            System.out.println(proportion);
            StringBuilder stringBuilder = new StringBuilder();
            while ((len=reader.read(bytes)) > 0) {
                if(isStop==true){
                    //Panel.resetGui();
                    //break;
                    throw new InterruptedException("停止执行");
                }
                String str = new String(bytes,0,len,fromEncoding);
                stringBuilder.append(str);
                Panel.textArea.append(str);
                Panel.progressBar.setValue((int)(reader.getFilePointer()/proportion));
            }

            Panel.progressBar.setString(Panel.progressBar.getString()+"       "+"完成");
            if(Panel.progressBar.getString()!=null&&Panel.progressBar.getString().startsWith("waiting")){
                Panel.progressBar.setString("转换完成");
            }
            reader.close();
            return stringBuilder.toString();
        }
        return "null";
    }

    public static void convert(File file, String toCharsetName,
                               String content) throws Exception {
        if (!Charset.isSupported(toCharsetName)) {
            throw new UnsupportedCharsetException(toCharsetName);
        }
        OutputStream outputStream = new FileOutputStream(file);
        OutputStreamWriter outWrite = new OutputStreamWriter(outputStream,
                toCharsetName);
        outWrite.write(content);
        outWrite.close();
    }

    public static int[] isRightEncoding(String selectCharset, ArrayList<File> files) {
        ArrayList<Integer> indexs = new ArrayList<Integer>();
        for (int i = 0; i < files.size(); i++) {
            if (!selectCharset.equals(Check.ckCharSet(files.get(i)))) {
                indexs.add(i);
            }
        }
        int[] index = new int[indexs.size()];
        int c = 0;
        for (int i : indexs) {
            index[c] = i;
            c++;
        }

        return index;

    }

}
