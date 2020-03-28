package com.water.melon.ui.me.agent.myuser;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.bean.AddVipBean;
import com.water.melon.net.bean.AgentBean;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.MyAgentBean;

import java.util.List;

public class AgentUserContract {

    interface Present extends BasePresenter {

        void getAgentUser(BaseRequest<AgentUserBean> baseRequest);

        void addView(AddVipBean addVipBean);
    }

    interface View extends BaseView<Present> {
        void setAgentUser(AgentUserBean agentUserBean,boolean err);

        void setVips(List<MyAgentBean.Vips> vips);
    }
}
