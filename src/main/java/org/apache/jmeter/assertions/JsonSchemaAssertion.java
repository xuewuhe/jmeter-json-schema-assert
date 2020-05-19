package org.apache.jmeter.assertions;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONObject;
import org.apache.jmeter.JsonSchemaValidator;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;

public class JsonSchemaAssertion extends AbstractTestElement implements Serializable, Assertion {
    private static final Logger log = LoggerFactory.getLogger(JsonSchemaAssertion.class);
    private String JSONPATH = "JSONPATH";
    private String SCHEMA = "SCHEMA";

    public void setJSONPATH(String jsonpath){
        this.setProperty(JSONPATH, jsonpath);
    }

    public String getJSONPATH(){
        return this.getPropertyAsString(JSONPATH);
    }

    public void setSCHEMA(String value){
        this.setProperty(SCHEMA, value);
    }

    public String getSCHEMA(){
        return this.getPropertyAsString(SCHEMA);
    }

    @Override
    public AssertionResult getResult(SampleResult sampleResult) {
        String jsonpath = getJSONPATH();
        String schema = getSCHEMA();
        AssertionResult assertionResult = new AssertionResult(getName());
        String responseJson = sampleResult.getResponseDataAsString();
        if (responseJson.isEmpty()){
            return assertionResult.setResultForNull();
        }
        Object object = JsonPath.read(responseJson, jsonpath);
        String read = objectToString(object);
        Map<String, Object> map = JsonSchemaValidator.validateJsonByFgeByJsonNode(JsonSchemaValidator.getJsonNodeFromString(read), JsonSchemaValidator.getJsonNodeFromString(schema));
        if ((boolean) map.get("success")){
            assertionResult.setFailure(false);
        }else {
            assertionResult.setFailureMessage((String)map.get("message"));
            assertionResult.setFailure(true);
        }
        return assertionResult;
    }

    public static String objectToString(Object subj) {
        String str;
        if (subj == null) {
            str = "null";
        } else if (subj instanceof Map) {
            //noinspection unchecked
            str = new JSONObject((Map<String, ?>) subj).toJSONString();
        } else if (subj instanceof Double || subj instanceof Float) {
            str = decimalFormatter.get().format(subj);
        } else {
            str = subj.toString();
        }
        return str;
    }

    private static ThreadLocal<DecimalFormat> decimalFormatter =
            ThreadLocal.withInitial(JsonSchemaAssertion::createDecimalFormat);

    private static DecimalFormat createDecimalFormat() {
        DecimalFormat decimalFormatter = new DecimalFormat("#.#");
        decimalFormatter.setMaximumFractionDigits(340); // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMinimumFractionDigits(1);
        return decimalFormatter;
    }
}
