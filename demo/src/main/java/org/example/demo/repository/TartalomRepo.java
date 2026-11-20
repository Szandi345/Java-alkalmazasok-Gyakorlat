package org.example.demo.repository;
import org.example.demo.Tartalom;
import org.springframework.data.repository.CrudRepository;

public interface TartalomRepo extends CrudRepository<Tartalom, Integer> {
}
