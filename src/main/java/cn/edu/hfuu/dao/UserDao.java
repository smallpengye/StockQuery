package cn.edu.hfuu.dao;

import cn.edu.hfuu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

//java 8
    Optional<User> findByUsername(String username);

    @Query("from User where username = ?1")
    List<User> findByUsername2(String username);

    @Query("from User where username = ?1")
    User findOneByUsername(String username);

    @Modifying
    @Query("update User set roles = ?1 where username = ?2")
    void updateUserRoles(String roles,String name);

    @Modifying
    @Query("delete User where username=?1")
    void deleteUser(String name);


//    default List<User> findByUsername3(String username, String password) {
//
//        Specifications.where((Specification<User>) (root, query, cb) -> null);
//
//    }
}
