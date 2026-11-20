package org.example.demo.repository;
import org.example.demo.entity.Uzenet;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UzenetRepository extends CrudRepository<Uzenet, Long> {
    List<Uzenet> findAllByOrderByDatumDesc();
}
