package com.demo.utils;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * 利用jodconverter(基于OpenOffice服务)将文件(*.doc、*.docx、*.xls、*.ppt)转化为html格式或者pdf格式，
 * 使用前请检查OpenOffice服务是否已经开启, OpenOffice进程名称：soffice.exe | soffice.bin
 * @author lx
 * @Date: 2018/9/19 09:19
 * @Description:
 */
public class File2PdfUtils {

    private static Logger logger = LoggerFactory.getLogger(File2PdfUtils.class);

    private static File2PdfUtils file2PdfUtils;

    /**
     * 获取Doc2HtmlUtil实例
     */
    public static synchronized File2PdfUtils getFile2PdfUtilsInstance() {
        if (file2PdfUtils == null) {
            file2PdfUtils = new File2PdfUtils();
        }
        return file2PdfUtils;
    }

    /**
     * 转化为 PDF String
     * @param fileEnd           文件后缀（doc , xls等）
     * @param filePathName     文件路径
     * @return
     * @throws Exception
     */
    public String getPdfString(String fileEnd, String filePathName){

        try {

            File file = new File(filePathName);
            FileInputStream fileInputStream = new FileInputStream(file);

            InputStream inputStream = file2PdfUtils.getPdfStream(fileEnd,fileInputStream);

            byte[] bytes = InputStreamToByte(inputStream);

            Base64.Encoder encoder = Base64.getEncoder();

            return encoder.encodeToString(bytes);

        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 转化为 PDF String
     * @param fileEnd      文件后缀（doc , xls等）
     * @param fileInput    文件流
     * @return
     */
    public String getPdfString(String fileEnd, InputStream fileInput){

        try {

            logger.info("start to pdfString");

            InputStream inputStream = file2PdfUtils.getPdfStream(fileEnd,fileInput);

            byte[] bytes = InputStreamToByte(inputStream);

            Base64.Encoder encoder = Base64.getEncoder();

            String base64Str = encoder.encodeToString(bytes);

            logger.info("base64:"+base64Str);

            return base64Str;

        } catch (Exception e) {

            logger.info("getPdfString fail:"+e.getMessage());

            return null;
        }

    }

    public byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int ch;

        while ((ch = is.read(buffer)) != -1) {
            bytestream.write(buffer,0,ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

    /**
     * 转换文件成pdf流
     *
     * @param fileEnd:
     * @param fileInput
     * @throws IOException
     */
    public InputStream getPdfStream(String fileEnd, InputStream fileInput) throws Exception{
        String fileType = "";
        if("xlsx".equals(fileEnd)) {             //xlsx格式的文件转成xls处理
            fileType = "xls";
        }else if("docx".equals(fileEnd)){        //docx格式的文件转成doc处理
            fileType = "doc";
        }else {
            fileType = fileEnd;
        }
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();                //连接openoffice
            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);   //使用StreamOpenOfficeDocumentConverter可以转07版的
            DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();     //jar包里的类
            DocumentFormat inputFormat = formatReg.getFormatByFileExtension(fileType);         //源文件的格式
            DocumentFormat pdfFormat = formatReg.getFormatByFileExtension("pdf");              //转成的格式
            ByteArrayOutputStream pdfstream = new ByteArrayOutputStream();                     //保存转成pdf的流的数组
            converter.convert(fileInput, inputFormat, pdfstream, pdfFormat);                   //将文件流转换成pdf流
            InputStream pdfInput = new BufferedInputStream(new ByteArrayInputStream(pdfstream.toByteArray()));//把pdf流转成输入流
            pdfstream.flush();
            pdfstream.close();
            return pdfInput;
        } catch(Exception e) {
            logger.info("OpenOfficeConnection error:"+e.getMessage());
        } finally {
            try{
                if(connection != null){
                    connection.disconnect();
                    connection = null;
                }
            }catch(Exception e){
                logger.info("OpenOfficeConnection close error:"+e.getMessage());
            }
        }
        return null;
    }

    /**
     * 转换文件成pdf
     *
     * @param fromFileInputStream:
     * @throws IOException
     */
    public boolean file2pdfString(InputStream fromFileInputStream, String toFilePath,String type) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if(".doc".equals(type)){
            docFileName = "doc_" + timesuffix + ".doc";
            htmFileName = "doc_" + timesuffix + ".pdf";
        }else if(".docx".equals(type)){
            docFileName = "docx_" + timesuffix + ".docx";
            htmFileName = "docx_" + timesuffix + ".pdf";
        }else if(".xls".equals(type)){
            docFileName = "xls_" + timesuffix + ".xls";
            htmFileName = "xls_" + timesuffix + ".pdf";
        }else if(".ppt".equals(type)){
            docFileName = "ppt_" + timesuffix + ".ppt";
            htmFileName = "ppt_" + timesuffix + ".pdf";
        }else{
            return false;
        }

        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists())
            htmlOutputFile.delete();
        htmlOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
        } catch (ConnectException e) {
            System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
        }
        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(docInputFile, htmlOutputFile);
        connection.disconnect();
        // 转换完之后删除word文件
        docInputFile.delete();
        return true;
    }


}
