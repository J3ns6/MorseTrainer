import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Translator {

    static final char[] LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '.', ',', '?', '!'};

    static final String[] MORSE = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", "-----", ".----",
            "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", ".-.-.-", "--..--", "..--..", "-.-.--"};

    /**
     * single word to Morse code
     *
     * A single alphabetical word will translated into Morse code.
     *
     * @param word
     * @return
     */
    public static String abcToMorse(String word) {

        StringBuilder result = new StringBuilder();
        int len = word.length();
        char[] lettersArray = word.toCharArray();

        for (int i = 0; i < len; i++) {                     //go through every letter of the word
            for (int j = 0; j < LETTERS.length; j++) {      //goes through each element of the variable letters
                if (lettersArray[i] == LETTERS[j]) {        //which position the letter has in the variable
                    result.append(MORSE[j]);                //copies same position from the morse variable to result
                }
            }
        }
        return result.toString();
    }

    /**
     * sentences to Morse code
     *
     * An alphabetical sentence or sentences will translated into Morse code.
     *
     * @param input
     * @return
     */
    public static String abcToMorse(char[] input){

        StringBuilder result = new StringBuilder();
        int len = input.length;

        for (int i = 0; i < len; i++) {                     //go through every letter of the sentences
            if (input[i] == ' ') {                          //new word in the sentence -> add whitespace to result
                result.append(" ");
            } else {
                for (int j = 0; j < LETTERS.length; j++) {  //goes through each element of the variable letters
                    if (input[i] == LETTERS[j]) {           //which position the letter has in the variable
                        result.append(MORSE[j]);            //copies the same position from the morse variable to result
                        break;
                    }
                    if (j == LETTERS.length - 1) {          //no match with current letter -> Error
                        result.append("?");

                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * single Morse code to word
     *
     * A single Morse code word will translated to alphabetical word.
     *
     * @param input
     * @return
     */
    public String morseToAbc(String input) {

        StringBuilder result = new StringBuilder();
        String[] morseArray = input.split(" ");       //splits the single morsecode letters
        int len = morseArray.length;

        for (int i = 0; i < len; i++) {                     //go through every morsecode letter of the word
            for (int j = 0; j < MORSE.length; j++) {        //goes through each element of the variable morse
                if (morseArray[i].equals(MORSE[j])) {       //which position the morsecode letter has in the variable
                    result.append(LETTERS[j]);              //copies the same position from the letters variable to result
                    break;
                }
                if (j == MORSE.length -1) {                 //no match with current morsecode letter -> Error
                    result.append("?");

                }
            }
        }
        return result.toString();
    }

    /**
     * Morse code  to sentences
     *
     * A Morse code sentence or sentences will will translated to alphabetical sentence or sentences.
     *
     * @param input
     * @return
     */
    public String morseToAbc(String[] input) {

        StringBuilder result = new StringBuilder();
        int len = input.length;

        for (int i = 0; i < len; i++) {                     //go through every morsecode letter of the sentence
            if (input[i].equals(" ")) {                     //new word in the sentence -> add whitespace to result
                result.append(" ");
            } else {
                for (int j = 0; j < MORSE.length; j++) {    //goes through each element of the variable morse
                    if (input[i].equals(MORSE[j])) {        //which position the morsecode letter has in the variable
                        result.append(LETTERS[j]);          //copies the same position from the letters variable to result
                        break;
                    }
                    if (j == MORSE.length -1) {             //no match with current morsecode letter -> Error
                        result.append("?");
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * Expects single word as string to be translated to sound
     * Pass dot or stroke by character to sound function
     *
     * @param input the word you want to convert to morse code sound
     * @throws LineUnavailableException if the line from generateSound (sdl.open(af)) cannot be opened due to resource restrictions
     */
    public static void morseToSound(String input) throws LineUnavailableException {
        final char[] code = input.toCharArray();
        for (char c : code) {
            generateSound(c);
        }
    }

    /**
     * Expects an String array of several words or sentences to be translated from morsecode to sound
     *
     * @param input the word you want to convert to morse code sound
     * @throws LineUnavailableException if the line from generateSound (sdl.open(af)) cannot be opened due to resource restrictions
     */
    public static void morseToSound(String[] input) throws LineUnavailableException {
        for (String c : input) {
            if (c.equals(" ")) {
                try {
                    Thread.sleep(800); // delay to split up chars
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                final char[] morseChar = c.toCharArray();
                for (char morseCharacter : morseChar) {
                    generateSound(morseCharacter);
                }
            }
        }
    }

    /**
     * use the transfer parameter to generate a tone that is either long or short
     *
     * @param c '.' or '-', will be converted to sound
     * @throws LineUnavailableException if the line (sdl.open(af)) cannot be opened due to resource restrictions
     */
    private static void generateSound(char c) throws LineUnavailableException {
        int duration; // ms
        final int vol = 1;
        final float sampleRate = 8000f;
        byte[] buf = new byte[1];

        final AudioFormat af = new AudioFormat(
                sampleRate,         // sampling rate per sec
                8,     // memory size for one sample value
                1,          // mono
                true,        // 8bit with sign (+/-)
                false);
        final SourceDataLine sdl = AudioSystem.getSourceDataLine(af);

        if (c == '-') {
            duration = 400;
        } else {
            duration = 100;
        }

        sdl.open(af);
        sdl.start();

        // generate a sinus function
        for (int i = 0; i < duration * 8; i++) {
            double angle = i / (sampleRate / 440) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol); // current y-value of sinus

            // generate a sound from af that the user can hear
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

}