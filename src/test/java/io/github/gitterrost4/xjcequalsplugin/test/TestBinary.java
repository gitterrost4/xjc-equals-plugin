package io.github.gitterrost4.xjcequalsplugin.test;

import io.github.gitterrost4.xjcequalsplugin.test.basic.BinaryModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Array-typed fields must be hashed and compared by their content.
 *
 * Objects.hash()/Objects.equals() fall back to the identity hash and reference equality for an array, so
 * two models unmarshalled from the same document used to be unequal and to hash differently.
 */
public class TestBinary {

    private BinaryModel read(String resource) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(BinaryModel.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (BinaryModel) unmarshaller.unmarshal(this.getClass().getResourceAsStream(resource));
    }

    @Test
    public void equalBinaryContent() throws Exception {
        BinaryModel model = read("/binarymodel.xml");
        BinaryModel modelAgain = read("/binarymodel.xml");
        assertEquals("models with equal binary content are not equal", model, modelAgain);
        assertEquals("models with equal binary content hash differently", model.hashCode(), modelAgain.hashCode());
    }

    @Test
    public void differingBinaryContent() throws Exception {
        BinaryModel model = read("/binarymodel.xml");
        BinaryModel other = read("/binarymodel2.xml");
        Assert.assertNotEquals("models with differing binary content are equal", model, other);
        Assert.assertNotEquals("models with differing binary content hash equally", model.hashCode(), other.hashCode());
    }
}
