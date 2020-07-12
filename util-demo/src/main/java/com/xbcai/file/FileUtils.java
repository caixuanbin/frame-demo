package com.xbcai.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static List<String> readFile(String filePath) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<String> qqList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line.trim()).append(";");
            if(line.contains("2020年")){
                String str = builder.toString();
                if(str.contains("投递失败")){
                    qqList.add(str.split(";")[0]);
                }
                builder.setLength(0);
            }
        }
        qqList.forEach(System.out::println);
        reader.close();
        return qqList;
    }

    public static void main(String[] args) throws Exception{
        String path="D:\\副业\\淘宝\\邮件推送\\qq.txt";
        readFile(path);
    }
}
