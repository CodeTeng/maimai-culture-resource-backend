package com.teng.mai.controller;

import cn.hutool.core.io.FileUtil;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.config.CosClientConfig;
import com.teng.mai.constant.FileConstant;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.manager.CosManager;
import com.teng.mai.model.dto.UploadFileDTO;
import com.teng.mai.model.enums.FileUploadBizEnum;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 14:31
 */
@RestController
@Api(tags = "公共管理模块")
@Slf4j
public class CommonController {
    @Resource
    private UserService userService;
    @Resource
    private CosManager cosManager;
    @Resource
    private CosClientConfig cosClientConfig;

    @PostMapping("/image/upload")
    @ApiOperation("图片上传")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile, @Validated UploadFileDTO uploadFileDTO) {
        String biz = uploadFileDTO.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        UserVO user = userService.getCurrentUser();
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), user.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success(FileConstant.COS_HOST + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param fileUploadBizEnum 业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        long ONE_M = 2 * 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 2M");
            }
        } else if (FileUploadBizEnum.ARTICLE_COVER.equals(fileUploadBizEnum)) {
            ONE_M = 5 * 1024 * 1024L;
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 5M");
            }
        } else if (FileUploadBizEnum.VIDEO.equals(fileUploadBizEnum)) {
            ONE_M = 20 * 1024 * 1024L;
            if (fileSize > ONE_M) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "视频大小不能超过 20M");
            }
        }
        if (!Arrays.asList("jpeg", "jpg", "svg", "png", "gif", "mp4").contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
        }
    }

    @PostMapping("/video/upload")
    @ApiOperation("视频上传")
    public BaseResponse<String> videoUpload(@RequestPart("file") MultipartFile multipartFile, @Validated UploadFileDTO uploadFileDTO) {
        String biz = uploadFileDTO.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        validFile(multipartFile, fileUploadBizEnum);
        UserVO user = userService.getCurrentUser();
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), user.getId(), filename);
        File file = null;
        VodUploadClient client = new VodUploadClient(cosClientConfig.getAccessKey(), cosClientConfig.getSecretKey());
        VodUploadRequest request = new VodUploadRequest();
        request.setSubAppId(1310538376L);
        try {
            file = File.createTempFile(filepath, ".mp4");
            multipartFile.transferTo(file);
            request.setMediaFilePath(file.getAbsolutePath());
            VodUploadResponse response = client.upload("ap-chongqing", request);
            log.info("Upload MediaUrl = {}", response.getMediaUrl());
            log.info("Upload CoverUrl = {}", response.getCoverUrl());
            return ResultUtils.success(response.getMediaUrl());
        } catch (Exception e) {
            // 业务方进行异常处理
            log.error("Upload Err = {}", e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

}
