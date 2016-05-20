package com.epam.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by gavin on 16-5-13.
 */
@Entity
public class PoliceForce{

    private static final long serialVersionUID = 6807079984712622546L;

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
        return "PoliceForce{" +
                "code=" + code +
                ", label='" + label + '\'' +
                '}';
    }
}
