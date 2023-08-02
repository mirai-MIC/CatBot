package org.Simbot.utils.HttpClientUtils;

import org.asynchttpclient.RequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamsBuilder {

    private final Map<String, List<String>> params = new HashMap<>();

    public static ParamsBuilder create() {
        return new ParamsBuilder();
    }

    public ParamsBuilder addParam(String name, String value) {
        List<String> values = params.getOrDefault(name, new ArrayList<>());
        values.add(value);
        params.put(name, values);
        return this;
    }

    public ParamsBuilder addParams(Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            addParam(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public RequestBuilder applyParams(RequestBuilder requestBuilder) {
        requestBuilder.setFormParams(params);
        return requestBuilder;
    }

    public RequestBuilder applyParams(RequestBuilder requestBuilder, Map<String, String> params) {
        addParams(params);
        return applyParams(requestBuilder);
    }
}
