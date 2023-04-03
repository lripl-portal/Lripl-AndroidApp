package com.lripl.mvp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    private String serverStringResponse;
    private int serverHttpStatusCode;

    public void setserverHttpStatusCode(int serverHttpStatusCode) {
        this.serverHttpStatusCode = serverHttpStatusCode;
    }

    public void setserverStringResponse(String serverStringResponse) {
        this.serverStringResponse = serverStringResponse;
    }

    public int getserverHttpStatusCode() {
        return serverHttpStatusCode;
    }

    public String getserverStringResponse() {
        return serverStringResponse;
    }
}
