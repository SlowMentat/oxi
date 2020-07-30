package oxi.services;

import oxi.models.dto.PictureUpdateDto;
import oxi.models.dto.PictureDto;
import oxi.models.PictureProfile;
import oxi.models.BasePicture;
import oxi.models.Picture;
import oxi.models.PictureDelete;
import oxi.repositories.PictureDeleteRepository;

import java.lang.*;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Arrays;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

import org.springframework.web.multipart.*;
import org.springframework.stereotype.*;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

//import javax.media.jai.*;
//import javax.media.jai.codec.SeekableStream;
//import com.sun.media.jai.codec.SeekableStream;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.dto.PictureUpdateDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//Service class for managing image data
@Service
@ConfigurationProperties(prefix = "image")
public class ImageService{

	private static final Logger logger = LogManager.getLogger(ImageService.class);
	private static final String originalFolder = "/usr/images/original";
	private static final String largeFolder = "/usr/images/large";
	private static final String mediumFolder = "/usr/images/medium";
	private static final String smallFolder = "/usr/images/small";
	private static final String thumbnailFolder = "/usr/images/thumbnail";
	private String aspect_ratio;

	@Autowired
	private Environment env;

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired 
	private PictureDeleteRepository pictureDeleteRep;

	public ImageService(){
	}

	// Made static so jackson can serialize/deserialize class.
	// Non-static class will not have a zero argument constructor due to compiler adding hidden paramenters to exisitng constructor.
	static private class Crop{
		private float width = 0;
		private float height = 0;
		private int x = 0;
		private int y = 0;
		private float aspect = 0;
		private String unit = "%";
		private double rotation = 0;
		@JsonIgnore
		private int savedMaxHeight = 0;
		//private float rotation = 0;

		public Crop(){
		}

		public float getWidth(){return this.width;}
		public float getHeight(){return this.height;}
		public int getX(){return this.x;}
		public int getY(){return this.y;}
		public float getAspect(){return this.aspect;}
		public String getUnit(){return this.unit;}
		public double getRotation(){return this.rotation;}
		public int getSavedMaxHeight(){return this.savedMaxHeight;}
		//public float getRotation(){return this.rotation;}

		public void setWidth(float width){this.width = width;}
		public void setHeight(float height){this.height = height;}
		public void setX(int x){this.x = x;}
		public void setY(int y){this.y = y;}
		public void setAspect(float aspect){this.aspect = aspect;}
		public void setUnit(String unit){this.unit = unit;}
		public void setRotation(double rotation){this.rotation = rotation;}
		public void setSavedMaxHeight(int savedMaxHeight){this.savedMaxHeight = savedMaxHeight;}
		//public void setRotation(float rotation){this.rotation = rotation;}
	}

	public static BufferedImage readFromFile(File file) throws IOException{
		return ImageIO.read(file);
	}

