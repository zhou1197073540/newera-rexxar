package com.mzkj.usermock.bean_vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserAssets {
    private String total_assets;
    private String available_assets;
    private String locking_assets;

    public UserAssets() {
    }

    public UserAssets(String total_assets, String available_assets, String locking_assets) {
        this.total_assets = total_assets;
        this.available_assets = available_assets;
        this.locking_assets = locking_assets;
    }

    @Override
    public String toString() {
        return "UserAssets{" +
                "total_assets='" + total_assets + '\'' +
                ", available_assets='" + available_assets + '\'' +
                ", locking_assets='" + locking_assets + '\'' +
                '}';
    }
}
