package com.it.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
//文件上传工具类，用于在Spring应用中实现文件上传的功能
public class FileUploadUtil {
    // 用于存储文件上传的路径。默认路径为用户当前工作目录下的temp文件夹。
    private String path=System.getProperty("user.dir")+"/temp/";

    //定义了一个名为uploadFile的方法，用于实现文件上传功能。
    //该方法接收一个MultipartFile对象作为参数，表示要上传的文件。
    public String  uploadFile(MultipartFile file){
        System.out.println("path===="+path);
        String fileName=file.getOriginalFilename();
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        //log.info("开始上传文件，上传文件名{}，上传路径{}，新文件名{}",fileName,path,fileExtensionName);
        File fileDir=new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);
        try {
            //将MultipartFile对象中的文件内容传输到目标文件中
            file.transferTo(targetFile);
            //使用FtpUtil类的实例调用uploadFile()方法，将目标文件上传到FTP服务器。
            //最后，删除目标文件，确保文件上传后不会留下临时文件。
            new FtpUtil().uploadFile(uploadFileName,new FileInputStream(targetFile));
            targetFile.delete();
        } catch (IOException e) {
            return null;
        }
        return uploadFileName;
    }
}
