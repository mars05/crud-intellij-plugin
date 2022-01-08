package com.github.mars05.crud.intellij.plugin.util;

import com.github.mars05.crud.intellij.plugin.exception.BizException;
import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.StringModel;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public static String processTemplate(String templateSourceCode, Map<String, Object> dataModel) {
        Template template = newTemplate(templateSourceCode);
        try {
            StringWriter out = new StringWriter();
            template.process(dataModel, out);
            return out.toString();
        } catch (Exception e) {
            throw new BizException(e.getMessage(), e);
        }
    }

    public static String processSql(String templateSourceCode, Map<String, Object> dataModel) {
        for (Map.Entry<String, Object> entry : dataModel.entrySet()) {
            String value;
            if (entry.getValue() instanceof Iterable) {
                value = StreamSupport.stream(((Iterable<?>) entry.getValue()).spliterator(), false)
                        .map(TemplateUtils::getValueByType).collect(Collectors.joining(","));
            } else {
                value = getValueByType(entry.getValue());
            }
            entry.setValue(value);
        }
        return processTemplate(templateSourceCode, dataModel);
    }

    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }

    public static class NlpSd implements TemplateMethodModelEx {

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            Object dateObj = arguments.get(0);
            if (dateObj instanceof StringModel) {
                dateObj = ((StringModel) dateObj).getWrappedObject();
            } else if (dateObj instanceof SimpleDate) {
                dateObj = ((SimpleDate) dateObj).getAsDate();
            } else {
                dateObj = dateObj.toString();
            }
            LocalDateTime date;
            if (dateObj instanceof String) {
                date = LocalDateTime.parse(dateObj.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else if (dateObj instanceof LocalDateTime) {
                date = (LocalDateTime) dateObj;
            } else if (dateObj instanceof LocalDate) {
                LocalDate tempDate = (LocalDate) dateObj;
                date = tempDate.atStartOfDay();
            } else if (dateObj instanceof Date) {
                Date tempDate = (Date) dateObj;
                date = tempDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else {
                return "";
            }
            String sd = arguments.get(1).toString();
            int num = getNumbers(sd);
            if (StringUtils.contains(sd, "时")) {
                date = date.minusHours(num);
            } else if (StringUtils.contains(sd, "天")) {
                date = date.minusDays(num);
            } else {
                return "";
            }
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("nlpSd", new NlpSd());
        map.put("aaa", "2021-11-11 11:11:11");
        map.put("sd", "7个小时");
        String s = processTemplate("${nlpSd(aaa,sd)}", map);
        System.out.println(s);
    }

    //截取数字
    public static int getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return Integer.parseInt(matcher.group(0));
        }
        return 0;
    }

    private static String getValueByType(Object value) {
        if (value == null) {
            return null;
        }
        String columnValue;
        if (value instanceof Number) {
            columnValue = value.toString();
        } else {
            columnValue = "'" + value.toString() + "'";
        }
        return columnValue;
    }
}
