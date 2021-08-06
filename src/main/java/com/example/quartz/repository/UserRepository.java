package com.example.quartz.repository;

import com.example.quartz.model.MyUser;
import org.springframework.data.repository.CrudRepository;

interface UserRepository extends CrudRepository<MyUser, Long> {
}