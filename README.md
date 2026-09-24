# Shopping List Desktop

Birden fazla alışveriş listesi hazırlayıp ürünlerin miktarını, fiyatını ve alınıp alınmadığını takip etmeye yarayan masaüstü uygulaması. Java Swing ile geliştirilmiştir; listeler ve hesaplar bilgisayardaki SQLite veritabanında saklanır.

## Özellikler

- Kayıt ve giriş
- Kullanıcı başına birden fazla alışveriş listesi
- Ürün adı, marka, bağlantı, miktar, birim ve fiyat bilgileri
- Tamamlanan ürünleri işaretleme ve liste toplamı
- Veritabanını ilk çalıştırmada oluşturma

**Teknolojiler:** Java Swing, Maven ve SQLite. Yeni hesapların parolaları PBKDF2-HMAC-SHA256 ile işlenir.

## Çalıştırma

JDK 17 veya üzeri ve Maven 3.9 veya üzeri gerekir.

```bash
mvn clean package
java -jar target/shopping-list-desktop-1.0.0.jar
```

Veritabanı `data/database.db` yolunda yerel olarak oluşur ve Git'e eklenmez. Testler için `mvn test` çalıştırın.

Bu bir öğrenme projesidir. Eski yerel test hesaplarındaki düz metin parolalar, başarılı giriş sonrasında yeni parola biçimine geçirilir.

## Lisans

[MIT](LICENSE).
