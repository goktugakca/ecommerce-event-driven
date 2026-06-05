### REST yerine niye KAFKA kullanıyoruz?
- servisler birbirini REST ile çağırmış olsaydı servislerden birinde bir çökme meydana geldiğinde bütün sistem aksıcaktı. Kafka ise bu sorunu çözüyor servisler çökse bile atılan istekler kafka'da tutuluyor ve servis tekrar ayaklandığında kaldığı yerden okuyarak işine devam ediyor. İlerde farklı şeyler eklemek istediğimizde de kolay bir şekilde genişletme şansı veriyor.
### Record ile Class farkı
- Record sadece veri taşımak için tasarlanmış özel bir sınıf türüdür. Record constructor, getters, equals,hashCode,toString gibi metodları otomatik olarak üretir. Recordlar immutable verilerdir.Bu yüzden eventler için mükemmellerdir.
### add.type.headers=false ile value.default.type ilişkisi
- Spring JsonSerializer'ı varsayılan olarak mesah içine tam class adını paket adıyla beraber header olarak gömer. Consumer da bu header'a bakarak sınıfı oluşturmaya çalışır ancak farklı servislerde aynı paket yoktur her servisin kendi paketi vardır. Bu yüzden ...headers=false diyerek etiketi kaldırıyoruz ve düz bir JSON haline geliyor. Peki artık bu JSON'u neye çevirceği sorusuna da value.default.type ile cevap veriyoruz. Kendi OrderCreatedEvent'ine çevir diyoruz. Kısacası producer'da tip etiketini kapattığımız için consumer'da hedef tipi açıkça belirtmek zorundayız.
#### ddl-auto=update
- Hibernate, entitylerine bakıp tabloları otomatik oluşturur/günceller. Geliştirme için pratik, gerçek projede Flyway/Liquidbase gibi migration araçları kullanır.
#### show-sql=true
- çalışan sql'leri konsola yazar
### Spring Data Query Method nedir?
- Spring metod adına bakarak metodu otomatik olarak üretir. Buna "query method" denir.
## Docker Compose Nedir?
- Kafka'yı veritabanına ayrı ayrı docker run komutları ile başlatırken docker compose tüm bu altyapıyı tek bir dosyada (docker-compose.yml) tanımlamanı ve tek bir komut ile "docker compose up" ayağa kaldırmanı sağlar. x servislerini şu şu ayarlar ile çalıştır der gibi bir recipe yazmış oluyorsun.
### Docker Compose Dosyası Nasıl Oluşturulur?
- services altında her bir konteyner tanımlanır. image hangi imajı kullanacağını, port ise host ile konteyner arasındaki port eşleşmesini sağlar. Mesela 9092:9092 => host'un 9092'si ile konteynerin 9092'si eşleşecek demektir. Environment ise konteynera verilen ayarları belirtiyor. Kafka ve postgres ayarları elle "docker run" çalıştırırken kullanılan komutlardaki ayarların birebir aynısı.
- Kafka'nın "KAFKA_ADVERTISED_LISTENERS" değerinin "local:9092" olmasının nedeni order/inventory servislerinin konteyner'da değil kendi makinemizde nvm ile çalışıyor olması ve Kafka'ya localhost:9092 üzerinden bağlanıyor olmasından dolayı. İlerde servisleri konteyner'a taşırsak burası "kafka:9092" gibi servis adına döner.
### @Transactional Ne İşe Yarar?
- Transaction ya hep ya hiç mantığını içerir, veritabanında aşamalı işlemler yapılacağı zaman işlemlerden birinde sorun çıkarsa veritabanındaki diğer verilere dokunmaz rollback yapar. Bu sayede tutarsız bir işlemde bulunmaz.Sade olarak "oku-azalt-kaydet" işlemini bölünmeden tek bir atomik işlem olarak yapmaya yarar.
### AtomicLong nedir, nasıl sorunlara yol açar?
- AtomicLong bellekte yaşayan bir veri tipidir. Kullanıldığı service her ayağa kaldırıldığında kendini 1 sayısına resetler. Service kopyası çıkarttığında her service kendi atomiclong'una sahip olduğu için id çakışması yaşanır. Çözüm olarak id kalıcı,tek bir otoriteden gelmeli. En yaygın yol id'yi (generatedvalue) olarak veritabanına ürettirmektir. Dağıtık sistemlerde ise dağıtık id üreticileri kullanılır, Snowflake,UUID gibi.
### Order ve Inventory için ortak DB niye kullanmıyoruz?
- Her servis kendi verisinin sahibidir(loose coupling). Ancak ortak DB olduğunda bir servis diğerinin tablosuna direkt şekilde erişebilir, bu da servislerin birbirine sıkıca bağlanmasına yol açar oysa mikroservis mantığında servislerin birbiri ile API/event üzerinden iletişim kurmasını ister birbirlerinin verilerine karışmasını istemez.
- *Bağımsız şema değişikliği*. Ayrı DB'lerde inventory bir değişiklik yaptığında order servisini bozamaz ancak ortak dblerde şema değişikliği yapmaya çalıştığında diğer servisi bozabilir.
- DB çökmesinde iki servis de etkilenir bu durumdan *arıza izolasyonu*.
