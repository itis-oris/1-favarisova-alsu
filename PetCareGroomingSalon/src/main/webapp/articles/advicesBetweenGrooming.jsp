<%--
  Created by IntelliJ IDEA.
  User: Alsu
  Date: 08.12.2024
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Уход за питомцем между визитами в груминг-салон</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css' />">
</head>
<body>
<div class="container">
    <header>
        <h1>Советы по уходу за питомцем между посещениями груминг-салона</h1>
    </header>

   <hr><br>

    <section>
        <h2>Регулярное расчесывание</h2>
        <p>
            Расчесывание помогает сохранить шерсть питомца здоровой и блестящей, предотвращая образование колтунов. Важно использовать правильные инструменты:
        </p>
        <ul>
            <li><strong>Длинношерстные породы:</strong> Используйте металлические щетки или гребни с длинными зубьями. Расчесывайте шерсть от корней до кончиков 3-4 раза в неделю.</li>
            <li><strong>Короткошерстные породы:</strong> Массажные перчатки или резиновые щетки помогут удалить отмершие волоски и улучшить кровообращение.</li>
            <li><strong>Кудрявошерстные породы:</strong> Такие собаки, как пудели, нуждаются в ежедневном расчесывании для предотвращения спутывания.</li>
        </ul>
    </section>
    <section>
        <h2>Уход за лапами</h2>
        <p>
            Лапы питомца подвергаются постоянному воздействию внешней среды: грязь, реагенты, снег, жара. Вот что можно сделать:
        </p>
        <ul>
            <li>Протирайте лапы влажной тканью или промывайте их после каждой прогулки.</li>
            <li>Зимой используйте специальный воск для лап или защитные ботинки.</li>
            <li>Регулярно проверяйте когти и стригите их каждые 3-4 недели, если они не стачиваются естественным образом.</li>
        </ul>
    </section>
    <section>
        <h2>Гигиена ушей и глаз</h2>
        <p>
            Уши и глаза — чувствительные зоны, которые требуют особого внимания:
        </p>
        <ul>
            <li>Для чистки ушей используйте ватные диски и специальные растворы, рекомендованные ветеринаром.</li>
            <li>Регулярно проверяйте глаза на наличие покраснений или выделений. Удаляйте грязь с помощью влажной салфетки или ватного тампона.</li>
            <li>Если у вашей собаки светлая шерсть, используйте средства для удаления пятен от слез.</li>
        </ul>
    </section>
    <section>
        <h2>Гигиена зубов</h2>
        <p>
            Чистка зубов помогает предотвратить появление зубного камня и неприятного запаха:
        </p>
        <ul>
            <li>Используйте зубную пасту для собак и мягкую щетку.</li>
            <li>Предлагайте питомцу специальные жевательные лакомства, которые помогают чистить зубы.</li>
            <li>Регулярно посещайте ветеринара для профилактики заболеваний ротовой полости.</li>
        </ul>
    </section>


    <br>
    <h3><a href="${pageContext.request.contextPath}">Вернуться на главную страницу</a></h3>

</div>
</body>
</html>