	/**
     * Resizes an image to an absolute width and height (the image may not be proportional)
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static BufferedImage resize(BufferedImage inputImage,/*File inputFile,/* OutputStream outputStream,*/ int scaledWidth, int scaledHeight, Crop crop) throws IOException {
    	int x;
    	int y;
    	int width;
    	int height;
    	BufferedImage outputImage = null;

 		logger.debug("inputImage.getType() = " + inputImage.getType());
 		if(inputImage.getType() != 5){
 			//logger.debug("Unsupported image file type.  Ignoring file");
 			throw new IOException("Unsupported image file type.  Ignoring file");
 		}
 		else{
        	// creates output image
        	outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
 	
        	// scales the input image to the output image
        	Graphics2D g2d = outputImage.createGraphics();
        	g2d.drawImage(inputImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH), 0, 0, scaledWidth, scaledHeight, null);
        	g2d.dispose();
        }
 
 		return outputImage;
    }

	/**
     * Rotates the provided image data.
     * @param inputImage A BufferedImage instance representing the image data to rotate.
     * @param rotation The amount in degrees to rotatate inputImage.
     * @return An instance of BuffereImage representing the rotated inputImage.
     */
    public static BufferedImage rotateImage(BufferedImage inputImage, double rotation) throws IOException{
    	BufferedImage rotatedImage = inputImage;

        if(rotation != 0){
        	final double rad = Math.toRadians(rotation);
        	final double locationX = inputImage.getWidth() / 2;
        	final double locationY = inputImage.getHeight() / 2;  

        	final double scaleY = Math.abs(Math.sin(rad));
        	final double scaleX = Math.abs(Math.cos(rad));
        	final int rotWidth = (int)Math.floor(inputImage.getWidth() * scaleX + inputImage.getHeight() * scaleY);
        	final int rotHeight = (int)Math.floor(inputImage.getHeight() * scaleX + inputImage.getWidth() * scaleY);   

        	//final AffineTransform atx = AffineTransform.getRotateInstance(rad, locationX, locationY);
        	final AffineTransform atx = new AffineTransform();
        	atx.translate(rotWidth / 2, rotHeight / 2);
        	atx.rotate(rad, 0, 0);
        	atx.translate(-locationX, -locationY);

        	final AffineTransformOp atxOp = new AffineTransformOp(atx, AffineTransformOp.TYPE_BILINEAR);
        	rotatedImage = new BufferedImage(rotWidth, rotHeight, inputImage.getType());
        	atxOp.filter(inputImage, rotatedImage);
        }

        return rotatedImage;	
    }

	/**
     * Crops the provided image data.
     * @param inputImage A BufferedImage instance representing the image data to crop.
     * @param crop The Crop instance to use for cropping inputImage
     * @return An instance of BuffereImage representing the cropped inputImage.
     */
    public static BufferedImage cropImage(BufferedImage inputImage, /*File inputFile, OutputStream outputStream,*/ Crop crop) throws IOException{
   		int x;
    	int y;
    	int width;
    	int height;

        // Convert crop percentages to pixel
        if(crop.getUnit().equals("%")){
    		x = (int)crop.getX() * inputImage.getWidth() / 100;
    		y = (int)crop.getY() * inputImage.getHeight() / 100;
    		width = (int)crop.getWidth() * inputImage.getWidth() / 100;
    		height = (int)crop.getHeight() * inputImage.getHeight() / 100;
    	}
    	else{    		
    		x = (int)crop.getX();
    		y = (int)crop.getY();
    		width = (int)crop.getWidth();
    		height = (int)crop.getHeight();
    	}	

    	return inputImage.getSubimage(x, y, width, height);
    }

    private void writeBufferedImageToOutputStream(File file, BufferedImage bufferedImage, OutputStream outputStream) throws IOException{
 		String formatName = FilenameUtils.getExtension(file.getName());
        ImageIO.write(bufferedImage, formatName, outputStream);
    }

	/**
     * If necessary, saveOriginalImage rotates the passed jpg image data and saves to the filesystem.
     * @param data Byte array representation of the image.
     * @param rotation The amount in degrees to rotatate the image before saving to the filesystem.
     * @return An instance of BuffereImage representing the rotated rotated original image.
     */
    private BufferedImage saveOriginalImage(byte[] data, double rotation, String[] oglFilenameRef) throws IOException{
		File originalImagePath = null;

		try{
			originalImagePath = File.createTempFile("ogl", ".jpg", new File(originalFolder));
		}catch(Exception e){
			throw new IOException("Could not create temp file");
		}

		String oglFilename = FilenameUtils.getBaseName(originalImagePath.getName());
		// Allow the generated filename to be accessed outside of this method
		oglFilenameRef[0] = oglFilename;
		OutputStream originalBos = new BufferedOutputStream(new FileOutputStream(originalImagePath));

		//rotate original image
		InputStream oglInputStream = new ByteArrayInputStream(data);
		BufferedImage oglBI = ImageIO.read(oglInputStream);
		BufferedImage oglRotBI = rotateImage(oglBI, rotation);

        // Write rotated ImageBuffer to file via originalBOS.
		writeBufferedImageToOutputStream(originalImagePath, oglRotBI, originalBos);
		// Save BufferedOutputStreams to disk.
		originalBos.close();
		oglBI = null;

		return oglRotBI;
    }

	/**
     * Creates optimized, cropped images of the original.  Current sizes are:
     *   large - cropped original
     *   medium - cropped original at 1080px
     *   small - cropped original at 200px
     *   thumbnail - cropped original at 26px
     * @param oglRotBI The BufferedImage of the rotated original image
     * @param crop The Crop instance to use for cropping oglRotBI
     * @return An initialized instance of PictureUpdateDto.
     */
    private PictureUpdateDto createOptimizedImages(BufferedImage oglRotBI, Crop crop, String oglFilename) throws IOException{

		long mediumImageWidth = 1080L;
		long smallImageWidth = 200L;
		long thumbnailImageWidth = 26L;
		float aspectRatio = 0.75f;

		File largeImagePath = null;
		File mediumImagePath = null;
		File smallImagePath = null;
		File thumbnailImagePath = null;

		ObjectMapper mapper = new ObjectMapper();

		//First save original image
		try{
			largeImagePath = File.createTempFile("lrg", ".jpg", new File(largeFolder));
			mediumImagePath = File.createTempFile("med", ".jpg", new File(mediumFolder));
			smallImagePath = File.createTempFile("sml", ".jpg", new File(smallFolder));
			thumbnailImagePath = File.createTempFile("tnl", ".jpg", new File(thumbnailFolder));
		}
		catch(Exception e){
			throw new IOException("Could not create temp file");
		}

		String largeFilename = FilenameUtils.getBaseName(largeImagePath.getName());
		String mediumFilename = FilenameUtils.getBaseName(mediumImagePath.getName());
		String smallfilename = FilenameUtils.getBaseName(smallImagePath.getName());
		String thumbnailFilename = FilenameUtils.getBaseName(thumbnailImagePath.getName());

		//Write image bytes to file system
		//ByteArrayInputStream bis = new ByteArrayInputStream(data);		
		OutputStream largeBos = new BufferedOutputStream(new FileOutputStream(largeImagePath));
		OutputStream mediumBos = new BufferedOutputStream(new FileOutputStream(mediumImagePath));
		OutputStream smallBos = new BufferedOutputStream(new FileOutputStream(smallImagePath));
		OutputStream thumbnailBos = new BufferedOutputStream(new FileOutputStream(thumbnailImagePath));

		// Crop the rotated original image writing the cropped result to largeBOS.
		writeBufferedImageToOutputStream(largeImagePath, cropImage(oglRotBI, crop), largeBos);
		largeBos.close();

		logger.debug("smallImageWidth = " + smallImageWidth);
		logger.debug("mediumImageWidth = " + mediumImageWidth);
		logger.debug("thumbnailImageWidth = " + thumbnailImageWidth);
		logger.debug("aspectRatio = " + aspectRatio);

		//TODO:  make the scaled dimensions externally configurable maybe.
		//Scaling factor for small images
		int smallScaleWidth = (int)(smallImageWidth);
		int smallScaleHeight = (int)(smallImageWidth / aspectRatio)	;

		//Scaling factor for medium images
		int mediumScaleWidth = (int)(mediumImageWidth);
		int mediumScaleHeight = (int)(mediumImageWidth / aspectRatio);

		//Scaling factor for thumbnail images
		int thumbnailScaleWidth = (int)(thumbnailImageWidth);
		int thumbnailScaleHeight = (int)(thumbnailImageWidth / aspectRatio);

		logger.debug("smallScaleWidth: " + smallScaleWidth);
		logger.debug("smallScaleHeight: " + smallScaleHeight);
		logger.debug("thumbnailScaleWidth: " + thumbnailScaleWidth);
		logger.debug("thumbnailScaleHeight: " + thumbnailScaleHeight);

		BufferedImage largeBI = readFromFile(largeImagePath);

		// Resize cropped original
		writeBufferedImageToOutputStream(largeImagePath, resize(largeBI, smallScaleWidth, smallScaleHeight, crop), smallBos);
		writeBufferedImageToOutputStream(largeImagePath, resize(largeBI, mediumScaleWidth, mediumScaleHeight, crop), mediumBos);
		writeBufferedImageToOutputStream(largeImagePath, resize(largeBI, thumbnailScaleWidth, thumbnailScaleHeight, crop), thumbnailBos);

		// Save all resized images
		smallBos.close();
		mediumBos.close();
		thumbnailBos.close();
		largeBI = null;

		// Serialize to initialize PictureUpdateDto.
		String cropString = mapper.writeValueAsString(crop);
		logger.debug("crop instance serialization result = " + cropString);

		return new PictureUpdateDto(null, thumbnailFilename, smallfilename, mediumFilename, largeFilename, oglFilename, cropString);	
    }


	/**
     * Creates a new picture entity.  This function makes calls to saveOriginalImage and createOptimizedImages
     * @param data Byte array representation of the image.
     * @param cropString The string representation of the crop json object
     * @return An initialized instance of PictureUpdateDto created Picture entity.
     * @throws IOException
     */
	private PictureUpdateDto createPicture(byte[] data, String cropString) throws IOException{
		// Convert crop to Rectangle object.
		ObjectMapper mapper = new ObjectMapper();
		Crop crop = mapper.readValue(cropString, Crop.class);

		// Place holder for the generated original filename
		String[] oglFilenameRef = new String[1];

		// Save new original
		BufferedImage oglRotBI = saveOriginalImage(data, crop.getRotation(), oglFilenameRef);
		// Generate and save optimized images from original
		return createOptimizedImages(oglRotBI, crop, oglFilenameRef[0]);
	}

	/**
     * Updates an existing image object.  Here the original image is not modified, rather new optimized images are generated with the give crop json string.  The old optimized images are scheduled for deleteion.
     * @param picture An instance of a picture object.  This instance must at least have non null crop and originaluri properties.
     * @return An initialized instance of PictureUpdateDto representing the updated Picture entity.
     * @throws IOException
     */
	public PictureUpdateDto updatePictureEntity(Picture picture) throws IOException{
		if(picture == null) return null;
		if(picture.getCrop() == null) return null;
		if(picture.getOriginaluri() == null || picture.getOriginaluri().length() < 4) return null;

		ObjectMapper mapper = new ObjectMapper();
		Crop crop = mapper.readValue(picture.getCrop(), Crop.class);

		// Create BufferedImage of the existing original image
		BufferedImage oglBI = ImageIO.read(new File(originalFolder + "/" + picture.getOriginaluri() + ".jpg"));
		PictureUpdateDto pictureUpdateDto = createOptimizedImages(oglBI, crop, picture.getOriginaluri());

		// Schedule previous picture for deletion if update successful.
		if(pictureUpdateDto != null){
			PictureDelete pd = new PictureDelete(picture);
			logger.debug("PictureDelete created = " + pd.toString());
			pictureDeleteRep.save(pd);
			Picture managedPicture = entityManager.merge(picture);
			logger.debug("ImageService#updatePictureEntity managedPicture = " + managedPicture);

			// Update picture instance with new optimized filenames
			managedPicture.setThumbnailuri(pictureUpdateDto.getThumbnailuri());
			managedPicture.setSmalluri(pictureUpdateDto.getSmalluri());
			managedPicture.setMediumuri(pictureUpdateDto.getMediumuri());
			managedPicture.setLargeuri(pictureUpdateDto.getLargeuri());

			pictureUpdateDto.setId(picture.getIdText());
		}

		return pictureUpdateDto;
	}

	//Saves original image and return Picture enitity ID only
	//client will send a put request to Picture resource using the ID returned and including crop data.
	//server will create cropped image, create optimzed files, and update Picture enitty will new data
	private String provisionPictureProfile(byte[] data) throws IOException{
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		File imagePath = null;
		PictureProfile pictureProfile = null;

		//First save original image
		try{
			imagePath = File.createTempFile("ogl",".jpg", new File(originalFolder));
		}
		catch(Exception e){
			throw new IOException("Could not create temp file");
		}

		String filename = FilenameUtils.getBaseName(imagePath.getName());
		
		try{
			//Write image bytes to file system
			//ByteArrayInputStream bis = new ByteArrayInputStream(data);		
			OutputStream bos = new BufferedOutputStream(new FileOutputStream(imagePath));		
			bos.write(data);
			bos.close();
			//pictureProfile = new PictureProfile(null, null, null, null, null, filename, null);
			//entityManager.persist(pictureProfile);
		}
		catch(Throwable e){
			e.printStackTrace(printWriter);
			logger.error(stringWriter.toString());
			throw new IOException(e);
		}

		logger.debug("returning picturProfile");
		//return pictureProfile.getIdText();
		return filename;
	}	

	public String saveProfileImage(MultipartHttpServletRequest formData){
		logger.debug("Ivoking ImageService.uploadImage() method!");
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		Enumeration<String> parameters = formData.getParameterNames();
		String[] multipartPayload = formData.getParameterValues(parameters.nextElement());
		//trimm the base64 prefix to get the formData
		String file = multipartPayload[0].split(",")[1];

		if(!file.isEmpty()){
			try{
				byte[] bytes = DatatypeConverter.parseBase64Binary(file);
				return provisionPictureProfile(bytes);
			}
			catch(Exception e){
				e.printStackTrace(printWriter);
				logger.error(stringWriter.toString());
				//printWriter.flush();
				return null;
			}
		}
		else{
			return null;
		}
	}

	// Crops the original image
	// public PictureDto updateCroppedImages(PictureProfile pp){
	public PictureDto updateCroppedImages(PictureProfile pp){
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		String originalFilename = pp.getOriginaluri();
		String cropStr = pp.getCrop();
		int widthFactor = 1; 
		int heightFactor = 1;
		int x = 0;
		int y = 0;

		try{
			ObjectMapper mapper = new ObjectMapper();
			Crop crop = mapper.readValue(cropStr, Crop.class);
			logger.debug("originalFolder = " + originalFolder + ", originalFilename = " + originalFilename);
			File file = new File(originalFolder + "/" + originalFilename + ".jpg");
			BufferedImage bufImg = ImageIO.read(file);

			switch(crop.getUnit()){
				case "%":
					widthFactor = bufImg.getWidth() / 100;
					heightFactor = bufImg.getHeight() / 100;
					x = crop.getX() * bufImg.getWidth() / 100;
					y = crop.getY() * bufImg.getHeight() / 100;
					break;
				case "px":
					// divisor initialized to 1
					x = crop.getX();
					y = crop.getY();
					break;
				default:
					break;
			}

			BufferedImage croppedInputImage = bufImg.getSubimage(
				x,
				y,
				(int)(crop.getWidth() * widthFactor),
				(int)(crop.getHeight() * heightFactor)
			);
	
			// croppedInputImage references the existing bufImg data.  Copy crop results to new BufferedImage
			BufferedImage croppedOutputImage = new BufferedImage(croppedInputImage.getWidth(), croppedInputImage.getHeight(), croppedInputImage.getType());
	
        	Graphics2D g2d = croppedOutputImage.createGraphics();
        	g2d.drawImage(croppedInputImage, 0, 0, null);
        	g2d.dispose();
	
        	// Convert BufferedImage to byte array before invoking createPicture
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	ImageIO.write(croppedOutputImage, "jpg", baos);
        	baos.flush();

        	PictureDto updatedPictureDto = (PictureDto)createPicture(baos.toByteArray(), cropStr);
        	updatedPictureDto.setCrop(cropStr);
        	return updatedPictureDto;
		}
		catch(Exception e){
			e.printStackTrace(printWriter);
			logger.error(stringWriter.toString());
			//printWriter.flush();
			return null;
		}
	}

	/**
	* saves jpeg data to filesystem
	* @param MulitpartHttpServletRequest data 
	* @return String indicating generated filename.
	*/
	public PictureUpdateDto saveImage(MultipartHttpServletRequest formData){
		logger.debug("Ivoking ImageService.uploadImage() method!");
		
		//Iterator<String> itr = formData.getFileNames();
		//MultipartFile file = formData.getFile("imageFile");

		Enumeration<String> parameters = formData.getParameterNames();
		String[] multipartPayload = formData.getParameterValues(parameters.nextElement());

		String[] cropData = formData.getParameterValues("crop");
		String[] imageData = formData.getParameterValues("imageFile");

		//trimm the base64 prefix to get the formData
		String imageRawData = imageData[0].split(",")[1];

		if(!imageRawData.isEmpty()){
			/*File imgpath = null;
			try{
				//create temp file under specified directory and with randum number as filenme and specified extension
				imgpath = File.createTempFile("lrg",".jpg", new File(imgfolder));
			}catch(Exception e){
				return e.toString();
			}*/
			//insert new image entry in database
			try{
				//byte[] bytes = file.getBytes();
				byte[] bytes = DatatypeConverter.parseBase64Binary(imageRawData);
				/*BufferedOutputStream oStream = new BufferedOutputStream(new FileOutputStream(imgpath));
				oStream.write(bytes);
				oStream.close();
				
				//save small image and thumbnail
				String filename = FilenameUtils.getBaseName(imgpath.getName());

				//update image database
				return FilenameUtils.getBaseName(filename);*/
				return createPicture(bytes, cropData[0]);
			}
			catch(Exception e){
				logger.error("",e);
				return null;
			}
		}else{
			return null;
		}
	}


	/*public byte[] getImage(String filename){
		//Path file = Paths.get(imgfolder+filename);
		byte[] image = null;
		logger.debug("in ConsumerService.getImage()");
		try{
			iStream = new FileInputStream(imgfolder + filename + ".jpg");
			image = IOUtils.toByteArray(iStream);
			logger.debug("image byte[] length = " + image.length);
			return image;
		}catch(IOException e){
			logger.debug(e);
		}
		return image;
	}*/
}