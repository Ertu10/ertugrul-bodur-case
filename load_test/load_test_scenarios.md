# n11.com Search Module — Load Test Scenarios

## Overview
Bu yük test planı, https://www.n11.com/ header'daki **arama modülünün** davranışını araştırmak amacıyla hazırlanmıştır.

## Test Ortamı
- **Tool:** Apache JMeter
- **Thread (User) Sayısı:** 1
- **Ramp-up Period:** 1 saniye
- **Loop Count:** 3

## Test Senaryoları

### Senaryo 1: Ana Sayfa Yükleme
- **Request:** `GET /`
- **Amaç:** Arama modülünün bulunduğu ana sayfanın yüklenme performansını ölçmek
- **Assertion:** HTTP 200, Response time < 5000ms

### Senaryo 2: Normal Ürün Arama (laptop)
- **Request:** `GET /arama?q=laptop`
- **Amaç:** Tipik bir arama sorgusunun performansını ölçmek
- **Assertion:** HTTP 200, Response body contains arama sonuçları

### Senaryo 3: Farklı Keyword Arama (telefon)
- **Request:** `GET /arama?q=telefon`
- **Amaç:** Farklı keyword'lerle arama süresinin tutarlılığını kontrol etmek
- **Assertion:** HTTP 200

### Senaryo 4: Arama Sonuç Sayfalama
- **Request:** `GET /arama?q=laptop&pg=2`
- **Amaç:** Arama sonuçlarının 2. sayfasına erişim performansı
- **Assertion:** HTTP 200

### Senaryo 5: Boş Arama
- **Request:** `GET /arama?q=`
- **Amaç:** Boş query gönderildiğinde sistemin davranışını ve yanıt süresini ölçmek
- **Assertion:** HTTP 200 veya redirect

### Senaryo 6: Özel Karakter Aramasi
- **Request:** `GET /arama?q=%23test%26special`
- **Amaç:** URL encoding gerektiren özel karakterlerle aramanın performansını test etmek
- **Assertion:** HTTP 200

## Çalıştırma

### GUI Mode (Geliştirme)
```bash
jmeter -t load_test/n11_search_load_test.jmx
```

### Non-GUI Mode (Rapor Üretimi)
```bash
jmeter -n -t load_test/n11_search_load_test.jmx -l reports/load_results.jtl -e -o reports/load_html
```

## Metrikler
- Response Time (ortalama, min, max, persentil)
- Throughput (requests/sec)
- Error Rate (%)
- Latency
