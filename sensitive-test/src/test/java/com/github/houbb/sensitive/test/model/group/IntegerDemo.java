package com.github.houbb.sensitive.test.model.group;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author binbin.hou
 * date 2019/1/10
 */
public class IntegerDemo {

    Collection<String> stringCollection;

    @Override
    public String toString() {
        return "IntegerDemo{" +
                "stringCollection=" + stringCollection +
                '}';
    }

    public Collection<String> getStringCollection() {
        return stringCollection;
    }

    public void setStringCollection(Collection<String> stringCollection) {
        this.stringCollection = stringCollection;
    }

    public static void main(String[] args) {
        IntegerDemo demo = new IntegerDemo();
        List<String> stringList = Arrays.asList("1", "2", "3");
        demo.setStringCollection(stringList);
        System.out.println(demo);
    }
}
