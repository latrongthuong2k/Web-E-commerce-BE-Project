package com.ecommerce.myapp.Users.security.Token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {


    @Query(value = """
            select t from Token t inner join AppUser u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Integer id);


    @Query(value = """
            select count(*) from Token t
            where t.expired = false or t.revoked = false\s
            """)
    int findAllInValidToken();

    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying
    @Query( value = """
            DELETE FROM Token t WHERE  (t.expired = true or t.revoked = true )\s
            """)
    void deleteExpiredTokens();
}