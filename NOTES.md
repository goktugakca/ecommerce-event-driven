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
