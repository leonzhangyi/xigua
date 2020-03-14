package com.water.melon.ui.me.agent.create;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.CreateCodeBean;

import java.util.List;

public class AgentCodeContract {

    interface Present extends BasePresenter {
        void getAgentInfo();

        void createCode(CreateCodeBean codeBean);
    }

    interface View extends BaseView<Present> {

        void setAgentInfo(List<CreateCodeBean.CodeBean> codeBean);

        void setUserCode(List<CreateCodeBean.UserCodeBean> codeBeans);
    }
}
