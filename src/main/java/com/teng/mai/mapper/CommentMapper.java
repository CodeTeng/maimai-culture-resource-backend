package com.teng.mai.mapper;

import com.teng.mai.model.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teng.mai.model.vo.ReplyVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author teng
 * @description 针对表【comment(评论表)】的数据库操作Mapper
 * @createDate 2024-03-22 23:31:27
 * @Entity com.teng.mai.model.entity.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询子评论
     */
    List<ReplyVO> listReplies(@Param("commentIdList") List<Long> commentIdList);
}




