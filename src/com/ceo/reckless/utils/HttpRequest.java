package com.ceo.reckless.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static String cookieContent = null;

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if (param != null && !param.equals("")) {
                urlNameString += "?" + param;
            }
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "gzip");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 处理cookie
            if (map.containsKey("Set-Cookie")) {
                List cookieValueList = map.get("Set-Cookie");
                for (Object item : cookieValueList) {
                    String itemString = (String) item;
                    String[] array = itemString.split(" path=");    // 根据cookie格式把name=value后面的expire啥的都切掉
                    if (array != null && array.length > 1) {
                        cookieContent += array[0];
                    }
                }
            }

            return IoStreamUtils.readUTF8(connection.getInputStream());

            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param propMap
     *            用于发送自定义的prop参数,map中的内容会通过setRequestProperty传入connection对象
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, Map<String, String> propMap) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if (param != null && !param.equals("")) {
                urlNameString += "?" + param;
            }
            URL realUrl = new URL(urlNameString);

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();

            if (propMap == null || propMap.size() == 0) {
                LogUtils.logDebugLine("property map empty");
                return result;
            }

            // 设置通用的请求属性
            for (Map.Entry<String, String> itemEntry : propMap.entrySet()) {
                String key = itemEntry.getKey();
                String value = itemEntry.getValue();
                connection.setRequestProperty(key, value);
            }

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 处理cookie
            if (map.containsKey("Set-Cookie")) {
                List cookieValueList = map.get("Set-Cookie");
                for (Object item : cookieValueList) {
                    String itemString = (String) item;
                    String[] array = itemString.split(" path=");    // 根据cookie格式把name=value后面的expire啥的都切掉
                    if (array != null && array.length > 1) {
                        cookieContent += array[0];
                    }
                }
            }

            return IoStreamUtils.readUTF8(connection.getInputStream());

            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGetWithCookie(String url, String param, String cookieUrl, String cookieParam) {
        String result = "";
        BufferedReader in = null;
        try {

            if (cookieContent == null) {
                sendGet(cookieUrl, cookieParam);
            }

            String urlNameString = url;
            if (param != null && !param.equals("")) {
                urlNameString += "?" + param;
            }

//            LogUtils.logDebugLine(urlNameString);

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            if (cookieContent != null) {
                connection.setRequestProperty("Cookie", cookieContent);
            }
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "gzip");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }

            return IoStreamUtils.readUTF8(connection.getInputStream());

            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGetWithCookie(String url, String param, Map<String, String> propMap, String cookieUrl, String cookieParam) {
        String result = "";
        BufferedReader in = null;
        try {

            if (cookieContent == null) {
                sendGet(cookieUrl, cookieParam);
            }

            String urlNameString = url;
            if (param != null && !param.equals("")) {
                urlNameString += "?" + param;
            }

//            LogUtils.logDebugLine(urlNameString);

            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            if (cookieContent != null) {
                connection.setRequestProperty("Cookie", cookieContent);
            }

            if (propMap == null || propMap.size() == 0) {
                LogUtils.logDebugLine("property map empty");
                return result;
            }

            // 设置通用的请求属性
            for (Map.Entry<String, String> itemEntry : propMap.entrySet()) {
                String key = itemEntry.getKey();
                String value = itemEntry.getValue();
                connection.setRequestProperty(key, value);
            }

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }

            return IoStreamUtils.readUTF8(connection.getInputStream());

            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
}
