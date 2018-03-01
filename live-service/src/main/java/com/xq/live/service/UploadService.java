package com.xq.live.service;

import org.javatuples.Triplet;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-28 21:36
 * @copyright:hbxq
 **/
public interface UploadService {

    public String uploadFileToCos(String localPath, String userName);

    public boolean deleteTempImage(Triplet<String, String, String> triplet);
}
