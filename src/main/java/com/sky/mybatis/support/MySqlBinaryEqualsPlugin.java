package com.sky.mybatis.support;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;

public class MySqlBinaryEqualsPlugin  extends PluginAdapter {

	    public MySqlBinaryEqualsPlugin() {
	        super();
	    }

	    public boolean validate(List<String> warnings) {
	        return true;
	    }

	    @Override
	    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
	            IntrospectedTable introspectedTable) {

	        InnerClass criteria = null;
	        // first, find the Criteria inner class
	        for (InnerClass innerClass : topLevelClass.getInnerClasses()) {
	            if ("GeneratedCriteria".equals(innerClass.getType().getShortName())) { //$NON-NLS-1$
	                criteria = innerClass;
	                break;
	            }
	        }

	        if (criteria == null) {
	            // can't find the inner class for some reason, bail out.
	            return true;
	        }

	        for (IntrospectedColumn introspectedColumn : introspectedTable
	                .getNonBLOBColumns()) {
	            if (!introspectedColumn.isJdbcCharacterColumn()
	                    || !introspectedColumn.isStringColumn()) {
	                continue;
	            }

	            Method method = new Method();
	            method.setVisibility(JavaVisibility.PUBLIC);
	            method.addParameter(new Parameter(introspectedColumn
	                    .getFullyQualifiedJavaType(), "value")); //$NON-NLS-1$

	            StringBuilder sb = new StringBuilder();
	            sb.append(introspectedColumn.getJavaProperty());
	            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
	            sb.insert(0, "and"); //$NON-NLS-1$
	            sb.append("BinaryEquals"); //$NON-NLS-1$
	            method.setName(sb.toString());
	            method.setReturnType(FullyQualifiedJavaType.getCriteriaInstance());

	            sb.setLength(0);
	            sb.append("addCriterion(\"BINARY "); //$NON-NLS-1$
	            sb.append(Ibatis2FormattingUtilities
	                    .getAliasedActualColumnName(introspectedColumn));
	            sb.append(" = \", value, \""); //$NON-NLS-1$
	            sb.append(introspectedColumn.getJavaProperty());
	            sb.append("\");"); //$NON-NLS-1$
	            method.addBodyLine(sb.toString());
	            method.addBodyLine("return (Criteria) this;"); //$NON-NLS-1$

	            criteria.addMethod(method);
	        }

	        return true;
	    }
	}