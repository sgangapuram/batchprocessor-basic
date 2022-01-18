package com.srini.batch.batchprocessor;

import java.util.Objects;

public class LowerPerson {

    private String lowerFirstName;
    private String lowerLastName;


    public LowerPerson() {
    }

    public LowerPerson(String lowerFirstName, String lowerLastName) {
        this.lowerFirstName = lowerFirstName;
        this.lowerLastName = lowerLastName;
    }

    public String getLowerFirstName() {
        return lowerFirstName;
    }

    public void setLowerFirstName(String lowerFirstName) {
        this.lowerFirstName = lowerFirstName;
    }

    public String getLowerLastName() {
        return lowerLastName;
    }

    public void setLowerLastName(String lowerLastName) {
        this.lowerLastName = lowerLastName;
    }


    @Override
    public String toString() {
        return "LowerPerson{" +
                "lowerFirstName='" + lowerFirstName + '\'' +
                ", lowerLastName='" + lowerLastName + '\'' +
                '}';
    }
}
