package com.furenqiang.system.service;

import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.UploadFile;

import java.io.IOException;

public interface UploadFileService {

    String save(String name, String path, String fileTree, String originalName, String unzipFolder, Integer fileNum, float size);

    UploadFile getFileById(Integer id);

    String parseFile(java.io.File dest, String unzipPath, String fileName, String folder)throws IOException;

    ResponseResult getFileList();

    ResponseResult addChunkFiles(String originalFilename);

    ResponseResult getFileDetails(String id);
}
