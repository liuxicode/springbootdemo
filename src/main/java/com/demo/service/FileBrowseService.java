package com.demo.service;

import com.demo.utils.File2PdfUtils;
import com.demo.utils.FileDownUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: liuxi
 * @Date: 2018/9/20 18:18
 * @Description:
 */
@Service
public class FileBrowseService {

    private static Logger logger = LoggerFactory.getLogger(FileBrowseService.class);

    public String getFileBase64Code(String fileUrl){

        /*if(StringUtils.isBlank(fileUrl)){
            return "";
        }*/

        String fileType = getFileType(fileUrl);

        /*
        if(StringUtils.isBlank(fileType)){
            return "";
        }*/

        File2PdfUtils file2PdfUtils = File2PdfUtils.getFile2PdfUtilsInstance();

        InputStream in = FileDownUtils.download(fileUrl);

        if(in != null){
            String pdfString = file2PdfUtils.getPdfString(fileType,in);

            return pdfString;
        }

        return "";
    }

    private static String getFileType(String fileUrl){

       if(fileUrl.contains(".")){
           String[] strs = fileUrl.split("\\.");

           int size = strs.length;
           if(size > 1){
               return strs[size-1];
           }
       }

       return "";
    }

    public String testCode(String name) throws Exception {

        logger.info("------------------testCode start ----------------------");

        File2PdfUtils file2PdfUtils = File2PdfUtils.getFile2PdfUtilsInstance();

        /*String filename = "test.doc";
        Resource resource = new ClassPathResource("/file/jdk8.doc");
        File file = resource.getFile();*/
        //File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"file/jdk8.doc");

        //InputStream in = new FileInputStream(file);

        InputStream in = FileBrowseService.class.getResourceAsStream("/file/"+name);

        if(in != null){

            logger.info("1.file:"+in);

            String pdfString = file2PdfUtils.getPdfString("doc",in);

            return pdfString;
        }else{
            logger.info("InputStream  is null");

        }

        logger.info("------------------testCode end ----------------------");

        return "";
    }
}
