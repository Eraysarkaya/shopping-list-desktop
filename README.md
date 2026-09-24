# Shopping List Desktop

Alışveriş listelerini masaüstünde yönetmek için geliştirilmiş Java Swing uygulaması. Listeler ve ürünler yerel SQLite veritabanında saklanır.

## Özellikler

- Kayıt ve giriş
- Kullanıcı başına birden fazla liste
- Ürün adı, marka, bağlantı, miktar, birim ve fiyat bilgileri
- Tamamlanan ürünleri işaretleme ve liste toplamı
- Veritabanını ilk çalıştırmada oluşturma

**Teknolojiler:** Java Swing, Maven, SQLite. Yeni hesapların parolaları PBKDF2-HMAC-SHA256 ile işlenir.

## Çalıştırma

JDK 17+ ve Maven 3.9+ gerekir.

```bash
mvn clean package
java -jar target/shopping-list-desktop-1.0.0.jar
```

Veritabanı `data/database.db` yolunda yerel olarak oluşur ve Git'e eklenmez. Testler: `mvn test`.

Bu bir öğrenme projesidir. Eski yerel test hesaplarındaki düz metin parolalar, başarılı giriş sonrasında yeni parola biçimine geçirilir.

## Lisans

MIT.
