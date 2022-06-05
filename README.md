# SPT-AKI-Item-Editor for SPT-AKI 2.3.1 main information
https://www.dropbox.com/sh/z7qg6os9w2c5qv2/AADNt5uSKr5c5kyK9NPTuLq5a?dl=0  

JRE 9.0.4 download from link, and put package in archive to item editor folder. */SPT-AKI item editor/jre9.0.4  
  An important point, since I was born and am in Russia, I am aware of the problems of the state on the continent of the earth and the priority points of view, and I    should not write claims about this, because life is more difficult than it seems, with respect, so to speak :)

Скачать с ссылки JRE, открыть архив и переместить папку jre9.0.4 в каталог программы, путь выглядит так: */SPT-AKI item editor/jre9.0.4  
Важный момент, так как я родился и нахожусь в России, про проблемки государства на континете земли и приоритетные точки зрения я в курсе, и притензции по этому поводу писать не стоит, поскольку жизнь сложней чем кажется, с уважением, так сказать :)

# English description to SPT-AKI Item Editor test version

  SPT-AKI Item Editor is designed to edit server data, in this case location settings, items and global server settings. But there is a nuance, since without thread support in application functions (in a month or two, I will bring thread support in application fucntion and optimization for weak and strong computers into the program).  
  The advantages of the editor are that there is a convenient search by groups, which allows you not to remember the name of the item looking for it for about 10 minutes. The disadvantages are that you cannot change the source of the items.json game, since the editor is string-bound. A line will be added somewhere, everything will break. So it goes. But it will be fixed in version 1.
  
  Importantly, the editor itself is located under the open menu tab. Therefore, it must be hidden after selecting an item, or immediately if locations and global settings are being edited.

  It is worth noting that the program has partial shortcomings, since I wrote it right after reading a book on Java and, in principle, made it in 3 months as an example of an open source project, so in version V1 the code will improve by 50% and optimization will come up accordingly too.
  
  The editor implements:
1. Changes in the characteristics of item strings in items.json
1.1 Creating presets for replacing item characteristics  
1.2 Test tag system for search Rounds and Ammo boxes
3. It is possible to create and load different variations of items through items.json and global server characteristics through globals.json (in settings)
4. Changes to global characteristics in globals.json (experience, health, etc.)
5. Changes in location parameters (exit time, boss spawn, etc.)

  Requirements:
* [Be sure to download JRE version 9.0.4 to the editor directory](https://www.dropbox.com/sh/z7qg6os9w2c5qv2/AADNt5uSKr5c5kyK9NPTuLq5a?dl=0) because the application is written in JAVA, and if it is not on the computer without it, the program does not work. You can try the JAVA versions above, but I did not check.
* Due to the lack of thread support, processors from i7 or powerful i5, other processors at your own peril and risk
* It is advisable not to press several buttons quickly, since there is no blocking of flows yet. The start marker will be a faded button

  Current issues:
* Since the program is a test and for a device to work, the code is there, of course, at 5k (then I will fix it in a month because this is the previous version, but stable)
* No thread support in application functions
* Limited features in the test version of the preset creation system
* Requirements for matching source strings items.json and the current items.json of your game
* There are suspicions of memory consumption, but if the program is used for less than 12 hours, then there is no problem
* Not done adding tabs for easy reading after writing
  
  
  Further plans for the development of the program:
* Introduce thread support, refine the item preset system and the item reading system, make a system for adding strings to the item (as an example, available rounds for the weapon magazine)
* In v2, make it possible to work on all versions of SPT-AKI (the developments are already there), refinement of the system for viewing characteristics and data
* In v3, do a redesign of the interface
* (probably will not be) In v4, make settings system for bots file and loot on maps

# Описание программы SPT-AKI Item Editor тестовый образец

  SPT-AKI Item Editor предназначен для того, чтобы редактировать данные сервера, в данном случае настроек локаций, предметов и глобальных настроек сервера. Но есть нюанс, поскольку отсутствует поддержка потоковой работы функций, оптимизация средняя (через месяц-два завезу в прогу поддержку потока и оптимизацию для слабых и сильных компьютеров).
  Плюсы редактора в том что есть удобный поиск по группам что позволяет не вспоминать название предмета ищя его минут 10. Минусы в том что нельзя изменять исходник игры items.json, поскольку редактор привязан к строкам. Добавится где нибудь строка, все поломается. Такие дела. Но в версии 1 будет исправлено.

  Важно, сам редактор находится под открытой вкладкой меню. Поэтому её нужно скрыть после выбора предмета, либо сразу если редактируются локации и глобальные настройки

  Стоит отметить что, у программы есть частичные недочеты, посольку я её написал сразу после прочтения книжки по Java и в принципе, сделал месяца за 3 как пример проекта с открытым кодом, поэтому в версии V1 код улучшится на 50% и оптимизация подъедет соответсвенно тоже.

  Редактор реализует:
1. Изменения характеристик строк предметов в items.json  
1.1 Создание пресетов по замене характеристик предметов  
1.2 Тестовую систему тегов для поиска патрон и пачек патронов
3. Имеется возможность создания и загрузки разных вариаций предметов через items.json и глобальных характеристик сервера через globals.json (в настройках)
4. Изменения глобальных характеристик в globals.json (опыт, здоровье и т.д.)
5. Изменения параметров локаций (время выхода, спавн босса и т.д.)

  Требования:
* [Обязательно скачать JRE версии 9.0.4 в каталог редактора](https://www.dropbox.com/sh/z7qg6os9w2c5qv2/AADNt5uSKr5c5kyK9NPTuLq5a?dl=0) потому что написано приложение на JAVA, и если его нет на компьютере без него программа не работает. Можно попробовать и версии JAVA выше но я не проверял.
* Из за отсутсвия многопотка процессоры от i7 или мощные i5, остальные процессоры на свой страх и риск
* Желательно быстро на несколько кнопок не жать, поскольку пока нет блокировки потоков. Маркером запуска будет потускневшая кнопка

  Текущие проблемы:
* Поскольку программа тестовая и для устройства на работу, код там конечно, на 5ку (потом исправлю через месяц потому, что это версия предыдущая, но стабильная)
* Отсутствие потоковой работы функций приложения
* Ограниченность возможностей в тестовой версии системы создания пресета
* Требования к соответвию строк исходника items.json и текущему items.json вашей игры
* Есть подозрения на потребление пямяти, но если программа используется менее 12 часов, то проблем нет
* Не сделано добавление табуляции для уднобного чтения после записи

  Дальнейшие планы на разработку программы:
* Внедрить многопоток, доработать систему пресетов и систему считывания предметов, сделать систему добавления строк в предмет (как пример доступные пули для магазина)
* В v2 сделать возможность работы  на всех версиях SPT-AKI (наработки уже есть), доработка системы просмотра характеристик и данных
* В v3 сделать переработку интерфейса
* (возможно не будет) В v4 сделать настройку ботов и лута на картах
