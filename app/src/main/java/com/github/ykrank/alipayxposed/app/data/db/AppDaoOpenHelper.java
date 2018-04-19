package com.github.ykrank.alipayxposed.app.data.db;

import android.content.Context;

import com.github.ykrank.alipayxposed.app.data.db.dbmodel.DaoMaster;
import com.github.ykrank.androidtools.util.L;

import org.greenrobot.greendao.database.Database;


public class AppDaoOpenHelper extends DaoMaster.OpenHelper {

    public AppDaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        L.e("DB upgrade##oldVersion:" + oldVersion + ",newVersion:" + newVersion);
    }
}
