package com.infoplusvn.qrbankgateway.controller;

import com.infoplusvn.qrbankgateway.dto.UserAccountInfo;
import com.infoplusvn.qrbankgateway.dto.response.DataResponse;
import com.infoplusvn.qrbankgateway.dto.user.EditAccountDTO;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleUser;
import com.infoplusvn.qrbankgateway.entity.AccountEntity;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import com.infoplusvn.qrbankgateway.payload.JwtResponse;
import com.infoplusvn.qrbankgateway.service.Impl.AccountServiceImpl;
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
public class AccountController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;

    final String TOKEN_PREFIX = "Bearer";

    @Autowired
    private UserService userService;


    @Autowired
    private AccountServiceImpl accountService;

    @GetMapping("/getUser/{userName}")
    public UserAccountInfo getUserAccountInfo(@PathVariable("userName") String userName) {
        return accountService.findUserAccountInfo(userName);
    }

    @GetMapping("/getRole/{userName}")
    public ResponseEntity<Map<String, String>> getRoles(@PathVariable("userName") String userName) {
        String role = accountService.findRolesByUserName(userName);

        Map<String, String> response = new HashMap<>();
        response.put("roles", role);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<?> createAuthenticationToken(@RequestHeader("Authorization") String authorization) throws Exception {

        authorization = authorization.substring(6);
        log.info("authorization: {} " , authorization );
        byte[] decodedBytes = Base64.getDecoder().decode(authorization);
        String decodedString = new String(decodedBytes);
        String[] result = decodedString.split(":");

        String username = result[0];
        String password = result[1];

        Authentication a = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtTokenUtil.generateToken((UserDetails) a.getPrincipal());

        return ResponseEntity.ok(new JwtResponse(token, TOKEN_PREFIX, jwtTokenUtil.getExpiresIn(jwtTokenUtil.getExpirationDateFromToken(token))));
    }

    @DeleteMapping(value = "/deleteUser/{username}")
    public void deleteUser(@PathVariable String username){
        Long userId = accountService.getUserIdByUsername(username);
        accountService.deleteAccount(username,userId);
    }

    @PutMapping(value = "/updateUser")
    public ResponseEntity<DataResponse> updateUser(@RequestBody EditAccountDTO userDTO) {
        try {
            Long userId = accountService.getUserIdByUsername(userDTO.getUsername());
            UserEntity getUserByUserId = userService.getUserById(userId);

            if(getUserByUserId.getEmail() != null && getUserByUserId.getEmail() == userDTO.getEmail()) {
                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("Email đã được sử dụng").setData(null));
            }
            else {
                AccountEntity accountEntity = accountService.roleAdminUpdateUser(userDTO);
                UserDTORoleAdmin user = modelMapper.map(accountEntity,UserDTORoleAdmin.class);

                return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Updated success").setData(user));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
        }
    }

    @GetMapping(value = "/getAllUsers")
    public ResponseEntity<DataResponse> getAllUsers() {
        try {
            List<UserDTORoleAdmin> listUserDTO = accountService.findAllAccount();

            return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Success").setData(listUserDTO));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
        }
    }

    @GetMapping(value = "/adminGetUser/{username}")
    public ResponseEntity<DataResponse> getUserAdmin(@PathVariable String username) {
        Long userId = accountService.getUserIdByUsername(username);
        try {
            if(userId == null){
                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("không tìm thấy tài khoản có username = " + username).setData(null));
            }else {
                UserDTORoleAdmin userDTO = accountService.findDTOEditAccount(username);
                return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Success").setData(userDTO));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
        }
    }

    @PutMapping(value = "/updateUserDetails")
    public ResponseEntity<DataResponse> updateUser(@RequestBody UserDTORoleUser userDTO) {
        try {
            Long userId = accountService.getUserIdByUsername(userDTO.getUsername());
            UserEntity getUserByUserId = userService.getUserById(userId);

            if(getUserByUserId.getEmail() != null && getUserByUserId.getEmail() == userDTO.getEmail()) {
                return ResponseEntity.ok().body(new DataResponse().setStatus("500").setMessage("Email đã được sử dụng").setData(null));
            }
            else {
                AccountEntity accountEntity = accountService.roleUserUpdateUser(userDTO);
                UserDTORoleAdmin user = modelMapper.map(accountEntity,UserDTORoleAdmin.class);

                return ResponseEntity.ok().body(new DataResponse().setStatus("200").setMessage("Updated success").setData(user));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataResponse().setStatus("500").setMessage(ex.getMessage()).setData(null));
        }
    }

}
