package com.water.melon.ui.me.agent.myuser.total;

import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.base.net.BaseRequest;
import com.water.melon.net.bean.AgentUserBean;
import com.water.melon.net.bean.TotalBean;

import java.util.List;

public interface MyUserTotalContract {
    interface Present extends BasePresenter {

        void getTotalUser(BaseRequest<AgentUserBean> baseRequest);
    }

    interface View extends BaseView<Present> {
        void setAgentUser(List<TotalBean> userInfos, boolean err);
    }
}
