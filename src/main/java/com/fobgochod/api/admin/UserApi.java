package com.fobgochod.api.admin;

import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.domain.select.Option;
import com.fobgochod.domain.select.Options;
import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.domain.PasswordVO;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UserApi {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User body) {
        String id = userRepository.insert(body);
        return ResponseEntity.ok(userRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody User body) {
        User user = userRepository.findById(body.getId());
        if (user != null) {
            user.setEmail(body.getEmail());
            user.setRole(body.getRole());
            userRepository.update(body);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(userRepository.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> find(@RequestBody(required = false) User body) {
        return ResponseEntity.ok(userRepository.findAll(body));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page body) {
        return ResponseEntity.ok(userRepository.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(userRepository.deleteByIdIn(body.getIds()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        userRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/name")
    public ResponseEntity<?> modifyName(@RequestBody User body) {
        User user = userRepository.findById(body.getId());
        if (user != null) {
            user.setEmail(body.getEmail());
            userRepository.update(user);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/password/check")
    public ResponseEntity<?> checkPassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getName());
        if (user.getPassword().equals(body.getPwdHash())) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getName());
        if (user.getPassword().equals(body.getOldPwdHash())) {
            user.setPassword(body.getPwdHash());
            userRepository.update(user);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getName());
        user.setPassword(body.getPwdHash());
        userRepository.update(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        User user = userRepository.findByCode(name);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/option/group")
    public ResponseEntity<?> optionGroup(@RequestBody(required = false) User body) {
        List<User> users = userRepository.findAll(body);
        Map<RoleEnum, List<User>> userMap = users.stream().collect(Collectors.groupingBy(User::getRole));
        List<Options> optionGroup = new ArrayList<>();
        for (Map.Entry<RoleEnum, List<User>> entry : userMap.entrySet()) {
            Options options = new Options();
            options.setKey(entry.getKey().ordinal());
            options.setLabel(entry.getKey().name());
            for (User user : entry.getValue()) {
                options.getOptions().add(new Option(user.getCode(), user.getName(), user.getEmail()));
            }
            optionGroup.add(options);
        }
        return ResponseEntity.ok(optionGroup.stream().sorted(Comparator.comparing(Options::getKey).reversed()).collect(Collectors.toList()));
    }
}
