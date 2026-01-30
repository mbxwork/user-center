-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '主键'
        primary key,
    username     varchar(50)                        null comment '昵称',
    userAccount  varchar(50)                        not null comment '登录账号',
    avatarUrl    varchar(255)                       null comment '头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(255)                       not null comment '密码',
    phone        varchar(20)                        null comment '电话',
    email        varchar(100)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '用户状态 0 - 正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间（数据插入时间）',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间（数据更新时间）',
    isDelete     tinyint  default 0                 null comment '是否删除 0-未删除 1-已删除',
    userRole     int      default 0                 null comment '用户角色 0 - 普通用户 1 - 管理员'
)
    comment '用户表';



