package com.infoplusvn.qrbankgateway.controller;

import com.infoplusvn.qrbankgateway.dto.UserAccountInfo;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleUser;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORegisterRequest;
import com.infoplusvn.qrbankgateway.dto.response.DataResponse;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import com.infoplusvn.qrbankgateway.payload.JwtResponse;
import com.infoplusvn.qrbankgateway.service.UserService;
import com.infoplusvn.qrbankgateway.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/infogw/qr/v1")
@RestController
public class UserController {
    final String TOKEN_PREFIX = "Bearer";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


//    @GetMapping("/getUser/{userName}")
//    public UserAccountInfo getUserAccountInfo(@PathVariable("userName") String userName) {
//        return userService.findUserAccountInfo(userName);
//    }

//    @GetMapping("/getRole/{userName}")
//    public ResponseEntity<Map<String, String>> getRoles(@PathVariable("userName") String userName) {
//        String role = userService.findRolesByUserName(userName);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("roles", role);
//
//        return ResponseEntity.ok(response);
//    }

//    @PostMapping("/oauth/token")
//    public ResponseEntity<?> createAuthenticationToken(@RequestHeader("Authorization") String authorization) throws Exception {
//
//        authorization = authorization.substring(6);
//        log.info("authorization: {} " , authorization );
//        byte[] decodedBytes = Base64.getDecoder().decode(authorization);
//        String decodedString = new String(decodedBytes);
//        String[] result = decodedString.split(":");
//
//        String username = result[0];
//        String password = result[1];
//
//        Authentication a = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//
//        String token = jwtTokenUtil.generateToken((UserDetails) a.getPrincipal());
//
//        return ResponseEntity.ok(new JwtResponse(token, TOKEN_PREFIX, jwtTokenUtil.getExpiresIn(jwtTokenUtil.getExpirationDateFromToken(token))));
//    }

    


//    @GetMapping(value = "/getAllUsers")
//    public ResponseEntity<DataResponse> getAllUsers() {
//        try {
//            List<UserDTORoleAdmin> listUserDTO = userService.getAllUsers().stream().map(UserEntity -> modelMapper.map(UserEntity, UserDTORoleAdmin.class))
//                    .collect(Collectors.toList());
//
//
//            return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Success").setData(listUserDTO));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
//        }
//    }

//    @PostMapping(value = "/createUser")
//    public ResponseEntity<DataResponse> createUser(@RequestBody UserDTORegisterRequest userDTO) {
//        try {
//
//            if (userService.getUserByUserName(userDTO.getUsername()) != null || userService.getUserByEmail(userDTO.getEmail()) != null) {
//
//                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("Username hoặc Email đã được sử dụng").setData(null));
//
//            } else {
//                UserEntity userEntity = userService.createUser(userDTO);
//
//                return ResponseEntity.ok().body(new DataResponse().setStatus("201").setMessage("Created success").setData(null));
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
//        }
//
//    }



//    @PutMapping(value = "/update")
//    public ResponseEntity<DataResponse> updateUserByAdmin(@RequestBody UserDTORoleAdmin userDTO) {
//        try {
//            UserEntity getUserByEmail = userService.getUserByEmail(userDTO.getEmail());
//            String myEmail = getUserByEmail.getEmail();
//            if (userService.getUserByUserName(userDTO.getUsername()) == null) {
//                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("không tìm thấy tài khoản có username = " + userDTO.getUsername()).setData(null));
//            } else if(getUserByEmail != null && myEmail != userDTO.getEmail()) {
//                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("Email đã được sử dụng").setData(null));
//            }
//            else {
//                UserEntity userEntity = userService.roleAdminUpdateUser(userDTO);
//                UserDTORoleUser user = modelMapper.map(userEntity,UserDTORoleUser.class);
//                return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Updated success").setData(user));
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
//        }
//    }


//    @PutMapping(value = "/deactiveUser")
//    public ResponseEntity<DataResponse> deactiveUser(@RequestBody UserDTORoleAdmin userDTO) {
//        try {
//            if (userService.getUserByUserName(userDTO.getUsername()) == null) {
//                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("không tìm thấy tài khoản có username = " + userDTO.getUsername()).setData(null));
//            } else {
//                UserEntity userEntity = userService.deactiveUser(userDTO);
//                UserDTORoleAdmin user = modelMapper.map(userEntity,UserDTORoleAdmin.class);
//
//                return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Deactived success").setData(user));
//            }
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
//        }
//    }



//    @GetMapping(value = "/adminGetUser/{username}")
//    public ResponseEntity<DataResponse> getUserAdmin(@PathVariable String username) {
//        try {
//            UserEntity userEntity = userService.getUserByUserName(username);
////            if (userEntity == null) {
////                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("không tìm thấy tài khoản có username = " + username).setData(null));
////            } else {
//
//                UserDTORoleAdmin userDTORoleAdmin = modelMapper.map(userEntity, UserDTORoleAdmin.class);
//                return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Success").setData(userDTORoleAdmin));
////            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
//        }
//    }
}
