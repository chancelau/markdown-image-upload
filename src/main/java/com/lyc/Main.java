package com.lyc;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        System.out.println("开始~~~请耐心等待！！");
        StringBuilder fileContent = new StringBuilder();
        try {
            File mdFile = new File("/Users/lyc/Desktop/讲义/day12/苍穹外卖-day12.md");
            String txt = FileUtils.readFileToString(mdFile, "UTF-8");
            fileContent.append(txt);
            Set<String> imgTags = getImgTags(txt);
            System.out.println("一共"+imgTags.size()+"个图片需要上传!");
            //System.out.println("imgTags = " + imgTags);
            System.out.println("imgTags.size() = " + imgTags.size());
            int count = 0;
            for (String img : imgTags) {
                String s1 = img.split("\\s+")[1];//src="xx"
                String src = s1.split("\"")[1];
                try {
                    //图片文件
                    File file = new File(mdFile.getParent(), src);
                    //将图片文件上传到oss
                    byte[] bytes = FileUtils.readFileToByteArray(file);
                    String url = AliOssUtil.upload(bytes, src.split(File.separator)[1]);
                    int start = fileContent.indexOf(src);
                    int end = start + src.length();
                    fileContent.replace(start, end, url);
                    count++;
                    System.out.println(src+"上传成功！"+1.0*count/ imgTags.size()*100+"%");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println(src + "文件上传失败");
                }

                System.out.println(src);
            }
            //System.out.println("替换后的文本： " + fileContent);
            FileUtils.writeStringToFile(new File(mdFile.getName()),fileContent.toString(),"UTF-8");
        } catch (IOException e) {
            //读取出错啦
            e.printStackTrace();
        }


    }


    public static Set<String> getImgTags(String text) {

        // 1、定义爬取规则


        // 2、把正则表达式封装成一个Pattern对象
        Pattern pattern = Pattern.compile("<img\\s+src=\"(.+?)\"(.+?)/>");

        // 3、通过pattern对象去获取查找内容的匹配器对象。
        Matcher matcher = pattern.matcher(text);

        Set<String> imgTags = new HashSet<>();
        // 4、定义一个循环开始爬取信息
        while (matcher.find()) {
            String rs = matcher.group(); // 获取到了找到的内容了。
            imgTags.add(rs);
        }

        return imgTags;

    }

}
