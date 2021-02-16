package com.kalew515.pestmessageboardbackend.controller;

import com.kalew515.pestmessageboardbackend.checker.LoginChecker;
import com.kalew515.pestmessageboardbackend.interceptor.InterceptCheck;
import com.kalew515.pestmessageboardbackend.service.AttachmentService;
import com.kalew515.pestmessageboardbackend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@RestController
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/attachment")
    public Response<String> upload (MultipartFile file) throws IOException {
        if (file == null) {
            return Response.invalid("无效参数");
        }
        if (attachmentService.saveFile(file) >= 1) {
            return Response.success("上传成功");
        } else {
            return Response.invalid("上传失败");
        }
    }

    @GetMapping("/attachment/{uuid}")
    public ResponseEntity<FileSystemResource> download (@PathVariable String uuid) {
        String originalFilename = attachmentService.getOriginalFilenameByUUID(uuid);
        if (originalFilename == null) {
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        } else {
            File file = attachmentService.getFileByUUID(uuid);
            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            respHeaders.setContentDispositionFormData("attachment",
                                                      URLDecoder.decode(originalFilename,
                                                                        StandardCharsets.UTF_8));
            respHeaders.setAccessControlExposeHeaders(
                    Collections.singletonList("Content-Disposition"));
            return new ResponseEntity<>(new FileSystemResource(file), respHeaders, HttpStatus.OK);
        }
    }
}
