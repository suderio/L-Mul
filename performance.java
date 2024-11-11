import java.nio.ByteBuffer;

public class LMul {
    // Function to approximate multiplication using L-Mul algorithm
    public static float lMul(float a, float b) {
        int aBits = Float.floatToIntBits(a);
        int bBits = Float.floatToIntBits(b);

        int signA = (aBits >> 31) & 1;
        int expA = (aBits >> 23) & 0xFF;
        int mantA = aBits & 0x7FFFFF;

        int signB = (bBits >> 31) & 1;
        int expB = (bBits >> 23) & 0xFF;
        int mantB = bBits & 0x7FFFFF;

        int signResult = signA ^ signB;
        int expResult = expA + expB - 127;  // Subtract the bias of 127 for float32

        int mantResult = (mantA + mantB) >> 1;  // Simple approximation of mantissa addition

        int resultBits = (signResult << 31) | ((expResult & 0xFF) << 23) | (mantResult & 0x7FFFFF);

        return Float.intBitsToFloat(resultBits);
    }

    public static void main(String[] args) {
        float a = 1.75f;
        float b = 2.5f;
        final int iterations = 1000000;  // Number of iterations to test

        long start, end;
        float result = 0.0f;

        // Measure time for L-Mul
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            result = lMul(a, b);
        }
        end = System.nanoTime();
        double lMulTime = (end - start) / 1e9;
        System.out.printf("L-Mul result: %f, Time taken: %f seconds%n", result, lMulTime);

        // Measure time for standard multiplication
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            result = a * b;
        }
        end = System.nanoTime();
        double floatMulTime = (end - start) / 1e9;
        System.out.printf("Standard float multiplication result: %f, Time taken: %f seconds%n", result, floatMulTime);

        // Performance comparison
        System.out.printf("Performance difference: L-Mul is %.2f%% of the speed of standard multiplication%n", (lMulTime / floatMulTime) * 100);
    }
}

