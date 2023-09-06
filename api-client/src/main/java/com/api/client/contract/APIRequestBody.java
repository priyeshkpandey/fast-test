package com.api.client.contract;

import java.util.List;

public interface APIRequestBody {
    public RequestBodyType getType();
    public Object getObject();
    public List<MultipartI> getMultipartBodyList();
}
