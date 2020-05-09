package com.xinyu.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FileUpDownLoadUtils {
	
	private static String usrDir = "C:\\xinyu\\orderImage\\";

	/**
     * 根据路径确定目录，没有目录，则创建目录
     * 
     * @param path
     */
    private static void createDir(String path) {
        File fileDir = new File(path);
        if (!fileDir.exists() && !fileDir.isDirectory()) {// 判断/download目录是否存在
            fileDir.mkdir();// 创建目录
        }
    }

    /**
     * 将文件名解析成文件的上传路径
     * 
     * @param fileName
     * @return 上传到服务器的文件名
     */
    public static String transPath(String fileName) {
    	String path = "";
        createDir(usrDir);
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddhhmmssSSS");// 定义到毫秒
        String nowStr = dateformat.format(date);
        String filenameStr = fileName.substring(0, fileName.lastIndexOf("."));// 去掉后缀的文件名
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);// 后缀
        if (fileName.trim() != "") {// 如果名称不为"",说明该文件存在，否则说明该文件不存在
            path += filenameStr +"-"+ nowStr + "." + suffix;// 定义上传路径
        }
        return path;
    }

    /**
     * 提醒文件下载
     * 
     * @param fileName
     * @param path
     * @return
     */
    public static ResponseEntity<byte[]> downloadFile(String fileName, String path) {
        try {
            fileName = new String(fileName.getBytes("GB2312"), "ISO_8859_1");// 避免文件名中文不显示
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);

        ResponseEntity<byte[]> byteArr = null;
        try {
            byteArr = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArr;
    }
    
    /**
	 * 一般文件上传都会改名
	 * 
	 * @param file	要下载的文件
	 * @param response
	 */
	public static void download(String path, HttpServletResponse response) {
		try {
			File file = new File(usrDir + path);
			if (!file.exists()) {
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().print("找不到指定的文件");
			} else {
				// 以流的形式下载文件。
				InputStream fis = new BufferedInputStream(new FileInputStream(file));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				// 清空response
				response.reset();
				// 设置response的Header
				response.addHeader("Content-Disposition", "attachment;filename="
						+ new String(path.getBytes("GBK"), "ISO-8859-1"));
				response.addHeader("Content-Length", "" + file.length());
				OutputStream toClient = new BufferedOutputStream(response
						.getOutputStream());
				response.setContentType("application/octet-stream");
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * 将输入流中的数据写入字节数组
     * 
     * @param in
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream in, boolean isClose) {
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isClose) {
                try {
                    in.close();
                } catch (Exception e2) {
                    System.out.println("关闭流失败");
                }
            }
        }
        return byteArray;
    }
    
    public static String uploadFile(byte[] file, String fileName) throws Exception {
    	String path = FileUpDownLoadUtils.transPath(fileName);
        FileOutputStream out = new FileOutputStream(usrDir+path);
        out.write(file);
        out.flush();
        out.close();
        return path;
    }
 
    public static ResponseEntity<InputStreamResource> downloadFile(String url) throws IOException {
        FileSystemResource file = new FileSystemResource(url);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        //设置文件名并设置文件名编码，解决文件名异常问题
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", new
                String(file.getFilename().getBytes("GBK"),"ISO8859-1")));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
 
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
    
    public static void deleteFile(String pathnames){
    	String[] files = pathnames.split(";");
		for(String filepath : files) {
			File file = new File(usrDir+filepath);
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
