package com.github.mars05.crud.intellij.plugin.util;

import com.github.mars05.crud.intellij.plugin.exception.BizException;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.StringWriter;

/**
 * TemplateUtils
 *
 * @author xiaoyu
 */
public class TemplateUtils {
    public static Template newTemplate(String templateSourceCode) {
        try {
            return new Template(TemplateUtils.class.getName(), templateSourceCode, getConfiguration());
        } catch (IOException e) {
            throw new BizException(e.getMessage(), e);
        }
    }

    public static String processTemplate(String templateSourceCode, Object dataModel) {
        Template template = newTemplate(templateSourceCode);
        try {
            StringWriter out = new StringWriter();
            template.process(dataModel, out);
            return out.toString();
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        }
    }

    public static String processTemplateOfNoError(String templateSourceCode, Object dataModel) {
        try {
            return processTemplate(templateSourceCode, dataModel);
        } catch (Exception e) {
            return templateSourceCode;
        }
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }
}
