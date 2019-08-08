package cn.chenzw.easy.sso.server.support;


import cn.itm.ffcs.sso.server.constants.SSOConstants;
import cn.itm.ffcs.sso.server.entify.SSODefinition;
import cn.itm.ffcs.sso.server.exception.SSOException;
import cn.itm.ffcs.sso.server.utils.HttpHolder;
import com.bsnnms.bean.common.AESSecurity;
import com.bsnnms.bean.systemLog.bo.StaffLog;
import com.bsnnms.bean.user.SessionMgr_WorkFlow;
import com.bsnnms.bean.user.StaffInfo;
import com.bsnnms.bean.user.XJUserSecurity;
import com.bsnnms.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author chenzw
 */
public abstract class AbstractSSOTemplate {

    /**
     * ǰ�ô�����
     */
    protected abstract void preHandler(SSODefinition ssoDefinition) throws Exception;

    /**
     * У��key�Ƿ���ȷ
     * @param ssoDefinition
     * @return
     */
    private boolean checkKey(SSODefinition ssoDefinition) {
        String plainKey = null;
        try {
            plainKey = AESSecurity.decode(ssoDefinition.getKey(), ssoDefinition.getSourcePrivateKey());
        } catch (ApplicationException e) {
            throw new SSOException("key����ʧ��!");
        }
        String[] aPlainKey = StringUtils.split(plainKey, "##");
        if (aPlainKey != null && aPlainKey.length == 2) {
            // key���û�������ȷ
            if (!ssoDefinition.getPlainUserName().equals(aPlainKey[0])) {
                throw new SSOException("key��ԿУ��ʧ��!");
            }

            Date keyDate = null;
            try {
                keyDate = DateUtils.parseDate(aPlainKey[1], "yyyyMMddHHmmss");
            } catch (ParseException e) {
                throw new SSOException("key��Կ��Ч!");
            }
            Date today = Calendar.getInstance().getTime();
            if (Math.abs(keyDate.getTime() - today.getTime()) >= SSOConstants.LIMIT_MILLI_SECOND) {
                throw new SSOException("key��Կ�ѹ���! timestamp:[" + keyDate.getTime() + "]");
            }
        } else {
            throw new SSOException("��Ч��key��Կ!");
        }
        return true;
    }

    /**
     * У���Ƿ��ѵ�¼
     * @param ssoDefinition
     * @return
     */
    private boolean checkLoginedIn(SSODefinition ssoDefinition) {
        HttpSession session = ssoDefinition.getRequest().getSession();
        StaffInfo info = (StaffInfo) session.getAttribute("staffInfo");
        if (info != null && info.getLoginName().equals(ssoDefinition.getPlainUserName())) {
            return true;
        }
        return false;
    }

    /**
     * ����У��
     * @return
     */
    protected abstract boolean validate(SSODefinition ssoDefinition);

    /**
     * �û���¼
     */
    protected void login(SSODefinition ssoDefinition) {
        StaffInfo staffInfo = null;
        try {
            HttpServletRequest request = ssoDefinition.getRequest();
            HttpSession session = request.getSession();
            staffInfo = XJUserSecurity.check(ssoDefinition.getPlainUserName(), "");
            staffInfo.setLoginId(session.getId());
            staffInfo.setLoginIp(request.getRemoteAddr());
            session.setAttribute("staffInfo", staffInfo);
            SessionMgr_WorkFlow.logIn(session, staffInfo.getStaffId());

            // ��д��½��־
            StaffLog.addLoginLog(staffInfo, session);
        } catch (com.bsnnms.exception.SystemException e) {
            throw new SSOException("SSO��¼ʧ��! ��ϸ��Ϣ: [" + e.getMessage() + "]");
        } catch (ApplicationException e) {
            throw new SSOException("SSO��¼ʧ��! ��ϸ��Ϣ: [" + e.getMessage() + "]");
        }
    }


    /**
     * ���ô�����
     */
    protected abstract void postHandler(SSODefinition ssoDefinition) throws Exception;


    /**
     * Ĭ�ϵĺ��ô�����
     * @Description ����url����������ת��������ת��Ĭ����ҳ
     */
    private void defaultPostHandler(SSODefinition ssoDefinition) {
        try {
            if (!StringUtils.isBlank(ssoDefinition.getRedirectUrl())) {
                ssoDefinition.getResponse().sendRedirect(URLDecoder.decode(ssoDefinition.getRedirectUrl(), "UTF-8"));
            } else {
                ssoDefinition.getResponse().sendRedirect(SSOConstants.DEFAULT_REDIRECT_URL);
            }
        } catch (IOException e) {
            throw new SSOException("������תʧ��!");
        }
    }

    public void dispach() throws Exception {
        SSODefinition ssoDefinition = new SSODefinition(HttpHolder.REQUEST.get(), HttpHolder.RESPONSE.get());
        this.preHandler(ssoDefinition);
        if (checkKey(ssoDefinition) && validate(ssoDefinition)) {
            if (!checkLoginedIn(ssoDefinition)) {
                this.login(ssoDefinition);
            }
            this.postHandler(ssoDefinition);
            this.defaultPostHandler(ssoDefinition);
        }
    }


}
