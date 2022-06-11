package com.fobgochod.api.admin;

import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.PasswordVO;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.select.Option;
import com.fobgochod.domain.select.Options;
import com.fobgochod.entity.BaseEntity;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.support.security.Encrypt;
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
 * User 系统用户
 *
 * @author zhouxiao
 * @date 2020/11/11
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Encrypt
    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody User body) {
        String id = userRepository.insert(body);
        return ResponseEntity.ok(userRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody User body) {
        userRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @Encrypt
    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody User body) {
        User user = userRepository.findById(body.getId());
        if (user != null) {
            user.setEmail(body.getEmail());
            user.setRole(body.getRole());
            userRepository.update(body);
        }
        return ResponseEntity.ok(user);
    }

    @Encrypt
    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody User body) {
        return ResponseEntity.ok(userRepository.findById(body.getId()));
    }

    @Encrypt
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Page<User> body) {
        if (!FghConstants.ADMIN_USER.equals(UserUtil.getUserCode())) {
            BaseEntity baseEntity = body.getFilter().getEq();
            baseEntity.setTenantCode(UserUtil.getTenantCode());
        }
        return ResponseEntity.ok(userRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        userRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(userRepository.deleteByIdIn(body.getIds()));
    }

    @PostMapping("/password/check")
    public ResponseEntity<?> checkPassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getCode());
        if (user.getPassword().equals(body.getPwdHash())) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getCode());
        if (user.getPassword().equals(body.getOldPwdHash())) {
            user.setPassword(body.getPwdHash());
            userRepository.update(user);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getCode());
        user.setPassword(body.getPwdHash());
        userRepository.update(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/option/group")
    public ResponseEntity<?> optionGroup(@RequestBody(required = false) User body) {
        List<User> users = userRepository.findAll(body);
        Map<String, List<User>> userMap = users.stream().collect(Collectors.groupingBy(User::getRole));
        List<Options> optionGroup = new ArrayList<>();
        for (Map.Entry<String, List<User>> entry : userMap.entrySet()) {
            Options options = new Options();
            options.setKey(entry.getKey());
            options.setLabel(entry.getKey());
            for (User user : entry.getValue()) {
                options.getOptions().add(new Option(user.getId(), user.getCode(), user.getName(), user.getEmail()));
            }
            optionGroup.add(options);
        }
        return ResponseEntity.ok(optionGroup.stream().sorted(Comparator.comparing(Options::getKey).reversed()).collect(Collectors.toList()));
    }
}
