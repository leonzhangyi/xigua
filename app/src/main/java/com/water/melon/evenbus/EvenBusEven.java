package com.water.melon.evenbus;

/**
 * 创建者： awa.pi 在 2018/5/31.
 */

public class EvenBusEven {
    //下载列表通知
    public static class OffLineDownEven extends EvenBusEven {
        //0=暂停任务，1=添加新的下载视频，2=更新所有数据,3==全部暂停
        private int type;

        public OffLineDownEven(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    //下载完成列表
    public static class DownLoadDoneEven extends EvenBusEven {

    }

    //播放历史列表
    public static class PlayRecordEven extends EvenBusEven {

    }
}
