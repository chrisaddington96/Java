import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class ImageFrame extends JLabel{
    // instance variable to hold the buffered image
    private BufferedImage bim=null;
    private BufferedImage filteredbim=null;

    //  tell the paintcomponent method what to draw
    private boolean showfiltered=false;

    // here are a few kernels to try
    private final float[] LOWPASS3x3 =
            {0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f};
    private final float[] SHARPEN3x3 =
            {0.f, -1.f, 0.f, -1.f, 5.f, -1.f, 0.f, -1.f, 0.f};
    private final float[] EDGE3x3 =
            {0.f, -1.f, 0.f, -1.f, 4.0f, -1.f, 0.f, -1.f, 0.f};

    // Normalized Kernels
    private final float[] kernel3x3 =
            {0.10205837f, 0.11534902f, 0.10205837f, 0.11534902f, 0.13037045f, 0.11534902f, 0.10205837f, 0.11534902f, 0.10205837f};
    private final float[] kernel5x5 =
            {0.023527987f, 0.033969555f, 0.03839327f, 0.033969555f, 0.023527987f, 0.033969555f, 0.049045023f, 0.055431955f, 0.049045023f,
                    0.033969555f, 0.03839327f, 0.055431955f, 0.06265063f, 0.055431955f, 0.03839327f, 0.033969555f, 0.049045023f, 0.055431955f,
                    0.049045023f, 0.033969555f, 0.023527987f, 0.033969555f, 0.03839327f, 0.033969555f, 0.023527987f};
    private final float[] kernel7x7 =
            {0.0050841793f, 0.009377408f, 0.013539041f, 0.015302175f, 0.013539041f, 0.009377408f, 0.0050841793f, 0.009377408f, 0.017295964f, 0.0249718f,
                    0.028223775f, 0.0249718f, 0.017295964f, 0.009377408f, 0.013539041f, 0.0249718f, 0.036054123f, 0.040749304f, 0.036054123f, 0.0249718f,
                    0.013539041f, 0.015302175f, 0.028223775f, 0.040749304f, 0.04605592f, 0.040749304f, 0.028223775f, 0.015302175f, 0.013539041f, 0.0249718f,
                    0.036054123f, 0.040749304f, 0.036054123f, 0.0249718f, 0.013539041f, 0.009377408f, 0.017295964f, 0.0249718f, 0.028223775f, 0.0249718f,
                    0.017295964f, 0.009377408f, 0.0050841793f, 0.009377408f, 0.013539041f, 0.015302175f, 0.013539041f, 0.009377408f, 0.0050841793f};

    // Kernel size for blur operation, default is 3x3 kernel
    private int kernelSize = 1;

    // Constructor with no arguments
    public ImageFrame() {}

    // Overloaded constructor with buffered image object
    public ImageFrame(BufferedImage img){
        bim = img;
        filteredbim = new BufferedImage
                (bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));

        this.repaint();
    }

    // This mutator changes the image by resetting what is stored
    // The input parameter img is the new image;  it gets stored as an
    //     instance variable
    public void setImage(BufferedImage img) {
        if (img == null) return;
        bim = img;
        filteredbim = new BufferedImage
                (bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(bim.getWidth(), bim.getHeight()));
        showfiltered=false;
        this.repaint();
    }

    // Set operation to set kernel size
    public void setKernelSize(int size){
        kernelSize = size;
    }

    // accessor to get a handle to the bufferedimage object stored here
    public BufferedImage getImage() {
        return bim;
    }

    //  show current image by a scheduled call to paint()
    public void showImage() {
        if (bim == null) return;
        showfiltered=false;
        this.repaint();
    }

    //  apply the blur operator
    public void BlurImage() {
        if (bim == null) return;
        Kernel kernel = null;
        if(kernelSize == 1) {
            kernel = new Kernel(3, 3, kernel3x3);
        }
        else if(kernelSize == 2){
            kernel = new Kernel(5, 5, kernel5x5);
        }
        else if(kernelSize == 3){
            kernel = new Kernel(7, 7, kernel7x7);
        }
        if(kernel != null) {
            ConvolveOp cop = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

            // make a copy of the buffered image
            BufferedImage newbim = new BufferedImage
                    (bim.getWidth(), bim.getHeight(),
                            BufferedImage.TYPE_INT_RGB);
            Graphics2D big = newbim.createGraphics();
            big.drawImage(bim, 0, 0, null);

            // apply the filter the copied image
            // result goes to a filtered copy
            cop.filter(newbim, filteredbim);
            showfiltered = true;
            this.repaint();
        }
    }

    //  get a graphics context and show either filtered image or
    //  regular image
    public void paintComponent(Graphics g) {
        Graphics2D big = (Graphics2D) g;
        if (showfiltered)
            big.drawImage(filteredbim, 0, 0, this);
        else
            big.drawImage(bim, 0, 0, this);
    }

}
