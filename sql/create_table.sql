create table user
(
    id             bigint auto_increment comment '主键id'
        primary key,
    username       varchar(256)                           not null comment '用户账号',
    user_password  varchar(512)                           not null comment '用户密码',
    user_role      varchar(100) default 'user'            not null comment '用户角色 user/admin/ban',
    user_phone     varchar(11)                            null comment '用户手机号',
    user_real_name varchar(60)                            null comment '用户真实姓名',
    user_gender    tinyint      default 0                 not null comment '性别：0-男性，1-女性',
    user_age       tinyint unsigned                       null comment '用户年龄',
    user_email     varchar(50)                            null comment '用户邮箱',
    user_avatar    varchar(1024)                          null comment '用户头像',
    user_birthday  date                                   null comment '用户生日',
    user_profile   varchar(512)                           null comment '用户简介',
    user_status    tinyint      default 1                 not null comment '账号状态：0-禁用 1-正常',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted        tinyint      default 0                 not null comment '是否删除 0-未删除 1-已删除',
    constraint user_pk
        unique (username)
)
    comment '用户表' collate = utf8mb4_unicode_ci;

create table article
(
    id               bigint auto_increment comment '主键id'
        primary key,
    user_id          bigint                             not null comment '作者id',
    article_content  longtext                           not null comment '文章内容',
    article_cover    varchar(512)                       null comment '文章缩略图',
    article_title    varchar(256)                       not null comment '文章标题',
    article_abstract varchar(500)                       null comment '文章摘要，如果该字段为空，默认取文章的前500个字符作为摘要',
    fav_status       tinyint  default 0                 not null comment '是否被收藏 0-未收藏 1-已收藏',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '发表时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted          tinyint  default 0                 not null comment '是否删除 0-未删除 1-已删除'
)
    comment '文化资源表' collate = utf8mb4_unicode_ci;

create table article_fav
(
    id          bigint auto_increment comment '主键id'
        primary key,
    user_id     bigint                             not null comment '用户Id',
    article_id  bigint                             not null comment '文章id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '收藏时间'
)
    comment '文章收藏表' collate = utf8mb4_unicode_ci;

create table user_follow
(
    id             bigint auto_increment comment '主键id'
        primary key,
    user_id        bigint                             not null comment '关注人Id',
    user_follow_id bigint                             not null comment '被关注人id',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '关注时间'
)
    comment '用户关注表' collate = utf8mb4_unicode_ci;

create table comment
(
    id              bigint auto_increment comment '主键id'
        primary key,
    user_id         bigint                             not null comment '评论用户Id',
    topic_id        bigint                             not null comment '评论主题id',
    comment_content text                               not null comment '评论内容',
    reply_user_id   bigint                             null comment '回复用户id',
    parent_id       bigint                             null comment '父评论id',
    type            tinyint                            not null comment '评论类型 1.文章 2.留言 ... 可扩展',
    create_time     datetime default CURRENT_TIMESTAMP not null comment '评论时间',
    update_time     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted         tinyint  default 0                 not null comment '是否删除 0-未删除 1-已删除'
)
    comment '评论表' collate = utf8mb4_unicode_ci;

create index fk_comment_parent
    on comment (parent_id);

create index fk_comment_user
    on comment (user_id);

create table friends
(
    id          bigint auto_increment comment '主键id'
        primary key,
    from_id     bigint                             not null comment '发送申请的用户id',
    receive_id  bigint                             not null comment '接收申请的用户id ',
    is_read     tinyint  default 0                 not null comment '是否已读(0-未读 1-已读)',
    status      tinyint  default 0                 not null comment '申请状态 默认0 （0-未通过 1-已同意 2-已过期 3-已撤销）',
    remark      varchar(512)                       null comment '好友申请备注信息',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    deleted     tinyint  default 0                 not null comment '是否删除 0-未删除 1-已删除'
)
    comment '好友申请管理表';

create table chat
(
    id          bigint auto_increment comment '主键id'
        primary key,
    from_id     bigint                                   not null comment '发送消息人',
    to_id       bigint                                   not null comment '接收消息人',
    text        varchar(1024) collate utf8mb4_unicode_ci null comment '聊天记录',
    chat_type   tinyint                                  not null comment '聊天类型 1-私聊 2-群聊',
    create_time datetime default CURRENT_TIMESTAMP       not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP       not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '聊天消息表';