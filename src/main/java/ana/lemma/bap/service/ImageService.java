package ana.lemma.bap.service;

import ana.lemma.bap.dto.PropertyImageResponseDTO;
import ana.lemma.bap.exception.ResourceNotFoundException;
import ana.lemma.bap.exception.UnauthorizedActionException;
import ana.lemma.bap.model.Listing;
import ana.lemma.bap.model.PropertyImage;
import ana.lemma.bap.model.User;
import ana.lemma.bap.repository.ListingRepository;
import ana.lemma.bap.repository.PropertyImageRepository;
import jakarta.annotation.PostConstruct;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
  private final PropertyImageRepository propertyImageRepository;
  private final ListingRepository listingRepository;

  @Value("${application.storage.location}")
  private String storageLocation;

  public ImageService(
      PropertyImageRepository propertyImageRepository, ListingRepository listingRepository) {
    this.propertyImageRepository = propertyImageRepository;
    this.listingRepository = listingRepository;
  }

  @PostConstruct
  public void postConstruct() throws IOException {
    Files.createDirectories(Paths.get(storageLocation));
  }

  @Async("imageExecutor")
  public CompletableFuture<Void> processImageAndSaveAsync(byte[] imageBytes, String fileName)
      throws IOException {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
    BufferedImage image = ImageIO.read(inputStream);

    if (image == null) {
      return CompletableFuture.completedFuture(null);
    }

    // TODO: Refactor to scale on ratios.
    Image resultingImage = image.getScaledInstance(1200, 400, Image.SCALE_DEFAULT);
    BufferedImage output = new BufferedImage(1200, 400, BufferedImage.TYPE_INT_RGB);
    output.getGraphics().drawImage(resultingImage, 0, 0, null);

    File destinationFile = Paths.get(storageLocation, fileName).toFile();
    ImageIO.write(output, "jpg", destinationFile);
    return CompletableFuture.completedFuture(null);
  }

  @Transactional
  public PropertyImageResponseDTO uploadListingImage(
      Long listingId, MultipartFile file, boolean primaryImage) throws IOException {

    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    long maxSize = 5 * 1024 * 1024; // 5 MB

    if (file.getSize() > maxSize) {
      throw new IllegalArgumentException("Image too large");
    }


    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Listing listing =
        listingRepository
            .findByIdWithLock(listingId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Provided " + "listing id does not exist"));

    if (user != null && !Objects.equals(listing.getHost().getId(), user.getId())) {
      throw new UnauthorizedActionException("Cannot upload image. Owner " + "mismatch");
    }

    // TODO: Refactor to UUID v7
    String fileName =
            "listing-" + String.valueOf(listingId) + UUID.randomUUID().toString()+".jpg";
    PropertyImage propertyImage = new PropertyImage();
    propertyImage.setFilename(fileName);
    propertyImage.setListing(listing);
    propertyImage.setPrimaryImage(primaryImage);
    propertyImage.setFileUrl("/api/v1/images/" + fileName);

    propertyImageRepository.save(propertyImage);

    processImageAndSaveAsync(file.getBytes(), fileName);
    return toPropertyImageDTO(propertyImage);
  }

  public PropertyImageResponseDTO toPropertyImageDTO(PropertyImage image) {
    return new PropertyImageResponseDTO(
        image.getId(),
        image.getListing().getId(),
        image.getFilename(),
        image.getFileUrl(),
        image.isPrimaryImage());
  }
}
