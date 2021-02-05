package com.kalew515.pestmessageboardbackend.dao;

import com.kalew515.pestmessageboardbackend.mapper.AttachmentMapper;
import com.kalew515.pestmessageboardbackend.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentDao {

    @Autowired
    private AttachmentMapper attachmentMapper;

    // 插入文件信息
    public Integer insertAttachment (Attachment attachment) {
        return attachmentMapper.insert(attachment);
    }

    // 根据uuid获取文件名
    public String getFilenameByUUID (String uuid) {
        return attachmentMapper.selectById(uuid)
                               .getAttachmentName();
    }
}
