package com.xq.live.web.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.xq.live.common.BaseResp;
import com.xq.live.common.Constants;
import com.xq.live.common.ImageUtil;
import com.xq.live.common.ResultStatus;
import com.xq.live.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片上传controller
 *
 * @author zhangpeng32
 * @date 2018-02-13 18:53
 * @copyright:hbxq
 **/
@RestController
@RequestMapping("/img")
public class UploadController {
    private static Logger logger = Logger.getLogger(UploadController.class);

    // 1 初始化用户身份信息(secretId, secretKey)
    private COSCredentials cred = new BasicCOSCredentials(Constants.ACCESS_KEY, Constants.SECRET_KEY);
    // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    private ClientConfig clientConfig = new ClientConfig(new Region(Constants.REGION_NAME));
    // 3 生成cos客户端
    private COSClient cosClient = new COSClient(cred, clientConfig);

    private final String bucketName = Constants.BUCKET_NAME;

    /**
     * 单个文件上传
     *
     * @param uploadFile
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResp<Pair<String, String>> upload(@RequestParam("file") MultipartFile uploadFile, User user, HttpServletRequest request) {
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return new BaseResp<Pair<String, String>>(ResultStatus.error_para_user_empty);
        }

        if (uploadFile.isEmpty()) {
            return new BaseResp<Pair<String, String>>(ResultStatus.error_file_upload_empty);
        }

        try {
            Pair<String, String> result = this.uploadToCos(uploadFile, this.getUploadPath(request), user.getUserName());
            return new BaseResp<Pair<String, String>>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<Pair<String, String>>(ResultStatus.error_file_upload_error);
        }
    }


    /**
     * 上传多个图片
     * @param uploadfiles
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/upload/multi")
    public BaseResp<List<Pair<String, String>>> upload(@RequestParam("file") MultipartFile[] uploadfiles, User user, HttpServletRequest request) {
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            return new BaseResp<List<Pair<String, String>>>(ResultStatus.error_para_user_empty);
        }

        if (uploadfiles.length == 0) {
            return new BaseResp<List<Pair<String, String>>>(ResultStatus.error_file_upload_empty);
        }
        List<Pair<String, String>> result = new ArrayList<Pair<String, String>>();
        try {
            for (MultipartFile uploadFile : uploadfiles) {
                Pair<String, String> p = this.uploadToCos(uploadFile, this.getUploadPath(request), user.getUserName());
                result.add(p);
            }
            return new BaseResp<List<Pair<String, String>>>(ResultStatus.SUCCESS, result);
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResp<List<Pair<String, String>>>(ResultStatus.error_file_upload_error);
        }
    }

    /**
     * 上传文件到临时目录，并上传到云COS
     *
     * @param file
     * @param uploadPath
     * @param userName
     * @return
     * @throws IOException
     */
    private Pair<String, String> uploadToCos(MultipartFile file, String uploadPath, String userName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        String localPath = uploadPath + file.getOriginalFilename();
        byte[] bytes = file.getBytes();
        Path path = Paths.get(localPath);
        if (!path.toFile().getParentFile().exists()) {
            path.toFile().getParentFile().mkdirs();
        }
        Files.write(path, bytes);
        logger.error("图片上传成功："+localPath);
        //图片压缩
        String sourceImgPath = ImageUtil.compressByQuality(localPath, 0.8f);
        String smallImgPath = ImageUtil.compressByQuality(localPath, 0.3f);

        //上传文件到腾讯云cos--缩放0.8
        String imgUrl = uploadFileToCos(sourceImgPath, userName);
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        }
        //上传文件到腾讯云cos--缩放0.3
        String smallImgUrl = uploadFileToCos(smallImgPath, userName);
        if (StringUtils.isEmpty(smallImgUrl)) {
            return null;
        }

        //删除服务器上临时文件
        deleteTempImage(new Triplet<String, String, String>(localPath, sourceImgPath, smallImgPath));
        return Pair.with(imgUrl, smallImgUrl);
    }

    /**
     * 上传文件到腾讯云cos
     *
     * @param localPath
     * @return
     */
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


    /**
     * 删除临时文件
     *
     * @param triplet
     * @return
     */
    private boolean deleteTempImage(Triplet<String, String, String> triplet) {
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

    public String getUploadPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("") + File.separator + "WEB-INF" + File.separator + "upload" + File.separator;
    }

}
