package com.study.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.study.config.OSSConfigure;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by 张高 on 2017/10/26.
 */

public class OSSManageUtil {

    public static String uploadFile(MultipartFile multipartFile, String remotePath) throws Exception {
        // 流转换 将MultipartFile转换为oss所需的InputStream
        /*CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
        DiskFileItem fileItem = cf.getFileItem();
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();*/
        InputStream fileContent = multipartFile.getInputStream();
        //获取文件名，对文件名做随机处理
        String fileName = multipartFile.getOriginalFilename();
        fileName = new Date().getTime() + fileName.substring(fileName.lastIndexOf("."));
        // 加载配置文件，初始化OSSClient
        OSSConfigure ossConfigure = new OSSConfigure();
        //        OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(), ossConfigure.getAccessKeyId(),
        OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(), ossConfigure.getAccessKeyId(),
                ossConfigure.getAccessKeySecret());
        // 定义二级目录
        String remoteFilePath = remotePath.substring(0, remotePath.length()).replaceAll("\\\\", "/") + "/";
        // 创建上传Object的Metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileContent.available());
        objectMetadata.setContentEncoding("utf-8");
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType(contentType(fileName.substring(fileName.lastIndexOf("."))));
        objectMetadata.setContentDisposition("inline;filename=" + fileName);
        // 上传文件
        ossClient.putObject(ossConfigure.getFile(), remoteFilePath + fileName, fileContent, objectMetadata);
        // 关闭OSSClient
        ossClient.shutdown();
        // 关闭io流
        fileContent.close();
        return ossConfigure.getAccessUrl() + "/" + remoteFilePath + fileName;
    }

    // 下载文件
    @SuppressWarnings("unused")
    public static void downloadFile(OSSConfigure ossConfigure, String key, String filename)
            throws OSSException, ClientException, IOException {
        // 初始化OSSClient
        OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(), ossConfigure.getAccessKeyId(),
                ossConfigure.getAccessKeySecret());
        OSSObject object = ossClient.getObject(ossConfigure.getBucketName(), key);
        // 获取ObjectMeta
        ObjectMetadata meta = object.getObjectMetadata();

        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();

        ObjectMetadata objectData = ossClient.getObject(new GetObjectRequest(ossConfigure.getBucketName(), key),
                new File(filename));
        // 关闭数据流
        objectContent.close();

    }
/*
    */
/**
 *
 * @MethodName: updateFile
 * @Description: 更新文件:只更新内容，不更新文件名和文件地址。
 *      (因为地址没变，可能存在浏览器原数据缓存，不能及时加载新数据，例如图片更新，请注意)
 * @param file
 * @param fileType
 * @param oldUrl
 * @return String
 *//*

    public static String updateFile(File file,String fileType,String oldUrl){
        String fileName = getFileName(oldUrl);
        if(fileName==null) return null;
        return putObject(file,fileType,fileName);
    }
*/


    /**
     * 根据key删除OSS服务器上的文件 @Title: deleteFile @Description: @param @param
     * ossConfigure @param 配置文件实体 @param filePath 设定文件 @return void 返回类型 @throws
     */
    public static void deleteFile(OSSConfigure ossConfigure, String filePath) {
        OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(), ossConfigure.getAccessKeyId(),
                ossConfigure.getAccessKeySecret());
        ossClient.deleteObject(ossConfigure.getBucketName(), filePath);

    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType @Version1.0
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String contentType(String FilenameExtension) {
        if (FilenameExtension.equals(".BMP") || FilenameExtension.equals(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equals(".GIF") || FilenameExtension.equals(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equals(".JPEG") || FilenameExtension.equals(".jpeg") || FilenameExtension.equals(".JPG")
                || FilenameExtension.equals(".jpg") || FilenameExtension.equals(".PNG")
                || FilenameExtension.equals(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equals(".HTML") || FilenameExtension.equals(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equals(".TXT") || FilenameExtension.equals(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equals(".VSD") || FilenameExtension.equals(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equals(".PPTX") || FilenameExtension.equals(".pptx") || FilenameExtension.equals(".PPT")
                || FilenameExtension.equals(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equals(".DOCX") || FilenameExtension.equals(".docx") || FilenameExtension.equals(".DOC")
                || FilenameExtension.equals(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equals(".XML") || FilenameExtension.equals(".xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equals(".apk") || FilenameExtension.equals(".APK")) {
            return "application/octet-stream";
        }
        return "text/html";
    }

    /**
    * class_name: OSSManageUtil
    * package: com.study.util
    * describe: 获取oss对象列表
    * creat_user: ZhangGaoJun@gaojun.zhag6@icloud.com
    * creat_date: 2017/11/27
    * creat_time: 11:09
    **/
    public List<OSSObjectSummary> getOSSobjectSummary(String bucketName) throws IOException {
        OSSConfigure ossConfigure = new OSSConfigure();
        OSSClient ossClient = new OSSClient(ossConfigure.getEndpoint(), ossConfigure.getAccessKeyId(),
                ossConfigure.getAccessKeySecret());
        ObjectListing objectListing = ossClient.listObjects("vstu");
        return objectListing.getObjectSummaries();
    }
}
