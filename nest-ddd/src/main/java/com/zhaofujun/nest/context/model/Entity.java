package com.zhaofujun.nest.context.model;

import com.zhaofujun.nest.context.ServiceContext;
import com.zhaofujun.nest.NullException;
import com.zhaofujun.nest.core.EntityFactory;
import com.zhaofujun.nest.core.Identifier;
import com.zhaofujun.nest.utils.EntityUtils;

import java.io.Serializable;

public abstract class Entity<T extends Identifier> implements Serializable {
    protected T id;

    public T getId() {
        return id;
    }


//    public <U extends Role> U act(Class<U> clazz, Identifier identifier) {
//        if (identifier == null)
//            throw new NullException("角色的ID不能为空");
//        U u = EntityFactory.load(clazz, identifier);
//        if (u == null) {
//            u = EntityFactory.create(clazz, identifier);
//            if (Role.class.isInstance(u)) {
//                EntityUtils.setValue(Role.class, u, "actor", this);
//            }
//        }
//        return u;
//    }
//
//    public <U extends Role> U act(Class<U> clazz) {
//        return act(clazz, this.getId());
//    }


    private boolean _represented;

    //是否处于加载中状态
    private boolean _loading;

    private boolean _newInstance;

    private void updateEntityObject() {
        if (verify()) {
            ServiceContext.getCurrent()
                    .getContextUnitOfWork().updateEntityObject(this);
        } else {
            throw new VerifyFailedException("验证实体失败");
        }
    }

    private void newEntityObject() {
        if (verify()) {
            ServiceContext.getCurrent()
                    .getContextUnitOfWork().addEntityObject(this);
        } else {
            throw new VerifyFailedException("验证实体失败");
        }
    }


    protected boolean verify() {
        return true;
    }

    public void delete() {
        if (!this._newInstance) //  不是新对象的时候才需要调用移除操作
            ServiceContext.getCurrent().getContextUnitOfWork().removeEntityObject(this);
    }
}

