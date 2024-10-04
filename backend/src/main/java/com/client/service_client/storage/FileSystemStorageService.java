package com.client.service_client.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	public FileSystemStorageService(StorageProperties properties) {
        
        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty."); 
        }

		this.rootLocation = Paths.get(properties.getLocation());
	}

	@SuppressWarnings("null")
	@Override
	public String store(MultipartFile file, String destination) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}
			
			String originalFilename = file.getOriginalFilename();
			String extension = "";
		
			int dotIndex = originalFilename.lastIndexOf('.');
			if (dotIndex > 0) {
				extension = originalFilename.substring(dotIndex);
				originalFilename = originalFilename.substring(0, dotIndex);
			}
		
			String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now());
			String newFilename = originalFilename + "_" + timestamp + extension;
			
			Path destinationDir = this.rootLocation.resolve(Paths.get(destination).getFileName()).normalize();
			Path destinationFile = destinationDir.resolve(newFilename).normalize().toAbsolutePath();
			
			if (!Files.exists(destinationDir)) {
				Files.createDirectories(destinationDir);
			}
		
			if (!destinationFile.getParent().startsWith(this.rootLocation.toAbsolutePath())) {
				throw new StorageException("Cannot store file outside current directory.");
			}
		
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		
			return destinationFile.toString();
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
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
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void deleteFile(String source) {
		try {
			Path pathDelete = Paths.get(source);

			if(Files.exists(pathDelete)) {
				Files.delete(pathDelete);
			}
			else {
				throw new StorageFileNotFoundException("Could not find the file");
			}
		}
		catch (IOException e) {
			throw new StorageException("Could not delete the file", e);
		}
	}

	@Override
	public void init() {
		try {
			Path path = Paths.get(rootLocation.toString());
			
        	Files.createDirectories(path);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}