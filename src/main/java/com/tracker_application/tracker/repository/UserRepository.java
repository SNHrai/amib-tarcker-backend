package com.tracker_application.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import com.tracker_application.tracker.model.LeaveResponse;
import com.tracker_application.tracker.model.PasswordResponse;
import com.tracker_application.tracker.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {

    // public List<Employee> findByPassword(String password);

    // public User findByVerticleHeadId(int verticalId);

    // @Query("select u.userName from User u where userName = :userName")
    // @Query("select u from User u where u = :userName")
    @Query("select u from User u where u.empName = :empName")
    public User findByUserName(String empName);

    @Query("select u.id from User u where u.empName = :empName")
    public String findUserIdByUsername(String empName);

    @Query("Select u from User u where u.id = :id and u.password = :password")
    public User findByUserIdAndPassword(@Param(value = "id") String id, @Param(value = "password") String password);

    @Modifying
    @Transactional
    @Query("Update User u set u.password = :password where u.id = :id")
    public int resetPassword(@Param("id") String id, @Param("password") String passwrod);

    @Query("Select u.empName from User u where u.id= :id")
    public String findUserNameByUserId(@Param(value = "id") String id);

    @Query("Select u from User u where u.id= :id")
    public User findAllUserByUserId(@Param(value = "id") String id);

    @Modifying
    @Query("Update User u set u.email = :email where u.id = :id")
    public void updateEmailById(@Param(value = "email") String email, @Param(value = "id") String id);

    // @Query("select u from User u where u.userName = :userName and u.password =
    // :password")
    // public User findByUserNameandPassword(String userName, String password);

}
