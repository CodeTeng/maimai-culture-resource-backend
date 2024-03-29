package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.model.entity.Tag;
import com.teng.mai.service.TagService;
import com.teng.mai.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2024-03-28 14:37:09
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




