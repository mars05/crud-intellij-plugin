package com.github.mars05.crud.intellij.plugin.rpc;

import com.alibaba.fastjson.JSON;
import com.github.mars05.crud.hub.common.exception.BizException;
import com.github.mars05.crud.intellij.plugin.rpc.request.Request;
import com.github.mars05.crud.intellij.plugin.rpc.response.Response;
import com.github.mars05.crud.intellij.plugin.util.CrudUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author xiaoyu
 */
public class HubClient {
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private final String url;

    public HubClient() {
        this.url = "https://api-gateway.crud-hub.top/";
    }

    public HubClient(String url) {
        this.url = url;
    }

    private void check() {
        if (null == url) {
            throw new IllegalArgumentException("url不能为空");
        }
    }

    public <T extends Response> T execute(Request<T> request) {
        check();
        HttpURLConnection conn = null;
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(StringUtils.removeEnd(url, "/"));
            urlBuilder.append(request.getPath());
            if (METHOD_GET.equalsIgnoreCase(request.getMethod())) {
                List<String> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : request.getQuery().entrySet()) {
                    paramList.add(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), CrudUtils.UTF_8.toString()));
                }
                if (!paramList.isEmpty()) {
                    urlBuilder.append("?").append(String.join("&", paramList));
                }
            }
            URL url = new URL(urlBuilder.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(request.getMethod());
            setHeader(conn);
            if (METHOD_POST.equalsIgnoreCase(request.getMethod())) {
                setRequestBody(conn, request.getBody());
            }
            String responseBody = getResponseBody(conn);
            Class<? extends Response> responseClass = request.getResponseClass();
            return parseBody(responseBody, responseClass);
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }

    private <T extends Response> T parseBody(String body, Class<? extends Response> clazz) {
        try {
            Response response = JSON.parseObject(body, clazz);
            response.setBody(body);
            return (T) response;
        } catch (Exception e) {
            throw new IllegalStateException("服务端响应数据格式错误");
        }
    }

    private void setRequestBody(HttpURLConnection conn, String reqParam) throws IOException {
        OutputStream out = conn.getOutputStream();
        out.write(reqParam.getBytes());
        out.flush();
        out.close();
    }

    private String getResponseBody(HttpURLConnection conn) throws IOException {
        try {
            InputStream input = conn.getInputStream();
            String ce = conn.getHeaderField("Content-Encoding");
            if ("gzip".equalsIgnoreCase(ce)) {
                input = new GZIPInputStream(input);
            }
            return copyToString(input);
        } finally {
            conn.getInputStream().close();
        }
    }

    private String copyToString(InputStream in) throws IOException {
        if (in == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in, CrudUtils.UTF_8);
        char[] buffer = new char[4096];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            out.append(buffer, 0, bytesRead);
        }
        return out.toString();
    }

    private void setHeader(HttpURLConnection conn) {
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//        /*conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");*/
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setUseCaches(false);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
    }
}
