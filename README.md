# FairOptima REST API

**Kariyer Fuarı Stant ve Kaynak Optimizasyon Sistemi**

Üniversite veya kurumların düzenlediği kariyer fuarlarında katılımcı şirketleri, stant alanlarını ve altyapı kaynaklarını merkezi bir arka yüz servisinde yöneten RESTful web servisidir. Sunucu Uygulama Geliştirme dersi dönem projesi olarak geliştirilmiştir.

**Takım:** Berk Dinçer (2407231068), Ayşegül Aslan (2407231041)

---

## İçindekiler

1. [Teknolojiler](#teknolojiler)
2. [Mimari](#mimari)
3. [Gerekli Yazılımlar](#gerekli-yazılımlar)
4. [Veritabanı Kurulumu](#veritabanı-kurulumu)
5. [Bağlantı Ayarları](#bağlantı-ayarları)
6. [Uygulamayı Çalıştırma](#uygulamayı-çalıştırma)
7. [Adresler](#adresler)
8. [Örnek Kullanıcılar ve Roller](#örnek-kullanıcılar-ve-roller)
9. [Yetki Matrisi](#yetki-matrisi)
10. [API Uçları](#api-uçları)
11. [Hata Modeli](#hata-modeli)
12. [Postman Koleksiyonu](#postman-koleksiyonu)
13. [Proje Yapısı](#proje-yapısı)
14. [Modül Durumu](#modül-durumu)

---

## Teknolojiler

| Alan | Teknoloji |
|---|---|
| Dil / Çalışma ortamı | Java 21 |
| Çatı | Spring Boot 4.1.2 |
| Veri erişimi | JPA / Hibernate, `EntityManager` (Spring Data JPA **kesinlikle kullanılmaz**) |
| Veritabanı | MySQL 8.x |
| Güvenlik | Spring Security, HTTP Basic, veritabanı tabanlı kullanıcılar, Argon2id |
| Dokümantasyon | springdoc-openapi 3.0.3 (Swagger UI) |
| Yönetim uçları | Spring Boot Actuator |
| Derleme | Maven (Maven Wrapper dahil) |

---

## Mimari

İstekler şu akışı izler:

```
Controller  →  Service  →  Repository  →  MySQL
```

- **Controller:** `@RestController` ile işaretlenir, yalnızca HTTP istek/cevaplarını yönetir.
- **Service:** Arayüz + `@Service` gerçekleştirimi (Impl). İş kuralları ve `@Transactional` sınırları burada belirlenir.
- **Repository:** Arayüz + `@Repository` gerçekleştirimi (Impl). `EntityManager` ile `persist`, `find`, `merge`, `remove` ve parametreli JPQL sorguları yürütülür.
- **Bağımlılıklar (DI):** Yalnızca constructor injection ile alınır.
- **Hata yönetimi:** `@RestControllerAdvice` ile merkezi olarak yönetilir ve ortak `ErrorResponse` JSON modeliyle döner.
- **Veri transferi:** İstek/cevap gövdeleri için DTO'lar ve Jakarta Bean Validation kullanılır.

---

## Gerekli Yazılımlar

| Yazılım | Sürüm | Not |
|---|---|---|
| JDK | 21 (Eclipse Temurin önerilir) | `JAVA_HOME` ayarlı olmalı |
| MySQL | 8.x | WAMP kullanılıyorsa bu projede varsayılan port **3308** |
| Maven | Gerekmez | Projedeki `mvnw` / `mvnw.cmd` kullanılır |
| Git | Güncel | Opsiyonel |
| Postman | Güncel | Opsiyonel, test koleksiyonu için |

Sürüm kontrolü:

```powershell
java -version
.\mvnw -v
```

---

## Veritabanı Kurulumu

MySQL servisi çalışırken MySQL Workbench veya komut satırından boş bir veritabanı oluşturun:

```sql
CREATE DATABASE fairoptima_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Tablolar uygulama ilk açıldığında Hibernate tarafından otomatik oluşturulur (`ddl-auto=update`). Veriler uygulama yeniden başlatıldığında korunur. Roller ve örnek kullanıcılar yalnızca tablolar boşsa bir kez eklenir (`DataSeedService` üzerinden).

---

## Bağlantı Ayarları

Varsayılan değerler `src/main/resources/application.properties` içindedir ve WAMP standardına göredir (kullanıcı `root`, parola boş, port 3308). Farklı bir ortamda şu **ortam değişkenleriyle** ezilebilir:

| Ortam değişkeni | Varsayılan | Açıklama |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3308/fairoptima_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` | JDBC adresi |
| `DB_USERNAME` | `root` | Veritabanı kullanıcısı |
| `DB_PASSWORD` | *(boş)* | Veritabanı parolası |

Uygulama portu **8081**'dir (`server.port`).

Özel uygulama özellikleri (`@Value` ile okunur):

```properties
app.fair.system-name=FairOptima Management Engine
app.fair.version=1.0.0
app.fair.default-currency=TRY
```

---

## Uygulamayı Çalıştırma

**Windows (PowerShell):**

```powershell
# MySQL parolası boş değilse:
$env:DB_PASSWORD="parolaniz"

# MySQL farklı bir portta çalışıyorsa:
$env:DB_URL="jdbc:mysql://localhost:3306/fairoptima_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"

.\mvnw spring-boot:run
```

**Linux / macOS:**

```bash
export DB_PASSWORD="parolaniz"
./mvnw spring-boot:run
```

Konsolda `Started FairOptimaApiApplication` satırı görününce uygulama hazırdır.

Paketleyip çalıştırmak için:

```powershell
.\mvnw clean package
java -jar target\fair-optima-api-0.0.1-SNAPSHOT.jar
```

Birim testleri için (MySQL servisi çalışıyor olmalıdır):

```powershell
.\mvnw test
```

---

## Adresler

Taban adres: `http://localhost:8081`

| Amaç | Adres | Erişim |
|---|---|---|
| Swagger UI | `http://localhost:8081/swagger-ui/index.html` | Açık |
| OpenAPI (JSON) | `http://localhost:8081/v3/api-docs` | Açık |
| Actuator health | `http://localhost:8081/actuator/health` | Açık |
| Actuator info | `http://localhost:8081/actuator/info` | Açık |
| Actuator metrics | `http://localhost:8081/actuator/metrics` | Yalnızca `ADMIN` |
| API | `http://localhost:8081/api/...` | Kimlik doğrulama gerekli |

Swagger UI'da korumalı uçları denemek için sağ üstteki **Authorize** düğmesine tıklayıp aşağıdaki kullanıcılardan biriyle giriş yapın.

---

## Örnek Kullanıcılar ve Roller

Değerlendirmede kullanılabilecek kullanıcılar uygulama ilk açıldığında otomatik oluşturulur. Parolalar veritabanında **Argon2id** ile hash'lenmiş olarak saklanır.

| Kullanıcı adı | Parola | Rol | Durum | Yapabildikleri |
|---|---|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN` | Aktif | Okuma, ekleme/güncelleme, silme, tüm Actuator uçları |
| `company` | `company123` | `ROLE_COMPANY` | Aktif | Okuma, ekleme/güncelleme (PATCH dahil) |
| `reader` | `reader123` | `ROLE_READONLY` | Aktif | Yalnızca okuma (GET) |
| `passive` | `passive123` | `ROLE_READONLY` | **Pasif** | Giriş reddedilir (401) |

---

## Yetki Matrisi

Yetkilendirme HTTP metodu ve yol üzerinden uygulanır. Kimlik doğrulaması yoksa veya başarısızsa **401**, doğrulanmış ama yetkisiz kullanıcıya **403** döner.

| İşlem | `READONLY` | `COMPANY` | `ADMIN` |
|---|:---:|:---:|:---:|
| `GET /api/**` | ✅ | ✅ | ✅ |
| `POST /api/**` | ❌ 403 | ✅ | ✅ |
| `PUT /api/**` | ❌ 403 | ✅ | ✅ |
| `PATCH /api/**` | ❌ 403 | ✅ | ✅ |
| `DELETE /api/**` | ❌ 403 | ❌ 403 | ✅ |
| `POST /api/reservations/{id}/approve`, `/reject` | ❌ 403 | ❌ 403 | ✅ |
| `GET /actuator/metrics` | ❌ 403 | ❌ 403 | ✅ |
| `GET /actuator/health`, `/actuator/info` | Açık | Açık | Açık |

> Bazı spesifik kurallar servis katmanında da denetlenir. Örneğin şirketler onaylanmış aktif rezervasyonları varken silinemez, stantlar rezerve edilmişse silinemez.

---

## API Uçları

Uygulama temel olarak şu kaynakları sunar. Tüm uçların parametre, istek ve cevap (DTO) modelleri Swagger UI üzerinden incelenebilir.

| Yol | Açıklama |
|---|---|
| `/api/companies` | Şirket yönetimi (CRUD, `?sector=` ile sektöre göre filtreleme) |
| `/api/fairs` | Fuar etkinlikleri yönetimi |
| `/api/halls` | Fuarlara bağlı salonların yönetimi |
| `/api/booth-types` | Stant tiplerinin yönetimi |
| `/api/fairs/{fairId}/booths/available` | Fuardaki müsait stantlar |
| `/api/booths` | Salonlardaki stantların yönetimi (durum yönetimi ve çakışma kontrolleri) |
| `/api/reservations` | Şirketlerin stant rezervasyonları; onay/ret (`POST /{id}/approve`, `/{id}/reject`, yalnızca ADMIN) ve iptal (`POST /{id}/cancel`) |
| `/api/pricing/quote` | Stant fiyat teklifi (`?basePrice=&days=&plan=standard\|early`). `@Primary` / `@Qualifier` ile seçilen iki fiyatlama gerçekleştirimi |
| `/api/system/info` | Uygulama bilgisi (tembel `@Lazy` oluşturulan servisten gelir) |

Örnek istek gövdesi (`POST` / `PUT /api/companies`):

```json
{
  "name": "Teknoloji A.S.",
  "sector": "Yazilim",
  "contactEmail": "info@teknoloji.com",
  "website": "https://teknoloji.com",
  "description": "Yazilim ve bulut cozumleri"
}
```

---

## Hata Modeli

Tüm hatalar ortak bir JSON modeliyle döner. HTTP durum kodu ile gövdedeki `status` her zaman tutarlıdır:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Şirket bulunamadı: id=9999",
  "timestamp": "2026-11-06T13:15:31.7808864"
}
```

| Kod | Ne zaman |
|---|---|
| 400 | Doğrulama hatası, bozuk JSON, geçersiz parametre tipi |
| 401 | Kimlik bilgisi yok, yanlış parola, pasif kullanıcı |
| 403 | Kimliği doğrulanmış kullanıcının yetkisi yok |
| 404 | Kayıt veya endpoint bulunamadı |
| 500 | Beklenmeyen sunucu hatası (ayrıntı sunucu günlüğüne yazılır) |

---

## Postman Koleksiyonu

`FairOptima.postman_collection.json` dosyasını Postman'de **Import** ile içe aktarın.

- **Base URL ve kimlik bilgileri koleksiyon değişkenleridir:** `baseUrl`, `adminUser`/`adminPass`, `companyUser`/`companyPass`, `readerUser`/`readerPass`, `passiveUser`/`passivePass`. Farklı bir adres veya parola kullanıyorsanız yalnızca bu değişkenleri güncelleyin.
- Koleksiyon **Collection Runner** ile baştan sona sırayla çalıştırılmalıdır (bazı istekler öncekilerin ürettiği ID'yi `{{companyId}}`, `{{boothId}}` gibi değişkenlerle kullanır).
- İçerik: listeleme, tek kayıt okuma, ekleme, tam güncelleme, kısmi güncelleme, silme işlemleri; stant ve rezervasyon ilişkileri; 400, 404, 401, 403 durum kodlarına yönelik negatif test senaryoları ve Actuator istekleri.

---

## Proje Yapısı

```
src/main/java/com/fairoptima/
├── config/        Uygulama yapılandırmaları, başlangıç verisi, OpenAPI tanımı
├── controller/    REST uç noktaları
├── dto/           İstek ve cevap modelleri (Jakarta Bean Validation ile)
├── entity/        JPA varlıkları ve Enum'lar (BoothStatus, FairStatus vb.)
├── exception/     Özel hata sınıfları, ErrorResponse, GlobalExceptionHandler
├── repository/    Repository arayüzleri
│   └── impl/      EntityManager kullanan veri erişim gerçekleştirimleri
├── security/      SecurityFilterChain, UserDetailsService, 401/403 işleyicileri
└── service/       Servis arayüzleri
    └── impl/      İş mantığı ve transaction yönetimi gerçekleştirimleri
```

---

## Modül Durumu

Projeye ait tüm gereksinimler ve veri modelleri (Fuar, Salon, Stant, Şirket, Rezervasyon) entegre edilmiştir. İlk tasarımda bahsi geçen Spring AI modülü proje kapsamından çıkarılmıştır.

| Modül | Durum |
|---|---|
| Şirketler (Company) CRUD | ✅ Tamamlandı |
| Güvenlik: kullanıcılar, roller, HTTP Basic, Argon2id, 401/403 | ✅ Tamamlandı |
| Ortak hata yönetimi ve doğrulama | ✅ Tamamlandı |
| Actuator (health, info, korumalı metrics) | ✅ Tamamlandı |
| Swagger / OpenAPI dokümantasyonu | ✅ Tamamlandı |
| Fuar, Salon, Stant tipi ve Stant yönetimi | ✅ Tamamlandı |
| Rezervasyon ve kaynak/çakışma kuralları | ✅ Tamamlandı |
| Fiyat teklifi servisi (`@Primary` / `@Qualifier`) | ✅ Tamamlandı |
| Postman test koleksiyonu | ✅ Tamamlandı |