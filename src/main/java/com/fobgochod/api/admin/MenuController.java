package com.fobgochod.api.admin;

import com.fobgochod.domain.base.BatchFid;
import com.fobgochod.domain.base.Page;
import com.fobgochod.entity.admin.Menu;
import com.fobgochod.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menus")
public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Menu body) {
        String id = menuRepository.insert(body);
        return ResponseEntity.ok(menuRepository.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        menuRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody Menu body) {
        menuRepository.update(body);
        return ResponseEntity.ok(menuRepository.findById(body.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        return ResponseEntity.ok(menuRepository.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> find(@RequestBody(required = false) Menu body) {
        return ResponseEntity.ok(menuRepository.findAll(body));
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody(required = false) Page<Menu> body) {
        return ResponseEntity.ok(menuRepository.findByPage(body));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody BatchFid body) {
        return ResponseEntity.ok(menuRepository.deleteByIdIn(body.getIds()));
    }

    @DeleteMapping("/drop")
    public ResponseEntity<?> dropCollection() {
        menuRepository.dropCollection();
        return ResponseEntity.ok().build();
    }
}
