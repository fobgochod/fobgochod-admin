package com.fobgochod.api.admin;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.select.Option;
import com.fobgochod.entity.admin.Role;
import com.fobgochod.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User 系统用户
 *
 * @author zhouxiao
 * @date 2020/11/11
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody Role body) {
        String id = roleRepository.insert(body);
        return ResponseEntity.ok(roleRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody Role body) {
        roleRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody Role body) {
        roleRepository.update(body);
        return ResponseEntity.ok(roleRepository.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody Role body) {
        return ResponseEntity.ok(roleRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<Role> body) {
        return ResponseEntity.ok(roleRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        roleRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(roleRepository.deleteByIdIn(body.getIds()));
    }

    @GetMapping("/option")
    public ResponseEntity<?> option() {
        List<Option> options = new ArrayList<>();
        List<Role> roles = roleRepository.findAll();
        roles.forEach(o -> options.add(new Option(o.getId(), o.getCode(), o.getName())));
        return ResponseEntity.ok(options);
    }
}
