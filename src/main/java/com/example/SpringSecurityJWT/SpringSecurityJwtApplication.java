package com.example.SpringSecurityJWT;

import com.example.SpringSecurityJWT.domain.entities.ERole;
import com.example.SpringSecurityJWT.domain.entities.RoleEntity;
import com.example.SpringSecurityJWT.domain.entities.UserEntity;
import com.example.SpringSecurityJWT.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@AllArgsConstructor
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner init(){
		return args -> {
			UserEntity userEntity1 = new UserEntity()
					.builder()
					.username("asaavedraAdmin")
					.password(passwordEncoder.encode("1234"))
					.email("asaavedra@gmail.com")
					.roles(
							Set.of(
									RoleEntity.builder()
											.name(ERole.valueOf(ERole.ADMIN.name()))
											.build()
							)
					)
					.build();

			UserEntity userEntity2 = new UserEntity()
					.builder()
					.username("asaavedraUser")
					.password(passwordEncoder.encode("12345"))
					.email("asaavedra@gmail.com")
					.roles(
							Set.of(
									RoleEntity.builder()
											.name(ERole.valueOf(ERole.USER.name()))
											.build()
							)
					)
					.build();

			UserEntity userEntity3 = new UserEntity()
					.builder()
					.username("asaavedraInvit")
					.password(passwordEncoder.encode("123456"))
					.email("asaavedra@gmail.com")
					.roles(
							Set.of(
									RoleEntity.builder()
											.name(ERole.valueOf(ERole.INVITED.name()))
											.build()
							)
					)
					.build();

			userRepository.save(userEntity1);
			userRepository.save(userEntity2);
			userRepository.save(userEntity3);
		};
	}

}
