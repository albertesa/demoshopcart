package albertesa.sample.prj.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import albertesa.sample.prj.services.UserService;
import albertesa.sample.prj.services.UserService.User;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	
	private UserService userSvc;
	private PasswordEncoder bcryptEncoder;

	@Autowired
	public JwtUserDetailsService(UserService userSvc, PasswordEncoder bcryptEncoder) {
		super();
		this.userSvc = userSvc;
		this.bcryptEncoder = bcryptEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			Optional<User> optUsr = userSvc.getUser(email);
			if (optUsr.isEmpty()) {
				throw new UsernameNotFoundException("User not found");
			}
			return new JwtUserDetails(optUsr.get());
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public User save(JwtRequest user) throws JsonProcessingException {
		return userSvc.addUser(user.getEmail(), bcryptEncoder.encode(user.getPassword()));
	}

}
