package com.stixis.ems.security;


import com.stixis.ems.model.Employee;
import com.stixis.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	EmployeeRepository userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


		Employee user = userDao.findByEmail(username);

		if (user == null){
			throw new UsernameNotFoundException("user not found with email: "+username);
		}
		return user;
	}
}