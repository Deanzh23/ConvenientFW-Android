package com.dean.android.framework.convenient.database;

import com.dean.android.framework.convenient.database.type.TableUtil;

/**
 * 条件适配器
 * <p>
 * Created by Dean on 16/5/24.
 */
public class Selector {

    private StringBuilder mSQLBuilder;

    public Selector() {
    }

    public Selector(String column, String symbol, Object value) {
        initSQLIfNotExists();

        if (value == null)
            mSQLBuilder.append(column + " " + "is" + " " + null);
        else
            mSQLBuilder.append(column + " " + symbol + " " + TableUtil.getTableTypeValue(value));
    }

    public Selector and(String column, String symbol, Object value) {
        valueNullCheck("AND", column, symbol, value);

        return this;
    }

    public Selector or(String column, String symbol, Object value) {
        valueNullCheck("OR", column, symbol, value);

        return this;
    }

    public Selector like(String column, String value) {
        initSQLIfNotExists();

        mSQLBuilder.append(column + " LIKE " + TableUtil.getTableTypeValue(value));

        return this;
    }

    public Selector andLike(String column, String value) {
        mSQLBuilder.append(" AND " + column + " LIKE " + TableUtil.getTableTypeValue(value));

        return this;
    }

    public Selector orLike(String column, String value) {
        mSQLBuilder.append(" OR " + column + " LIKE " + TableUtil.getTableTypeValue(value));

        return this;
    }

    public Selector orderBy(String columns, boolean desc) {
        mSQLBuilder.append(" ORDER BY " + columns);
        mSQLBuilder.append(desc ? " DESC" : " ASC");

        return this;
    }

    public Selector andBrace(String column, String symbol, Object value) {
        valueNullCheck("( AND", column, symbol, value);

        return this;
    }

    public Selector orBrace(String column, String symbol, Object value) {
        valueNullCheck("( OR", column, symbol, value);

        return this;
    }

    public Selector endBrace() {
        mSQLBuilder.append(" ) ");

        return this;
    }

    /**
     * 拼装括号中的 AND 条件
     *
     * @param selector
     */
    public Selector andWhere(Selector selector) {
        mSQLBuilder.append(" AND ( ");
        mSQLBuilder.append(selector.getSQLBuilder().toString());
        mSQLBuilder.append(" ) ");

        return this;
    }

    /**
     * 拼装括号中的 OR 条件
     *
     * @param selector
     */
    public Selector orWhere(Selector selector) {
        mSQLBuilder.append(" OR ( ");
        mSQLBuilder.append(selector.getSQLBuilder().toString());
        mSQLBuilder.append(" ) ");

        return this;
    }

    private void initSQLIfNotExists() {
        if (mSQLBuilder == null)
            mSQLBuilder = new StringBuilder();
    }

    /**
     * 获取sql语句
     *
     * @return
     */
    public StringBuilder getSQLBuilder() {
        return mSQLBuilder;
    }

    private void valueNullCheck(String link, String column, String symbol, Object value) {
        if (value == null) {
            if ("=".equals(symbol))
                mSQLBuilder.append(" " + link + " " + column + " is " + null);
            else if ("!=".equals(symbol))
                mSQLBuilder.append(" " + link + " " + column + " is not " + null);
        } else
            mSQLBuilder.append(" " + link + " " + column + " " + symbol + " " + TableUtil.getTableTypeValue(value));
    }

}
