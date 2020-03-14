package com.water.melon.ui.me.agent.myagent.setagent;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.MyAgentBean;

import java.util.List;

public class SetAgentContract {
    interface Present extends BasePresenter {
        void getAgentData(BaseRequest<AgentUserBean> baseRequest);

        void setAgent(AgentUserBean.UserInfo userInfo);
    }

    interface View extends BaseView<Present> {
        void setAgentDate(List<AgentUserBean.UserInfo> myAgentBeans, boolean isDate);
    }
}
