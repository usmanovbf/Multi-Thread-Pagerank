# Description
PageRank calculator of specified web-site. It can crawle pages and calculate them in multithread option.
# Описание задачи и параметры для запуска программы:
```java -jar app/multi-thread-pagerank-1.0.jar <опции>```
### Задача: реализовать алгоритм PageRank.

Задача разбивается на три подзадачи:

1. Начав с некоторого сайта (например, kpfu.ru), сформировать матрицу смежностей
web-страниц. (15 баллов). Рекомендуемый размер матрицы -- 100x100.

```java -jar app/multi-thread-pagerank-1.0.jar http://seclub.org/  ```


2. По данной матрице смежности вычислить PageRank, используя степенной метод.
(10 баллов)

```java -jar app/multi-thread-pagerank-1.0.jar http://seclub.org/  ```

3. Изменить программу п.2 так, чтобы матрица смежности хранилась с помощью
одного из методов хранения разреженной матрицы.
(10 баллов)

```java -jar app/multi-thread-pagerank-1.0.jar http://seclub.org/   sparsedmatrix ```

4. Изменить программу п.3 так,чтобы программа использовала параллельность или
многопоточность. Измерить ускорение.
(15 баллов)

```java -jar app/multi-thread-pagerank-1.0.jar http://seclub.org/  crawlerthreads=3 pagesnumber=10 sparsedmatrix calculatorthreads=5```