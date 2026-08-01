package com.github.houbb.sensitive.test.core.sensitive;

import com.alibaba.fastjson2.filter.BeanContext;
import com.alibaba.fastjson2.JSONException;
import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.SensitiveEntry;
import com.github.houbb.sensitive.core.api.SensitiveUtil;
import com.github.houbb.sensitive.core.api.context.SensitiveContext;
import com.github.houbb.sensitive.core.api.strategory.StrategyChineseName;
import com.github.houbb.sensitive.core.api.strategory.StrategyPhone;
import com.github.houbb.sensitive.core.bs.SensitiveBs;
import com.github.houbb.sensitive.core.support.deepcopy.FastJson2DeepCopy;
import com.github.houbb.sensitive.core.support.filter.DefaultContextValueFilter;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * GitHub issue #14/#21/#24/#25 regression tests.
 */
public class IssueRegressionTest {

    /**
     * Issue #24: Fastjson2 can provide a method-backed context without a Field.
     */
    @Test
    public void issue24MethodBackedContextWithoutField() throws Exception {
        Method getter = MethodBackedBean.class.getMethod("getValue");
        BeanContext beanContext = new BeanContext(MethodBackedBean.class, getter, null,
                "value", null, String.class, String.class, 0L, null);
        DefaultContextValueFilter filter = new DefaultContextValueFilter(SensitiveContext.newInstance());

        String value = "keep-me";
        Assert.assertSame(value, filter.process(beanContext, new MethodBackedBean(), "value", value));
    }

    /**
     * Issue #24: a null object several levels down must not break JSON masking.
     */
    @Test
    public void issue24DeeplyNestedNullValue() {
        NestedRoot root = new NestedRoot();
        NestedChild child = new NestedChild();
        child.setName("张三");
        child.setChild(null);
        root.setChild(child);

        String json = SensitiveUtil.desJson(root);

        Assert.assertEquals("{\"child\":{\"name\":\"张*\"}}", json);
    }

    /**
     * Issue #25: a null JavaBean marked as a sensitive entry must remain null.
     */
    @Test
    public void issue25NullSensitiveEntryObject() {
        NestedRoot root = new NestedRoot();
        root.setChild(null);

        NestedRoot copy = SensitiveUtil.desCopy(root);

        Assert.assertNotSame(root, copy);
        Assert.assertNull(copy.getChild());
    }

    /**
     * Issue #25: null array entries must be retained while non-null entries are masked.
     */
    @Test
    public void issue25NullSensitiveEntryArrayElement() {
        NestedRoot root = new NestedRoot();
        NestedChild child = new NestedChild();
        child.setName("李四");
        root.setChildren(new NestedChild[]{null, child});

        NestedRoot copy = SensitiveUtil.desCopy(root);

        Assert.assertEquals(2, copy.getChildren().length);
        Assert.assertNull(copy.getChildren()[0]);
        Assert.assertEquals("李*", copy.getChildren()[1].getName());
    }

    /**
     * Issue #21: deep copy must preserve an object cycle instead of overflowing the stack.
     */
    @Test
    public void issue21CyclicDeepCopy() {
        CyclicNode root = new CyclicNode();
        root.setName("王五");
        root.setNext(root);

        CyclicNode copy = SensitiveBs.newInstance()
                .deepCopy(FastJson2DeepCopy.getInstance(true))
                .desCopy(root);

        Assert.assertNotSame(root, copy);
        Assert.assertSame(copy, copy.getNext());
        Assert.assertEquals("王*", copy.getName());
        Assert.assertEquals("王五", root.getName());
        Assert.assertSame(root, root.getNext());
    }

    /**
     * 循环检测默认关闭，避免改变默认深拷贝路径的性能和行为。
     */
    @Test(expected = JSONException.class)
    public void issue21CycleDetectionDisabledByDefault() {
        CyclicNode root = new CyclicNode();
        root.setNext(root);

        FastJson2DeepCopy.getInstance().deepCopy(root);
    }

    /**
     * Issue #14: collection masking must keep every entry and the visible phone prefix/suffix.
     */
    @Test
    public void issue14CollectionKeepsAllDataAndType() {
        PhoneSetBean bean = new PhoneSetBean();
        bean.setPrefix("prefix-data");
        bean.setSuffix("suffix-data");
        bean.setPhones(new LinkedHashSet<>(Arrays.asList("13800138000", "13900139000")));

        PhoneSetBean copy = SensitiveUtil.desCopy(bean);

        Assert.assertNotSame(bean, copy);
        Assert.assertEquals("prefix-data", copy.getPrefix());
        Assert.assertEquals("suffix-data", copy.getSuffix());
        Assert.assertEquals(2, copy.getPhones().size());
        Assert.assertTrue(copy.getPhones().contains("1380****000"));
        Assert.assertTrue(copy.getPhones().contains("1390****000"));
        Assert.assertEquals(new LinkedHashSet<>(Arrays.asList("13800138000", "13900139000")), bean.getPhones());
    }

    public static class MethodBackedBean {
        public String getValue() {
            return "keep-me";
        }
    }

    public static class NestedRoot {
        @SensitiveEntry
        private NestedChild child;

        @SensitiveEntry
        private NestedChild[] children;

        public NestedChild getChild() {
            return child;
        }

        public void setChild(NestedChild child) {
            this.child = child;
        }

        public NestedChild[] getChildren() {
            return children;
        }

        public void setChildren(NestedChild[] children) {
            this.children = children;
        }
    }

    public static class NestedChild {
        @Sensitive(strategy = StrategyChineseName.class)
        private String name;

        @SensitiveEntry
        private NestedChild child;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public NestedChild getChild() {
            return child;
        }

        public void setChild(NestedChild child) {
            this.child = child;
        }
    }

    public static class CyclicNode {
        @Sensitive(strategy = StrategyChineseName.class)
        private String name;

        @SensitiveEntry
        private CyclicNode next;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public CyclicNode getNext() {
            return next;
        }

        public void setNext(CyclicNode next) {
            this.next = next;
        }
    }

    public static class PhoneSetBean {
        private String prefix;

        @SensitiveEntry
        @Sensitive(strategy = StrategyPhone.class)
        private Set<String> phones;

        private String suffix;

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public Set<String> getPhones() {
            return phones;
        }

        public void setPhones(Set<String> phones) {
            this.phones = phones;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }
}
