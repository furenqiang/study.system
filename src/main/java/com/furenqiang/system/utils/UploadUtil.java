package com.furenqiang.system.utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadUtil {

    //文件重命名
    public static String generateFileName(String originalFilename){
        List<String> fnList = Arrays.asList(originalFilename.split("\\."));
        //文件重命名：原名称+时间戳+后缀
        String fileName=fnList.get(0)+new Timestamp(System.currentTimeMillis()).getTime()+"."+fnList.get(1);
        return fileName;
    }

    public static List<String> unzipFile(File zipFile, String unzipPath, String fileName)throws IOException {
        //先解压
        List<String> fileNames = new ArrayList<>();
        String fileEncoding = null;
        try {
            fileEncoding = checkEncoding(zipFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String fileEncoding1 = (fileEncoding != null) ? fileEncoding : "UTF-8";
        try (ZipArchiveInputStream zais = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile), 4096), fileEncoding1)) {
            ZipArchiveEntry entry = null;
            while ((entry = zais.getNextZipEntry()) != null) {
                //遍历压缩包，如果进行有选择解压，可在此处进行过滤
                File tmpFile = new File(unzipPath, entry.getName());
                if (entry.isDirectory()) {
                    tmpFile.mkdirs();
                } else {
                    fileNames.add(entry.getName());
                    File file = new File(tmpFile.getAbsolutePath());
                    if (!file.exists()) {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                    }
                    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile), 4096)) {
                        IOUtils.copy(zais, os);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    //判断字符编码
    private static String checkEncoding(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        byte[] b = new byte[3];
        try {
            int i = in.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        if (b[0] == -1 && b[1] == -2) {
            return "UTF-16";
        } else if (b[0] == -2 && b[1] == -1) {
            return "Unicode";
        } else if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
            return "UTF-8";
        } else {
            return "GBK";
        }
    }
//
//    public static List<String> getFileTree(String unzipPath) {
//        List<String>filePaths = new ArrayList<>();
//        LinkedList<File> list = new LinkedList<File>();
//        File dir = new File(unzipPath);
//        File[] file = dir.listFiles();
//
//        for (int i = 0; i < file.length; i++) {
//            if (file[i].isDirectory()) {
//                // 把第一层的目录，全部放入链表
//                list.add(file[i]);
//            }
//            filePaths.add(file[i].getAbsolutePath());
//        }
//        // 循环遍历链表
//        while (!list.isEmpty()) {
//            // 把链表的第一个记录删除
//            File tmp = list.removeFirst();
//            // 如果删除的目录是一个路径的话
//            if (tmp.isDirectory()) {
//                // 列出这个目录下的文件到数组中
//                file = tmp.listFiles();
//                if (file == null) {// 空目录
//                    continue;
//                }
//                // 遍历文件数组
//                for (int i = 0; i < file.length; ++i) {
//                    if (file[i].isDirectory()) {
//                        // 如果遍历到的是目录，则将继续被加入链表
//                        list.add(file[i]);
//                    }
//                    filePaths.add(file[i].getAbsolutePath());
//                }
//            }
//        }
//        return filePaths;
//    }
}
