package com.licenta.repository;

import com.licenta.domain.AppUser;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Transactional
    @Modifying
    @Query("delete from AppUser a where a.user.id=:id")
    void deleteAppUser(@Param("id") Long id);

    @Transactional
    @Query("select a from AppUser a where a.user.id=:id")
    Optional<AppUser> findAppUserByUserId(@Param("id") Long id);
}
