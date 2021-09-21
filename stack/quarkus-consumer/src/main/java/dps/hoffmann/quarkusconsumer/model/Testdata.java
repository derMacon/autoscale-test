package dps.hoffmann.quarkusconsumer.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Testdata extends PanacheEntity {

    public int testid;
    public String teststr;

}
