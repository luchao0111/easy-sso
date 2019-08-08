package cn.chenzw.easy.sso.server.support;

import cn.itm.ffcs.sso.server.utils.SSOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Console;

/**
 * ϵͳ��Կ������
 * @author chenzw
 */
public class SourceKeyGenerator {

    public static void main(String[] args) {
        String source = "";
        if (ArrayUtils.isEmpty(args)) {
            Console console = System.console();
            if (console == null) {
                throw new IllegalStateException("����ʹ�ÿ���̨");
            }
            while (StringUtils.isBlank(source)) {
                source = console.readLine("������ϵͳ(source)��ʶ����");
            }
        }

        System.out.println("ϵͳ:" + source);
        System.out.println("��Կ:" + SSOUtils.getSourcePrivateKey(source));
    }
}
