package zuba.robert.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zuba.robert.entities.Equipment;
import zuba.robert.security.entities.User;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Integer> {
    Optional<Equipment> findByIdAndOwner(Integer id, User user);
}
