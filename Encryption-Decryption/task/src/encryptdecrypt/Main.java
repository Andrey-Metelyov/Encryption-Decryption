package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

interface Encrypter {
    String enc(String data, Integer key);
}

interface Decrypter {
    String dec(String data, Integer key);
}

class ShiftEncrypter implements Encrypter {

    @Override
    public String enc(String data, Integer key) {
        char[] arr = data.toCharArray();
        key = key % ('z' - 'a' + 1);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= 'a' && arr[i] <= 'z') {
                arr[i] += key;
                if (arr[i] < 'a') {
                    arr[i] = (char) ('z' - ('a' - arr[i]));
                } else if (arr[i] > 'z') {
                    arr[i] = (char) ('a' + arr[i] - 'z' - 1);
                }
            }
            if (arr[i] >= 'A' && arr[i] <= 'Z') {
                arr[i] += key;
                if (arr[i] < 'A') {
                    arr[i] = (char) ('Z' - ('A' - arr[i]));
                } else if (arr[i] > 'Z') {
                    arr[i] = (char) ('A' + arr[i] - 'Z' - 1);
                }
            }
        }
        return String.valueOf(arr);
    }
}

class UnicodeEncrypter implements Encrypter {

    @Override
    public String enc(String data, Integer key) {
        char[] arr = data.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            arr[i] += key;
        }
        return String.valueOf(arr);
    }
}

class ShiftDecrypter implements Decrypter {
    @Override
    public String dec(String data, Integer key) {
        char[] arr = data.toCharArray();
        key = key % ('z' - 'a' + 1);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= 'a' && arr[i] <= 'z') {
                arr[i] -= key;
                if (arr[i] < 'a') {
                    arr[i] = (char) ('z' - ('a' - arr[i]) + 1);
                } else if (arr[i] > 'z') {
                    arr[i] = (char) ('a' + arr[i] - 'z');
                }
            }
            if (arr[i] >= 'A' && arr[i] <= 'Z') {
                arr[i] -= key;
                if (arr[i] < 'A') {
                    arr[i] = (char) ('Z' - ('A' - arr[i]) + 1);
                } else if (arr[i] > 'Z') {
                    arr[i] = (char) ('A' + arr[i] - 'Z');
                }
            }
        }
        return String.valueOf(arr);
    }
}

class UnicodeDecrypter implements Decrypter {
    @Override
    public String dec(String data, Integer key) {
        char[] arr = data.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= key;
        }
        return String.valueOf(arr);
    }
}

class Cypher {
    private Encrypter encrypter;

    public void setEncrypter(Encrypter encrypter) {
        this.encrypter = encrypter;
    }
    String enc(String data, Integer key) {
        return encrypter.enc(data, key);
    }
}

class Decipher {
    private Decrypter decrypter;

    public void setDecrypter(Decrypter decrypter) {
        this.decrypter = decrypter;
    }
    String dec(String data, Integer key) {
        return decrypter.dec(data, key);
    }

}

public class Main {
    public static void main(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            arguments.put(args[i], args[i + 1]);
        }

        String mode = arguments.getOrDefault("-mode", "enc");
        int key = Integer.parseInt(arguments.getOrDefault("-key", "0"));

        String data;
        if (arguments.containsKey("-data")) {
            data = arguments.get("-data");
        } else if (arguments.containsKey("-in")) {
            data = readFile(arguments.get("-in"));
        } else {
            data = "";
        }

        String alg;
        alg = arguments.getOrDefault("-alg", "shift");

        if (mode.equals("enc")) {
            Encrypter enc;
            if (alg.equals("shift")) {
                enc = new ShiftEncrypter();
            } else {
                enc = new UnicodeEncrypter();
            }
            Cypher cypher = new Cypher();
            cypher.setEncrypter(enc);
            data = cypher.enc(data, key);
        } else if (mode.equals("dec")) {
            Decrypter dec;
            if (alg.equals("shift")) {
                dec = new ShiftDecrypter();
            } else {
                dec = new UnicodeDecrypter();
            }
            Decipher decipher = new Decipher();
            decipher.setDecrypter(dec);
            data = decipher.dec(data, key);
        }

        if (arguments.containsKey("-out")) {
            writeFile(arguments.get("-out"), data);
        } else {
            System.out.println(data);
        }

    }

    private static void writeFile(String fileName, String data) {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println("Error writing file " + fileName);
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            System.out.println("Error reading file " + fileName);
            e.printStackTrace();
        }
        return "";
    }

/*    private static String dec(String text, int key) {
        return enc(text, -key);
    }

    private static String enc(String text, int key) {
        char[] arr = text.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            arr[i] += key;
        }
        return String.valueOf(arr);
    }
*/
}
