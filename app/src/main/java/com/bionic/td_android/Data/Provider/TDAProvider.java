package com.bionic.td_android.Data.Provider;

import java.util.Arrays;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bionic.td_android.BuildConfig;
import com.bionic.td_android.Data.Provider.base.BaseContentProvider;
import com.bionic.td_android.Data.Provider.job.JobColumns;
import com.bionic.td_android.Data.Provider.ride.RideColumns;
import com.bionic.td_android.Data.Provider.shift.ShiftColumns;
import com.bionic.td_android.Data.Provider.user.UserColumns;
import com.bionic.td_android.Data.Provider.workschedule.WorkscheduleColumns;

public class TDAProvider extends BaseContentProvider {
    private static final String TAG = TDAProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.bionic.td_android";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_JOB = 0;
    private static final int URI_TYPE_JOB_ID = 1;

    private static final int URI_TYPE_RIDE = 2;
    private static final int URI_TYPE_RIDE_ID = 3;

    private static final int URI_TYPE_SHIFT = 4;
    private static final int URI_TYPE_SHIFT_ID = 5;

    private static final int URI_TYPE_USER = 6;
    private static final int URI_TYPE_USER_ID = 7;

    private static final int URI_TYPE_WORKSCHEDULE = 8;
    private static final int URI_TYPE_WORKSCHEDULE_ID = 9;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, JobColumns.TABLE_NAME, URI_TYPE_JOB);
        URI_MATCHER.addURI(AUTHORITY, JobColumns.TABLE_NAME + "/#", URI_TYPE_JOB_ID);
        URI_MATCHER.addURI(AUTHORITY, RideColumns.TABLE_NAME, URI_TYPE_RIDE);
        URI_MATCHER.addURI(AUTHORITY, RideColumns.TABLE_NAME + "/#", URI_TYPE_RIDE_ID);
        URI_MATCHER.addURI(AUTHORITY, ShiftColumns.TABLE_NAME, URI_TYPE_SHIFT);
        URI_MATCHER.addURI(AUTHORITY, ShiftColumns.TABLE_NAME + "/#", URI_TYPE_SHIFT_ID);
        URI_MATCHER.addURI(AUTHORITY, UserColumns.TABLE_NAME, URI_TYPE_USER);
        URI_MATCHER.addURI(AUTHORITY, UserColumns.TABLE_NAME + "/#", URI_TYPE_USER_ID);
        URI_MATCHER.addURI(AUTHORITY, WorkscheduleColumns.TABLE_NAME, URI_TYPE_WORKSCHEDULE);
        URI_MATCHER.addURI(AUTHORITY, WorkscheduleColumns.TABLE_NAME + "/#", URI_TYPE_WORKSCHEDULE_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return TDASQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_JOB:
                return TYPE_CURSOR_DIR + JobColumns.TABLE_NAME;
            case URI_TYPE_JOB_ID:
                return TYPE_CURSOR_ITEM + JobColumns.TABLE_NAME;

            case URI_TYPE_RIDE:
                return TYPE_CURSOR_DIR + RideColumns.TABLE_NAME;
            case URI_TYPE_RIDE_ID:
                return TYPE_CURSOR_ITEM + RideColumns.TABLE_NAME;

            case URI_TYPE_SHIFT:
                return TYPE_CURSOR_DIR + ShiftColumns.TABLE_NAME;
            case URI_TYPE_SHIFT_ID:
                return TYPE_CURSOR_ITEM + ShiftColumns.TABLE_NAME;

            case URI_TYPE_USER:
                return TYPE_CURSOR_DIR + UserColumns.TABLE_NAME;
            case URI_TYPE_USER_ID:
                return TYPE_CURSOR_ITEM + UserColumns.TABLE_NAME;

            case URI_TYPE_WORKSCHEDULE:
                return TYPE_CURSOR_DIR + WorkscheduleColumns.TABLE_NAME;
            case URI_TYPE_WORKSCHEDULE_ID:
                return TYPE_CURSOR_ITEM + WorkscheduleColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_JOB:
            case URI_TYPE_JOB_ID:
                res.table = JobColumns.TABLE_NAME;
                res.idColumn = JobColumns._ID;
                res.tablesWithJoins = JobColumns.TABLE_NAME;
                res.orderBy = JobColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_RIDE:
            case URI_TYPE_RIDE_ID:
                res.table = RideColumns.TABLE_NAME;
                res.idColumn = RideColumns._ID;
                res.tablesWithJoins = RideColumns.TABLE_NAME;
                res.orderBy = RideColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_SHIFT:
            case URI_TYPE_SHIFT_ID:
                res.table = ShiftColumns.TABLE_NAME;
                res.idColumn = ShiftColumns._ID;
                res.tablesWithJoins = ShiftColumns.TABLE_NAME;
                res.orderBy = ShiftColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_USER:
            case URI_TYPE_USER_ID:
                res.table = UserColumns.TABLE_NAME;
                res.idColumn = UserColumns._ID;
                res.tablesWithJoins = UserColumns.TABLE_NAME;
                res.orderBy = UserColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_WORKSCHEDULE:
            case URI_TYPE_WORKSCHEDULE_ID:
                res.table = WorkscheduleColumns.TABLE_NAME;
                res.idColumn = WorkscheduleColumns._ID;
                res.tablesWithJoins = WorkscheduleColumns.TABLE_NAME;
                res.orderBy = WorkscheduleColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_JOB_ID:
            case URI_TYPE_RIDE_ID:
            case URI_TYPE_SHIFT_ID:
            case URI_TYPE_USER_ID:
            case URI_TYPE_WORKSCHEDULE_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
