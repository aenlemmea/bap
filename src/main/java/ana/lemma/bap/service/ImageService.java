package ana.lemma.bap.service;

import ana.lemma.bap.dto.PropertyImageResponseDTO;
import ana.lemma.bap.exception.ResourceNotFoundException;
import ana.lemma.bap.exception.UnauthorizedActionException;
import ana.lemma.bap.model.Listing;
import ana.lemma.bap.model.PropertyImage;
import ana.lemma.bap.model.User;
import ana.lemma.bap.repository.ListingRepository;
import ana.lemma.bap.repository.PropertyImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    private final PropertyImageRepository propertyImageRepository;
    private final ListingRepository listingRepository;
    private final Cloudinary cloudinary;

    public ImageService(PropertyImageRepository propertyImageRepository, ListingRepository listingRepository, Cloudinary cloudinary) {
        this.propertyImageRepository = propertyImageRepository;
        this.listingRepository = listingRepository;
        this.cloudinary = cloudinary;
    }

//  @Async("imageExecutor")
//  public CompletableFuture<Void> processImageAndSaveAsync(byte[] imageBytes, String fileName)
//      throws IOException {
//    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
//    BufferedImage image = ImageIO.read(inputStream);
//
//    if (image == null) {
//      return CompletableFuture.completedFuture(null);
//    }
//
//    // TODO: Refactor to scale on ratios.
//    Image resultingImage = image.getScaledInstance(1200, 400, Image.SCALE_DEFAULT);
//    BufferedImage output = new BufferedImage(1200, 400, BufferedImage.TYPE_INT_RGB);
//    output.getGraphics().drawImage(resultingImage, 0, 0, null);
//
//    File destinationFile = Paths.get(storageLocation, fileName).toFile();
//    ImageIO.write(output, "jpg", destinationFile);
//    return CompletableFuture.completedFuture(null);
//  }

    @Transactional
    public PropertyImageResponseDTO uploadListingImage(Long listingId, MultipartFile file, boolean primaryImage) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        long maxSize = 5 * 1024 * 1024; // 5 MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("Image too large");
        }

        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Listing listing = listingRepository.findByIdWithLock(listingId)
                .orElseThrow(() -> new ResourceNotFoundException("Provided listing id does not exist"));

        if (user != null && !Objects.equals(listing.getHost()
                .getId(), user.getId())) {
            throw new UnauthorizedActionException("Cannot upload image. Owner mismatch");
        }

        String publicId = "listings/listing-" + listingId + "-" + UUID.randomUUID();

        // Upload file to Cloudinary with automatic resizing transformations
        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(),
                        ObjectUtils.asMap("public_id", publicId, "folder", "listings", "transformation",
                                new com.cloudinary.Transformation().width(1200)
                                        .height(400)
                                        .crop("fill")
                                        .gravity("center")));

        // Cloudinary returns the full HTTPS image URL
        String cloudImageUrl = uploadResult.get("secure_url")
                .toString();

        PropertyImage propertyImage = new PropertyImage();
        propertyImage.setFilename(publicId);
        propertyImage.setListing(listing);
        propertyImage.setPrimaryImage(primaryImage);
        propertyImage.setFileUrl(cloudImageUrl);

        propertyImageRepository.save(propertyImage);

        return toPropertyImageDTO(propertyImage);
    }

    public PropertyImageResponseDTO toPropertyImageDTO(PropertyImage image) {
        return new PropertyImageResponseDTO(image.getId(), image.getListing()
                .getId(), image.getFilename(), image.getFileUrl(), image.isPrimaryImage());
    }
}
