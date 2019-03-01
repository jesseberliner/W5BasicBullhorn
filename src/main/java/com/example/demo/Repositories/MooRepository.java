package com.example.demo.Repositories;

import com.example.demo.Models.Moo;
import org.springframework.data.repository.CrudRepository;

public interface MooRepository extends CrudRepository<Moo, Long> {
}
