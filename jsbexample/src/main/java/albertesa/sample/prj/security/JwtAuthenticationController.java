package albertesa.sample.prj.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import albertesa.sample.prj.config.AppConfig;
import albertesa.sample.prj.services.UserService.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "JwtAuthenticationController", description = "Users Management API based on JWT token authentication")
//@CrossOrigin
public class JwtAuthenticationController {

	private AuthenticationManager authenticationManager;
	private JwtTokenUtil jwtTokenUtil;
	private AppConfig appCfg;
	private JwtUserDetailsService userDetailsService;

	@Autowired
	public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
			AppConfig appCfg, JwtUserDetailsService userDetailsService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.appCfg = appCfg;
		this.userDetailsService = userDetailsService;
	}

	@Operation(summary = "Login user",
			description = "Login user by username and password.",
			tags = { "UserController" })
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "successful operation",
	                content = @Content(schema = @Schema(implementation = User.class)))})
	@RequestMapping(value = "/login", method = RequestMethod.POST,
			produces = "application/json")
	public ResponseEntity<JwtResponse> login(
			@Parameter(description="Username and password", 
            required=true, schema=@Schema(implementation = Void.class))
			@RequestBody JwtRequest authenticationRequest,
			HttpServletResponse response) throws Exception {
		Authentication authTok = authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		final String token = jwtTokenUtil.generateToken(authenticationRequest.getEmail());
		CookieUtil.addXsrfCookie(appCfg, response, authTok);
		CookieUtil.addCartCookie(response, authTok);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private Authentication authenticate(String email, String password) throws Exception {
		try {
			Authentication authTok = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			SecurityContextHolder.getContext().setAuthentication(authTok);
			return authTok;
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@Operation(summary = "SignUp user and persist.",
			description = "Signup a new user by email and password.",
			tags = { "UserController" })
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "successful operation",
	                content = @Content(schema = @Schema(implementation = JwtRequest.class)))})
	@RequestMapping(value = "/signup", method = RequestMethod.POST,
			produces = "application/json")
	public ResponseEntity<SignupResponse> signup(
			@Parameter(description="EMail and password", 
            required=true, schema=@Schema(implementation = Void.class))
			@RequestBody JwtRequest user) throws Exception {
		User userCreated = userDetailsService.save(user);
		return ResponseEntity.ok(SignupResponse.ok(userCreated.getId()));
	}
}