# rep_one



## Krótko o projekcie

Projekt to owoc nauki wybranych technologii, to dowód mojego z nimi doświadczenia, to probierz stopnia ich znajomości, wreszcie, to wyjście naprzeciw potrzebom procesu rekrutacji.
Co z punktu biznesowego oferuje aplikacja? Jako "przyszły były" pracownik kancelarii komorniczej chciałem usprawnić pracę na kilku szczeblach:
- Terminarz z wpłatami klientów pozwala zrezygnować z używania w identycznym celu tradycyjnego kalendarza, co sprawia, iż wprowadzanie nowych terminów, przeglądanie ich etc. jest dużo wygodniejsze.
- W skutek zbiegów egzekucyjnych do kancelarii trafiają akta egzekucyjne innych kancelarii. Akta te są następnie rejestrowane i dalsza egzekucja prowadzona jest "pod szyldem" kancelarii, która akta otrzymała. Zdarza się, że wiedza o tym, w posiadaniu jakich niezarejestrowanych akt jest kancelaria, bywa niezwykle przydatna, stąd aplikacja oferuje możliwość sporządzenia spisu takich akt. 
- Każdego dnia pracownicy kancelarii kontaktują się z urzędami państwowymi, pracodawacami dłużników, wierzycielami itp. itd. Aplikacja oferuje możliwość stworzenia bazy kontaktowej.
- Niektóre postępowania egzekucyjne wymagają podjęcia stosownych czynności w określnym czasie. Termin podjęcia czynności bywa czasem odległy. Aplikacja przechowuje szczegóły takich czynności, z terminem, dłużnikiem oraz opisem czynności/zagadnienia.
	

## Baza danych
		
W projekcie używam bazy danych **MySQL**. Folder `databaseSQL` zawiera pliki: `crudoneDB.sql` oraz `crudoneTestDB.sql`, które tworzą dwie bazy danych - zwykłą oraz testową. Plik `crudoneDB.sql` nie tylko tworzy bazę danych, ale również wprowadza do niej przykładowe dane - w tym również dane dotyczące użytkowników. Domyślnie utworzono cztery profile użytkoników. 

Profil administratora - login: `Admini`, hasło: `Admini`

Profile "zwykłych" użytkowników - `UzytkA`, `UzytkB` oraz `UzytkC`, hasło dla wszystkich użytkowników identyczne jak wyżej.

Niżej instruktaż, w trzech krokach, importowania ww. plików za pomocą programu **MySQL Workbench**. 
![mysql1](https://user-images.githubusercontent.com/32525977/35484920-da61dd5a-0457-11e8-9907-c479167ee0cb.png)

Po dokonaniu importu należy dokonać stosownych zmiań w pliku `context.xml` znajdującym się w folderze `src/main/webapp/META-INF` - należy zmienić wartości polom `username` oraz `password` - tak aby ich wartości były identyczne z tymi używanymi w **MySQL Workbench**. Identyczny krok należy podjąć wobec pliku `jdbc.properties`, pliku konfiguracyjnym dla testowej bazy danych, plik znajduje się w `src/test/resources`.

## Jak uruchomić aplikację

Uruchamiamy w Eclipse.
- Klonujemy repozytorium.
- Dokonujemy importu istniejącego projektu mavenowego.
- Dokonujemy zmiany w Project Properties w `Deployment Assembly` dodając folder `/src/main/webapp`
- W razie pojawienia się błędów weryfikujemy konfigurację Eclipse w następujących miejscach: `Build-path`, `Deployment Assembly`, `Project Facets` oraz `Web Project Settings`.
- Uruchamiamy aplikację używająć Tomcata.
				

## Technologie

Tworząc aplikację korzystałem min. z:
- Spring
- MySQL
- Tomcat 
- Maven
- Junit
- Mockito
- Lambok

## Refaktoryzacja / Zmiany
Wiele rzeczy można by w aplikacji zmienić, uzupełnić lub poprawić, niżej wyłącznie kilka z kroków, które można by podjąć:
- zwiększyć tzw. *test coverage*
- uzupełnić dokumentację
- wprowadzić klasy DTO
- "rozciągnąć" Spring Security na klasy RESTowe, obecnie o bezpieczeństwo REST "troszczy się" wyłącznie ta linijka w konfiguracji: `<security:http pattern="/restclient/**" security="none" />`
- zmienić logowanie na logowanie do pliku
- poprawić warstwę wizualną 
- udostępnić nowe funkcjonalności


## Grafiki
![crudone6](https://user-images.githubusercontent.com/32525977/35484808-c77d42da-0455-11e8-955e-a6594a138178.png)
![crudone1](https://user-images.githubusercontent.com/32525977/35419974-4e52aad8-023b-11e8-9333-c88403c61a8c.png)
![crudone2](https://user-images.githubusercontent.com/32525977/35420046-b4c31424-023b-11e8-92b6-d57aafdecdad.png)
![crudone3](https://user-images.githubusercontent.com/32525977/35420055-bed1af3e-023b-11e8-9188-5c6118139712.png)
![crudone4](https://user-images.githubusercontent.com/32525977/35420066-cb4dd2e2-023b-11e8-9e99-50a35f43fecd.png)
![crudone5](https://user-images.githubusercontent.com/32525977/35509842-157d8eae-04f6-11e8-9fd4-9f937c848422.png)
