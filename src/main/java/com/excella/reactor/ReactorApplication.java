package com.excella.reactor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReactorApplication {

	public static void main (String[] args)
    {
        int[] A = { 8, 7, 2, 5, 3, 1 };
        int sum = 10;

		findPair(A, sum);
		
		ParallelStreamHarness.run();
    }


	//////////////////
    /// 5.2: Mapping
	//////////////////

    public void mappingExercise() {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs =
            numbers1.stream()
                    .flatMap(i ->
                            numbers2.stream()
                                    .filter(j -> (i + j) % 3 == 0)
                                    .map(j -> new int[]{i, j})
                            )
                    .collect(Collectors.toList());

        System.out.println(numbers1);
        System.out.println(numbers2);
    }

	// Naive method to find a pair in an array with given sum
    // O(n^2), auxillary space used is O(1)
    public static void findPair(int[] A, int sum)
    {
        // consider each element except last element
        for (int i = 0; i < A.length - 1; i++)
        {
            // start from i'th element till last element
            for (int j = i + 1; j < A.length; j++)
            {
                // if desired sum is found, print it and return
                if (A[i] + A[j] == sum)
                {
                    System.out.println("Pair found at index " + i + " and " + j);
                    return;
                }
            }
        }

        // No pair with given sum exists in the array
        System.out.println("Pair not found");
    }

}
