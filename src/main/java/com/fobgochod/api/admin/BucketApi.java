package com.fobgochod.api.admin;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.domain.select.Option;
import com.fobgochod.domain.select.Options;
import com.fobgochod.domain.v2.BatchFid;
import com.fobgochod.domain.v2.BucketVO;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.entity.admin.Bucket;
import com.fobgochod.entity.admin.User;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.repository.BucketRepository;
import com.fobgochod.repository.TaskRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.util.AESCipher;
import com.fobgochod.util.UserUtil;
import com.mongodb.MongoNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bucket 存储区
 *
 * @author zhouxiao
 * @date 2020/10/29
 */
@RestController
@RequestMapping("/buckets")
public class BucketApi {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BucketRepository bucketRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Bucket body) {
        MongoNamespace.checkDatabaseNameValidity(body.getCode());
        String id = bucketRepository.insert(body);
        return ResponseEntity.ok(bucketRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        bucketRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody Bucket body) {
        bucketRepository.update(body);
        return ResponseEntity.ok(bucketRepository.findById(body.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(bucketRepository.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(bucketRepository.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(bucketRepository.deleteByIdIn(body.getIds()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        bucketRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    /**
     * 注册Bucket
     *
     * @param body
     * @return
     */
    @PostMapping("/apply")
    public ResponseEntity<?> applyBucket(@RequestBody BucketVO body) {
        MongoNamespace.checkDatabaseNameValidity(body.getBucket());
        if (StringUtils.isEmpty(body.getOwner()) || StringUtils.isEmpty(body.getPassword())) {
            throw new BusinessException("所有者和密码都不能为空");
        }
        String pwdHash = AESCipher.getSHA256(body.getPassword());
        User oldUser = userRepository.findByCode(body.getOwner());
        if (oldUser != null) {
            // 用户存在，检查密码
            User oldCheck = userRepository.findByCodeAndPassword(body.getOwner(), pwdHash);
            if (oldCheck == null) {
                throw new BusinessException(String.format("用户[%s]已经存在，请输入正确密码", body.getOwner()));
            }
        }
        Bucket oldBucket = bucketRepository.findByCode(body.getBucket());
        if (oldBucket != null) {
            throw new BusinessException(String.format("存储区[%s]已经存在", body.getBucket()));
        }
        if (body.getTask() != null && !taskRepository.existsByCode(body.getTask())) {
            throw new BusinessException("策略不存在" + body.getTask());
        }
        Bucket bucket = new Bucket();
        bucket.setCode(body.getBucket());
        bucket.setOwner(body.getOwner());
        bucket.setTask(body.getTask());
        bucket.setName(body.getDescription());
        bucketRepository.insert(bucket);
        if (oldUser == null) {
            // 用户不存在，创建用户
            User user = new User();
            user.setName(body.getOwner());
            user.setEmail(body.getOwner());
            user.setPassword(pwdHash);
            user.setRole(RoleEnum.Owner);
            userRepository.insert(user);
        }
        return ResponseEntity.ok(pwdHash);
    }

    /**
     * 设置Bucket策略
     *
     * @return
     */
    @PostMapping("/task")
    public ResponseEntity<?> modifyTask(@RequestBody BucketVO body) {
        Bucket bucket = bucketRepository.findByCode(body.getBucket());
        if (body.getTask() != null && !taskRepository.existsByCode(body.getTask())) {
            throw new BusinessException("策略不存在" + body.getTask());
        }
        bucket.setTask(body.getTask());
        bucketRepository.update(bucket);
        return ResponseEntity.ok(bucket);
    }

    /**
     * 编辑Bucket
     *
     * @param body
     * @return
     */
    @PostMapping("/edit")
    public ResponseEntity<?> modify(@RequestBody BucketVO body) {
        Bucket bucket = bucketRepository.findByCode(body.getBucket());
        if (body.getTask() != null && !taskRepository.existsByCode(body.getTask())) {
            throw new BusinessException("策略不存在" + body.getTask());
        }
        bucket.setName(body.getDescription());
        bucket.setTask(body.getTask());
        bucket.setTenantId(body.getTenantId());
        bucketRepository.update(bucket);
        return ResponseEntity.ok(bucket);
    }

    @GetMapping("/owner")
    public ResponseEntity<?> owner() {
        return ResponseEntity.ok(bucketRepository.findByOwner(UserUtil.getUserName()));
    }

    /**
     * 获取Bucket可选策略
     *
     * @return
     */
    @GetMapping("/task")
    public ResponseEntity<?> bucketTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/option")
    public ResponseEntity<?> bucketSelect(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO_KEY) JwtUser userInfo) {
        List<Options> optionGroup = new ArrayList<>();
        List<Bucket> myBuckets = bucketRepository.findByOwner(userInfo.getUsername());
        Options myOptions = new Options();
        for (Bucket bucket : myBuckets) {
            myOptions.getOptions().add(new Option(bucket.getCode(), bucket.getName(), bucket.getOwner()));
        }
        myOptions.setKey(99);
        myOptions.setLabel("我的存储区");
        optionGroup.add(myOptions);

        User user = userRepository.findByCode(userInfo.getUsername());
        if (RoleEnum.Admin.equals(user.getRole())) {
            List<Bucket> buckets = bucketRepository.findAll();
            Options options = new Options();
            for (Bucket bucket : buckets) {
                options.getOptions().add(new Option(bucket.getCode(), bucket.getName(), bucket.getOwner()));
            }
            options.setKey(1);
            options.setLabel("存储区");
            optionGroup.add(options);
        }
        return ResponseEntity.ok(optionGroup.stream().sorted(Comparator.comparing(Options::getKey).reversed()).collect(Collectors.toList()));
    }
}
