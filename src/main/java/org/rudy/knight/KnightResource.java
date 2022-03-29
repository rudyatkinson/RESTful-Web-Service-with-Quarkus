package org.rudy.knight;

import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/knights")
public class KnightResource
{
    @Inject
    KnightRepository knightRepository;

    @GET
    public Response get()
    {
        return Response.ok(knightRepository.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response get(
            @PathParam("id") String id)
    {
        Knight knight = knightRepository.findById(new ObjectId(id));
        return knight != null ?
                Response.ok(knight).build() :
                Response.noContent().build();
    }

    @GET
    @PathParam("/search/{name}")
    public Response search(
            @PathParam("name") String name)
    {
        Knight knight = knightRepository.findByName(name);
        return knight != null ?
                Response.ok(knight).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response create(Knight knight) throws URISyntaxException
    {
        knightRepository.persist(knight);
        return Response.created(new URI("/" + knight.id)).build();

        /* OR
        return Response.ok(knight).build();
         */
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") String id, Knight knight)
    {
        knight.id = new ObjectId(id);
        knightRepository.update(knight);
        return Response.ok(knight).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(
            @PathParam("id") String id)
    {
        Knight knight = knightRepository.findById(new ObjectId(id));
        knightRepository.delete(knight);
        return Response.noContent().build();
    }
}
