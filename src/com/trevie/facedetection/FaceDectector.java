/**
 * 
 */
package com.trevie.facedetection;

/**
 * @author Trevie
 *
 * References: https://www.openshift.com/blogs/day-12-opencv-face-detection-for-java-developers
 * 			   http://stackoverflow.com/questions/20317611/face-detection-using-a-webcam-interface-in-java-in-windows
 *   Many others
 */

import java.awt.Panel;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDectector {

	/**
	 * @param args
	 */
	private static Boolean detectFacesEnabled = true;
	private static String face_cascade_file = FaceDectector.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1);;
	private static CascadeClassifier face_cascade_classifier; // = new CascadeClassifier(face_cascade_file);
	private static String windowName = "Webcam Face Detection";
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		System.out.println("Loading classifier from " + face_cascade_file);
		face_cascade_classifier = new CascadeClassifier(face_cascade_file);

		VideoCapture capture;
		Mat frame = new Mat();
		
		JFrame mainWindow = new JFrame(windowName);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setSize(640, 480);
		Panel panel = new Panel();
		mainWindow.setContentPane(panel);
		ImageIcon ii = new ImageIcon();
		JLabel winImg = new JLabel(ii);
		//winImg.setToolTipText("You're hovering");
		//winImg.setText("text here");
		panel.add(winImg);
		mainWindow.pack();
		mainWindow.setVisible(true);
		
		System.out.println("About to connect to camera");
		capture = new VideoCapture(0); // can also pass a file name (e.g. .AVI file), or nothing at all
		BufferedImage img;
		
		if (!capture.isOpened())
		{
			System.out.println("Failed to connect to camera");
			//capture.release();
			//capture.open(0);
			/*if (!capture.isOpened())
				System.out.println("Try #2 failed");*/
		} else {
			System.out.println("Connected to a camera");
			int frameCount = 0;
			while (true)
			{
				//capture.retrieve(frame);
				//detectAndDisplay(frame);
				//capture.release();
				
				capture.read(frame);
				if (!frame.empty())
				{
					//System.out.println("Got a frame");
					if (frameCount < 2)	// the first time sizes are set, they don't seem to take
					{
						mainWindow.setSize(frame.width() + 40, frame.height() + 40);
						winImg.setSize(frame.width(), frame.height());
						mainWindow.pack();
						System.out.println("Setting window to " + (frame.width() + 40) + "x" + (frame.height() + 40));
						System.out.println("Setting image panel to " + frame.width() + "x" + frame.height());
						frameCount++;
					}

					// Uncomment one of the following 2 lines
					if (detectFacesEnabled)
						img = detectAndHighlight(frame);	// this for face detection -- includes a call to matToBufferedImage
					else
						img = matToBufferedImage(frame);	// this for raw web cam footage 

					ii.setImage(img);
					panel.repaint();	// not needed if the winImg JPanel is constantly resized
				} else {
					System.out.println("Didn't get a frame!");	// On some web cams the first try fails.  Repeated failure is a problem.
				}
			}
		}
		
		System.out.println("Exiting program");
		return;

		// === Load a static image and detect (a) face(s) in it === 
		/*System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // probably opencv_java248 or something similar
		System.out.println("\nRunning FaceDetector");
		
		String fileClassifier = FaceDectector.class.getResource("haarcascade_frontalface_default.xml").getPath().substring(1);
		System.out.println("Loading classifier from " + fileClassifier);
		CascadeClassifier faceDetector = new CascadeClassifier(fileClassifier);
		if (faceDetector.empty())
			System.out.println("Got nothing from " + fileClassifier);
		//CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_default.xml");
		String fileIn = "trevie844.png";
		String path = FaceDectector.class.getResource(fileIn).getPath().substring(1);
		System.out.println("path to image: " + path);
		Mat image = Highgui.imread(path);
		System.out.println("Loaded " + fileIn + ": " + image.width() + "x" + image.height());
		if (image.empty())
			System.out.println("empty");
			
		//MatOfRect faceDetections = new MatOfRect();
		//faceDetector.detectMultiScale(image,  faceDetections);
		
		//Detect from a greyscale image
		MatOfRect faceDetections = new MatOfRect();
		Mat frame_gray = new Mat();
		Imgproc.cvtColor(image, frame_gray, Imgproc.COLOR_BGRA2GRAY);
		Imgproc.equalizeHist(frame_gray, frame_gray);
		faceDetector.detectMultiScale(frame_gray, faceDetections);
		
		System.out.println("Detections: " + faceDetections.width() + "x" + faceDetections.height());
		System.out.println("Detections: " + faceDetections.cols() + "x" + faceDetections.rows());
		
		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
		
		for (Rect rect : faceDetections.toArray())
		{
			Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255,0));
			System.out.println(String.format("Face at (%s, %s)-(%s,%s).  %sx%s", rect.x, rect.y, rect.x+rect.width, rect.y+rect.height, rect.width, rect.height));
		}
		
		String fileOut = "out.png";
		System.out.println(String.format("Writing %s", fileOut));
		System.out.println(image.width() + "x" + image.height());
		Highgui.imwrite(fileOut, image);
		*/ // === END loading a static image and detecting a face in it ===

	} // end main()
	
	/*
	public static void detectAndDisplay(Mat frame)
	{
		MatOfRect faceDetections = new MatOfRect();
		
		face_cascade_classifier.detectMultiScale(frame, faceDetections);
		System.out.println(String.format("Detected %s face(s)", faceDetections.toArray().length));
		
		Rect[] facesArray = faceDetections.toArray();
		
		for (int i = 0; i < facesArray.length; i++)
		{
			Point center = new Point(facesArray[i].x + facesArray[i].width * 0.5, facesArray[i].y + facesArray[i].height * 0.5);
			Core.ellipse(frame, center, new Size(facesArray[i].width * 0.5, facesArray[i].height * 0.5), 0, 0, 360, new Scalar(255, 0, 255), 4, 8, 0);
			
			
		} // end going through all faces
		//Highgui.imwrite(windowName, frame);
		MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".png", frame, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try
		{
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			System.out.println("Error trying to convert image");
			e.printStackTrace();
		}
		
	} // end detectAndDisplay()
	*/
	
	public static BufferedImage detectAndHighlight(Mat frame)
	{
		MatOfRect faceDetections = new MatOfRect();
		
		face_cascade_classifier.detectMultiScale(frame, faceDetections);
		//System.out.println(String.format("Detected %s face(s)", faceDetections.toArray().length));
		
		Rect[] facesArray = faceDetections.toArray();
		
		for (int i = 0; i < facesArray.length; i++)
		{
			Point center = new Point(facesArray[i].x + facesArray[i].width * 0.5, facesArray[i].y + facesArray[i].height * 0.5);
			Core.ellipse(frame, center, new Size(facesArray[i].width * 0.5, facesArray[i].height * 0.5), 0, 0, 360, new Scalar(255, 0, 255), 4, 8, 0);
			
			
		} // end going through all faces
		
		// Alternate to calling matToBufferedImage: convert to image format in-memory (.png, .jpg, etc.) and load it
		// There's no major performance different, it seems.
		
		/*MatOfByte matOfByte = new MatOfByte();
		Highgui.imencode(".png", frame, matOfByte);
		byte[] byteArray = matOfByte.toArray();
		BufferedImage bufImage = null;
		try
		{
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			System.out.println("Error trying to convert image");
			e.printStackTrace();
		}*/
		
		BufferedImage bufImage = matToBufferedImage(frame);
		
		//return matToBufferedImage(bufImage);
		return bufImage;
	} // end detectAndDisplay()
	
	public static BufferedImage matToBufferedImage(Mat matrix)
	{
		//System.out.println("Converting Mat to BI");
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int)matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);
		switch (matrix.channels())
		{
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			// BGR to RGB
			byte b;
			for (int i = 0; i < data.length; i += 3)
			{
				b = data[i];
				data[i] = data[i+2];
				data[i+2] = b;
			}
			break;
		default:
			return null;
		}
		BufferedImage img2 = new BufferedImage(cols, rows, type);
		img2.getRaster().setDataElements(0, 0, cols, rows, data);
		return img2;
	}

}
