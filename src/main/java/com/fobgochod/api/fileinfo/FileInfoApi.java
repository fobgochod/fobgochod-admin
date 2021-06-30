package com.fobgochod.api.fileinfo;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.file.FileInfo;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.service.client.DirectoryCrudService;
import com.fobgochod.service.client.FileInfoCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FileInfo 文件信息
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/fileinfo")
public class FileInfoApi {

    @Autowired
    private FileInfoCrudService fileInfoCrudService;
    @Autowired
    private DirectoryCrudService directoryCrudService;

    /**
     * 更新文件状态
     *
     * @param id
     * @return
     */
    @PutMapping("/{id}/{completed}")
    public StdData modify(@PathVariable String id,
                          @PathVariable boolean completed) {
        FileInfo fileInfo = fileInfoCrudService.findById(id);
        fileInfo.setCompleted(completed);
        fileInfoCrudService.update(fileInfo);
        return StdData.ofSuccess(fileInfo);
    }

    /**
     * 修改
     *
     * @param body
     * @return
     */
    @PutMapping
    public StdData modify(@RequestBody FileInfo body) {
        FileInfo fileInfo = fileInfoCrudService.findById(body.getId());
        if (fileInfo == null) {
            throw new BusinessException("文件不存在" + body.getId());
        }
        if (!directoryCrudService.exists(body.getDirectoryId())) {
            throw new BusinessException("目录不存在" + body.getDirectoryId());
        }
        if (body.getName() != null) {
            fileInfo.setName(body.getName());
        }
        fileInfo.setDisplayName(body.getDisplayName());
        fileInfo.setTag(body.getTag());
        if (body.getExtension() != null) {
            fileInfo.setExtension(body.getExtension());
        }
        if (body.getContentType() != null) {
            fileInfo.setContentType(body.getContentType());
        }
        fileInfo.setExpireDate(body.getExpireDate());
        fileInfo.setDirectoryId(body.getDirectoryId());
        fileInfo.setMetadata(body.getMetadata());
        fileInfoCrudService.update(fileInfo);
        return StdData.ofSuccess(fileInfo);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public StdData findById(@PathVariable String id) {
        return StdData.ofSuccess(fileInfoCrudService.findById(id));
    }

    /**
     * 查询
     *
     * @return
     */
    @GetMapping
    public StdData find() {
        return StdData.ofSuccess(fileInfoCrudService.findAll());
    }

    /**
     * 分页查询
     *
     * @param body
     * @return
     */
    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(fileInfoCrudService.findByPage(body));
    }

    @DeleteMapping("/drop")
    public StdData dropCollection() {
        fileInfoCrudService.dropCollection();
        return StdData.ok();
    }

    /**
     * 查询目录下文件
     *
     * @param directoryId
     * @return
     */
    @GetMapping("/directory/{directoryId}")
    public StdData findByDirectoryId(@PathVariable String directoryId) {
        return StdData.ofSuccess(fileInfoCrudService.findByDirId(directoryId));
    }
}
