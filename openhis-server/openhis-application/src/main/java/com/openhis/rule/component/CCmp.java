package com.openhis.rule.component;

import org.springframework.stereotype.Component;

import com.yomahub.liteflow.core.NodeComponent;

@Component("c")
public class CCmp extends NodeComponent {

    @Override
    public void process() {
        // do your business
        System.out.println("___ccc");
    }
}
