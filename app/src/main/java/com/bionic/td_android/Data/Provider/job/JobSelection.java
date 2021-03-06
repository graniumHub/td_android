package com.bionic.td_android.Data.Provider.job;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.bionic.td_android.Data.Provider.base.AbstractSelection;

/**
 * Selection for the {@code job} table.
 */
public class JobSelection extends AbstractSelection<JobSelection> {
    @Override
    protected Uri baseUri() {
        return JobColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code JobCursor} object, which is positioned before the first entry, or null.
     */
    public JobCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new JobCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public JobCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code JobCursor} object, which is positioned before the first entry, or null.
     */
    public JobCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new JobCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public JobCursor query(Context context) {
        return query(context, null);
    }


    public JobSelection id(long... value) {
        addEquals("job." + JobColumns._ID, toObjectArray(value));
        return this;
    }

    public JobSelection idNot(long... value) {
        addNotEquals("job." + JobColumns._ID, toObjectArray(value));
        return this;
    }

    public JobSelection orderById(boolean desc) {
        orderBy("job." + JobColumns._ID, desc);
        return this;
    }

    public JobSelection orderById() {
        return orderById(false);
    }

    public JobSelection userid(Long... value) {
        addEquals(JobColumns.USERID, value);
        return this;
    }

    public JobSelection useridNot(Long... value) {
        addNotEquals(JobColumns.USERID, value);
        return this;
    }

    public JobSelection useridGt(long value) {
        addGreaterThan(JobColumns.USERID, value);
        return this;
    }

    public JobSelection useridGtEq(long value) {
        addGreaterThanOrEquals(JobColumns.USERID, value);
        return this;
    }

    public JobSelection useridLt(long value) {
        addLessThan(JobColumns.USERID, value);
        return this;
    }

    public JobSelection useridLtEq(long value) {
        addLessThanOrEquals(JobColumns.USERID, value);
        return this;
    }

    public JobSelection orderByUserid(boolean desc) {
        orderBy(JobColumns.USERID, desc);
        return this;
    }

    public JobSelection orderByUserid() {
        orderBy(JobColumns.USERID, false);
        return this;
    }

    public JobSelection jobid(Long... value) {
        addEquals(JobColumns.JOBID, value);
        return this;
    }

    public JobSelection jobidNot(Long... value) {
        addNotEquals(JobColumns.JOBID, value);
        return this;
    }

    public JobSelection jobidGt(long value) {
        addGreaterThan(JobColumns.JOBID, value);
        return this;
    }

    public JobSelection jobidGtEq(long value) {
        addGreaterThanOrEquals(JobColumns.JOBID, value);
        return this;
    }

    public JobSelection jobidLt(long value) {
        addLessThan(JobColumns.JOBID, value);
        return this;
    }

    public JobSelection jobidLtEq(long value) {
        addLessThanOrEquals(JobColumns.JOBID, value);
        return this;
    }

    public JobSelection orderByJobid(boolean desc) {
        orderBy(JobColumns.JOBID, desc);
        return this;
    }

    public JobSelection orderByJobid() {
        orderBy(JobColumns.JOBID, false);
        return this;
    }
}
