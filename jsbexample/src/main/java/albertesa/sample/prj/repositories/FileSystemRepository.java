package albertesa.sample.prj.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import albertesa.sample.prj.config.AppConfig;
import albertesa.sample.prj.services.ImageNotFoundException;
import albertesa.sample.prj.services.ImageService;

@Service
public class FileSystemRepository implements ImageService {

	private final Path imagesFolder;

	@Autowired
	public FileSystemRepository(AppConfig appCfg) {
		this.imagesFolder = Paths.get(appCfg.getImagesPath());
	}

	@Override
	public void store(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new FileSystemException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new FileSystemException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.imagesFolder.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new FileSystemException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.imagesFolder, 1)
				.filter(path -> !path.equals(this.imagesFolder))
				.map(this.imagesFolder::relativize);
		}
		catch (IOException e) {
			throw new FileSystemException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return imagesFolder.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new ImageNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new ImageNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(imagesFolder.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(imagesFolder);
		}
		catch (IOException e) {
			throw new FileSystemException("Could not initialize storage", e);
		}
	}
}
