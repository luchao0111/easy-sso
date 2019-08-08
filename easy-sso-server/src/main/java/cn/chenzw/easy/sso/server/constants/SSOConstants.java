package cn.chenzw.easy.sso.server.constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * �̶�ֵ
 * @author chenzw
 */
public final class SSOConstants {


    private SSOConstants() {
    }


    public static final String CONFIG_FILE_NAME = "sso.properties";

    /**
     * ��Դϵͳ-��ʶ��
     */
    public static final String SOURCE_IDENTIFIER;

    /**
     *  һ������Կ-��ʶ��
     */
    public static final String KEY_IDENTIFIER;

    /**
     * �û��ʺ�-��ʶ��
     */
    public static final String USERNAME_IDENTIFIER;

    /**
     * ��תurl
     */
    public static final String REDIRECT_URL_IDENTIFIER;

    /**
     * ��Կ
     */
    public static final String PRIVATE_KEY;

    /**
     * ʱЧ�ԣ�Ĭ��:30���ӣ�
     */
    public static final int LIMIT_MILLI_SECOND;

    /**
     * Ĭ�ϵ���ת��ҳ
     */
    public static final String DEFAULT_REDIRECT_URL;


    static {
        Properties properties = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        if (is != null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // ע��Ĭ��ֵ
        SOURCE_IDENTIFIER = properties.getProperty("sourceIdentifier", "source");
        KEY_IDENTIFIER = properties.getProperty("keyIdentifier", "key");
        USERNAME_IDENTIFIER = properties.getProperty("usernameIdentifier", "username");
        REDIRECT_URL_IDENTIFIER = properties.getProperty("redirectUrlIdentifier", "url");
        PRIVATE_KEY = properties.getProperty("privateKey", "FFCS_SSO");
        LIMIT_MILLI_SECOND = Integer
                .parseInt(properties.getProperty("limitMilliSecond", String.valueOf(30 * 60 * 1000)));
        DEFAULT_REDIRECT_URL = properties.getProperty("defaultRedirectUrl", "/index.jsp");
    }

}
