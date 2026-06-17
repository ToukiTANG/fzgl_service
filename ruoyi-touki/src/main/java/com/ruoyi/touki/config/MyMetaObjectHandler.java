package com.ruoyi.touki.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    public MyMetaObjectHandler() {
        System.out.println("======== MyMetaObjectHandler Loaded ========");
    }


    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("======== insertFill ========");
        String username = SecurityUtils.getUsername();
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());

        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());

        this.strictInsertFill(metaObject, "createBy", String.class, username);

        this.strictInsertFill(metaObject, "updateBy", String.class, username);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String username = SecurityUtils.getUsername();
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());

        this.strictUpdateFill(metaObject, "updateBy", String.class, username);
    }
}
