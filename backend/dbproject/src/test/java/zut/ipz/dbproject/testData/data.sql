-- Tworzenie tabeli Użytkownicy
CREATE TABLE Uzytkownicy
(
    ID             INT PRIMARY KEY,
    Imie           VARCHAR(50),
    Nazwisko       VARCHAR(50),
    Email          VARCHAR(100) UNIQUE,
    DataUtworzenia DATE
);

-- Tworzenie tabeli Zamowienia
CREATE TABLE Zamowienia
(
    ID             INT PRIMARY KEY,
    UzytkownikID   INT,
    DataZamowienia DATE,
    FOREIGN KEY (UzytkownikID) REFERENCES Uzytkownicy (ID)
);

-- Tworzenie tabeli Produkty
CREATE TABLE Produkty
(
    ID            INT PRIMARY KEY,
    NazwaProduktu VARCHAR(100),
    Cena          DECIMAL(10)
);

-- Tworzenie tabeli SzczegolyZamowienia
CREATE TABLE SzczegolyZamowienia
(
    ID              INT PRIMARY KEY,
    ZamowienieID    INT,
    ProduktID       INT,
    Ilosc           INT,
    CenaJednostkowa DECIMAL(10),
    FOREIGN KEY (ZamowienieID) REFERENCES Zamowienia (ID),
    FOREIGN KEY (ProduktID) REFERENCES Produkty (ID)
);