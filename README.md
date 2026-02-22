# Insider QA Assessment Project — Ertugrul Bodur

Senior QA Engineer case study projesi. Java ekosistemi kullanılarak, UI otomasyon, yük testi ve API testlerini içerir.

## Teknoloji Stack

| Alan | Teknoloji |
|---|---|
| UI Test | Java 17 + Selenium 4 + TestNG |
| API Test | RestAssured + TestNG |
| Load Test | Apache JMeter |
| Build | Maven |
| Driver Management | WebDriverManager (Bonigarcia) |
| Screenshot | TestNG ITestListener (fail durumunda otomatik) |
| Design Pattern | Page Object Model (POM) |

## Proje Yapısı

```
├── pom.xml                              # Maven build dosyası
├── testng.xml                           # TestNG suite konfigürasyonu
├── src/main/java/com/insider/
│   ├── config/ConfigReader.java         # Config okuma
│   ├── driver/DriverFactory.java        # Browser factory (Chrome/Firefox)
│   ├── pages/                           # Page Object Model
│   │   ├── BasePage.java
│   │   ├── HomePage.java
│   │   ├── CareersPage.java
│   │   ├── QualityAssurancePage.java
│   │   ├── OpenPositionsPage.java
│   │   └── LeverApplicationPage.java
│   └── utils/TestListener.java          # Screenshot on failure
├── src/test/java/com/insider/
│   ├── ui/InsiderCareerTest.java        # UI test senaryoları
│   └── api/PetStoreApiTest.java         # API test senaryoları
└── load_test/
    ├── n11_search_load_test.jmx         # JMeter test planı
    └── load_test_scenarios.md           # Senaryo açıklamaları
```

## Gereksinimler

- Java 17+
- Maven 3.8+
- Chrome ve/veya Firefox browser
- Apache JMeter 5.6+ (load test için)

## Kurulum

```bash
# Projeyi klonla
git clone <repo-url>
cd ertugrul_bodur_case

# Bağımlılıkları indir
mvn clean compile
```

## Testleri Çalıştırma

### UI Testleri

```bash
# Chrome ile (default)
mvn test -Dtest=com.insider.ui.InsiderCareerTest -Dbrowser=chrome

# Firefox ile
mvn test -Dtest=com.insider.ui.InsiderCareerTest -Dbrowser=firefox
```

### API Testleri

```bash
mvn test -Dtest=com.insider.api.PetStoreApiTest
```

### Tüm Testleri Çalıştır

```bash
# Chrome ile
mvn test -Dbrowser=chrome

# Firefox ile
mvn test -Dbrowser=firefox
```

### Load Test (JMeter)

```bash
# GUI modunda
jmeter -t load_test/n11_search_load_test.jmx

# Non-GUI modunda (rapor üretimi)
jmeter -n -t load_test/n11_search_load_test.jmx -l reports/load_results.jtl -e -o reports/load_html
```

## Test Senaryoları

### UI Test (Insider Careers Flow)

| Step | Açıklama |
|---|---|
| 1 | useinsider.com ana sayfası açılır, tüm ana bloklar kontrol edilir |
| 2 | QA Careers → "See all QA jobs" → Istanbul, Turkey + QA department filtrele |
| 3 | Tüm ilanların Position, Department, Location bilgileri doğrulanır |
| 4 | "View Role" tıklanır → Lever Application form sayfasına yönlendirildiği doğrulanır |

### API Test (Petstore CRUD)

**Positive:** Create → Read → Update → Verify Update → Find by Status → Delete → Verify Delete

**Negative:** Invalid ID (404) | String ID (400/404) | Missing required field | Invalid status | Delete non-existing

### Load Test (n11.com Arama)

6 senaryo: Ana sayfa yükleme, ürün aramaları, sayfalama, boş arama, özel karakter araması

## Raporlar

- **Fail Screenshot'ları:** `reports/screenshots/`
- **JMeter Raporu:** `reports/load_html/`

## Önemli Notlar

- BDD framework kullanılmamıştır (TestNG)
- Fail durumunda otomatik screenshot alınır
- Browser `-Dbrowser` parametresi ile değiştirilebilir
- Page Object Model (POM) tam uyumlu
- Thread-safe driver yönetimi (paralel çalıştırma desteği)
