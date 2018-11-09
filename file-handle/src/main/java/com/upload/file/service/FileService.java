package com.upload.file.service;

import com.upload.file.bean.Upload;
import com.upload.file.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    @Autowired
    FileMapper fileMapper;

    public int saveImageUrl(Upload upload) {
        return fileMapper.saveImageUrl(upload);
    }
}
