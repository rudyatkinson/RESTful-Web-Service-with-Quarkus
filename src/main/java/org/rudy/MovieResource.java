package org.rudy;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.mutiny.pgclient.PgPool;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/movies")
public class MovieResource
{
    @Inject
    PgPool db;

    @PostConstruct
    void config()
    {
        InitDb();
    }

    @GET
    public Multi<Movie> get()
    {
        return Movie.findAll(db);
    }

    @GET
    @Path("{id}")
    public Uni<Response> get(
            @PathParam("id") Long id)
    {
        return Movie.findById(db, id)
                .onItem().transform(movie -> movie != null ? Response.ok(movie) : Response.status(Response.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @POST
    public Uni<Response> create(Movie movie)
    {
        return Movie.save(db, movie.getTitle())
                .onItem().transform(id -> URI.create("/movies/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(
            @PathParam("id") Long id)
    {
        return Movie.delete(db, id)
                .onItem().transform(deleted -> deleted ? Response.status(Response.Status.NO_CONTENT) : Response.status(Response.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    private void InitDb()
    {
        db.query("DROP TABLE IF EXISTS movies").execute()
                .flatMap(m -> db.query("CREATE TABLE movies (id SERIAL PRIMARY KEY, title TEXT NOT NULL)").execute())
                .flatMap(m -> db.query("INSERT INTO movies (title) VALUES('The Lord of the Rings')").execute())
                .flatMap(m -> db.query("INSERT INTO movies (title) VALUES('Dune')").execute())
                .await()
                .indefinitely();
    }
}