package com.whatistest.resource;

import com.whatistest.httpserver.HttpServerClient;
import com.whatistest.httpserver.HttpServerInterface;
import com.whatistest.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rubyvirusqq@gmail.com
 */
@RequestMapping("/servers")
@RestController
public class ServerResource {

    @Value("${spring.application.name}")
    private String serverName;

    @RequestMapping("/info")
    public Object getInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", serverName);
        return result;
    }

    @RequestMapping(value = "/encryption")
    public String getEncryption(@RequestParam String requestHost, @RequestParam String requestPath, @RequestParam String requestType, @RequestParam String requestData) {

        // 初始化两个对象，list存放参数，map存放参数与值
        List<String> list = new ArrayList<String>();
        Map<String, String> map = new HashMap<String, String>();
        String[] tmpRequestData = requestData.split("&");
        for (int x = 0; x < tmpRequestData.length; x++) {
            String[] strings = tmpRequestData[x].split("=");
            list.add(strings[0]);
            System.out.println(strings.length);
            if (strings[0].equals("nonce") && strings.length == 1) {
                map.put(strings[0], "12345678");
            } else if (strings[0].equals("timestamp") && strings.length == 1) {
                map.put(strings[0], TimeUtils.getTimeStamp());
            } else {
                map.put(strings[0], strings[1]);
            }
        }
        // 实例化秘钥计算实例
        EncryptionServer encryptionServer = new EncryptionServer();
        String result = encryptionServer.sort(list, map);

        // 添加秘钥
        map.put("signature", result);

        // 响应内容
        String responseContent = "";
        // 实例化http客户端
        HttpServerInterface httpServerInterface = new HttpServerClient();
        // 根据不同请求类型，调用后端不同请求
        if (requestType.equals("GET")) {
            responseContent = httpServerInterface.httpGet(requestHost, requestPath, map);
        } else if (requestType.equals("POST")) {
            responseContent = httpServerInterface.httpPost(requestHost, requestPath, map);
        }

        return responseContent;
    }
}
