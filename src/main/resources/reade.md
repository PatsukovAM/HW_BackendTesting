ProductTests

1  void positivePostProductTest() позитивный тест: создание подукта с полями, удовлетворяющим условиям в описание
2  void positiveGetProductTest() позитивный тест: поиск созданного продукта
3  void positivePutProductTest() позитивный тест: обновление плей у продукта
4  void positiveDeleteProductTest() позитивный тест: удаление продукта   
5  void negativeGetProductTest() негативный тест: поиск несуществующего продукта 
6  void negativePostProductNullTitleTest() негативный тест: создание продукта с пустым полем title
7  void negativePostProductNullPriceTest() негативный тест: создание продукта с пустым полем price
8  void negativePostProductNegativePriceTest() негативный тест: создание продукта с отрицателным зачением поля  price
9  void negativePostProductOverFlowPriceTest() негативный тест: переполнение поля price положительным значение (в реализации был создан специальный класс с полем Long price) // тут спорно или это переполнение или неверный тип входных данных
10 void negativeGetProductNegativeIdTest() негативный тест: поиск продукта с отрицателным значением поля id
  