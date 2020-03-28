package com.water.melon.net.bean;

public class UserBean {
    private String user_id;//用户id
    private String nickname;//用户昵称
    private String mobile;//手机号码
    private String vip;// 会员 0非会员 1体验卡, 2周卡, 3月卡, 4季卡, 5年卡, 6永久卡 7 代理卡
    private String group_id;// 0.游客,1.注册用户,2.代理,3.总代理
    private String end_date;//会员到期日期
    private String imei;

    private String avatar_url;
    private String status;
    private String wx;
    private String qq;
    private String help;
    private String invitation_code;

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    //"user_id": 16001,
//        "avatar_url": "http:\/\/38.27.103.12\/static\/images\/avatar\/man-1.png",
//        "vip": 0,
//        "nickname": "游客用户",
//        "mobile": "",
//        "end_date": "2020-03-14 17:38:26",
//        "group_id": 0,
//        "status": 1,
//        "wx": "预留客服微信",
//        "qq": "预览客服QQ",
//        "help": "http:\/\/38.27.103.12\/help\/"

}
