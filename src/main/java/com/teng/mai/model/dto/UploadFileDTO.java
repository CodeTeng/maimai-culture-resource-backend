package com.teng.mai.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 14:32
 */
@Data
@ApiModel("文件上传请求体")
public class UploadFileDTO implements Serializable {
    @ApiModelProperty(value = "业务类型", required = true, example = "user_avatar")
    @NotBlank(message = "业务类型不能为空")
    private String biz;
    private static final long serialVersionUID = 1L;
}
