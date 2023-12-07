package com.furenqiang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.furenqiang.system.entity.UploadFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UploadFileMapper extends BaseMapper<UploadFile> {

    int save(UploadFile uploadFile);

    UploadFile getFileById(@Param("id") int id);

    List<UploadFile> getFileList();
}
