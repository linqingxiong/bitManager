package com.xiong.bitmanager.common.config.mybatis;

import cn.hutool.json.JSONUtil;
import com.xiong.bitmanager.pojo.po.ProxyConfig;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName JsonTypeHandler
 * @Description TODO
 * @Author admin
 * @Date 2025/3/15 21:17
 * @Version 1.0
 **/
@MappedTypes(ProxyConfig.ProxyInfo.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonTypeHandler extends BaseTypeHandler<ProxyConfig.ProxyInfo> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    ProxyConfig.ProxyInfo parameter, JdbcType jdbcType) {
        try {
            ps.setString(i, JSONUtil.toJsonStr(parameter));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProxyConfig.ProxyInfo getNullableResult(ResultSet rs, String columnName) {
        try {
            return parse(rs.getString(columnName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProxyConfig.ProxyInfo getNullableResult(ResultSet rs, int columnName) {
        try {
            return parse(rs.getString(columnName));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProxyConfig.ProxyInfo getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private ProxyConfig.ProxyInfo parse(String json) {
        return JSONUtil.toBean(json, ProxyConfig.ProxyInfo.class);
    }
}
