package com.wildcodeschool.wildCircus.repository;

import com.wildcodeschool.wildCircus.entity.Inscription;
import com.wildcodeschool.wildCircus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    List<Inscription> findAllByUser(User user);

}
