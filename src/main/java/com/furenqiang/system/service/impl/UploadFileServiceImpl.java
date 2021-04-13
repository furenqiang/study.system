package com.furenqiang.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.UploadFile;
import com.furenqiang.system.mapper.UploadFileMapper;
import com.furenqiang.system.service.UploadFileService;
import com.furenqiang.system.vo.FileTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    UploadFileMapper fileMapper;

    List <FileTree> node=new LinkedList();

    @Override
    public String parseFile(java.io.File dest,String unzipPath,String fileName,String folder)throws IOException {
        node.removeAll(node);
        int level=0;
        List<String> fnList = Arrays.asList(fileName.split("\\."));
        List<FileTree> files=getFile(unzipPath+folder+"/"+fnList.get(0),1,level);
        List<FileTree> fileTree = getFileTree(files);
        FileTree ft=new FileTree();
        ft.setId(0);
        ft.setpId(0);
        ft.setHasChildren(true);
        ft.setChildren(fileTree);
        ft.setName(fnList.get(0));
        return JSONObject.toJSONString(ft);
    }

    //递归成树
    private List<FileTree> getFileTree(List<FileTree> files) {
        List<FileTree> fileTreeList=new LinkedList<>();
        for(FileTree ft:files){
            //[{"id":1,"name":"testnextfile","pId":0},{"id":11,"name":"安阳市安阳县.json","pId":1},
            // {"id":2,"name":"安阳市.json","pId":0},{"id":3,"name":"鹤壁市.json","pId":0}]
            if(ft.isHasChildren()&&ft.getpId()==0){
                List<FileTree> childrenTree = getChildrenTree(ft, files);
                ft.setChildren(childrenTree);
                fileTreeList.add(ft);
            }else {
                if(ft.getpId()==0){
                    fileTreeList.add(ft);
                }
            }
        }
        return fileTreeList;
    }

    private List<FileTree> getChildrenTree(FileTree ft,List<FileTree> files) {
        List<FileTree> ftList=new LinkedList<>();
        for(FileTree f:files){
            if(f.isHasChildren()&&ft.getId()!=f.getId()&&f.getId()>ft.getId()){
                if(f.getpId()==ft.getId()){
                    List<FileTree> childrenTree = getChildrenTree(f, files);
                    f.setChildren(childrenTree);
                    ftList.add(f);
                }
            }else{
                if(f.getpId()==ft.getId()){
                    ftList.add(f);
                }
            }
        }
        return ftList;

    }

    private  List<FileTree> getFile(String path,int id,int pid) {
        java.io.File file = new java.io.File(path);
        if(file.exists()) {
            java.io.File[] array = file.listFiles();
            List fileList = Arrays.asList(array);
            //对读到的本地文件夹进行排序
            Collections.sort(fileList, new Comparator<java.io.File>() {
                @Override
                public int compare(java.io.File o1, java.io.File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });

            for (int i = 0; i < array.length; i++) {
                FileTree tree = new FileTree();
                tree.setpId(pid);
                tree.setId(id);
                tree.setName(array[i].getName());
                //判断是否为文件夹，是的话进行递归
                if (array[i].isDirectory()) {
                    tree.setHasChildren(true);
                    node.add(tree);
                    //进行递归，此时的pid为上一级的id
                    getFile(array[i].getPath(), id * 10 + 1 + i, id);
                    id++;
                } else {
                    tree.setHasChildren(false);
                    node.add(tree);
                    id++;
                }
            }
        }
        else
        {
            System.out.println("文件不存在");
        }
        return node;
    }

    @Override
    public ResponseResult getFileList() {
        ResponseResult res=new ResponseResult();
        List<UploadFile> fileList = fileMapper.getFileList();
        res.setTotal((long) fileList.size());
        res.setCode(ResponseEnum.SUCCESS.getCode());
        res.setData(fileList);
        res.setMessage(ResponseEnum.SUCCESS.getMessage());
        return res;
    }

    @Override
    public String save(String name, String path,String fileTree,String originalName,String unzipFolder,Integer fileNum,float size) {
        UploadFile uf=new UploadFile();
        uf.setName(name);
        uf.setPath(path);
        uf.setOriginalName(originalName);
        uf.setUnzipFolder(unzipFolder);
        uf.setParseFile(fileTree);
        uf.setFileNum(fileNum);
        uf.setSize(size);
        int save = fileMapper.save(uf);
        if(save>0){
            return "上传成功！";
        }
        return "长传失败！";
    }

    @Override
    public UploadFile getFileById(Integer id) {
        return fileMapper.getFileById(id);
    }
}
