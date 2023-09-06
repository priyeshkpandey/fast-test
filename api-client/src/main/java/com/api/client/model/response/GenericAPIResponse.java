package com.api.client.model.response;

import com.api.client.contract.APIResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GenericAPIResponse implements APIResponse {
    private Object object;
    private String statusCode;
    private String status;
}
