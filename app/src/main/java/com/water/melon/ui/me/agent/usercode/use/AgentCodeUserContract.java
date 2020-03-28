package com.water.melon.ui.me.agent.usercode.use;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentCodeHisBean;

import java.util.List;

public class AgentCodeUserContract {
    interface Present extends BasePresenter {

        void getCodeList(AgentCodeHisBean agentCodeHisBean);
        void getCodeList1(AgentCodeHisBean agentCodeHisBean);
    }

    interface View extends BaseView<Present> {
        void setType(List<AgentCodeHisBean.Types> type);
        void setDate(List<AgentCodeHisBean.CodeBean> date,boolean err);
    }
}
