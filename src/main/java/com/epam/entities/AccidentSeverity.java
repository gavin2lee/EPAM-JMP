package com.epam.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by gavin on 16-5-13.
 */
@Entity
public class AccidentSeverity{
    private Integer code;
    private String label;

    @Id
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "AccidentSeverity{" +
                "code=" + code +
                ", label='" + label + '\'' +
                '}';
    }
}
