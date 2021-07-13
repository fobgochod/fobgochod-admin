package com.fobgochod.api.admin;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.domain.select.Option;
import com.fobgochod.domain.select.Options;
import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.TenantRepository;
import com.fobgochod.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tenant 系统用户
 *
 * @author zhouxiao
 * @date 2020/11/11
 */
@RestController
@RequestMapping("/tenants")
public class TenantApi {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Tenant body) {
        String id = tenantRepository.insert(body);
        return ResponseEntity.ok(tenantRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        tenantRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody Tenant body) {
        Tenant tenant = tenantRepository.findById(body.getId());
        if (tenant != null) {
            tenant.setEmail(body.getEmail());
            tenantRepository.update(body);
        }
        return ResponseEntity.ok(tenant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(tenantRepository.findById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(tenantRepository.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(tenantRepository.deleteByIdIn(body.getIds()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        tenantRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/option")
    public ResponseEntity<?> option() {
        List<Option> options = new ArrayList<>();
        List<Tenant> tenants = tenantRepository.findAll();
        tenants.forEach(o -> options.add(new Option(o.getCode(), o.getName())));
        return ResponseEntity.ok(options);
    }

    @GetMapping("/option/group")
    public ResponseEntity<?> optionGroup(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO_KEY) JwtUser userInfo) {

        List<Options> optionGroup = new ArrayList<>();
        List<Tenant> myBuckets = tenantRepository.findByOwner(userInfo.getUsername());

        Options myOptions = new Options();
        for (Tenant bucket : myBuckets) {
            myOptions.getOptions().add(new Option(bucket.getCode(), bucket.getName(), bucket.getOwner()));
        }
        myOptions.setKey(99);
        myOptions.setLabel("我的租户");
        optionGroup.add(myOptions);

        User user = userRepository.findByCode(userInfo.getUsername());
        if (RoleEnum.Admin.equals(user.getRole())) {
            List<Tenant> buckets = tenantRepository.findAll();
            Options options = new Options();
            for (Tenant bucket : buckets) {
                options.getOptions().add(new Option(bucket.getCode(), bucket.getName(), bucket.getOwner()));
            }
            options.setKey(1);
            options.setLabel("租户");
            optionGroup.add(options);
        }
        return ResponseEntity.ok(optionGroup.stream().sorted(Comparator.comparing(Options::getKey).reversed()).collect(Collectors.toList()));
    }
}
