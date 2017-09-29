package com.lesso.common.util;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by czx on 2017/9/27.
 */
public class FastDFSUtils {

    public static String uploadPic(byte[] pic, String name, long size){
              String path = null;
               //ClassPath下的文件Spring
              ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
               try {
                   ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
                   //客服端
                   TrackerClient trackerClient = new TrackerClient();
                   TrackerServer trackerServer = trackerClient.getConnection();
//                   StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
//                   StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
                   StorageClient1 storageClient = new StorageClient1();
                   //扩展名, 获取扩展名, apach 下common包中已有公用方法.
                       // String extension = FilenameUtils.getExtension(name);
                        String extension=name.substring(name.lastIndexOf(".")+1);
                         //设置图片meta信息
                      NameValuePair[] meta_list = new NameValuePair[3];
                       meta_list[0] = new NameValuePair("filename", name);
                       meta_list[1] = new NameValuePair("fileext", extension);
                       meta_list[2] = new NameValuePair("filesize", String.valueOf(size));
                       //上传且返回path
                         path = storageClient.upload_file1(pic, extension, meta_list);
               } catch (Exception e) {
                         // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
               return path;
            }

    public static String uploadPic(String picPath, String name, long size){
        String path = null;
        //ClassPath下的文件Spring
        ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
        try {
            ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
            //客服端
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
//                   StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
//                   StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
            StorageClient1 storageClient = new StorageClient1();
            //扩展名, 获取扩展名, apach 下common包中已有公用方法.
            // String extension = FilenameUtils.getExtension(name);
            String extension=name.substring(name.lastIndexOf(".")+1);
            //设置图片meta信息
            NameValuePair[] meta_list = new NameValuePair[3];
            meta_list[0] = new NameValuePair("filename", name);
            meta_list[1] = new NameValuePair("fileext", extension);
            meta_list[2] = new NameValuePair("filesize", String.valueOf(size));
            //上传且返回path
            path = storageClient.upload_file1(picPath, extension, meta_list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return path;
    }

}
