package si.uni_lj.fri.rso.ir_image_retrieval.api;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;
import si.uni_lj.fri.rso.ir_image_retrieval.cdi.ImageDatabase;
import si.uni_lj.fri.rso.ir_image_retrieval.models.Image;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("images")
@Log
public class ImageRetrievalResource {
    @Inject
    private ImageDatabase imageDatabase;

    @Context
    protected UriInfo uriInfo;

    private Logger log = Logger.getLogger(ImageRetrievalResource.class.getName());

    @GET
    @Metered
    public Response getAllImages() {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            List<Image> images = imageDatabase.getImages();
            return Response.ok(images).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }

    @GET
    @Path("/filtered")
    public Response getImagesFiltered() {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            List<Image> pictures = imageDatabase.getImagesFilter(uriInfo);
            return Response.status(Response.Status.OK).entity(pictures).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }

    @GET
    @Metered
    @Path("/{imageId}")
    public Response getImage(@PathParam("imageId") String imageId, @DefaultValue("true") @QueryParam("includeExtended") boolean includeExtended) {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            Image image = imageDatabase.getImage(imageId, includeExtended);
            return image != null
                    ? Response.ok(image).build()
                    : Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }

    @POST
    @Metered
    public Response addNewImage(Image image) {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            imageDatabase.createImage(image);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }

    @DELETE
    @Metered
    @Path("/{imageId}")
    public Response deleteImage(@PathParam("imageId") String imageId) {
        if (ConfigurationUtil.getInstance().getBoolean("rest-config.endpoint-enabled").orElse(false)) {
            imageDatabase.deleteImage(imageId);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("{\"reason\": \"Endpoint disabled.\"}").build();
        }
    }
}
