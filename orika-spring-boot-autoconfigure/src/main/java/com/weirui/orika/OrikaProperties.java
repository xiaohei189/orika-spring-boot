package com.weirui.orika;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "orika")
public class OrikaProperties {

    /**
     * 扫描报名 ,多个报名使用,分隔
     */
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
