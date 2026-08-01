package com.github.houbb.sensitive.core.support.deepcopy;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 对象图循环引用检测测试。
 */
public class ObjectGraphCycleDetectorTest {

    @Test
    public void cyclicGraphTest() {
        Node root = new Node("root");
        Node child = new Node("child");
        root.setNext(child);
        child.setNext(root);

        Assert.assertTrue(ObjectGraphCycleDetector.hasCycle(root));
    }

    @Test
    public void sharedReferenceIsNotCycleTest() {
        Node shared = new Node("shared");
        SharedRoot root = new SharedRoot();
        root.setLeft(shared);
        root.setRight(shared);

        Assert.assertFalse(ObjectGraphCycleDetector.hasCycle(root));
    }

    @Test
    public void acyclicContainersTest() {
        Node first = new Node("first");
        Node second = new Node("second");
        first.setNext(second);

        Map<String, Object> values = new HashMap<>();
        values.put("nodes", Arrays.asList(first, second));
        values.put("array", new Object[]{"value", 1L});

        Assert.assertFalse(ObjectGraphCycleDetector.hasCycle(values));
    }

    @Test
    public void pureValueBeanTest() {
        PureValueBean bean = new PureValueBean("value", 42L);

        Assert.assertFalse(ObjectGraphCycleDetector.hasCycle(bean));
    }

    public static class Node {
        private String name;
        private Node next;

        public Node(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public static class SharedRoot {
        private Node left;
        private Node right;

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    public static class PureValueBean {
        private String name;
        private long value;

        public PureValueBean(String name, long value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public long getValue() {
            return value;
        }
    }
}
