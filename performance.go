package main

import (
	"fmt"
	"math"
	"time"
	"unsafe"
)

// Function to approximate multiplication using L-Mul algorithm
func lMul(a, b float32) float32 {
	aBits := *(*uint32)(unsafe.Pointer(&a))
	bBits := *(*uint32)(unsafe.Pointer(&b))

	signA := (aBits >> 31) & 1
	expA := (aBits >> 23) & 0xFF
	mantA := aBits & 0x7FFFFF

	signB := (bBits >> 31) & 1
	expB := (bBits >> 23) & 0xFF
	mantB := bBits & 0x7FFFFF

	signResult := signA ^ signB
	expResult := expA + expB - 127 // Subtract the bias of 127 for float32

	mantResult := (mantA + mantB) >> 1 // Simple approximation of mantissa addition

	resultBits := (signResult << 31) | ((expResult & 0xFF) << 23) | (mantResult & 0x7FFFFF)

	result := *(*float32)(unsafe.Pointer(&resultBits))
	return result
}

func main() {
	a := float32(1.75)
	b := float32(2.5)
	const iterations = 1000000 // Number of iterations to test

	var result float32

	// Measure time for L-Mul
	start := time.Now()
	for i := 0; i < iterations; i++ {
		result = lMul(a, b)
	}
	lMulTime := time.Since(start).Seconds()
	fmt.Printf("L-Mul result: %f, Time taken: %f seconds\n", result, lMulTime)

	// Measure time for standard multiplication
	start = time.Now()
	for i := 0; i < iterations; i++ {
		result = a * b
	}
	floatMulTime := time.Since(start).Seconds()
	fmt.Printf("Standard float multiplication result: %f, Time taken: %f seconds\n", result, floatMulTime)

	// Performance comparison
	fmt.Printf("Performance difference: L-Mul is %.2f%% of the speed of standard multiplication\n", (lMulTime/floatMulTime)*100)
}

