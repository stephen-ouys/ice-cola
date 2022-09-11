package cn.stephen12.icecola.component.dictionary.jpa;

import cn.stephen12.icecola.component.dictionary.model.Dict;
import cn.stephen12.icecola.component.dictionary.model.Dictionary;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字典Hibernate 类型定义
 *
 * @author ouyangsheng
 * @date 2022-03-08
 **/
public class DictUserType implements UserType, DynamicParameterizedType {
    /**
     * 全类名
     */
    public static final String DICT_TYPE_NAME = "cn.stephen12.icecola.component.dictionary.jpa.DictUserType";
    private static final String SQL_TYPE_PROPERTY_NAME = "SqlType";

    /**
     * 默认Java 类型-Sql 类型的映射
     */
    private static final Map<Type, Integer> DEFAULT_TYPE_TO_SQL_TYPE_MAP = new ConcurrentHashMap<>(16);

    static {
        /**
         * 默认JAVA 类型 到SQL 类型映射关系
         */
        DEFAULT_TYPE_TO_SQL_TYPE_MAP.put(Long.TYPE, Types.BIGINT);
        DEFAULT_TYPE_TO_SQL_TYPE_MAP.put(Integer.TYPE, Types.INTEGER);
        DEFAULT_TYPE_TO_SQL_TYPE_MAP.put(Short.TYPE, Types.SMALLINT);
        DEFAULT_TYPE_TO_SQL_TYPE_MAP.put(Byte.TYPE, Types.TINYINT);
        DEFAULT_TYPE_TO_SQL_TYPE_MAP.put(String.class, Types.VARCHAR);
        DEFAULT_TYPE_TO_SQL_TYPE_MAP.put(Boolean.TYPE, Types.BOOLEAN);
        DEFAULT_TYPE_TO_SQL_TYPE_MAP.put(Character.TYPE, Types.CHAR);
    }

    private Class<? extends cn.stephen12.icecola.component.dictionary.model.Dictionary> enumClass;
    private int sqlType;

    @Override
    public void setParameterValues(Properties parameters) {
        //设置枚举类
        setEnumClass(parameters);

        //设置 SqlType
        setSqlType(parameters);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{this.sqlType};
    }

    @Override
    public Class returnedClass() {
        return this.enumClass;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        String code = rs.getString(names[0]);
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(dict -> dict.codeToStr().equals(code))
                .findFirst().orElse(null);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value != null) {
            Object code = ((Dictionary) value).getCode();
            st.setObject(index, code, this.sqlType);
        } else {
            st.setNull(index, this.sqlType);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    private void setEnumClass(Properties parameters) {
        final ParameterType parameterType = (ParameterType) parameters.get(PARAMETER_TYPE);
        if (parameterType != null) {
            enumClass = parameterType.getReturnedClass().asSubclass(Enum.class);
        }
    }

    private void setSqlType(Properties parameters) {
        Integer sqlType = Optional.ofNullable(getSqlTypeFromParameter(parameters))
                .orElseGet(() -> getSqlTypeFromDefaultMap());

        if (sqlType != null) {
            this.sqlType = sqlType;
        } else {
            this.sqlType = Types.INTEGER;
        }
    }

    private Integer getSqlTypeFromParameter(Properties parameters) {
        final String val = (String) parameters.get(SQL_TYPE_PROPERTY_NAME);
        if (val != null) {
            return Integer.parseInt(val);
        }
        return null;
    }

    private Integer getSqlTypeFromDefaultMap() {
        if (this.enumClass != null) {
            Type codeType = Dict.getCodeType(enumClass);
            return DictUserType.DEFAULT_TYPE_TO_SQL_TYPE_MAP.get(codeType);
        }
        return null;
    }
}
