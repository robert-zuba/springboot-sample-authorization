package zuba.robert.controllers;


import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zuba.robert.dto.EquipmentDto;
import zuba.robert.entities.Equipment;
import zuba.robert.repositories.EquipmentRepository;
import zuba.robert.security.entities.User;

import java.util.Optional;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;

    public EquipmentController(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @PostMapping
    public void add(@RequestBody EquipmentDto equipmentDto) {
        User user = this.getUser().orElseThrow();

        Equipment equipment = new Equipment();
        equipment.setName(equipmentDto.name);
        equipment.setOwner(user);

        this.equipmentRepository.save(equipment);
    }

    @GetMapping
    public Equipment find(@RequestParam Integer id) {
        User user = this.getUser().orElseThrow();

        return this.equipmentRepository.findByIdAndOwner(id, user).orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404)));
    }


    @GetMapping("/all")
    @Secured("ROLE_ADMIN")
    public Iterable<Equipment> findAll() {
        User user = this.getUser().orElseThrow();

        return this.equipmentRepository.findAll();
    }


    private Optional<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.of((User) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
