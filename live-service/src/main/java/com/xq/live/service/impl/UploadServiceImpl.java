package com.xq.live.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.xq.live.common.Constants;
import com.xq.live.service.UploadService;
import com.xq.live.web.controller.UploadController;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Triplet;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * ${DESCRIPTION}
 *
 * @author zhangpeng32
 * @date 2018-02-28 21:38
 * @copyright:hbxq
 **/
@Service
public class UploadServiceImpl implements UploadService {

    private static Logger logger = Logger.getLogger(UploadServiceImpl.class);

    // 1 初始化用户身份信息(secretId, secretKey)
    private COSCredentials cred = new BasicCOSCredentials(Constants.ACCESS_KEY, Constants.SECRET_KEY);
    // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    private ClientConfig clientConfig = new ClientConfig(new Region(Constants.REGION_NAME));
    // 3 生成cos客户端
    private COSClient cosClient = new COSClient(cred, clientConfig);

    private final String bucketName = Constants.BUCKET_NAME;

    @Override
    public String uploadFileToCos(String localPath, String userName) {
        String cosPath = null;
        // 获取文件名
        File file = new File(localPath);
        String key = "/" + userName + "_" + file.getName();
        try {
            PutObjectResult putObjectResult = cosClient.putObject(bucketName, key, file);
            String etag = putObjectResult.getETag();
            if (StringUtils.isNotEmpty(etag)) {
                cosPath = Constants.COS_IMAGE_BASE_PATH + key;
                return cosPath;
            }
        } catch (CosServiceException e) {
            logger.error("上传图片到cos异常CosServiceException ：" + e.getErrorMessage());
        } catch (CosClientException e1) {
            logger.error("上传图片到cos异常CosClientException ：" + e1.getStackTrace());
        }
        return null;
    }

    @Override
    public boolean deleteTempImage(Triplet<String, String, String> triplet) {
        try {
            for (Object tl : triplet) {
                if (tl == null) {
                    continue;
                }
                File file = new File(tl.toString());
                if (file.exists()) {
                    file.delete();
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("删除文件异常");
        }
        return false;
    }
}
