# Databases
Uitleg over het gebruik van een MySQL database in een Java applicatie.
In de /src map staat een uitgewerkt voorbeeld waarin gebruik gemaakt wordt van een aparte Database klasse om alle acties richting de database af te handelen en een Repository klasse voor alle functionaliteit met betrekking tot (in dit geval) de Student.

## JDBC
Om een connectie te maken vanuit een Java applicatie naar een MySQL database maken we gebruik van JDBC (Java Database Connectivity).
Dit biedt een generieke API (Application Programming Interface) voor connectie naar verschillende soorten databases.
Dit betekent dat de aanroep naar de API vanuit onze Java code uniform is, maar dat JDBC met verschillende soorten databases kan communiceren.
JDBC gebruikt hiervoor database drivers.
Voor elk database management systeem (Microsoft SQL, MySQL, Oracle, Sybase, etc) is een andere database driver nodig.

## Connector/J database driver voor MySQL (of MariaDB)
Om via JDBC connectie te kunnen maken met een MySQL database, is een MySQL database driver nodig.
Een driver die geschikt is voor MySQL is de Connector/J driver.
Voor Windows is deze driver te installeren als onderdeel van de [MySQL installatie](https://dev.mysql.com/downloads/installer/).
Mocht je MySQL al hebben geïnstalleerd zonder de Connector/J optie aan te vinken, dan kun je de installer opnieuw uitvoeren en deze optie alsnog toevoegen.
De driver is ook los te downloaden vanaf de [MySQL](https://dev.mysql.com/downloads/connector/j/) of de [MariaDB](https://mariadb.com/downloads/#connectors) website.

## Library toevoegen
Nadat de Connector/J driver is gedownload en/of geïnstalleerd, dient deze library als mysql-connector-java-8.0.xx.jar bestand toegevoegd te worden aan ons Java project.
Wanneer je Connector/J hebt geïnstalleerd als onderdeel van de MySQL installatie, dan vind je het .jar bestand in de directory %ProgramFiles(x86)%\MySQL\Connector J 8.0.
Hoe je het .jar bestand toevoegt aan je project hangt af van welke IDE je gebruikt:
- **IntelliJ**: ga naar File -> Project Structure, selecteer aan de linkerkant Modules, ga naar het tabblad Dependencies en klik op het plusje aan de rechter kant om een JAR toe te voegen. Selecteer vervolgens het .jar bestand.
- **NetBeans**: ga naar File -> Project Properties, selecteer links de category 'Libraries', klik op het plusje bij classpath om een nieuwe JAR toe te voegen. Selecteer vervolgens het .jar bestand.
- **Visual Studio Code**: open het .classpath bestand van je Java project en voeg een classpathentry toe met kind="lib" en path naar het .jar bestand.

## De Java code

### Database connectie object aanmaken
Voordat we een query kunnen uitvoeren, zullen we eerst een database connectie object aan moeten maken.
Hiervoor gebruiken we de getConnection() methode van de java.sql.DriverManager.
Deze methode heeft 3 argumenten nodig: een url met daarin het type driver, de hostname, de poort, de database naam en eventuele configuratie opties en verder nog een gebruikersnaam en een wachtwoord:
```java
String url = "jdbc:mysql://localhost:3306/databaseName?useSSL=false";
String user = "username";
String password = "Welkom01";

Connection conn = DriverManager.getConnection(url, user, password);
```
De variabele 'conn' bevat nu dus een Connection object die we kunnen gebruiken om query's uit te voeren.

### Een query uitvoeren zonder resultaat
Om een query uit te voeren hebben we allereerst een Statement object nodig.
Deze maken we aan door conn.CreateStatement() methode aan te roepen.
Wanneer we een query willen uitvoeren zonder resultaat (bijvoorbeeld een INSERT, UPDATE of DELETE query), dan gebruiken we de executeUpdate() methode van het Statement object:
```java
Statement statement = conn.createStatement();
int aantal = statement.executeUpdate("INSERT INTO tabelnaam (kolom1, kolom2) VALUES ('waarde1', 'waarde2')");
```
Het aantal geeft aan hoeveel records er zijn bewerkt (toegevoegd, gewijzigd of verwijderd) bij het uitvoeren van de query.
Het is raadzaam om in de code te controleren of dit aantal klopt.
Voeren we bijvoorbeeld een INSERT query uit om één record toe te voegen, dan controleren we of het aantal bewerkte records ook inderdaad 1 is.

### Een query uitvoeren met resultaat
Wanneer we een query willen uitvoeren die wel een resultaat terug geeft (bijvoorbeeld een SELECT query), dan maken we gebruik van de executeQuery() methode van het Statement object.
Deze levert ons een ResultSet waar we met een while-loop doorheen kunnen stappen.
Elke iteratie van de while-loop geeft ons het volgende record uit de tabel, tot er geen records meer zijn:
```java
Statement statement = conn.createStatement();
ResultSet resultSet = statement.executeQuery("SELECT kolomnaam FROM tabelnaam");

while (resultSet.next()) {
    String waarde = resultSet.getString("kolomnaam");
    System.out.println(waarde);
}

resultSet.close();
```
Nadat we klaar zijn met de ResultSet roepen we de close() methode aan om de ResultSet te sluiten en resources vrij te geven.

### Prepared statements
Bij PHP heb je geleerd dat we Prepared Statements kunnen gebruiken om onder andere je code te beveiligen tegen SQL injection aanvallen. Bij Java gaat dat op een soortgelijk manier. We maken hiervoor een PreparedStatement object aan, koppelen de argumenten en voeren het PreparedStatement uit:
```java
String query = "UPDATE tabelnaam SET kolomnaam = ? WHERE id = ?";

PreparedStatement statement = conn.prepareStatement(query);
statement.setString(1, 'Waarde');
statement.setInt(2, 100);

ResultSet resultSet = statement.executeQuery();
```
Het eerste argument van de setString en setInt methodes is de parameterIndex, oftewel het volgnummer van het vraagteken in de query.
Deze begint bij 1.
Het tweede argument is de waarde die we willen toekennen.
Nadat we executeQuery hebben uitgevoerd kunnen we met een while-loop door de records heen lopen, zoals in het voorgaande voorbeeld.

### Transactions
Om de integriteit van je data te borgen, kan het nodig zijn om meerdere query's als één batch uit te voeren en ook in zijn geheel vast te leggen (commit) of in zijn geheel terug te draaien als één van de query's in de batch mislukt (rollback).
Denk bijvoorbeeld aan een tabel met orders en een tabel met orderregels.
We willen geen orderregels toevoegen als het toevoegen van de order is mislukt, maar andersom willen we ook geen lege, of een halve order overhouden als het toevoegen van de orderregels mislukt.
Om deze query's als één geheel uit te voeren kun je gebruik maken van transacties:
```java
try {
    conn.setAutoCommit(false);

    // voer hier meerdere query's achter elkaar uit
    
    conn.commit();
}
catch (SQLException ex) {
    conn.rollback();
}
```

### Connectie sluiten
Als we klaar zijn met de connectie, is het goed gebruik om de connectie te sluiten zodat resources worden vrijgegeven:
```java
conn.close();
```
