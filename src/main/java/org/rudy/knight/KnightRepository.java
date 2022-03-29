package org.rudy.knight;

import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KnightRepository implements PanacheMongoRepository<Knight> {

    public Knight findByName(String name)
    {
        return find("name", name).firstResult();
    }
}
