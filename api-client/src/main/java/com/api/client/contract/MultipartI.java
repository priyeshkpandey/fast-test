package com.api.client.contract;

import java.io.File;

public interface MultipartI {
    public String getControlName();
    public String getContentBody();
    public String getMimeType();
    public File getFile();
}
