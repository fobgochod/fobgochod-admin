package com.fobgochod.support.editor;

import com.fobgochod.entity.file.FileInfo;

import java.beans.PropertyEditorSupport;

public class FileInfoPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(FileInfo.get(text));
    }
}
