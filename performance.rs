use std::time::Instant;

fn l_mul(a: f32, b: f32) -> f32 {
    let a_bits = a.to_bits();
    let b_bits = b.to_bits();

    let sign_a = (a_bits >> 31) & 1;
    let exp_a = (a_bits >> 23) & 0xFF;
    let mant_a = a_bits & 0x7FFFFF;

    let sign_b = (b_bits >> 31) & 1;
    let exp_b = (b_bits >> 23) & 0xFF;
    let mant_b = b_bits & 0x7FFFFF;

    let sign_result = sign_a ^ sign_b;
    let exp_result = exp_a + exp_b - 127; // Subtract the bias of 127 for float32

    let mant_result = (mant_a + mant_b) >> 1; // Simple approximation of mantissa addition

    let result_bits = (sign_result << 31) | ((exp_result & 0xFF) << 23) | (mant_result & 0x7FFFFF);

    f32::from_bits(result_bits)
}

fn main() {
    let a: f32 = 1.75;
    let b: f32 = 2.5;
    const ITERATIONS: usize = 1_000_000; // Number of iterations to test

    let mut result = 0.0;

    // Measure time for L-Mul
    let start = Instant::now();
    for _ in 0..ITERATIONS {
        result = l_mul(a, b);
    }
    let l_mul_time = start.elapsed().as_secs_f64();
    println!("L-Mul result: {}, Time taken: {} seconds", result, l_mul_time);

    // Measure time for standard multiplication
    let start = Instant::now();
    for _ in 0..ITERATIONS {
        result = a * b;
    }
    let float_mul_time = start.elapsed().as_secs_f64();
    println!("Standard float multiplication result: {}, Time taken: {} seconds", result, float_mul_time);

    // Performance comparison
    println!("Performance difference: L-Mul is {:.2}% of the speed of standard multiplication", (l_mul_time / float_mul_time) * 100.0);
}

