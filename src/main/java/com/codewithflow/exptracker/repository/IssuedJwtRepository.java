package com.codewithflow.exptracker.repository;

import com.codewithflow.exptracker.entity.IssuedJwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IssuedJwtRepository extends JpaRepository<IssuedJwt, Long> {

    @Query(value = """
      select t from IssuedJwt t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    Optional<List<IssuedJwt>> findAllValidTokenByUser(Long id);

    Optional<IssuedJwt> findByToken(String token);
}
