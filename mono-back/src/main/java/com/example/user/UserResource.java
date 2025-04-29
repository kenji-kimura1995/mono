package com.example.user;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/api/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    @GET
    public List<User> listAll(@QueryParam("id") Long id, @QueryParam("name_like") String nameLike) {
        if (id != null) {
            User user = userRepository.findById(id);
            return user != null ? List.of(user) : List.of();
        }
        if (nameLike != null && !nameLike.isEmpty()) {
            return userRepository.list("name LIKE ?1", "%" + nameLike + "%");
        }
        return userRepository.listAll();
    }

    @POST
    @Transactional
    public Response create(User user) {
        if (user.name == null || user.address == null) {
            throw new BadRequestException("Name and address are required");
        }
        userRepository.persist(user);
        return Response.created(URI.create("/api/users/" + user.id)).entity(user).build();  // 201 Created
    }

    @GET
    @Path("/{id}")
    public User findById(@PathParam("id") Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public User update(@PathParam("id") Long id, User updatedUser) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException();
        }
        user.name = updatedUser.name;
        user.address = updatedUser.address;
        return user;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = userRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException();
        }
        return Response.noContent().build();  // 204 No Content
    }
}
