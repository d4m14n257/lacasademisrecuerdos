package com.client.service_client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.client.service_client.model.User;
import com.client.service_client.model.enums.UserStatus;

public interface UserRepository extends JpaRepository<User, String>{
    @Query(value = 
        "SELECT * FROM User u WHERE u.email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value =
        "SELECT * FROM User u WHERE u.username = :username AND u.status", nativeQuery = true)
    Optional<User> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM User u " +
        "WHERE u.username = :username OR u.email = :email")
    Boolean existsUser(@Param("username") String username, @Param("email") String email);

    @Query(value = 
        "SELECT u.id, u.email, u.username, u.status, u.first_name, u.last_name " + 
        "FROM User u " + 
        "ORDER BY CASE " + 
        "WHEN u.status = 'active' THEN 1 " +
        "WHEN u.status = 'inactive' THEN 2 " +
        "WHEN u.status = 'blocked' THEN 3 " +
        "ELSE 3 END", nativeQuery = true)
    List<Object[]> findAllUsers();

    @Modifying
    @Query(value = 
        "UPDATE User u " +
        "SET u.status = :status " +
        "WHERE u.id = :id", nativeQuery = true)
    void updateStatus(String id, UserStatus status);

    @Modifying
    @Query(value = 
        "UPDATE User u " +
        "SET email = :email, " +
        "username = :username, " +
        "first_name = :first_name, " +
        "last_name = :last_name " +
        "WHERE id = :id", nativeQuery = true)
    void editUser (String id, String email, String username, String first_name, String last_name);

    @Modifying
    @Query(value = 
        "UPDATE User u " +
        "SET u.password = :password " +
        "WHERE u.id = :id", nativeQuery = true)
    void changePassword(String id, String password);

    @Query(value = 
        "SELECT u.password " +
        "FROM User u " +
        "WHERE u.id = :id ", nativeQuery = true)
    String findPasswordById(String id);
}
