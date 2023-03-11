package co.kr.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.kr.board.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
	 Optional<RefreshToken> findByKey(String key);
}
