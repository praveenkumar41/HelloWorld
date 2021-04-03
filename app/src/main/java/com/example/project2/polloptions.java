package com.example.project2;

import java.io.Serializable;

public class polloptions implements Serializable {

    public String cricketerName;

    public polloptions() {

    }

    public polloptions(String cricketerName) {
        this.cricketerName = cricketerName;
    }

    public String getCricketerName() {
        return cricketerName;
    }

    public void setCricketerName(String cricketerName) {
        this.cricketerName = cricketerName;
    }

}