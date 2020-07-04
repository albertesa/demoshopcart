package albertesa.sample.prj.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import albertesa.sample.prj.services.ImageNotFoundException;
import albertesa.sample.prj.services.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/image")
@Tag(name = "ImageController", description = "Enables image upload")
public class ImageController {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	private final ImageService imageSvc;

	@Autowired
	public ImageController(ImageService imageSvc) {
		this.imageSvc = imageSvc;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<String>> listImages() throws IOException {
		Collection<String> coll = imageSvc.loadAll().map(p-> p.getFileName().toString()).collect(Collectors.toList());
		return new ResponseEntity<Collection<String>>(coll, HttpStatus.OK);
	}

	@RequestMapping(value = "/{imgname}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<Resource> getImage(@PathVariable String imgname) {
		logger.info("Get image: {}", imgname);
		Resource file = imageSvc.loadAsResource(imgname);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<UploadResponse> uploadImage(@RequestParam("img") MultipartFile img) {
		logger.info("Upload image: {}", img.getName());
		imageSvc.store(img);
		return new ResponseEntity<UploadResponse>(new UploadResponse("success"), HttpStatus.OK);
	}

	@ExceptionHandler(ImageNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(ImageNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
