package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;


public class Main {

    public static void main(String[] args) {
        processArgs(args);
        Random random = new Random();
        for (int threadCount = 2; threadCount <= 64; threadCount = 2*threadCount) {
            for (int arraySize = 1000000; arraySize <= 10000000; arraySize = 2 * arraySize) {
                int[] array = new int[arraySize];
                ArrayList<Long> timeList = new ArrayList<>();
                ParSort.threadPool = new ForkJoinPool(threadCount);

                System.out.println("Degree of parallelism: " + ParSort.threadPool.getParallelism()+", Arraysize: "+arraySize);
                for (int j = 50; j < 100; j++) {
                    ParSort.cutoff = (arraySize/1000) * (j + 1);
                    long time;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);


                    System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");

                }
                try {
                    FileOutputStream fis = new FileOutputStream("./src/result_"+threadCount+"_threads_arraySize_"+arraySize+".csv");
                    OutputStreamWriter isr = new OutputStreamWriter(fis);
                    BufferedWriter bw = new BufferedWriter(isr);
                    int j = 50;
                    for (long i : timeList) {
                        String content = (double)(arraySize/1000) * (j + 1) / arraySize + "," + (double) i / 10 + "\n";
                        j++;
                        bw.write(content);
                        bw.flush();
                    }
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}