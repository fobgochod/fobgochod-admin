package com.fobgochod.domain.select;

import java.util.ArrayList;
import java.util.List;

/**
 * 下拉分组
 *
 * @author seven
 * @date 2021/3/13
 */
public class Options {

    private long key;
    private String label;
    private List<Option> options;

    public Options() {
        this.options = new ArrayList<>();
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
