package com.mycompany.mds.model.database.util;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {

    @Produces
    @Dependent
    @PersistenceContext(unitName = "stockPU")
    private EntityManager em;
}
