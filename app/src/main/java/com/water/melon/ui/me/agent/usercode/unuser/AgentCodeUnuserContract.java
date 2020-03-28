package com.water.melon.ui.me.agent.usercode.unuser;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentCodeHisBean;

import java.util.List;

public class AgentCodeUnuserContract {
    interface Present extends BasePresenter {

        void getDate(AgentCodeHisBean agentCodeHisBean);

        void getCodeList(AgentCodeHisBean agentCodeHisBean1);
    }

    interface View extends BaseView<Present> {
        void setDate(AgentCodeHisBean Types, boolean err);

        void setType(List<AgentCodeHisBean.Types> type);
    }
}
