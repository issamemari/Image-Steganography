import org.junit.Test;

public class ImageTest {
    @Test
    public void getters() {
        Image image = new Image("/home/latiif/Desktop/red.bmp",null);


        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Pixel p =image.get(i,j);
                p.setR((byte) 0xFF);
                p.setG((byte) 0xFF);
                p.setB((byte) 0xFF);
                image.set(i,j,p);
            }
        }

        image.exportImage("~/Desktop/new","jpg");
    }

    @Test
    public void Encode(){

        LSB lsb = new LSB(5,3);

        String msg = "12";
        Image result = lsb.embed(msg.getBytes(),new Image("~/Desktop/red.bmp",null));



        result.exportImage("~/Desktop/coded","bmp");
    }

    @Test
    public void Extract(){

        LSB lsb = new LSB(0,3);

        lsb.extract(new Image("/home/latiif/Desktop/coded.bmp",null));


    }
}
