package com.fobgochod.domain.select;

/**
 * 下拉项
 *
 * @author seven
 * @date 2021/3/13
 */
public class Option {

    private long key;
    private String value;
    private String label;
    private String other;

    public Option() {
        this.key = System.currentTimeMillis();
    }

    public Option(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public Option(String value, String label, String other) {
        this.value = value;
        this.label = label;
        this.other = other;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
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
