package com.anonymity.topictalks.controllers;

import com.alibaba.fastjson.JSON;
import com.anonymity.topictalks.models.dtos.GenderDTO;
import com.anonymity.topictalks.models.dtos.UserDTO;
import com.anonymity.topictalks.models.payloads.requests.AvatarRequest;
import com.anonymity.topictalks.models.payloads.requests.UserTopicRequest;
import com.anonymity.topictalks.models.payloads.requests.UserUpdateRequest;
import com.anonymity.topictalks.models.payloads.responses.DataResponse;
import com.anonymity.topictalks.models.payloads.responses.ErrorResponse;
import com.anonymity.topictalks.models.persists.user.UserPO;
import com.anonymity.topictalks.services.IUserService;
import com.anonymity.topictalks.services.IUserTopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@Tag(name = "User", description = "")
public class UserController {
    private final IUserService userService;

    @PostMapping("/unban/{userId}")
    public ResponseEntity<String> unban(@PathVariable("userId") Long id) {
        userService.unBanUser(id);
        return new ResponseEntity<>("Unbanned!", HttpStatus.OK);
    }

    @PostMapping("/upload")

    public ResponseEntity<?> create(@RequestBody AvatarRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();
        boolean isSuccess = userService.updateAvatar(request.getImage(), request.getUserId());
        if (bindingResult.hasErrors() || isSuccess == false) {
            dataResponse.setStatus(HttpStatus.FORBIDDEN.value());
            dataResponse.setDesc(HttpStatus.FORBIDDEN.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("Failure to upload avatar");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.CREATED.value());
        dataResponse.setSuccess(true);
        dataResponse.setDesc(HttpStatus.CREATED.getReasonPhrase());
        dataResponse.setData("Upload avatar successfully");
        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")

    public ResponseEntity<?> findAllUsers(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        DataResponse dataResponse = new DataResponse();
        Page<UserDTO> allUsers = userService.findAllUsers(page,size);

        if (allUsers.isEmpty()) {
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(null);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(allUsers);

        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN')")

    public ResponseEntity<?> findByNickName(@RequestParam("keyword") String nickname,
                                            @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        DataResponse dataResponse = new DataResponse();
        Page<UserDTO> allUsers = userService.searchByNickName(nickname,page,size);

        if (allUsers.isEmpty()) {
            dataResponse.setStatus(HttpStatus.NO_CONTENT.value());
            dataResponse.setDesc(HttpStatus.NO_CONTENT.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData(null);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(allUsers);

        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/{id}")

    public ResponseEntity<?> findUserById(@PathVariable("id") long id) {
        DataResponse dataResponse = new DataResponse();

        UserDTO userPO = userService.getUserById(id);

        if (userPO == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData(null);

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(userPO);

        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("/{id}")

    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody UserUpdateRequest request, BindingResult bindingResult) {
        DataResponse dataResponse = new DataResponse();

        if (bindingResult.hasErrors()) {
            dataResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            dataResponse.setDesc(HttpStatus.BAD_REQUEST.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        if (request.getEmail() != null) {
            if (userService.checkDuplicateEmail(id, request.getEmail()) == true) {
                dataResponse.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                dataResponse.setDesc("Update user failure");
                dataResponse.setSuccess(false);
                dataResponse.setData(JSON.parseObject("{\"message\":\"This email has already in use by another account\"}"));

                return ResponseEntity.ok(dataResponse);

            }
        }
        Object userUpdated = userService.updateUser(id, request);

        if (userUpdated == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(userUpdated);

        return ResponseEntity.ok(dataResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")

    public ResponseEntity<?> deleteUserById(@PathVariable("id") long id) {
        DataResponse dataResponse = new DataResponse();

        boolean isRemoved = userService.remove(id);

        if (!isRemoved) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData("");

        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("/ban")

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> banUser(@RequestParam("id") long id,@RequestParam("num") long numDateBan) {
        DataResponse dataResponse = new DataResponse();
        UserDTO userBanned = userService.banUser(id,numDateBan);

        if (userBanned == null) {//NOT FOUND
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());//404
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());//NOT FOUND
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(userBanned);

        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/all-age")

    public ResponseEntity<?> getAgeOfAllUsers() {
        DataResponse dataResponse = new DataResponse();

        List<Integer> ageList = userService.getAgeOfAllUsers();

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(ageList);

        return ResponseEntity.ok(dataResponse);
    }

    @GetMapping("/all-gender")

    public ResponseEntity<?> getGenderOfAllUsers() {
        DataResponse dataResponse = new DataResponse();

        GenderDTO genderDTO = userService.getAllGenderOfUser();

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(genderDTO);

        return ResponseEntity.ok(dataResponse);
    }

    @PutMapping("/status-profile")

    public ResponseEntity<?> setStatusProfile(@RequestParam("id") long id,@RequestParam("isPublic") boolean isPublic) {
        DataResponse dataResponse = new DataResponse();
        UserDTO userDto = userService.updateStatusProfile(id,isPublic);

        if (userDto == null) {
            dataResponse.setStatus(HttpStatus.NOT_FOUND.value());
            dataResponse.setDesc(HttpStatus.NOT_FOUND.getReasonPhrase());
            dataResponse.setSuccess(false);
            dataResponse.setData("");

            return ResponseEntity.ok(dataResponse);
        }

        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setDesc(HttpStatus.OK.getReasonPhrase());
        dataResponse.setSuccess(true);
        dataResponse.setData(userDto);

        return ResponseEntity.ok(dataResponse);
    }
}
