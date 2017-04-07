package com.sky.mybatis.support;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;


public class LombokAnnotationPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {

        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("lombok.*");

        topLevelClass.addAnnotation("@ToString");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@Builder");
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }
}
