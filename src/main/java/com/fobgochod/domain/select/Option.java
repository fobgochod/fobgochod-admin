package com.fobgochod.domain.select;

import com.fobgochod.util.SnowFlake;

/**
 * 下拉项
 *
 * @author seven
 * @date 2021/3/13
 */
public class Option {

    private String key;
    private String value;
    private String label;
    private String other;

    public Option() {
        this.key = SnowFlake.getInstance().get();
    }

    public Option(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Option(String key, String value, String label) {
        this(key, value);
        this.label = label;
    }

    public Option(String key, String value, String label, String other) {
        this(key, value, label);
        this.other = other;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
