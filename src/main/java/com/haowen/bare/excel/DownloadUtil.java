package com.haowen.bare.excel;

import com.alibaba.excel.util.IoUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtil {

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws Exception{

        InputStream inputStream = null;
        FileOutputStream fos = null;

        try {

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 得到输入流
            inputStream = conn.getInputStream();

            // 文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            fos = new FileOutputStream(file);

            int len = 0;
            //读取文件输入流，写入到输出流ByteArray中，输入流转成了输出流
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf)) != -1) {
                fos.write(buf,0,len);
            }

            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (fos != null){
                fos.close();
            }

            if (inputStream != null){
                inputStream.close();
            }

        }
    }

}
