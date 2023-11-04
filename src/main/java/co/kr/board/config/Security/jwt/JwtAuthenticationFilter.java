package co.kr.board.config.Security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.IncorrectClaimException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getRequestURI());

        String token  = resolveToken(request);
        log.info("token value::"+token);

        try{
            if (token != null && jwtTokenProvider.validateAccessToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info(authentication);
                log.debug("Save authentication in SecurityContextHolder.");
            }
        }catch (IncorrectClaimException e) { // 잘못된 토큰일 경우
            SecurityContextHolder.clearContext();
            log.debug("Invalid JWT token.");
            response.sendError(403);
        } catch (UsernameNotFoundException e) { // 회원을 찾을 수 없을 경우
            SecurityContextHolder.clearContext();
            log.debug("Can't find user.");
            response.sendError(403);
        }

        filterChain.doFilter(request, response);
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
