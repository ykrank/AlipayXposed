package com.github.ykrank.alipayxposed.app.data.db;

import com.github.ykrank.alipayxposed.app.data.db.dbmodel.DaoMaster;
import com.github.ykrank.alipayxposed.app.data.db.dbmodel.DaoSession;
import com.github.ykrank.androidtools.guava.Supplier;
import com.github.ykrank.androidtools.guava.Suppliers;


public final class AppDaoSessionManager {
    private DaoMaster.OpenHelper daoHelper;
    private final Supplier<DaoSession> mDaoSessionSupplier = new Supplier<DaoSession>() {

        @Override
        public DaoSession get() {
            return new DaoMaster(daoHelper.getWritableDb()).newSession();
        }
    };

    private volatile Supplier<DaoSession> mDaoSessionMemorized = Suppliers.memoize(mDaoSessionSupplier);

    public AppDaoSessionManager(DaoMaster.OpenHelper daoHelper) {
        this.daoHelper = daoHelper;
    }

    /**
     * Used for re invalidate daoSession if database change.
     */
    public void invalidateDaoSession() {
        mDaoSessionMemorized = Suppliers.memoize(mDaoSessionSupplier);
    }

    public DaoSession getDaoSession() {
        return mDaoSessionMemorized.get();
    }

    /**
     * close db, then call {@link #getDaoSession()} will throw null exception.
     * you should call {@link #invalidateDaoSession()} before use db
     */
    public void closeDb() {
        daoHelper.close();
        mDaoSessionMemorized = null;
    }
}
