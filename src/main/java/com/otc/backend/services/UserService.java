package com.otc.backend.services;

import com.otc.backend.dto.RegistrationDto;
import com.otc.backend.exception.EmailAlreadyTakenException;
import com.otc.backend.exception.RoleNotFoundException;
import com.otc.backend.exception.UserDoesNotExistException;
import com.otc.backend.models.Role;
import com.otc.backend.models.Users;
import com.otc.backend.repository.RoleRepository;
import com.otc.backend.repository.UserRepository;
import com.otc.backend.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<Users> registerUser(RegistrationDto registrationDtoBody){

        try {
            String firstName = registrationDtoBody.getFirstName();
            String lastName = registrationDtoBody.getLastName();
            String emailAddress = registrationDtoBody.getEmailAddress();
            String password = registrationDtoBody.getPassword();
            String telephone = registrationDtoBody.getTelephone();
            String username;

            String name = firstName +lastName;
            boolean nameTaken = true;
            String tempName = " ";

            while(nameTaken) {
                tempName = generateUserName(name);

                if(userRepository.findByUsername(tempName).isEmpty()){
                    nameTaken = false;
                }
            }

            username = tempName;

            if (userRepository.existsByEmailAddress(emailAddress)) {
                return new ApiResponse<>(false, "Email address is already registered");
            }

            String encodedPassword = passwordEncoder.encode(password);

            Role userRole = roleRepository.findByAuthority("USER").orElseThrow(() -> new RoleNotFoundException("Role not found"));

            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);
            Users newUser = userRepository.save(new Users(username, encodedPassword, firstName, lastName, emailAddress, telephone, authorities));

            return new ApiResponse<>(true, "User registered successfully", newUser);
        } catch (Exception e) {

            e.printStackTrace();
            return new ApiResponse<>(false, "Internal server error");
        }
    }


    private String generateUserName(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name + generatedNumber;
    }

    public void generateEmailVerification(String username) {
        Users user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
        user.setVerification(generatedVerificationNumber());
        userRepository.save(user);
    }

   public Users setPassword(String username, String password) {
     Users user = userRepository.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
      String encodedPassword = passwordEncoder.encode(password);
      user.setPassword(encodedPassword);
         return userRepository.save(user);
    }

   private Long generatedVerificationNumber() {
         long generatedNumber = (long)Math.floor(Math.random() * 100_000_000);
         return  generatedNumber;
    }

    public Users getUserByUsername(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(UserDoesNotExistException::new);
    }

    public Users updateUser(Users user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    public Users getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(UserDoesNotExistException::new);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users updateUser(Long userId, Users updatedUser){

        Users user = userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException("Call not found with the given Id : " + userId)
        );
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());

        Users updatedUserObj = userRepository.save(user);
       return updatedUserObj;

    }

    public void deleteUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException("User not found with the given Id : " + userId)
        );

        userRepository.deleteById(userId);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));

        Set<GrantedAuthority> authorities = user.getAuthorities()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), authorities);

        return userDetails;
    }

    public String getUsername(String username) {
        Optional<Users> user = userRepository.findByUsername(username);

        if (user != null) {
            return user.get().getUsername();
        } else {
            throw new UserDoesNotExistException();
        }
    }


}
