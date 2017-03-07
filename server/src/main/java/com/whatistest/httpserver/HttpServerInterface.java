package com.whatistest.httpserver;

import java.util.Map;

/**
 * Created by rubyvirusqq@gmail.com on 2017-1-24.
 */
public interface HttpServerInterface {
    public String httpPost(String requestHost, String requestPath, Map<String, String> params);

    public String httpGet(String requestHost, String requestPath, Map<String, String> params);

}
