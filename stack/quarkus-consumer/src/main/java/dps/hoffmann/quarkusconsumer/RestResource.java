package dps.hoffmann.quarkusconsumer;

import dps.hoffmann.quarkusconsumer.model.Testdata;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Random;
import java.util.UUID;

@Path("/test")
public class RestResource {

    @Inject
    EntityManager em;

//    @GET
//    public String test() {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Testdata> cq = cb.createQuery(Testdata.class);
//        Root<Testdata> rootEntry = cq.from(Testdata.class);
//        CriteriaQuery<Testdata> all = cq.select(rootEntry);
//        TypedQuery<Testdata> allQuery = em.createQuery(all);
//
//        String out = allQuery.getResultList().toString();
//        return "test: " + out;
//    }

    @GET
    @Transactional
    public String test() {
        Testdata t = new Testdata();
        t.testid = new Random().nextInt();
        t.teststr = UUID.randomUUID().toString();

        t.persist();

        String out = "not working";
        if (t.isPersistent()) {
            out = "works";
        }

        return "alles neu: " + Testdata.listAll();
    }


}