package ru.rickSanchez.company.homework_5;

public class Main {
    static final int size = 10000;
    static final int h = size / 2;
    public static void main(String[] args) {
        //Отличие первого метода от второго:
        //Первый просто бежит по массиву и вычисляет значения.
        arrCalculation(size);
        //Второй разбивает массив на два массива, в двух потоках
        // высчитывает новые значения и потом склеивает эти массивы
        // обратно в один.
        splitArray(size);
    }

    public static void splitArray(int size){
        //1) Создаем одномерный длинный массив, например:
        float[] arr = new float[size];
        int before = 0;
        int after = 0;
        //2) Заполняем этот массив единицами:
        for(int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }
        //3) Засекаем время выполнения:
        before = (int) System.currentTimeMillis();
        //4) Проходим по всему массиву и для каждой ячейки считаем новое
        //значение по формуле:
        for(int i = 0; i < arr.length; i++) {
            //разбиваем массив на два массива, в двух потоках
            // высчитывает новые значения и потом склеивает эти массивы
            // обратно в один.
            float[] arrSplit_a = new float[size/2];
            float[] arrSplit_b = new float[size/2];
            //первый массив
            System.arraycopy(arr,0,arrSplit_a,0,arr.length/2);
            //второй массив
            System.arraycopy(arr,arr.length/2,arrSplit_b,0,arr.length/2);
            for(int j = 0; j < arrSplit_a.length; j++) {
                arrSplit_a[j] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }

            for(int j = 0; j < arrSplit_b.length; j++) {
                arrSplit_b[j] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
            //склеиваем обратно
            System.arraycopy(arrSplit_a,0,arr,0,arrSplit_a.length-1);
            System.arraycopy(arrSplit_b,0,arr,arr.length/2,arrSplit_b.length-1);
        }
        //5) Проверяется время окончания метода:
        after = (int) System.currentTimeMillis();
        //6) В консоль выводим время работы:
        System.out.println("Время выполнения = " + (after - before) + " мс.");
    }




    public static void arrCalculation(int size){
        //1) Создаем одномерный длинный массив, например:
        float[] arr = new float[size];
        int before = 0;
        int after = 0;
        //2) Заполняем этот массив единицами:
        for(int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }
        //3) Засекаем время выполнения:
        before = (int) System.currentTimeMillis();
        //4) Проходим по всему массиву и для каждой ячейки считаем новое
        //значение по формуле:
        for(int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        //5) Проверяется время окончания метода:
        after = (int) System.currentTimeMillis();
        //6) В консоль выводим время работы:
        System.out.println("Время выполнения = " + (after - before) + " мс.");
    }


}
