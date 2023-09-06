package hello.until.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import hello.until.exception.CustomException;
import hello.until.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.until.exception.CustomException;
import hello.until.exception.ExceptionCode;
import hello.until.user.entity.User;
import hello.until.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	@Transactional 
	public void createUser(String email, String password) {
		
		validateUser(email); 
			
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		userRepository.save(user);
		
	}
	
	private void validateUser(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		
		if(user.isPresent())
			throw new CustomException(ExceptionCode.DUPLICATE_EMAIL_USER_TO_CREATE);
	}

	public User updateUser(long userId, String email, String password){
		User user = this.getUserById(userId).orElseThrow(
				() -> new CustomException(ExceptionCode.NO_USER_TO_UPDATE)
		);

		user.updateEmail(email);
		user.updatePassword(password);
		return userRepository.save(user);
	}

    @Transactional(readOnly = true)
    public Optional<User> getUserById(long id){
        return userRepository.findById(id);
    }
}
