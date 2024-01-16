package com.stixis.ems.security;


import com.stixis.ems.dao.TokenRepository;
import com.stixis.ems.model.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class logoutService implements LogoutHandler {

	@Autowired
	TokenRepository tokenDao;

	@Override
	@CacheEvict(value="employees",allEntries = true)
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.out.println("inside logout");
		final String authorizationHeader = request.getHeader("Authorization");
		final String jwtToken;

		if(authorizationHeader!= null && authorizationHeader.startsWith("Bearer ")){
			jwtToken = authorizationHeader.substring(7);
			Token token = tokenDao.findByToken(jwtToken);
			if(token!=null){
				token.setRevoked(true);
				token.setExpired(true);
				tokenDao.save(token);
			}
		}
	}
}
