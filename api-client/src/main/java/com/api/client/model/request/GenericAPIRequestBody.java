package com.api.client.model.request;

import com.api.client.contract.APIRequestBody;
import com.api.client.contract.MultipartI;
import com.api.client.contract.RequestBodyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
public class GenericAPIRequestBody implements APIRequestBody {
    private RequestBodyType type;
    private Object object;
    private List<MultipartI> multipartBodyList;
}
