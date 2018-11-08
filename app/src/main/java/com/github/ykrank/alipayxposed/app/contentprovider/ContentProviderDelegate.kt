package com.github.ykrank.alipayxposed.app.contentprovider

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

interface ContentProviderDelegate{
    /**
     * 表名
     */
    val tableName:String

    fun setContext(context: Context)

    /**
     * @see android.content.ContentProvider.insert
     */
    fun insert(uri: Uri, values: ContentValues): Uri?
    /**
     * @see android.content.ContentProvider.query
     */
    fun query(uri: Uri, projection: Array<String>?, selection: String?,
              selectionArgs: Array<String>?, sortOrder: String?): Cursor?
    /**
     * @see android.content.ContentProvider.update
     */
    fun update(uri: Uri, values: ContentValues?, selection: String?,
               selectionArgs: Array<String>?): Int
    /**
     * @see android.content.ContentProvider.delete
     */
    fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int
}