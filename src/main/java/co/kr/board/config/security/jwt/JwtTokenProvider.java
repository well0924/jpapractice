package co.kr.board.config.security.jwt;

import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import co.kr.board.login.domain.Role;
import co.kr.board.login.domain.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	
	 @Value("${jwt.secret}")
	 private String secretKey;

	    // 토큰 유효시간 30분
	    private long tokenValidTime = 30 * 60 * 1000L;
	    
	    private long refreshtokenValidTime = 1000 * 60 * 60 * 24 * 7;

	    private final UserDetailsService userDetailsService;

	    // 객체 초기화, secretKey를 Base64로 인코딩
	    protected void init() {
	        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	    }
	    
	    // JWT 토큰 생성
	    public TokenDto createTokenDto(String userPk,Role roles){
	        Claims claims = Jwts.claims().setSubject(userPk);// JWT payload에 저장되는 정보 단위

	        claims.put("roles",roles);// 정보 저장 (key-value)
	        long now = (new Date()).getTime();
	        Date accessTime = new Date(now+tokenValidTime);

	        //토큰 생성
	        String accessToke = Jwts
	                .builder()
	                .setClaims(claims)
	                .setExpiration(accessTime)// set Expire Time
	                .signWith(SignatureAlgorithm.HS256,secretKey)// 사용할 암호화 알고리즘과 signature에 들어갈 secret 값 세팅
	                .compact();

	        //refresh 토큰 생성
	        String refreshToken = Jwts
	                .builder()
	                .setClaims(claims)
	                .setExpiration(new Date(now+refreshtokenValidTime))
	                .signWith(SignatureAlgorithm.HS256,secretKey)
	                .compact();

	        return TokenDto.builder()
	                .accessToken(accessToke)
	                .refreshToken(refreshToken)
	                .ExpirationTime(accessTime.getTime())
	                .build();
	    }

	    // JWT 토큰에서 인증 정보 조회
	    public Authentication getAuthentication(String token) {
	        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPK(token));
	        return new UsernamePasswordAuthenticationToken(token,"",userDetails.getAuthorities());
	    }

	    // 토큰에서 회원 정보 추출
	    public String getUserPK(String token) {
	        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	    }

	    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN": "TOKEN 값"
	    public String resolveToken(HttpServletRequest request) {
	        return request.getHeader("X-AUTH-TOKEN");
	    }

	    // 토큰의 유효성 + 만료일자 확인
	    public boolean validateToken(String jwtToken) {
	        try {
	            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
	            //return !claims.getBody().getExpiration().before(new Date());
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
	    
}
