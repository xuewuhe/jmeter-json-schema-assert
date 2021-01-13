package org.apache.jmeter.assertions.gui;

import org.apache.jmeter.assertions.JsonSchemaAssertion;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class JsonSchemaAssertionGui extends AbstractAssertionGui implements ChangeListener {

    private JLabeledTextField json;
    private JTextArea schema;

    public JsonSchemaAssertionGui(){
        init();
    }

    private void init(){
        this.setLayout(new BorderLayout());
        this.setBorder(this.makeBorder());
        this.add(this.makeTitlePanel(), "North");
        VerticalPanel panel = new VerticalPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        json = new JLabeledTextField("Json path exists:");
        //schema = new JLabeledTextArea("Expected JsonSchema:");
        //schema.setToolTipText("please input json schema");
        //修改textarea的行数，因为原先的JLabeledTextArea限制行数为4，jsonSchema的行数基本都大于4，十分不好
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Expected JsonSchema:");
        schema = new JTextArea();
        schema.setRows(16);
        schema.setLineWrap(true);
        schema.setWrapStyleWord(true);
        jPanel.add(label, BorderLayout.NORTH);
        jPanel.add(new JScrollPane(schema), BorderLayout.CENTER);

        panel.add(json);
        panel.add(jPanel);
        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        json.setText("$.");
        schema.setText("");
    }

    @Override
    public void configure(TestElement element) {//调用次方法显示TestElement保存的gui数据信息
        super.configure(element);
        JsonSchemaAssertion jsonSchemaAssertion = (JsonSchemaAssertion) element;
        json.setText(jsonSchemaAssertion.getJSONPATH());
        schema.setText(jsonSchemaAssertion.getSCHEMA());
    }
    @Override
    public void stateChanged(ChangeEvent e) {

    }

    @Override
    public String getStaticLabel() {
        return "Json Schema assertion";
    }

    @Override
    public String getLabelResource() {
        return null;
    }

    @Override
    public TestElement createTestElement() {//将gui中填写的数据保存到TestElement中
        JsonSchemaAssertion jsonSchemaAssertion = new JsonSchemaAssertion();
        this.modifyTestElement(jsonSchemaAssertion);
        return jsonSchemaAssertion;
    }

    @Override
    public void modifyTestElement(TestElement testElement) {//获取新修改的gui中数据
        super.configureTestElement(testElement);
        if (testElement instanceof JsonSchemaAssertion){
            JsonSchemaAssertion jsonSchemaAssertion = (JsonSchemaAssertion)testElement;
            jsonSchemaAssertion.setJSONPATH(this.json.getText());
            jsonSchemaAssertion.setSCHEMA(this.schema.getText());

        }
    }
}
