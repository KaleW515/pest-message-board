package com.kalew515.pestmessageboardbackend.service;

import com.kalew515.pestmessageboardbackend.dao.AttachmentDao;
import com.kalew515.pestmessageboardbackend.model.Attachment;
import com.kalew515.pestmessageboardbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class AttachmentService {

    @Value("${avatar.save-path}")
    private String fileSavePath;

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private CurrUserService currUserService;

    @Autowired
    private UserService userService;

    private String getFileSavePath () {
        File path = new File(fileSavePath);
        if (!path.isDirectory()) {
            boolean mkdirs = path.mkdirs();
        }
        return path.getAbsolutePath();
    }

    public Integer saveFile (MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID()
                          .toString();
        FileOutputStream fos = new FileOutputStream(String.format("%s/%s", getFileSavePath(), uuid));
        fos.write(file.getBytes());
        fos.close();
        Attachment attachment = new Attachment(uuid, file.getOriginalFilename());
        User user = new User();
        user.setUserId(currUserService.getCurrUser()
                                      .getUserId());
        user.setAvatarUUID(uuid);
        userService.updateAvatar(user);
        return attachmentDao.insertAttachment(attachment);
    }

    public String getOriginalFilenameByUUID (String uuid) {
        return attachmentDao.getFilenameByUUID(uuid);
    }

    public File getFileByUUID (String uuid) {
        return new File(String.format("%s/%s", getFileSavePath(), uuid));
    }
}
