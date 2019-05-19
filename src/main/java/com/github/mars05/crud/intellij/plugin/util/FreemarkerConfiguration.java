package com.github.mars05.crud.intellij.plugin.util;

import com.intellij.openapi.util.text.StringUtil;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;

public class FreemarkerConfiguration extends Configuration {
    private String basePackagePath;

    public FreemarkerConfiguration() {
        this("");
    }

    public FreemarkerConfiguration(String basePackagePath) {
        super(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        this.basePackagePath = basePackagePath;
        setClassLoaderForTemplateLoading(ClassLoader.getSystemClassLoader(), basePackagePath);
        setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
        setClassicCompatible(true);
        setDefaultEncoding("UTF-8");
        setLocale(new Locale("zh_CN"));
    }

    @Override
    public void setTemplateLoader(TemplateLoader templateLoader) {
        super.setTemplateLoader(new TemplateLoader() {
            @Override
            public Object findTemplateSource(String name) {
                if (!StringUtil.isEmptyOrSpaces(basePackagePath)) {
                    if (!StringUtils.startsWith(basePackagePath, "/")) {
                        basePackagePath = "/" + basePackagePath;
                    }
                }
                if (!StringUtils.startsWith(name, "/")) {
                    name = "/" + name;
                }
                InputStream inputStream = getClass().getResourceAsStream(basePackagePath + name);
                if (inputStream != null) {
                    return inputStream;
                }
                return null;
            }

            @Override
            public long getLastModified(Object templateSource) {
                return 0;
            }

            @Override
            public void closeTemplateSource(Object templateSource) throws IOException {
                ((InputStream) templateSource).close();
            }

            @Override
            public Reader getReader(Object templateSource, String encoding) throws IOException {
                return new InputStreamReader((InputStream) templateSource, encoding);
            }
        });
    }
}
