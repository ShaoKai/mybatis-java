package com.sky.mybatis.support;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Retrieve JSON from CLOB columns and convert JSON to value object automatically.
 * 
 * <pre>
 * 
 *   <tabletableName="..">
 * 		...
 *  	<columnOverride column="JSON" javaType="{custom_vo_class}" jdbcType="CLOB" typeHandler="{custom_type_handler}"/>
 *   </table>
 * 
 * class MyTypeHandler extends ClobJsonTypeHandler<custom_vo_class> {
 * 		protected Class getVoClass() {
 * 			return {custom_vo_class}.class;
 * 		}
 * 	
 * 		@Override
 * 		protected TypeReference getTypeReference() {
 * 			return new TypeReference<{custom_vo_class}l>() {
 * 			};
 * 		}
 * 	}
 * 
 * </pre>
 */
public abstract class ClobJsonTypeHandler<T> extends BaseTypeHandler<T> {

	private static ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("rawtypes")
    protected abstract Class getVoClass();

	@SuppressWarnings("rawtypes")
    protected abstract TypeReference getTypeReference();

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		try {
			ps.setString(i, mapper.writeValueAsString(parameter));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		@SuppressWarnings("unchecked")
		T vo = (T) BeanUtils.instantiate(getVoClass());
		String json = "";
		Clob clob = rs.getClob(columnName);
		if (clob != null) {
			int size = (int) clob.length();
			json = clob.getSubString(1, size);
		}
		if (StringUtils.isNotBlank(json)) {
			try {
				vo = mapper.readValue(json, getTypeReference());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vo;
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		@SuppressWarnings("unchecked")
		T vo = (T) BeanUtils.instantiate(getVoClass());
		String json = "";
		Clob clob = rs.getClob(columnIndex);
		if (clob != null) {
			int size = (int) clob.length();
			json = clob.getSubString(1, size);
		}
		if (StringUtils.isNotBlank(json)) {
			try {
				vo = mapper.readValue(json, getTypeReference());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vo;
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		@SuppressWarnings("unchecked")
		T vo = (T) BeanUtils.instantiate(getVoClass());
		String json = "";
		Clob clob = cs.getClob(columnIndex);
		if (clob != null) {
			int size = (int) clob.length();
			json = clob.getSubString(1, size);
		}
		if (StringUtils.isNotBlank(json)) {
			try {
				vo = mapper.readValue(json, getTypeReference());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vo;
	}

}
