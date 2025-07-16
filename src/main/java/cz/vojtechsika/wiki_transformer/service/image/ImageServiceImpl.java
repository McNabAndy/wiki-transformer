package cz.vojtechsika.wiki_transformer.service.image;

import cz.vojtechsika.wiki_transformer.dto.WikiConversionContext;
import cz.vojtechsika.wiki_transformer.exception.ImageFetchException;
import cz.vojtechsika.wiki_transformer.service.PathValidationService;
import cz.vojtechsika.wiki_transformer.util.FileNameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

/**
 * Service responsible for extracting image URLs from a wiki page context,
 * creating a download directory named after the output file, and saving
 * each image under its original filename.
 */
@Service
public class ImageServiceImpl implements ImageService{

    /**
     * Service to validate write permissions
     */
    private final PathValidationService pathValidationService;

    /**
     * Client for downloading images
     */
    private final ImageDownloader imageDownloader;

    /**
     * Extractor for finding image URLs
     */
    private final ImageExtractor imageExtractor;


    /**
     * Constructs the ImageServiceImpl with its dependencies.
     *
     * @param theImageDownloader         client for downloading images
     * @param theImageExtractor          extractor for finding image URLs
     * @param thePathValidationService   service to validate write permissions
     */
    @Autowired
    public ImageServiceImpl(ImageDownloader theImageDownloader,
                            ImageExtractor theImageExtractor,
                            PathValidationService thePathValidationService) {
        imageDownloader = theImageDownloader;
        imageExtractor = theImageExtractor;
        pathValidationService = thePathValidationService;

    }


    /**
     * Orchestrates the entire image download process:
     * <ol>
     *   <li>Extracts and filters image URLs from the provided context.</li>
     *   <li>Creates a download directory named after the unique title.</li>
     *   <li>Downloads and saves each image.</li>
     * </ol>
     *
     * @param context  the conversion context containing wiki URL, output path and unique title
     * @throws IOException if any I/O error occurs during directory creation or file writing
     */
    @Override
    public void downloadAllImages(WikiConversionContext context) throws IOException {
        List<String> filteredImageUrls = extractAndFilterImageUrls(context);

        if (!filteredImageUrls.isEmpty()) {
            System.out.println("The wiki page contains: " + filteredImageUrls.size() + " images, starting downloading process...");

            Path downloadDir = initializeImageDownloadDirectory(context);

            saveAllImages(downloadDir, filteredImageUrls);
        } else {
            System.out.println("No pictures found in the wiki page");
        }
    }

    // Private helper methods

    /**
     * Creates the download directory under the output path,
     * named after the unique title, and validates write permissions.
     *
     * @param context  the conversion context containing base path and unique title
     * @return the path to the newly created (or existing) download directory
     * @throws IOException if directory creation or write-check fails
     */
    private Path initializeImageDownloadDirectory(WikiConversionContext context) throws IOException {
        Path downloadDir = context.getFilePath().resolve(context.getUniqueTitle());
        try {
            Files.createDirectories(downloadDir);
            pathValidationService.checkWrite(downloadDir);
            System.out.println("Download directory for images was successfully created: " + downloadDir.toAbsolutePath() + "\n");
        } catch (IOException e) {
            throw new IOException("Could not create directory at: " + downloadDir.toAbsolutePath(), e);
        }
        return downloadDir;
    }

    /**
     * Extracts all image URLs from the wiki page and applies filtering logic
     * (e.g. removing thumbnails).
     *
     * @param context  the conversion context containing the wiki URL
     * @return a list of filtered, absolute image URLs
     * @throws IOException if extraction fails due to a REST client error
     */
    private List<String> extractAndFilterImageUrls(WikiConversionContext context) throws IOException {
        try{
            List<String> imageUrls = imageExtractor.extractImageUrls(context.getWikiUrl());
            return imageExtractor.filteredImageUrls(imageUrls);
        } catch (RestClientException e){
            throw new IOException("Error extracting image urls from " + context.getWikiUrl(), e);
        }
    }

    /**
     * Downloads the raw bytes of an image from its URL, ensuring
     * successful HTTP response and non-null body.
     *
     * @param imageUrl  the absolute URL of the image
     * @return byte array of the image content
     * @throws IOException if download fails or body is empty
     */
    private byte[] downloadImageData(String imageUrl) throws IOException {
        try {
            ResponseEntity<byte[]> response = imageDownloader.getImage(imageUrl);
            if (!response.getStatusCode().is2xxSuccessful()){
                throw new IOException("Image download failed: HTTP " + response.getStatusCode());
            }
            byte[] imageData= response.getBody();

            if (imageData == null){
                throw new IOException("Image download returned empty body for URL: " + imageUrl);
            }
            return imageData;
        } catch (ImageFetchException e){
            throw new IOException("Error fetching image from " + imageUrl, e);
        }
    }


    /**
     * Iterates over a list of image URLs, downloads each one and saves it
     * to the given download directory.
     *
     * @param downloadDir        the directory where images will be saved
     * @param filteredImageUrls  list of image URLs to download
     * @throws IOException if any I/O error occurs for any image
     */
    private void saveAllImages(Path downloadDir, List<String> filteredImageUrls) throws IOException {
        for (String imageUrl : filteredImageUrls) {

            Path targetFilepath = downloadDir.resolve(FileNameUtil.getNameWithExtension(imageUrl));

            byte[] imageData = downloadImageData(imageUrl);

            saveImage(targetFilepath, imageData);
        }
    }


    /**
     * Saves a single image byte array to disk, logging success,
     * and wrapping any I/O error in a descriptive exception.
     *
     * @param targetFilepath  the full path (including filename) to write to
     * @param imageData       raw image bytes
     * @throws IOException if writing to disk fails
     */
    private void saveImage(Path targetFilepath, byte[] imageData) throws IOException {
        try {
            Files.write( targetFilepath, imageData);
            System.out.println("Image: " + targetFilepath.getFileName() + " saved");
        } catch ( IOException e ) {
            throw new IOException("Error saving image: " + targetFilepath.getFileName() + " to " + targetFilepath, e);
        }
    }



}
