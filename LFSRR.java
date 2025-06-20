//Made with Love by Mousa Emarah
//https://linkedin.com/in/mousa123


public class LFSRR {
    private String seed;       // Current state of the LFSR
    private int[] tapPositions; // Tap positions for feedback

    // Constructor: Initialize LFSR with seed and tap positions
    public LFSRR(String seed, int[] tapPositions) {
        this.seed = seed;
        this.tapPositions = tapPositions;
    }

    // Generate the next bit in the sequence
    public int step() {
        int feedbackBit = 0;
        for (int tap : tapPositions) {
            feedbackBit ^= Character.getNumericValue(seed.charAt(tap));
        }
        seed = seed.substring(1) + feedbackBit; // Shift and add feedback bit
        return Character.getNumericValue(seed.charAt(seed.length() - 1));
    }

    // Generate a sequence of k bits
    public String generate(int k) {
        StringBuilder sequence = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sequence.append(step());
        }
        return sequence.toString();
    }

    // Helper method to read user input
    private static String readInput(String prompt) {
        System.out.print(prompt);
        try {
            StringBuilder input = new StringBuilder();
            int ch;
            while ((ch = System.in.read()) != '\n') {
                input.append((char) ch);
            }
            return input.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    // Main method
    public static void main(String[] args) {
        // Step 1: Ask the user for the initial seed
        String seed = readInput("Enter the initial seed (binary string, e.g., 1011): ");
        int n = seed.length(); // Number of flip-flops

        // Step 2: Ask the user to specify the tap positions
        System.out.println("Choose tap positions between 0 and " + (n - 1) + ":");
        String tapInput = readInput("Enter the tap positions (comma-separated, e.g., 0,2): ");
        String[] tapStrings = tapInput.split(",");
        int[] tapPositions = new int[tapStrings.length];
        for (int i = 0; i < tapStrings.length; i++) {
            tapPositions[i] = Integer.parseInt(tapStrings[i].trim());
        }

        // Step 3: Create an LFSR object
        LFSRR lfsr = new LFSRR(seed, tapPositions);

        // Step 4: Generate a sequence of at least 100 bits
        String bitSequence = lfsr.generate(100);
        System.out.println("Generated 100-bit sequence: " + bitSequence);

        // Step 5: Demonstrate stream cipher key generation
        String plaintext = readInput("Enter the plaintext to encrypt: ");
        String plaintextBinary = toBinary(plaintext); // Convert plaintext to binary
        System.out.println("Plaintext in Binary: " + plaintextBinary);

        // Generate keystream of the same length as the plaintext binary
        String keystream = lfsr.generate(plaintextBinary.length());
        System.out.println("Keystream: " + keystream);

        // Encrypt the plaintext by XORing with the keystream
        String ciphertextBinary = xor(plaintextBinary, keystream);
        System.out.println("Encrypted text (binary): " + ciphertextBinary);

        // Reset the LFSR to its initial state for decryption
        lfsr = new LFSRR(seed, tapPositions); // Reinitialize the LFSR

        // Decrypt the ciphertext by XORing with the same keystream
        String decryptedBinary = xor(ciphertextBinary, keystream);
        System.out.println("Decrypted text (binary): " + decryptedBinary);

        // Convert decrypted binary back to text
        String decryptedText = binaryToText(decryptedBinary);
        System.out.println("Decrypted text: " + decryptedText);
    }

    // Helper method to convert text to binary
    private static String toBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        return binary.toString();
    }

    // Helper method to convert binary to text
    private static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String byteString = binary.substring(i, Math.min(i + 8, binary.length()));
            int charCode = Integer.parseInt(byteString, 2);
            text.append((char) charCode);
        }
        return text.toString();
    }

    // Helper method to perform XOR on two binary strings
    private static String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }
}