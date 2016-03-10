package com.bssys.ws_nio2;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpRequest {
	 
    private final String raw;
    private String method;
    private String location;
    private String version;
    private Map<String, String> headers = new HashMap<String, String>();

    public HttpRequest(String raw) {
        this.raw = raw;
        parse();
    }

    private void parse() {
        // parse the first line
        StringTokenizer tokenizer = new StringTokenizer(raw);
        method = tokenizer.nextToken().toUpperCase();
        location = tokenizer.nextToken();
        version = tokenizer.nextToken();
        // parse the headers
        String[] lines = raw.split("\r\n");
        for (int i = 1; i < lines.length; i++) {
            String[] keyVal = lines[i].split(":", 2);
            headers.put(keyVal[0], keyVal[1]);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getLocation() {
        return location;
    }

    public String getHead(String key) {
        return headers.get(key);
    }
    
    public Map<String, String> getHeaders(){
    	return headers;
    }
}
