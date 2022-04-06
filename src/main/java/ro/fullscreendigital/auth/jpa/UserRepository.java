package ro.fullscreendigital.auth.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.fullscreendigital.auth.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select user from User user where user.username = :username")
     User findByUsername(@Param("username") String username);

    @Query("select count(user.id) from User user where user.username = :username")
     int validateUsername(@Param("username") String username);

    @Query("select count(user.id) from User user where user.email = :email")
     int validateEmail(@Param("email") String email);
}
