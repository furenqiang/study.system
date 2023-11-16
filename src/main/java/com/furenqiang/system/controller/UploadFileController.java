package com.furenqiang.system.controller;

import cn.novelweb.tool.upload.local.LocalUpload;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.MyProps;
import com.furenqiang.system.entity.SysChunkFiles;
import com.furenqiang.system.entity.UploadFile;
import com.furenqiang.system.service.UploadFileService;
import com.furenqiang.system.utils.EncodingUtils;
import com.furenqiang.system.utils.UploadUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/uploadFile")
@Api(tags = "本地和ftp服务器的上传与下载功能")
public class UploadFileController {

    @Autowired
    UploadFileService uploadfileService;
    
    @Autowired
    private MyProps myProps;

    /**
     * @Author Eric
     */
    @ApiOperation(value = "获取文件列表", httpMethod = "GET")
    @ApiResponses({@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败")})
    @GetMapping(value = "/getFileList")
    public ResponseResult getFileList() {
        return uploadfileService.getFileList();
    }

    /**
     * @Author Eric
     */
    @ApiOperation(value = "上传并保存文件", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "excelfile", value = "文件", dataType = "CommonsMultipartFile")})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败")})
    @PostMapping(value = "uploadAndSave", headers = ("content-type=multipart/*"))
    public String save(@ApiParam(value = "file", required = true) MultipartFile file,
                       HttpServletRequest request) {
        if (file.isEmpty()) {
            return "上传失败，文件为空";
        }

        //生成唯一文件名
        String fileName = UploadUtil.generateFileName(file.getOriginalFilename());
        int size = (int) file.getSize();
        log.info("-------上传文件:" + fileName + ",大小为：" + size);
        File dest = new File(myProps.getFilePath() + "/" + fileName);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            //解析存parseFile
            List<String> fnList = Arrays.asList(fileName.split("\\."));
            //先解压
            List<String> numList = UploadUtil.unzipFile(dest, myProps.getUnzipPath() + "/" + fnList.get(0), fnList.get(0));
            String jsonFileTree = uploadfileService.parseFile(dest, myProps.getUnzipPath(), file.getOriginalFilename(), fnList.get(0));
            //文件名称、路径存库
            //获取解压后文件大小
            long length = FileUtils.sizeOfDirectory(new File(myProps.getUnzipPath() + fnList.get(0)));
            DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
            float unzipSize = Float.parseFloat(df.format((float) length / 1024000));
            String save = uploadfileService.save(fileName, dest.getPath(), jsonFileTree, file.getOriginalFilename(),
                    myProps.getUnzipPath() + fnList.get(0), numList.size(), unzipSize);
            return save;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "上传失败，非法状态异常";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "上传失败，服务器异常";
        }
    }

    /**
     * @Author Eric
     */
    @ApiOperation(value = "下载文件", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "原文件ID", dataType = "String"),
            @ApiImplicitParam(name = "filePath", value = "文件完整路径", dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "文件名称", dataType = "String")})
    @RequestMapping("/download")
    public void ftpDownload(HttpServletResponse response, Integer id, String filePath, String fileName) throws Exception {
        UploadFile fileById = uploadfileService.getFileById(id);
        //testzip\testnextfile\nextnextfile\安阳市安阳县安阳村.json
        if (!(filePath == null)) {
            filePath = fileById.getUnzipFolder() + "/" + filePath;
        } else {
            filePath = fileById.getPath();
            fileName = fileById.getName();
        }
        File file = new File(filePath);
        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            log.info("----------文件下载：---" + URLEncoder.encode(fileName, "UTF-8"));
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * @Author Eric
     */
    @ApiOperation(value = "FTP上传并保存文件", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "excelfile", value = "文件", dataType = "CommonsMultipartFile")})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败")})
    @PostMapping("/ftpUploadAndSave")
    public String ftpUploadAndSave(@ApiParam(value = "file", required = true) MultipartFile file,
                                   HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            return "上传失败，文件为空！";
        }
        FTPClient ftp = new FTPClient();
        ftp.connect(myProps.getFtpIp(), myProps.getFtpPort());
        boolean login = ftp.login(myProps.getFtpUsername(), myProps.getFtpPassword());
        if (!login) {
            return "上传失败，ftp用户名或密码错误！";
        }
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        ftp.setCharset(Charset.forName("UTF-8"));
        ftp.setControlEncoding("UTF-8");
        try {
            String fileName = UploadUtil.generateFileName(file.getOriginalFilename());
            OutputStream os = ftp.storeFileStream(new String(fileName.getBytes("GBK"), "iso-8859-1"));
            InputStream inputStream = file.getInputStream();
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(b)) != -1) {
                os.write(b, 0, len);
            }
            os.close();
            inputStream.close();
            ftp.logout();
            //解析存parseFile
            File dest = new File(myProps.getFtpPath() + "/" + fileName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }
            file.transferTo(dest); //保存文件
            List<String> fnList = Arrays.asList(fileName.split("\\."));
            //先解压
            List<String> numList = UploadUtil.unzipFile(dest, myProps.getFtpUnzipPath() + "\\" + fnList.get(0), fnList.get(0));
            String jsonFileTree = uploadfileService.parseFile(dest, myProps.getFtpUnzipPath(), file.getOriginalFilename(), fnList.get(0));
            //存库
            //获取解压后文件大小
            long length = FileUtils.sizeOfDirectory(new File(myProps.getFtpUnzipPath() + fnList.get(0)));
            DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
            float unzipSize = Float.parseFloat(df.format((float) length / 1024000));
            uploadfileService.save(fileName, "ftp://" + myProps.getFtpIp() + ":" + myProps.getFtpPort() + "/" + fileName, jsonFileTree,
                    file.getOriginalFilename(), fnList.get(0), numList.size(), unzipSize);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "上传到ftp服务器成功！";
    }

    /**
     * @Author Eric
     */
    @ApiOperation(value = "ftp下载文件", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "原文件ID", dataType = "String"),
            @ApiImplicitParam(name = "filePath", value = "文件完整路径", dataType = "String")})
    @RequestMapping("/ftpDownload")
    public String ftpDownload(HttpServletResponse response, Integer id, String filePath) throws Exception {
        UploadFile fileById = uploadfileService.getFileById(id);
        if (!(filePath == null)) {
            //处理ftp服务器编码问题
            List<String> fnList = Arrays.asList(filePath.split("/"));
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < fnList.size(); i++) {
                if (i == fnList.size() - 1) {
                    sb.append(URLEncoder.encode(fnList.get(i), "GBK"));
                } else {
                    sb.append(URLEncoder.encode(fnList.get(i), "GBK")).append("/");
                }
            }
            return myProps.getFtpUnzipUrl() + URLEncoder.encode(fileById.getUnzipFolder(), "GBK") + "/" + sb.toString();
            //return myProps.getFtpUnzipUrl()+ filePath;
        } else {
            String name = fileById.getName();
            return "ftp://" + myProps.getFtpIp() + ":" + myProps.getFtpPort() + "/" + URLEncoder.encode(name, "GBK");
        }
    }

    /**
     * 断点续传方式上传文件：用于大文件上传
     *
     * @param param
     * @param request
     * @return
     * @Author Eric
     */
    @ApiOperation(value = "断点续传方式上传文件：用于大文件上传", httpMethod = "POST")
    @ApiResponses({@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败")})
    @PostMapping(value = "/breakPointUpload", consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json;" +
            "charset=UTF-8")
    public ResponseResult breakPointUpload(UploadFileParam param, HttpServletRequest request) {
        try {
            // 这里的 chunkSize(分片大小) 要与前端传过来的大小一致
            cn.novelweb.tool.http.Result result = LocalUpload.fragmentFileUploader(param, myProps.getFilePath(), myProps.getUnzipPath(), 1024L,
                    request);
            //存库
            ResponseResult responseResult = uploadfileService.addChunkFiles(param.getFile().getOriginalFilename());
            responseResult.setData(result);
            return responseResult;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseResult(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());
    }

    /**
     * 添加文件
     * 断点续传完成后上传文件信息进行入库操作
     *
     * @param dto
     * @return
     */
//    @ApiOperation(value = "断点续传完成后添加文件信息", httpMethod = "POST")
//    @ApiImplicitParams({ @ApiImplicitParam(name = "fileName", value = "文件名称", dataType = "String"),
//            @ApiImplicitParam(name = "suffix", value = "文件后缀", dataType = "String") })
//    @ApiResponses({ @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败") })
//    @PostMapping("/addChunkFiles")
//    public ResponseResult addChunkFiles(@RequestBody SysChunkFiles dto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return new ResponseResult(ResponseEnum.ERROR.getCode(), bindingResult.getAllErrors().get(0).getDefaultMessage());
//        }
//        return uploadfileService.addChunkFiles(dto);
//    }

    /**
     * 文件下载
     *
     * @param id
     * @param request
     * @param response
     */
    @ApiOperation(value = "下载文件", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "文件ID", dataType = "String")})
    @ApiResponses({@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 400, message = "失败")})
    @GetMapping(value = "/download/{id}")
    public void viewFilesImage(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            ResponseResult fileDetails = uploadfileService.getFileDetails(id);
            SysChunkFiles sysChunkFiles = (SysChunkFiles) fileDetails.getData();
            String filename = sysChunkFiles.getFilePath();
            inputStream = getFileInputStream(sysChunkFiles);
            response.setHeader("Content-Disposition", "attachment;filename=" + EncodingUtils.convertToFileName(request, filename));
            // 获取输出流
            outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("文件下载出错", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream getFileInputStream(SysChunkFiles files) {
        try {
            File file = new File(myProps.unzipPath + File.separator + files.getFilePath());
            return new FileInputStream(file);
        } catch (Exception e) {
            log.error("获取文件输入流出错", e);
        }
        return null;
    }
}
