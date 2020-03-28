package com.water.melon.ui.netresource;

import com.chad.library.adapter.base.entity.SectionEntity;

public class MyNetResourtSection extends SectionEntity<NetResoutVideoInfo> {
    public MyNetResourtSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MyNetResourtSection(NetResoutVideoInfo netResoutVideoInfo) {
        super(netResoutVideoInfo);
    }
}
