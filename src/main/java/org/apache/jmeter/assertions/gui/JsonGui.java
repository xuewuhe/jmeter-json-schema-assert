package org.apache.jmeter.assertions.gui;

import org.apache.jmeter.testelement.TestElement;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JsonGui extends AbstractAssertionGui implements ChangeListener {
    @Override
    public String getLabelResource() {
        return null;
    }

    @Override
    public TestElement createTestElement() {
        return null;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {

    }
}
