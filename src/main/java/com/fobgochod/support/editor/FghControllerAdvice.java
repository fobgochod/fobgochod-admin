package com.fobgochod.support.editor;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.entity.file.FileInfo;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class FghControllerAdvice {

    /**
     * 将Header里面的{@link FghConstants#HTTP_HEADER_API_ARG_KEY}json字符串转换成 {@link FileInfo}对象
     */
    @InitBinder(FghConstants.HTTP_HEADER_API_ARG_KEY)
    public void fileInfoBinder(WebDataBinder binder) {
        binder.registerCustomEditor(FileInfo.class, new FileInfoPropertyEditor());
    }
}
