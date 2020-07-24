package com.wildcodeschool.wildCircus.repository;

import com.wildcodeschool.wildCircus.entity.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {

    public List<Cours> findAll();

}
