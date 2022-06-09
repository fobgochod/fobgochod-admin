package com.fobgochod.api.menu;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.menu.Menu;
import com.fobgochod.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody Menu body) {
        String id = menuRepository.insert(body);
        return ResponseEntity.ok(menuRepository.findById(id));
    }

    @PostMapping("/del")
    public ResponseEntity<?> delete(@RequestBody Menu body) {
        menuRepository.deleteById(body.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mod")
    public ResponseEntity<?> modify(@RequestBody Menu body) {
        menuRepository.update(body);
        return ResponseEntity.ok(menuRepository.findById(body.getId()));
    }

    @PostMapping("/get")
    public ResponseEntity<?> findById(@RequestBody Menu body) {
        return ResponseEntity.ok(menuRepository.findById(body.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page<Menu> body) {
        return ResponseEntity.ok(menuRepository.findByPage(body));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        menuRepository.dropCollection();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(menuRepository.deleteByIdIn(body.getIds()));
    }
}
