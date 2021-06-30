package com.fobgochod.api.admin;

import com.fobgochod.domain.StdData;
import com.fobgochod.domain.enumeration.RoleEnum;
import com.fobgochod.domain.select.Option;
import com.fobgochod.domain.select.Options;
import com.fobgochod.domain.v2.Page;
import com.fobgochod.domain.v2.PasswordVO;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public StdData create(@RequestBody User body) {
        String id = userRepository.insert(body);
        return StdData.ofSuccess(userRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public StdData delete(@PathVariable String id) {
        userRepository.deleteById(id);
        return StdData.ok();
    }

    @PutMapping
    public StdData modify(@RequestBody User body) {
        User user = userRepository.findById(body.getId());
        if (user != null) {
            user.setEmail(body.getEmail());
            user.setRole(body.getRole());
            userRepository.update(body);
        }
        return StdData.ofSuccess(user);
    }

    @PutMapping("/name")
    public StdData modifyName(@RequestBody User body) {
        User user = userRepository.findById(body.getId());
        if (user != null) {
            user.setEmail(body.getEmail());
            userRepository.update(user);
        }
        return StdData.ofSuccess(user);
    }

    @GetMapping("/{id}")
    public StdData findById(@PathVariable String id) {
        return StdData.ofSuccess(userRepository.findById(id));
    }

    @GetMapping
    public StdData find(@RequestBody(required = false) User body) {
        return StdData.ofSuccess(userRepository.findAll(body));
    }

    @PostMapping("/search")
    public StdData search(@RequestBody(required = false) Page body) {
        return StdData.ofSuccess(userRepository.findByPage(body));
    }

    @PostMapping("/password/check")
    public StdData checkPassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getName());
        if (user.getPassword().equals(body.getPwdHash())) {
            return StdData.ofSuccess(true);
        }
        return StdData.ofSuccess(false);
    }

    @PostMapping("/password/change")
    public StdData changePassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getName());
        if (user.getPassword().equals(body.getOldPwdHash())) {
            user.setPassword(body.getPwdHash());
            userRepository.update(user);
        }
        return StdData.ok();
    }

    @PostMapping("/password/reset")
    public StdData resetPassword(@RequestBody PasswordVO body) {
        User user = userRepository.findByCode(body.getName());
        user.setPassword(body.getPwdHash());
        userRepository.update(user);
        return StdData.ok();
    }

    @GetMapping("/name/{name}")
    public StdData findByName(@PathVariable String name) {
        User user = userRepository.findByCode(name);
        return StdData.ofSuccess(user);
    }

    @GetMapping("/option")
    public StdData userSelect(@RequestBody(required = false) User body) {
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
        return StdData.ofSuccess(optionGroup.stream().sorted(Comparator.comparing(Options::getKey).reversed()).collect(Collectors.toList()));
    }
}
