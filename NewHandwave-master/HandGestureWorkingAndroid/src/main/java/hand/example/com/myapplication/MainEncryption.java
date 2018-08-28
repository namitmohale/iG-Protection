package hand.example.com.myapplication;



import java.io.*;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;


public class MainEncryption {

    static String name;

    public boolean DBST(int op, String filePath, String key, String uploadfilepath) {
        boolean flag = false;
        String toBeSaved = "";
        String theText;
        int choice = op;
        
        if (choice == 1) {
            try {
             File f=new File(filePath);
             File f1=new File(uploadfilepath);

           
                 
                        String kind = "Blowfish"; //Blowfish Alorithms
                       

                        Cipher c = Cipher.getInstance(kind);
                        Key k = new SecretKeySpec(key.getBytes(), kind);

                        c.init(Cipher.ENCRYPT_MODE, k);

                        FileInputStream fis =
                                new FileInputStream(f.getCanonicalPath());
                        FileOutputStream fos =
                                new FileOutputStream(f1.getCanonicalPath());
                        CipherOutputStream cos = new CipherOutputStream(fos, c);
                        final int size = (int) f.length();
                        byte[] buffer = new byte[0xFFFF];

                       

                        for (int len; (len = fis.read(buffer)) != -1;) {
                            cos.write(buffer, 0, len);

                          
                        }

                        cos.flush();
                        cos.close();
                        fos.flush();
                        fos.close();
                        fis.close();
                        
                        
                      
                
                
                flag = true;
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        } else if (choice == 2) {
            try {
                 File f=new File(filePath);
             File f1=new File(uploadfilepath);

           
                 
                        String kind = "Blowfish"; //Blowfish Alorithms
                       

                        Cipher c = Cipher.getInstance(kind);
                        Key k = new SecretKeySpec(key.getBytes(), kind);

                        c.init(Cipher.DECRYPT_MODE, k);

                        FileInputStream fis =
                                new FileInputStream(f.getCanonicalPath());
                        FileOutputStream fos =
                                new FileOutputStream(f1.getCanonicalPath());
                        CipherOutputStream cos = new CipherOutputStream(fos, c);
                     
                        byte[] buffer = new byte[0xFFFF];

                       

                        for (int len; (len = fis.read(buffer)) != -1;) {
                            cos.write(buffer, 0, len);

                          
                        }

                        cos.flush();
                        cos.close();
                        fos.flush();
                        fos.close();
                        fis.close();
                        
                        
                      
                
                
                flag = true;
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }

        } else if (choice == 3) {
            flag = false;
        }
        return flag;
    }

 /*   public static byte[] getFile(String name) {

        byte[] readFromFile = null;
        String txt = name;
        System.out.println("file name="+name);
        try {
            FileInputStream in = new FileInputStream(txt);
            readFromFile = new byte[in.available()];
            in.read(readFromFile);
            in.close();
        } catch (IOException e) {
            System.out.println("\nSorry - file not found!\n");
        }
        return readFromFile;
    }
*/
    /*public static String saveFile(byte[] toSave, String uploadfilepath) {

        String txt = uploadfilepath;
        try {
            FileOutputStream out = new FileOutputStream(txt);
            out.write(toSave);
            out.close();

        } catch (IOException e) {
        }
        return txt;
    }

    class Encryption {

        public Encryption(byte[] fileBytes, String key) {
            this.fileBytes = fileBytes;
            this.key = key;

            keys = new char[key.length()];
            pivot = (int) (fileBytes.length / 2);

            delta = 0x9e3779b9;
            alpha = 0x7f2637c6;
            beta = 0x5d656dc8;
            gamma = 0x653654d9;

            sumA = (long) (alpha >> key.charAt(0));
            sumB = (long) (beta << key.charAt(1));
            sumC = (long) (gamma >> key.charAt(2));
            sumD = (long) (delta >> key.charAt(3));

            if (fileBytes.length % 5 > 0) {
                inter = (int) ((fileBytes.length - 1) / 5);
            } else {
                inter = (int) (fileBytes.length / 5);
            }

            forLevel2 = key.length();
        }

        public void setFileBytes(byte[] newBytes) {
            fileBytes = newBytes;
        }

        public byte[] getFileBytes() {
            return fileBytes;
        }

        public void encrypt() {

            int f = 0;
            boolean truth = true;

            key = keyStream();

            keys = new char[key.length()];

            for (int c = 0; c < key.length(); c++) {
                keys[c] = key.charAt(c);
            }


            System.out.println("\nEncrypting\n");

            for (int extra = 0; extra < 127; extra++) {
                for (int i = 0; i < fileBytes.length; i = i + keys.length) {
                    if (truth == false) {
                        break;
                    }
                    f = 0;
                    for (int j = i; j < i + keys.length; j++) {

                        if (j >= fileBytes.length) {
                            truth = false;
                            break;
                        }

                        fileBytes[j] = (byte) ((fileBytes[j]^ (keys[f] - 'A' << sumD)) ^ (keys[f] + sumD));

                        sumD -= delta;
                        f++;
                    }

                }
                fileBytes = splitNSwap(fileBytes);
                setFileBytes(fileBytes);
            }
            setFileBytes(level2(fileBytes, true));
        }

        public void decrypt() {

            setFileBytes(level2(fileBytes, false));
            int f = 0;
            boolean truth = true;

            key = keyStream();

            keys = new char[key.length()];

            for (int c = 0; c < key.length(); c++) {
                keys[c] = key.charAt(c);
            }


            System.out.println("\nDecrypting\n");
            for (int extra = 0; extra < 127; extra++) {
                fileBytes = getFileBytes();

                fileBytes = splitNSwap(fileBytes);

                for (int i = 0; i < fileBytes.length; i = i + keys.length) {

                    if (truth == false) {
                        break;
                    }

                    f = 0;
                    for (int j = i; j < i + keys.length; j++) {

                        if (j >= fileBytes.length) {
                            truth = false;
                            break;
                        }

                        fileBytes[j] = (byte) ((fileBytes[j]
                                ^ (keys[f] - 'A' << sumD)) ^ (keys[f] + sumD));

                        sumD -= delta;
                        f++;
                    }
                }
                setFileBytes(fileBytes);
            }
        }

        public byte[] splitNSwap(byte[] zeBytes) {
            if (zeBytes.length % 2 == 0) {
                pivot = (int) (zeBytes.length / 2);
            } else {
                pivot = (int) ((zeBytes.length - 1) / 2);
            }
            fileBytez = new byte[zeBytes.length];
            for (int reverse = 0; reverse < pivot; reverse++) {
                fileBytez[reverse] = (byte) (zeBytes[reverse + pivot] ^ fileBytez[reverse]);
            }
            for (int reverseB = pivot; reverseB < zeBytes.length; reverseB++) {
                fileBytez[reverseB] = (byte) (zeBytes[reverseB - pivot] ^ fileBytez[reverseB]);
            }
            setFileBytes(fileBytez);
            return fileBytez;

        }

        public String scrambleKey(String toBeScrambledFurther) {
            pivot = (int) (toBeScrambledFurther.length() / 2);
            String newKey = "";
            String sub1 = "", sub2 = "";

            for (int a = 0; a < pivot; a++) {
                sub1 += toBeScrambledFurther.charAt(a + pivot);
            }

            for (int b = pivot; b < toBeScrambledFurther.length(); b++) {
                sub2 += toBeScrambledFurther.charAt(b - pivot);
            }

            newKey = sub1 + sub2;
            return newKey;
        }

        public byte[] level2(byte[] oldBytes, boolean state) {
            if (state) {
                System.out.println("Scrambling encrypted data");
            } else {
                System.out.println("\nDescrambling encrypted data");
            }

            int s = forLevel2;

            int stop = oldBytes.length % s;

            byte[] newBytes = new byte[oldBytes.length];
            byte[] tempBytes = new byte[oldBytes.length - stop];
            byte[] resultBytes = new byte[oldBytes.length];
            byte[] remainderBytes = new byte[stop];

            int hello = oldBytes.length - stop;

            for (int old = 0; old < oldBytes.length - stop; old++) {
                tempBytes[old] = oldBytes[old];
            }

            for (int old = 0; old < stop; old++) {
                remainderBytes[old] = oldBytes[(oldBytes.length - stop + old)];
            }

            if (state) {
                for (int outer = 0; outer < s; outer++) {
                    for (int c = outer; c < hello + outer; c += s) {
                        if (c + s < oldBytes.length) {
                            newBytes[c] = (byte) (oldBytes[c + s] - sumA);
                            newBytes[c + s] = (byte) (oldBytes[c] + sumB);
                        } else {
                            break;
                        }
                    }
                }
            } else if (!state) {
                for (int outer = s - 1; outer >= 0; outer--) {
                    for (int c = (hello - 1 - outer); c >= 0 - outer; c -= s) {
                        if (c - s >= 0) {
                            newBytes[c - s] = (byte) (oldBytes[c] - sumB);
                            newBytes[c] = (byte) (oldBytes[c - s] + sumA);

                        } else {
                            break;
                        }
                    }
                    if (outer <= 0) {
                        break;
                    } else {
                        continue;
                    }
                }
            }

            for (int rep = 0; rep < newBytes.length; rep++) {
                resultBytes[rep] = newBytes[rep];
            }

            for (int rep = 0; rep < remainderBytes.length; rep++) {
                resultBytes[rep] = remainderBytes[rep];
            }
            setFileBytes(newBytes);
            return newBytes;
        }

        public String keyStream() {

            System.out.println("\nGenerating key stream\n");

            String answer = key;
            String thekey = key;

            for (int i = 0; i < (thekey.length() * 128); i++) {
                answer = answer + getPart(thekey);
                thekey = getPart(thekey);
            }
            return answer;
        }

        public String getPart(String thekey) {
            char[] keyPart = new char[thekey.length()];
            String result = "";

            for (int c = 0; c < thekey.length() - 1; c++) {
                keyPart[c] = (char) (thekey.charAt(c + 1) - 1);
            }

            keyPart[thekey.length() - 1] = thekey.charAt(0);
            for (int put = 0; put < keyPart.length; put++) {
                result = result + keyPart[put];
            }

            return result;
        }
        private String key;
        private char[] keys;
        private byte[] fileBytes;
        private byte[] fileBytez;
        private int pivot;
        private int inter;
        private long alpha;
        private long beta;
        private long gamma;
        private long delta;
        private long sumA;
        private long sumB;
        private long sumC;
        private long sumD;
        private byte[] fileBytesB;
        private int forLevel2;
    }*/
}