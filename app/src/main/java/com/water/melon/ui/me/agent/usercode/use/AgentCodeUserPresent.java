package com.water.melon.ui.me.agent.usercode.use;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.water.melon.base.mvp.BasePresenter;
import com.water.melon.base.mvp.BasePresenterParent;
import com.water.melon.base.mvp.BaseView;
import com.water.melon.net.ApiImp;
import com.water.melon.net.BaseApiResultData;
import com.water.melon.net.ErrorResponse;
import com.water.melon.net.IApiSubscriberCallBack;
import com.water.melon.net.bean.AgentCodeHisBean;
import com.water.melon.ui.netresource.NetResoutVideoInfo;
import com.water.melon.utils.GsonUtil;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Types;
import java.util.List;

public class AgentCodeUserPresent extends BasePresenterParent implements AgentCodeUserContract.Present {
    public static final String TAG = "AgentCodeUserPresent";
    private AgentCodeUserContract.View mView;

    public AgentCodeUserPresent(BaseView mBaseView, LifecycleProvider lifecycleProvider) {
        super(mBaseView, lifecycleProvider);
        mView = (AgentCodeUserContract.View) mBaseView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.initView();
    }

    @Override
    public void getCodeList(AgentCodeHisBean agentCodeHisBean) {
        ApiImp.getInstance().getCodeList(agentCodeHisBean, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
//                if (!agentCodeHisBean.getHandle().equals("before")) {
//                    mView.setDate(null, true);
//                }

            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, agentCodeHisBean.getHandle());
                LogUtil.e(TAG, "getCodeList.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("")) {
                    if (agentCodeHisBean.getHandle().equals("before")) {
                        List<AgentCodeHisBean.Types> Types = GsonUtil.toClass(result, new TypeToken<List<AgentCodeHisBean.Types>>() {
                        }.getType());
                        mView.setType(Types);
                    }
                }

            }
        });

    }

    @Override
    public void getCodeList1(AgentCodeHisBean agentCodeHisBean) {
        ApiImp.getInstance().getCodeList(agentCodeHisBean, getLifecycleTransformerByStopToFragment(), mView, new IApiSubscriberCallBack<BaseApiResultData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(ErrorResponse error) {
//                mView.showLoadingDialog(false);
                ToastUtil.showToastShort(error.getErr());
                if (!agentCodeHisBean.getHandle().equals("before")) {
                    mView.setDate(null, true);
                }

            }

            @Override
            public void onNext(BaseApiResultData data) {
                LogUtil.e(TAG, agentCodeHisBean.getHandle());
                LogUtil.e(TAG, "getCodeList.getResult() = " + data.getResult());
                String result = data.getResult();
                if (result != null && !result.equals("")) {
                    if (agentCodeHisBean.getHandle().equals("before")) {
                        List<AgentCodeHisBean.Types> Types = GsonUtil.toClass(result, new TypeToken<List<AgentCodeHisBean.Types>>() {
                        }.getType());
                        mView.setType(Types);
                    } else {
                        if (result.contains("list")) {
                            String list = "";
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(result);
                                list = obj.getString("list");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            List<AgentCodeHisBean.CodeBean> Types = GsonUtil.toClass(list, new TypeToken<List<AgentCodeHisBean.CodeBean>>() {
                            }.getType());
                            mView.setDate(Types, false);
                        }else {
                            mView.setDate(null, false);
                        }

                    }
                } else {
                    if (!agentCodeHisBean.getHandle().equals("before")) {
                        mView.setDate(null, false);
                    }
                }

            }
        });

    }
}
