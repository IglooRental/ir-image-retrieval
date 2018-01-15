package si.uni_lj.fri.rso.ir_image_retrieval.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;

@Entity(name = "images")
@NamedQueries(value = {
        @NamedQuery(name = "Image.getAll", query = "SELECT p FROM images p")
})
@UuidGenerator(name = "idGenerator")
public class Image {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "image_data", columnDefinition = "VARCHAR")
    private String imageData;

    public Image(String id, String imageId, String imageData) {
        this.id = id;
        this.imageId = imageId;
        this.imageData = imageData;
    }

    public Image() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
