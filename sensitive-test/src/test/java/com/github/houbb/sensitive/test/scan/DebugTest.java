package com.github.houbb.sensitive.test.scan;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.scan.factory.SimpleCharsScanFactory;
import com.github.houbb.sensitive.core.support.scan.SensitiveScanBsContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 调试测试类
 */
public class DebugTest {

    @Test
    public void debugTest() {
        System.out.println("=== 开始调试测试 ===");

        try {
            // 测试基本脱敏功能
            String text = "用户手机号：13912345678";
            System.out.println("原始文本: " + text);

            String result = SensitiveScanBsContext.scanAndReplace(text);
            System.out.println("脱敏结果: " + result);

            // 检查是否脱敏
            if (result.contains("13912345678")) {
                System.err.println("错误：手机号未被脱敏！");
            } else if (result.contains("*")) {
                System.out.println("成功：手机号已被脱敏");
            } else {
                System.err.println("异常：结果中既没有原手机号也没有脱敏标记");
            }

            // 获取 CharsScanBs 实例
            Object charsScanBs = SensitiveScanBsContext.getCharsScanBs();
            System.out.println("CharsScanBs 实例: " + charsScanBs);
            System.out.println("CharsScanBs 类名: " + (charsScanBs != null ? charsScanBs.getClass().getName() : "null"));

        } catch (Exception e) {
            System.err.println("发生异常: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 调试测试结束 ===");
    }

    @Test
    public void testCharsScanBsDirectly() {
        System.out.println("\n=== 直接测试 chars-scan 库（不带前缀） ===");

        try {
            // 直接使用 chars-scan 库进行测试
            List<String> scanTypes = Arrays.asList("1", "2", "3", "4", "5", "9");
            CharsScanBs charsScanBs = CharsScanBs.newInstance()
                    .charsScanFactory(new SimpleCharsScanFactory(scanTypes))
                    .init();

            String text = "用户手机号：13912345678";
            System.out.println("原始文本: " + text);

            // 执行替换
            String result = charsScanBs.scanAndReplace(text);
            System.out.println("替换结果: " + result);

            // 检查是否脱敏
            if (result.contains("13912345678")) {
                System.err.println("错误：chars-scan 库未能脱敏手机号！");
            } else if (result.contains("*")) {
                System.out.println("成功：chars-scan 库成功脱敏手机号");
            }

        } catch (Exception e) {
            System.err.println("发生异常: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 直接测试结束 ===\n");
    }

    @Test
    public void testCharsScanBsWithPrefix() {
        System.out.println("\n=== 测试 chars-scan 库（带前缀字符集合） ===");

        try {
            // 测试带前缀字符集合的情况
            List<String> scanTypes = Arrays.asList("1", "2", "3", "4", "5", "9");

            // 构建前缀字符集合
            java.util.Set<Character> prefixCharSet = new java.util.HashSet<>();
            String prefixStr = ":：,，'\"'\"()+()（）";
            for (char c : prefixStr.toCharArray()) {
                prefixCharSet.add(c);
            }

            System.out.println("前缀字符集合: " + prefixCharSet);
            System.out.println("前缀字符集合大小: " + prefixCharSet.size());

            CharsScanBs charsScanBs = CharsScanBs.newInstance()
                    .charsScanFactory(new SimpleCharsScanFactory(scanTypes))
                    .prefixCharSet(prefixCharSet)  // 添加前缀字符集合
                    .init();

            String text = "用户手机号：13912345678";
            System.out.println("原始文本: " + text);

            // 执行替换
            String result = charsScanBs.scanAndReplace(text);
            System.out.println("替换结果: " + result);

            // 检查是否脱敏
            if (result.contains("13912345678")) {
                System.err.println("错误：带前缀字符集合时未能脱敏手机号！");
            } else if (result.contains("*")) {
                System.out.println("成功：带前缀字符集合时成功脱敏手机号");
            }

        } catch (Exception e) {
            System.err.println("发生异常: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 测试结束 ===\n");
    }
}