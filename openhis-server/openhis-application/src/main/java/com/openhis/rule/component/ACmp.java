package com.openhis.rule.component;

import org.springframework.stereotype.Component;

import com.yomahub.liteflow.core.NodeComponent;

@Component("a")
public class ACmp extends NodeComponent {

    @Override
    public void process() {
        // do your business
        System.out.println("___aaa");
    }
}
