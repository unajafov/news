//package az.najafov.deforestationnews.service;
//
//import az.najafov.deforestationnews.dto.LoginRequestDto;
//import az.najafov.deforestationnews.dto.RegistrationRequestDto;
//import az.najafov.deforestationnews.dto.TokenResponseDto;
//import az.najafov.deforestationnews.dto.UserRequestDto;
//import az.najafov.deforestationnews.dto.UserResponseDto;
//import az.najafov.deforestationnews.exception.EntityNotFoundException;
//import az.najafov.deforestationnews.exception.PasswordMismatchException;
//import az.najafov.deforestationnews.mapper.UserMapper;
//import az.najafov.deforestationnews.model.Region;
//import az.najafov.deforestationnews.model.User;
//import az.najafov.deforestationnews.repository.RegionRepository;
//import az.najafov.deforestationnews.repository.RoleRepository;
//import az.najafov.deforestationnews.repository.UserRepository;
//import az.najafov.deforestationnews.security.AuthenticationRequestDto;
//import az.najafov.deforestationnews.security.Role;
//import az.najafov.deforestationnews.security.provider.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final AuthenticationManager authenticationManager;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserRepository userRepository;
//    private final UserMapper userMapper;
//    private final RegionRepository regionRepository;
//    private final RoleRepository roleRepository;
//
//    @Value("${static.role.admin_id}")
//    private Long adminRoleId;
//
//    public void create(UserRequestDto requestDto) {
//        User user = new User();
//        user.setUsername(requestDto.getUserName());
//        Region region = regionRepository.findById(requestDto.getRegionId()).orElseThrow(() ->
//                new EntityNotFoundException(Region.class, requestDto.getRegionId()));
//        user.setRegion(region);
//        userRepository.save(user);
//    }
//
//    public UserResponseDto getById(Long id) {
//        return userMapper.toResponseDto(userRepository.findById(id).orElseThrow(() ->
//                new EntityNotFoundException(User.class, "with ID : " + id)));
//    }
//
//    public List<UserResponseDto> getAll() {
//        return userRepository.findAll().stream().map(userMapper::toResponseDto).toList();
//    }
//
////    public void update(Long id, UserRequestDto requestDto) {
////        User user = userRepository.findById(id).orElseThrow(() ->
////                new EntityNotFoundException(User.class, "with ID : " + id));
////        user.setUsername(requestDto.getUserName());
////        userRepository.save(user);
////    }
////
////    public void delete(Long id) {
////        userRepository.deleteById(id);
////    }
//
////    public void register(RegistrationRequestDto registrationRequestDto) {
////        checkConfirmationPassword(registrationRequestDto.getPassword(), registrationRequestDto.getConfirmedPassword());
////        User user = new User();
////        user.setUsername(registrationRequestDto.getUsername());
////        if (Objects.nonNull(registrationRequestDto.getRegionId())) {
////            Region region = regionRepository.findById(registrationRequestDto.getRegionId()).orElseThrow(() ->
////                    new EntityNotFoundException(Region.class, registrationRequestDto.getRegionId()));
////            user.setRegion(region);
////        }
////        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
////        Role companyUserRole = roleRepository.findById(adminRoleId).orElseThrow(() ->
////                new EntityNotFoundException(Role.class, adminRoleId));
////        user.setRoles(Set.of(companyUserRole));
////        userRepository.save(user);
////    }
//
////    public TokenResponseDto login(LoginRequestDto generalUserLoginRequestDto) {
////        AuthenticationRequestDto authenticationRequestDto = new AuthenticationRequestDto();
////        authenticationRequestDto.setUsername(generalUserLoginRequestDto.getUsername());
////        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequestDto,
////                generalUserLoginRequestDto.getPassword());
////        authentication = authenticationManager.authenticate(authentication);
////        SecurityContextHolder.getContext().setAuthentication(authentication);
////
////        return new TokenResponseDto(jwtTokenProvider.createToken(authentication));
////    }
//
//    private void checkConfirmationPassword(String password, String confirmedPassword) {
//        if (!password.equals(confirmedPassword)) {
//            throw new PasswordMismatchException("Password must be same");
//        }
//    }
//
//}
