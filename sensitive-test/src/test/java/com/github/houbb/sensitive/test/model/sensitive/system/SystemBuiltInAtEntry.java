package com.github.houbb.sensitive.test.model.sensitive.system;

import com.github.houbb.sensitive.annotation.SensitiveEntry;

/**
 * 系统内置注解-对象
 * @author binbin.hou
 * date 2019/1/15
 * @since 0.0.3
 */
public class SystemBuiltInAtEntry {

    @SensitiveEntry
    private SystemBuiltInAt entry;

    public SystemBuiltInAt getEntry() {
        return entry;
    }

    public void setEntry(SystemBuiltInAt entry) {
        this.entry = entry;
    }

    @Override
    public String toString() {
        return "SystemBuiltInAtEntry{" +
                "entry=" + entry +
                '}';
    }

}
