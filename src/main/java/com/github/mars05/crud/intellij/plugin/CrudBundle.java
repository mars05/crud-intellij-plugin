package com.github.mars05.crud.intellij.plugin;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.util.ResourceBundle;

/**
 * {@link ResourceBundle}/localization utils for the crud plugin.
 */
public class CrudBundle {
	/**
	 * The {@link ResourceBundle} path.
	 */
	@NonNls
	private static final String BUNDLE_NAME = "messages.CrudBundle";
	/**
	 * The {@link ResourceBundle} instance.
	 */
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private CrudBundle() {
	}

	public static String message(@PropertyKey(resourceBundle = BUNDLE_NAME) String key, Object... params) {
		return CommonBundle.message(BUNDLE, key, params);
/*        if (!StringUtil.isEmptyOrSpaces(message)) {
            try {
                message = new String(message.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return message;*/
	}

}
