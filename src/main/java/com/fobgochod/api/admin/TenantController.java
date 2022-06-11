package com.fobgochod.api.admin;

import com.fobgochod.auth.holder.UserDetails;
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
import com.fobgochod.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tenant 系统用户
 *
 * @author zhouxiao
 * @date 2020/11/11
 */
@RestController
@RequestMapping("/tenants")
public class TenantController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody Tenant body) {
        String id = tenantRepository.insert(body);
        return ResponseEntity.ok(tenantRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody Tenant body) {
        tenantRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody Tenant body) {
        Tenant tenant = tenantRepository.findById(body.getId());
        if (tenant != null) {
            tenant.setEmail(body.getEmail());
            tenantRepository.update(body);
        }
        return ResponseEntity.ok(tenant);
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody Tenant body) {
        return ResponseEntity.ok(tenantRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<Tenant> body) {
        if (!FghConstants.ADMIN_USER.equals(UserUtil.getUserCode())) {
            Tenant baseEntity = body.getFilter().getEq();
            baseEntity.setOwner(UserUtil.getUserCode());
        }
        return ResponseEntity.ok(tenantRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        tenantRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(tenantRepository.deleteByIdIn(body.getIds()));
    }

    @GetMapping("/option")
    public ResponseEntity<?> option() {
        List<Option> options = new ArrayList<>();
        List<Tenant> tenants = tenantRepository.findAll();
        tenants.forEach(o -> options.add(new Option(o.getId(), o.getCode(), o.getName(), o.getOwner())));
        return ResponseEntity.ok(options);
    }

    @GetMapping("/option/group")
    public ResponseEntity<?> optionGroup(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO) UserDetails userDetails) {

        List<Tenant> tenants;
        User user = userRepository.findByCode(userDetails.getUserCode());
        if (RoleEnum.Admin.name().equals(user.getRole())) {
            tenants = tenantRepository.findAll();
        } else {
            tenants = tenantRepository.findByOwner(userDetails.getUserCode());
        }

        List<Options> optionGroup = new ArrayList<>();
        Map<String, List<Tenant>> tenantsMap = tenants.stream().collect(Collectors.groupingBy(Tenant::getOwner));
        for (Map.Entry<String, List<Tenant>> entry : tenantsMap.entrySet()) {
            Options options = new Options();
            for (Tenant tenant : entry.getValue()) {
                options.getOptions().add(new Option(tenant.getId(), tenant.getCode(), tenant.getName(), tenant.getOwner()));
            }
            User temp = userRepository.findByCode(entry.getKey());
            options.setKey(temp.getId());
            options.setLabel(temp.getName());
            optionGroup.add(options);
        }
        return ResponseEntity.ok(optionGroup.stream()
                .sorted(Comparator.comparing(Options::getKey))
                .collect(Collectors.toList()));
    }
}
