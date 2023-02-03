package com.epam.esm.repository.user;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLogin(String username);

    @Query("SELECT role " +
            "FROM Role role " +
            "WHERE role.name = :name ")
    Optional<Role> findRoleByName(String name);
}
