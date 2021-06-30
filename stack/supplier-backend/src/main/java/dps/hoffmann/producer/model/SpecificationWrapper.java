package dps.hoffmann.producer.model;

import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
@ToString
public class SpecificationWrapper implements Serializable {

    private String specificationName;
    private String xsdContent;
    private String xPath;

    @Override
    public boolean equals(Object o) {
        return o instanceof SpecificationWrapper
                && this.getSpecificationName().equalsIgnoreCase(
                ((SpecificationWrapper) o).getSpecificationName());
    }

}
