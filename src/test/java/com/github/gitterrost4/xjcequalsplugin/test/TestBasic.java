package com.github.gitterrost4.xjcequalsplugin.test;

import com.github.gitterrost4.xjcequalsplugin.test.basic.Model;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Paul Kramer
 */
public class TestBasic {

    @Test
    public void testEquals() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(Model.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Model model1 = (Model) unmarshaller.unmarshal(this.getClass().getResourceAsStream("/model.xml"));
        Model model1Again = (Model) unmarshaller.unmarshal(this.getClass().getResourceAsStream("/model.xml"));
        //same models should be equal
        assertEquals("same models are not equal", model1, model1Again);
        Model model2 = (Model) unmarshaller.unmarshal(this.getClass().getResourceAsStream("/model2.xml"));
        Assert.assertNotEquals("different models should not be equal", model1, model2);
    }
}
