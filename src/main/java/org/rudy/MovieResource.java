package org.rudy;

import io.vertx.core.json.Json;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/movies")
public class MovieResource
{
    public static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies()
    {
        return Response.ok(movies).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countMovies()
    {
        return movies.size();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMovie(Movie newMovie)
    {
        movies.add(newMovie);
        return Response.ok(movies).build();
    }

    @PUT
    @Path("updateTitle/{id}/{title}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMovieTitle(
            @PathParam("id")Long id,
            @PathParam("title") String title)
    {
        movies = movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .map(movie ->
                {
                    movie.setTitle(title);
                    return movie;
                }).collect(Collectors.toList());

        return Response.ok(movies).build();
    }

    @PUT
    @Path("updateId/{title}/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMovieId(
            @PathParam("title") String title,
            @PathParam("id") Long id
    )
    {
        movies = movies.stream()
                .filter(movie -> movie.getTitle().equals(title))
                .map(movie ->
                {
                    movie.setId(id);
                    return movie;
                }).collect(Collectors.toList());

        return Response.ok(movies).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeMovie(
            @PathParam("id") Long id)
    {
        Optional<Movie> movieToDelete = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();

        if (movieToDelete.isPresent())
        {
            movies.remove(movieToDelete.get());
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
