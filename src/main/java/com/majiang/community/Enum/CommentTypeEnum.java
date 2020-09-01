package com.majiang.community.Enum;


//枚举类

import com.majiang.community.model.Comment;

public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    public static boolean isExit(Integer type) {
        for(CommentTypeEnum commentTypeEnum :CommentTypeEnum.values()){
            if (commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
