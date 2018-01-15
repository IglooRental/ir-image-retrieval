package si.uni_lj.fri.rso.ir_image_retrieval.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import si.uni_lj.fri.rso.ir_image_retrieval.models.Image;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
public class ImageDatabase {
    private Logger log = LogManager.getLogger(ImageDatabase.class.getName());

    @Inject
    private EntityManager em;

    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ObjectMapper objectMapper = new ObjectMapper();


    public List<Image> getImages() {
        TypedQuery<Image> query = em.createNamedQuery("Image.getAll", Image.class);
        return query.getResultList();
    }

    public List<Image> getImagesFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(em, Image.class, queryParameters);
    }

    public Image getImage(String imageId, boolean includeExtended) {
        Image image = em.find(Image.class, imageId);
        if (image == null) {
            throw new NotFoundException();
        }
        if (includeExtended) {
            // nothing here yet
        }
        return image;
    }

    public Image createImage(Image image) {
        try {
            beginTx();
            em.persist(image);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return image;
    }

    public Image putImage(String imageId, Image image) {
        Image p = em.find(Image.class, imageId);
        if (p == null) {
            return null;
        }
        try {
            beginTx();
            image.setId(p.getId());
            image = em.merge(image);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        return image;
    }

    public boolean deleteImage(String imageId) {
        Image p = em.find(Image.class, imageId);
        if (p != null) {
            try {
                beginTx();
                em.remove(p);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }
        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
