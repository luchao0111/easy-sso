package cn.chenzw.easy.sso.server.utils;

import cn.chenzw.easy.sso.server.constants.SSOConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.DigestUtils;

/**
 * @author chenzw
 */
public final class SSOUtils {

    private SSOUtils() {


    }

    /**
     * ����ϵͳ��Կ
     * @param source
     * @return
     */
    public static String getSourcePrivateKey(String source) {
        return DigestUtils.md5DigestAsHex((source + SSOConstants.PRIVATE_KEY).getBytes());
    }

    /**
     * ���ɴ�����Ϣ��
     * @param title
     * @param msg
     * @return
     */
    public static String buildHtmlMsg(String title, String msg) {
        return "<div style=\"position: relative; width: 100%; height: 500px; text-align:center;\">"
                + "<div style=\" width: 300px; margin: 100px auto; padding: 10px; border: 1px solid #ccc; border-radius: 5px; overflow: hidden;\">"
                + "<div style=\"padding: 10px 3px; border-bottom: 1px solid #ccc; font-weight: bold;\">" + title
                + "</div>" + "<div style=\"padding: 10px 3px;\">" + "��������ʾ��: " + msg + "</div></div></div>";
    }

    /**
     * ��ȡ��������ĵ�һ��ֵ
     * @param params
     * @return
     */
    public static String getFirstParamter(Object params) {
        if (params == null || ArrayUtils.isEmpty((String[]) params)) {
            return "";
        }
        return ((String[]) params)[0];
    }

}
