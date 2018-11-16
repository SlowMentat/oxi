package oxi.services;

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
import java.io.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

import org.springframework.web.multipart.*;
import org.springframework.stereotype.*;

//import javax.media.jai.*;
//import javax.media.jai.codec.SeekableStream;
//import com.sun.media.jai.codec.SeekableStream;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import oxi.models.dto.PictureUpdateDto;

//Service class for managing image data
@Service
public class ImageService{

	private static final Logger logger = LogManager.getLogger(ConsumerService.class);
	private static String largeFolder = "/usr/images/large";
	private static String smallFolder = "/usr/images/small";
	private static String thumbnailFolder = "/usr/images/thumbnail";

	public ImageService(){
	}

	/**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static void resize(File inputFile, OutputStream outputStream, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        ///File inputFile = new File(inputFile);
        BufferedImage inputImage = ImageIO.read(inputFile);
 		logger.debug("inputImage.getType() = " + inputImage.getType());
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH), 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        //String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
 		String formatName = FilenameUtils.getExtension(inputFile.getName());
        // writes to output file
        ImageIO.write(outputImage, formatName, outputStream);
    }
 
    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param percent a double number specifies percentage of the output image
     * over the input image.
     * @throws IOException
     */
   /* public static void resize(String inputImagePath, String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);

        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }*/

	private PictureUpdateDto CreateImageObject(byte[] data) throws IOException{
		File largeImagePath = null;
		File smallImagePath = null;
		File thumbnailImagePath = null;

		//First save original image
		try{
			largeImagePath = File.createTempFile("lrg",".jpg", new File(largeFolder));
			smallImagePath = File.createTempFile("sml",".jpg", new File(smallFolder));
			thumbnailImagePath = File.createTempFile("tnl",".jpg", new File(thumbnailFolder));
		}catch(Exception e){
			throw new IOException("Could not create temp file");
		}
		String largeFilename = FilenameUtils.getBaseName(largeImagePath.getName());
		String smallfilename = FilenameUtils.getBaseName(smallImagePath.getName());
		String thumbnailFilename = FilenameUtils.getBaseName(thumbnailImagePath.getName());

		//Write image bytes to file system
		ByteArrayInputStream bis = new ByteArrayInputStream(data);		
		OutputStream largeBos = new BufferedOutputStream(new FileOutputStream(largeImagePath));
		OutputStream smallBos = new BufferedOutputStream(new FileOutputStream(smallImagePath));
		OutputStream thumbnailBos = new BufferedOutputStream(new FileOutputStream(thumbnailImagePath));
		largeBos.write(data);
		largeBos.close();

		BufferedImage buf = ImageIO.read(largeImagePath);

		logger.debug("buf.getWidth() returns: " + buf.getWidth());
		logger.debug("buf.getHieght() returns: " + buf.getHeight());
		//TODO:  make the scaled dimensions externally configurable maybe.
		//Scaling factor for small images
		int smallScaleWidth = (int)(200L);
		int smallScaleHeight = (int)(300L);
		//Scaling factor for thumbnail images
		int thumbnailScaleWidth = (int)(26L);
		int thumbnailScaleHeight = (int)(38L);

		logger.debug("smallScaleWidth: " + smallScaleWidth);
		logger.debug("smallScaleHeight: " + smallScaleHeight);

		logger.debug("thumbnailScaleWidth: " + thumbnailScaleWidth);
		logger.debug("thumbnailScaleHeight: " + thumbnailScaleHeight);

		resize(largeImagePath, smallBos, smallScaleWidth, smallScaleHeight);
		resize(largeImagePath, thumbnailBos, thumbnailScaleWidth, thumbnailScaleHeight);

		smallBos.close();
		thumbnailBos.close();

		return new PictureUpdateDto(null, thumbnailFilename, smallfilename, largeFilename);		
	}

	/*
	saves jpeg data to filesystem
	@param MulitpartHttpServletRequest data 
	@return String indicating generated filename.
	*/
	public PictureUpdateDto saveImage(MultipartHttpServletRequest data){
		logger.debug("Ivoking ImageService.uploadImage() method!");
		
		//Iterator<String> itr = data.getFileNames();
		//MultipartFile file = data.getFile("imageFile");

		Enumeration<String> parameters = data.getParameterNames();
		String[] multipartPayload = data.getParameterValues(parameters.nextElement());
		//trimm the base64 prefix to get the data
		String file = multipartPayload[0].split(",")[1];

		if(!file.isEmpty()){
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
				byte[] bytes = DatatypeConverter.parseBase64Binary(file);
				/*BufferedOutputStream oStream = new BufferedOutputStream(new FileOutputStream(imgpath));
				oStream.write(bytes);
				oStream.close();
				
				//save small image and thumbnail
				String filename = FilenameUtils.getBaseName(imgpath.getName());

				//update image database
				return FilenameUtils.getBaseName(filename);*/
				return CreateImageObject(bytes);
			}catch(Exception e){
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